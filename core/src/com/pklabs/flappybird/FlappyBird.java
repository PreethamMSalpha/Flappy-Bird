package com.pklabs.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	//ShapeRenderer shapeRenderer;
	Texture gameover;

	Texture[] birds;
	int flapstate = 0;
	int birdY;
	int velocity = 0;
	Circle birdCircle;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;

	int gameState = 0;
	int gravity = 2;

	Texture bottomtube;
	Texture toptube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;
	float tubeVelocity= 4;
	int numberOfTubes =4;
	float[] tubeX = new float[numberOfTubes];
	float[] tubeOffset = new float[numberOfTubes];
	float distanceBetweenTubes;
	Rectangle[] topTubeRectangle;
    Rectangle[] bottomTubeRectangle;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		//shapeRenderer = new ShapeRenderer();
		gameover = new Texture("gameover.png");
		birdCircle = new Circle();

		font = new BitmapFont();
		font.setColor(Color.WHITE);
		font.getData().setScale(10);;

		birds = new Texture[2];
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");

		bottomtube = new Texture("bottomtube.png");
		toptube = new Texture("toptube.png");
		maxTubeOffset = Gdx.graphics.getHeight()/2 - gap/2 - 100;
		randomGenerator = new Random();
		distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;
		topTubeRectangle = new Rectangle[numberOfTubes];
		bottomTubeRectangle = new Rectangle[numberOfTubes];

		startGame();


	}



	public void startGame(){

		birdY = Gdx.graphics.getHeight()/2 - birds[flapstate].getHeight()/2;

		for (int i=0;i<4;i++){

			tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);
			tubeX[i] = Gdx.graphics.getWidth()/2 - toptube.getWidth()/2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;
			topTubeRectangle[i] = new Rectangle();
			bottomTubeRectangle[i] = new Rectangle();

		}

	}


	@Override
	public void render () {

		batch.begin();
		batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

		if (gameState == 1){

			if (tubeX[scoringTube] < Gdx.graphics.getWidth()/2 ){

				score ++;

				Gdx.app.log("score", String.valueOf(score));

				if (scoringTube < numberOfTubes - 1){
					scoringTube ++;
				}else{
					scoringTube = 0;
				}

			}

			if (Gdx.input.justTouched()){
				//Gdx.app.log("screen","touched");
				velocity = -18;

			}

			for (int i=0 ; i<numberOfTubes; i++) {

				if (tubeX[i] < - toptube.getWidth()){

					tubeX[i] += numberOfTubes * distanceBetweenTubes;
					tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);


				}else{

					tubeX[i] = tubeX[i] - tubeVelocity;

				}

				batch.draw(toptube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
				batch.draw(bottomtube, tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i]);

				topTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(), toptube.getHeight());
				bottomTubeRectangle[i] = new Rectangle(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i], bottomtube.getWidth(),bottomtube.getHeight());

			}


			if (birdY > 0){

				velocity = velocity + gravity;
				birdY -= velocity;

			}else {
				gameState = 2;
			}

		}else if(gameState == 0){

			if (Gdx.input.justTouched()){
			gameState = 1;
			}

		}else if (gameState == 2){

			batch.draw(gameover , Gdx.graphics.getWidth()/2 - gameover.getWidth()/2 , Gdx.graphics.getHeight()/2 - gameover.getHeight()/2);

			if (Gdx.input.justTouched()){
				gameState = 1;
				startGame();
				score = 0;
				scoringTube = 0;
				velocity = 0 ;
			}

		}

		if (flapstate == 0){
			flapstate = 1;
		}else
			flapstate=0;



		batch.draw(birds[flapstate], Gdx.graphics.getWidth()/2 - birds[flapstate].getWidth()/2,birdY);
		font.draw(batch,String.valueOf(score),100,200);
		batch.end();

		birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);

		//shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
		//shapeRenderer.setColor(Color.RED);
		//shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for (int i=0 ; i<numberOfTubes; i++) {

           // shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],toptube.getWidth(), toptube.getHeight());
            //shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 - bottomtube.getHeight() + tubeOffset[i], bottomtube.getWidth(),bottomtube.getHeight());

            if (Intersector.overlaps(birdCircle,topTubeRectangle[i]) || Intersector.overlaps(birdCircle,bottomTubeRectangle[i])){

            	//Gdx.app.log("collision", "occured");
				gameState = 2;

			}
        }

		//shapeRenderer.end();

	}
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
	}
}
