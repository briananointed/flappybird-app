package com.bmw.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	private SpriteBatch batch;
	Texture background;
	Texture[] birds;
	Texture gameover;
	byte flapstate = 0;
	float birdY = 0;
	float velocity = 0;
	float gravity = 2;
	byte gamestate = 0;
	Texture topTube, bottomTube;
	float gap = 400;
	float maxTubeOffset;
	Random randomGenerator;
	int numberOfTubes = 4;
	float tubeVelocity = 4;
	int score = 0;
	int scoringTube = 0;
	BitmapFont font;
	float []tubeOffset = new float[numberOfTubes];
	float []tubeX = new float[numberOfTubes];
	float distanceBetweenTubes;

	Circle birdCircle;

	Rectangle []toptubeRectangles,bottomtubeRectangles;

	ShapeRenderer shapeRenderer;


	@Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		birds[0]= new Texture("bird.png");
		birds[1]= new Texture("bird2.png");
		gameover = new Texture("gameover.png");

        topTube = new Texture("toptube.png");
        bottomTube = new Texture("bottomtube.png");
        maxTubeOffset = Gdx.graphics.getHeight()/2 - gap /2 - 100;
        randomGenerator = new Random();
        distanceBetweenTubes = Gdx.graphics.getWidth() * 3/4;

        birdCircle = new Circle();
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(10);

        toptubeRectangles = new Rectangle[numberOfTubes];
        bottomtubeRectangles = new Rectangle[numberOfTubes];

      //  shapeRenderer = new ShapeRenderer();
        startGame();

	}

	public void  startGame(){
        score = 0;
        scoringTube = 0;
        velocity = 0;
        birdY = (Gdx.graphics.getHeight() - birds[flapstate].getHeight())/2;

        for(int i = 0; i < numberOfTubes ; i++){

            tubeX[i] = Gdx.graphics.getWidth() /2 - topTube.getWidth() /2 + Gdx.graphics.getWidth() + i * distanceBetweenTubes;

            toptubeRectangles[i] = new Rectangle();
            bottomtubeRectangles[i] = new Rectangle();
        }

    }

	@Override
	public void render () {

        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

	    if(gamestate == 1){


            if(tubeX[scoringTube] < Gdx.graphics.getWidth()/2){

                    score++;

                    if(scoringTube < numberOfTubes - 1){
                        scoringTube ++;

                    }else{
                        scoringTube = 0;
                    }
            }
            if (Gdx.input.justTouched()) {

               velocity = -30;

            }

            for (int i = 0; i < numberOfTubes; i++){

                if (tubeX[i] < - topTube.getWidth()){

                    tubeX[i] += numberOfTubes * distanceBetweenTubes;
                    tubeOffset[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() - gap - 200);

                }else {
                    tubeX[i] = tubeX[i] - tubeVelocity;

                }
                batch.draw(topTube,tubeX[i],(Gdx.graphics.getHeight() + gap)/2 + tubeOffset[i]);
                batch.draw(bottomTube,tubeX[i],(Gdx.graphics.getHeight() -gap)/2 - bottomTube.getHeight() + tubeOffset[i]);

                toptubeRectangles[i] = new Rectangle(tubeX[i],(Gdx.graphics.getHeight() + gap)/2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
                bottomtubeRectangles[i] = new Rectangle(tubeX[i],(Gdx.graphics.getHeight() -gap)/2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());
            }

            if(birdY >0){

                velocity = velocity + gravity;
                birdY -=velocity;
            }else{
                gamestate = 2;
            }
        }else if(gamestate == 0){

            if (Gdx.input.justTouched()) {

                gamestate = 1;

            }
        }else if (gamestate == 2){

	        batch.draw(gameover,(Gdx.graphics.getWidth() - gameover.getWidth())/2,(Gdx.graphics.getHeight() - gameover.getHeight())/2);

            if (Gdx.input.justTouched()) {

                gamestate = 1;
                startGame();

            }
        }

        if(flapstate == 0){
            flapstate = 1;
        }else{
            flapstate = 0;
        }


		batch.draw(birds[flapstate],(Gdx.graphics.getWidth() - birds[flapstate].getWidth())/2,birdY);

	    font.draw(batch,String.valueOf(score),100,200);
        birdCircle.set(Gdx.graphics.getWidth()/2,birdY + birds[flapstate].getHeight()/2,birds[flapstate].getWidth()/2);

       // shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
       // shapeRenderer.setColor(Color.BLUE);
       // shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for(int i = 0; i < numberOfTubes; i++){

       //     shapeRenderer.rect(tubeX[i],(Gdx.graphics.getHeight() + gap)/2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());
       //     shapeRenderer.rect(tubeX[i],(Gdx.graphics.getHeight() -gap)/2 - bottomTube.getHeight() + tubeOffset[i],bottomTube.getWidth(),bottomTube.getHeight());

            if(Intersector.overlaps(birdCircle,toptubeRectangles[i]) || Intersector.overlaps(birdCircle,bottomtubeRectangles[i])){

                gamestate = 2;
            }

        }
    //    shapeRenderer.end();
        batch.end();

    }

}
