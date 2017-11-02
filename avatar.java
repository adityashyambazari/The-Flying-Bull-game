package com.mygdx.theFlyingBull;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import java.awt.Color;
import java.util.Random;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javafx.scene.media.MediaPlayer;
import sun.rmi.runtime.Log;

public class avatar extends ApplicationAdapter implements InputProcessor {
	SpriteBatch batch;
	Texture img,startScreen,gameOver;
    Texture[] appa;
    Texture[] stone;
    int flapState;
    int delay = 0;
    float appaY = 0;
    float velocity = 0;
    int gameState = 0;
    int upDown=0;
    float stoneVelocity = 4;
    int noOfStones = 5 ;
    int randomStone;
    float distanceBetweenTubes;
    //ShapeRenderer shapeRenderer;
    Circle[] circles ;
    Rectangle appaRectangle;

    float appaScaleX ;
    float appaScaleY ;

    int highScore;





    com.badlogic.gdx.Preferences preferences;



   /* Stage stage;
    Texture myTexture;
    TextureRegion myTextureRegion;
    TextureRegionDrawable myTexRegionDrawable;
    ImageButton button;*/



    int score;
    int scoreTimer;

    BitmapFont font;


    Music titleSong;


    float[] stoneX = new float[noOfStones] ;

    Random randomGenerator;
    float[] stoneY = new float[noOfStones];

    float[] stoneScaleX = new float[noOfStones];
    float[] stoneScaleY = new float[noOfStones];
	
	@Override
	public void create () {
		batch = new SpriteBatch();



        Gdx.input.setInputProcessor(this);

        titleSong = Gdx.audio.newMusic(Gdx.files.internal("avatarTitle.wav"));


        font = new BitmapFont();
        font.getData().setScale(5*Gdx.graphics.getWidth()/1050);


		img = new Texture("background.png");
        startScreen = new Texture("start.png");

        preferences = Gdx.app.getPreferences("highScore");
        highScore = preferences.getInteger("highScore",0);

        distanceBetweenTubes = Gdx.graphics.getWidth()/2;


        //shapeRenderer = new ShapeRenderer();


        appa = new Texture[4];
        appa[0] = new Texture("appa1.png");
        appa[1] = new Texture("appa2.png");
        appa[2] = new Texture("appa3.png");
        appa[3] = new Texture("appa4.png");

        appaScaleX = Gdx.graphics.getWidth()*appa[0].getWidth()/1050;
        appaScaleY = Gdx.graphics.getHeight()*appa[0].getHeight()/1920;


        gameOver = new Texture("gameOver.png");

        stone = new Texture[8];
        stone[0] = new Texture("stone1.png");
        stoneScaleX[0] = Gdx.graphics.getWidth()*stone[0].getWidth()/1080;
        stoneScaleY[0] = Gdx.graphics.getHeight()*stone[0].getWidth()/1920;

        stone[1] = new Texture("stone2.png");
        stoneScaleX[1] = Gdx.graphics.getWidth()*stone[1].getWidth()/1080;
        stoneScaleY[1] = Gdx.graphics.getHeight()*stone[1].getWidth()/1920;

        stone[2] = new Texture("stone3.png");
        stoneScaleX[2] = Gdx.graphics.getWidth()*stone[2].getWidth()/1080;
        stoneScaleY[2] = Gdx.graphics.getHeight()*stone[2].getWidth()/1920;

        stone[3] = new Texture("stone4.png");
        stoneScaleX[3] = Gdx.graphics.getWidth()*stone[3].getWidth()/1080;
        stoneScaleY[3] = Gdx.graphics.getHeight()*stone[3].getWidth()/1920;

        stone[4] = new Texture("stone5.png");
        stoneScaleX[4] = Gdx.graphics.getWidth()*stone[4].getWidth()/1080;
        stoneScaleY[4] = Gdx.graphics.getHeight()*stone[4].getWidth()/1920;


       /* myTexture = new Texture(Gdx.files.internal("tapToRestart.jpg"));
        myTextureRegion = new TextureRegion(myTexture);
        myTexRegionDrawable = new TextureRegionDrawable(myTextureRegion);
        button = new ImageButton(myTexRegionDrawable); //Set the button up

        stage = new Stage(new ScreenViewport()); //Set up a stage for the ui
        stage.addActor(button); //Add the button to the stage to perform rendering and take input.
        button.setPosition(300,500);
        Gdx.input.setInputProcessor(stage);
        */


        randomGenerator = new Random();

        //shapeRenderer = new ShapeRenderer();
        circles = new Circle[noOfStones];
        appaRectangle = new Rectangle();
        for (int i = 0; i<noOfStones;i++) {

            circles[i] = new Circle();
            stoneY[i] = randomGenerator.nextFloat() * (Gdx.graphics.getHeight() - stone[i].getHeight());

            stoneX[i] = Gdx.graphics.getWidth() / 2 + stone[i].getWidth() / 2 - Gdx.graphics.getWidth()  - i*distanceBetweenTubes;
        }
        appaY = Gdx.graphics.getHeight()/2 - appa[0].getHeight()/2;
        flapState = 0;

	}

