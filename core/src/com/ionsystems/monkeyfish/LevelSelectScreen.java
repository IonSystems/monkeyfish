package com.ionsystems.monkeyfish;

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

public class LevelSelectScreen implements Screen {

    final MonkeyFishGame game;
    
    OrthographicCamera camera;
    Stage stage;
    TextureAtlas atlas;
    Skin skin;
    TextureRegion hero;
    Color red;
    int screenWidth, screenHeight;
    Texture logo;
    Image imgLogo;
    Viewport viewport;
    Table levelsTable;
    ScrollPane scrollPane;
    int tempLevelIndex = 0;
    
    CheckBox chkSound, chkMusic, chkAntipeeedeeeeean;
    TextButton[] btnPlay = new TextButton[10];
    TextButton btnBack;

    public LevelSelectScreen(final MonkeyFishGame game) {
    	screenWidth = Gdx.graphics.getWidth();
    	screenHeight = Gdx.graphics.getHeight();;
        this.game = game;
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        skin.add("logo", new Texture("alpha.png"));
        
        //Buttons
        Preferences preferences = Gdx.app.getPreferences("My Options");
        chkSound = new CheckBox("Sound", skin);
        chkSound.setChecked(preferences.getBoolean("sound"));
        chkMusic = new CheckBox("Music", skin);
        chkMusic.setChecked(preferences.getBoolean("music"));
        chkAntipeeedeeeeean = new CheckBox("Antipodean", skin);
        chkAntipeeedeeeeean.setChecked(preferences.getBoolean("antipeeedeeeeean"));
        
       
        logo = new Texture(Gdx.files.internal("IONsystems.png"));
        imgLogo = new Image(logo);
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
                     game.setScreen(new GameScreen(game));
                 }
                
    		 });
        }
		
		scrollPane = new ScrollPane(levelsTable);
		stage.addActor(levelsTable);
       
        viewport = getViewport((Camera)camera);

		stage.setViewport(viewport);
        
		 Gdx.input.setInputProcessor(stage);
		 
		 btnBack.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
                     game.setScreen(new MainMenuScreen(game));
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