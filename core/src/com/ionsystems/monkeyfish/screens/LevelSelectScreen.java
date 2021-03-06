package com.ionsystems.monkeyfish.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ionsystems.monkeyfish.GameState;
import com.ionsystems.monkeyfish.Level;
import com.ionsystems.monkeyfish.Levels;
import com.ionsystems.monkeyfish.MonkeyFishGame;

public class LevelSelectScreen extends DefaultScreen implements Screen {

  
  
    Table levelsTable;
    ScrollPane scrollPane;
    int tempLevelIndex = 0;
    
    CheckBox chkSound, chkMusic, chkAntipeeedeeeeean;
    TextButton[] btnPlay = new TextButton[10];
    TextButton btnBack;
    Table hud;
    public LevelSelectScreen(final MonkeyFishGame game, Table hud) {
    	super(game, hud);

        //Buttons
        Preferences preferences = Gdx.app.getPreferences("My Options");
        chkSound = new CheckBox("Sound", skin);
        chkSound.setChecked(preferences.getBoolean("sound"));
        chkMusic = new CheckBox("Music", skin);
        chkMusic.setChecked(preferences.getBoolean("music"));
        chkAntipeeedeeeeean = new CheckBox("Antipodean", skin);
        chkAntipeeedeeeeean.setChecked(preferences.getBoolean("antipeeedeeeeean"));
        
        levelsTable = new Table(skin);
        levelsTable.setFillParent(true);
        levelsTable.setBackground(skin.getDrawable("default-pane"));
        btnBack = new TextButton("Back", skin);
        levelsTable.add(new Label("Name", skin));
        levelsTable.add(new Label("Difficulty", skin));
        levelsTable.add(new Label("Best Score", skin));
        levelsTable.row();
        
        for(int i = 1; i <= Levels.getInstance().getNumberOfLevels(); i++){
        	tempLevelIndex = i;
        	Level tempLevel = Levels.getInstance().getSpecificLevel(i);
        	btnPlay[i] = new TextButton("Play",skin);
        	levelsTable.add(tempLevel.getName());
        	levelsTable.add((Integer.toString(tempLevel.getDifficulty())));
        	levelsTable.add(btnPlay[i]);
        	levelsTable.row();
        	btnPlay[i].addListener(new ClickListener() {
                 @Override
                 public void clicked(InputEvent e, float x, float y){
                	 Levels.getInstance().gotoLevel(tempLevelIndex);
                	 game.setState(GameState.PLAYING);
                 }
    		});
        }
        levelsTable.add(btnBack).colspan(4);
		
		scrollPane = new ScrollPane(levelsTable);
		stage.addActor(levelsTable);
        stage.addActor(hud);
        viewport = getViewport((Camera)camera);

		stage.setViewport(viewport);
    
		 Gdx.input.setInputProcessor(stage);
		 
		 btnBack.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
                     //game.setScreen(new MainMenuScreen(game,));
            	 game.setState(game.getBackToState());
             }
            
		 });
    }

    static public Viewport getViewport (Camera camera) {
		int minWorldWidth = 640;
		int minWorldHeight = 480;
		int maxWorldWidth = 800;
		int maxWorldHeight = 480;
		Viewport viewport;
		viewport = new StretchViewport(minWorldWidth, minWorldHeight, camera);
		return viewport;
	}
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();
        game.batch.end();
        stage.draw();

    }

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		stage.dispose();
		
	}


        //...Rest of class omitted for succinctness.

}