	@Override
	public void render () {

        batch.begin();



        batch.draw(img, 0, 0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        if(gameState==1)
        {

            scoreTimer++;

            if(scoreTimer>100)
            {
                score++;
                scoreTimer = 0;
            }


            //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            //shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);


            titleSong.play();






            velocity++; //= velocity+ Gdx.graphics.getHeight()/1920;
            appaY-=velocity;

            if(Gdx.input.isTouched())
            {
            velocity = -10;
            }

	        delay++;
            if(delay >=10)
            {
            flapState = (flapState+1)%4;
            delay = 0;
            }


            for(int i = 0; i<noOfStones; i++)
            {

                if(stoneX[i]> stone[i].getWidth() + Gdx.graphics.getWidth())
                {
                    stoneX[i] -= noOfStones*distanceBetweenTubes;
                    stoneY[i] = (randomGenerator.nextFloat())*(Gdx.graphics.getHeight()- stone[i].getHeight());

                }
                stoneX[i] += 8*Gdx.graphics.getWidth()/1050;
                if(score>10)
                {
                    if(upDown == 0)
                    {
                        stoneY[i] +=8;upDown=1;
                    }
                    else
                    {
                        stoneY[i] -=8;upDown=0;
                    }
                }

                if(score>20)
                {
                    stoneX[i]+=4;
                }

                if(score>40)
                {
                    stoneX[i]+=2;
                }



                circles[i].set(stoneX[i]+ circles[i].radius,stoneY[i]+ circles[i].radius,stoneScaleY[i]/2);
                batch.draw(stone[i],stoneX[i],stoneY[i],stoneScaleX[i],stoneScaleY[i]);

           //     shapeRenderer.circle(stoneX[i] + circles[i].radius,stoneY[i]+ circles[i].radius,stoneScaleY[i]/2);
            }
            appaRectangle = new Rectangle(Gdx.graphics.getWidth()/2 - appaScaleX/2+30*Gdx.graphics.getWidth()/1080,Gdx.graphics.getHeight()*30/1920+appaY,appaScaleX-70*Gdx.graphics.getWidth()/1080,appaScaleY-90*Gdx.graphics.getHeight()/1920);
            batch.draw(appa[flapState],Gdx.graphics.getWidth()/2 - appaScaleX/2,appaY,appaScaleX,appaScaleY);
         //   shapeRenderer.rect(Gdx.graphics.getWidth()/2 - appaScaleX/2+30*Gdx.graphics.getWidth()/1080,Gdx.graphics.getHeight()*30/1920+appaY,appaScaleX-70*Gdx.graphics.getWidth()/1080,appaScaleY-90*Gdx.graphics.getHeight()/1920);


            for (int z = 0; z<noOfStones;z++)
            {

                if(Intersector.overlaps(circles[z],appaRectangle))
                {




                            gameState = 2;




                }
            }

            if(appaY < -50)
                gameState = 2;
            else if(appaY > Gdx.graphics.getHeight())
                gameState = 2;

            font.setColor(com.badlogic.gdx.graphics.Color.WHITE);


            font.draw(batch,"score : " + String.valueOf(score),Gdx.graphics.getWidth()-500*Gdx.graphics.getWidth()/1050,Gdx.graphics.getHeight()-40*Gdx.graphics.getHeight()/1920);
            font.draw(batch, "high score : " + highScore,Gdx.graphics.getWidth()-500*Gdx.graphics.getWidth()/1050 ,Gdx.graphics.getHeight()-100*Gdx.graphics.getHeight()/1920 );




        }
        else if(gameState==0)
        {

            batch.draw(startScreen,0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

            score = 0;






            if(Gdx.input.justTouched())
            {
                gameState = 1;
            }
            for (int i = 0; i<noOfStones;i++) {

                circles[i] = new Circle();
                stoneY[i] = randomGenerator.nextFloat() * (Gdx.graphics.getHeight() - stone[i].getHeight());

                stoneX[i] = Gdx.graphics.getWidth() / 2 + stone[i].getWidth() / 2 - Gdx.graphics.getWidth()  - i*distanceBetweenTubes;
            }
            appaY = Gdx.graphics.getHeight()/2 - appa[0].getHeight()/2;


        }
        else if(gameState==2) {



           batch.draw(gameOver, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());


          /*  stage.draw();
            button.addListener(new EventListener() {
                @Override
                public boolean handle(Event event) {
                    gameState = 0;
                    return true;
                }
            });
            */


            int getHighScore = highScore;

            if (score > highScore) {
                highScore = score;
                preferences.putInteger("highScore", highScore);

                preferences.flush();


                getHighScore = preferences.getInteger("highScore", highScore);
                highScore = getHighScore;


            }

            font.setColor(com.badlogic.gdx.graphics.Color.WHITE);


            font.draw(batch, "high score : " + getHighScore, 50 * Gdx.graphics.getWidth() / 1050, 100 * Gdx.graphics.getHeight() / 1920);

            font.draw(batch, "score : " + String.valueOf(score), 50 * Gdx.graphics.getWidth() / 1050, 160 * Gdx.graphics.getHeight() / 1920);


            if (Gdx.input.isTouched()) {
                gameState = 0;
            }

            titleSong.stop();


        }

       // shapeRenderer.end();
        batch.end();





    }
	
	@Override
	public void dispose () {

        titleSong.dispose();
		batch.dispose();
		img.dispose();
        //stage.dispose();
	}

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
