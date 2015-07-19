package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class MonkeyFishGame extends Game {
    public SpriteBatch batch;
    public BitmapFont font;
    public GameState gameState;
    
    LevelSelectScreen levelScreen;
    MainMenuScreen mainMenuScreen;
    OptionsScreen optionsScreen;
    PauseScreen pausedScreen;
    GameScreen playingScreen;

    public void create() {
        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        font = new BitmapFont();
        //Start in the main menu screen
       
        
        levelScreen = new LevelSelectScreen(this);
        mainMenuScreen = new MainMenuScreen(this);
        optionsScreen = new OptionsScreen(this);
        pausedScreen = new PauseScreen(this);
        playingScreen = new GameScreen(this);
        gameState = GameState.MAINMENU;
        
    }

    public void render() {
    	//System.out.println("Rendering");
    	
    	switch(gameState){
		case HIGHSCORES:
			//this.setScreen(new HighScoreScreen(this));
			break;
		case LEVEL_SELECT:
			this.setScreen(this.levelScreen);
			break;
		case MAINMENU:
			this.setScreen(new MainMenuScreen(this));
			break;
		case OPTIONS:
			this.setScreen(optionsScreen);
			break;
		case PAUSED:
			this.setScreen(pausedScreen);
			break;
		case PLAYING:
			this.setScreen(playingScreen);
			break;
		default:
			break;
    	}
    	super.render(); //important!
//    	Gdx.gl.glClearColor(0, 0.3f, 0.5f, 1.5f);
//		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
    
}

