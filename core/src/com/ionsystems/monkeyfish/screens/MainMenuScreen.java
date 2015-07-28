package com.ionsystems.monkeyfish.screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ionsystems.monkeyfish.GameState;
import com.ionsystems.monkeyfish.MonkeyFishGame;

public class MainMenuScreen extends DefaultScreen implements Screen  {

    final MonkeyFishGame game;
    TextureAtlas atlas;
    TextureRegion hero;
    Texture logo;
    Image imgLogo;

	
	Label creatorLabel;

    public MainMenuScreen(final MonkeyFishGame game, final Table hud) {
    	super(game,hud);
    	screenWidth = 800;
    	screenHeight = 480;
        this.game = game;
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
       
        creatorLabel  = new Label("Creators: Cameron Craig, Euan Mutch, Andrew Rigg, Stuart Thain",skin);
        
 
        //Buttons
        TextButton btnStart = new TextButton("Start",skin);
        TextButton btnOptions = new TextButton("Options", skin);
        TextButton btnHelp = new TextButton("Help", skin);
        TextButton btnAbout = new TextButton("About", skin);
        TextButton btnLevels = new TextButton("Levels", skin);
       
        logo = new Texture(Gdx.files.internal("IONsystems.png"));
        imgLogo = new Image(logo);
        Table root = new Table(skin);
		root.setFillParent(true);
		root.setBackground(skin.getDrawable("default-pane"));
		root.add(imgLogo).row();
		root.add(btnStart).row();
		root.add(btnOptions).row();
		root.add(btnHelp).row();
		root.add(btnAbout).row();
		root.add(btnLevels).row();
		root.add(creatorLabel).row();
		stage.addActor(root);
        viewport = getViewport((Camera)camera);

		stage.setViewport(viewport);
        
		
		 //Add a listener for the close button in the HUD
//		 game.getButton().addListener(new ClickListener() {
//             @Override
//             public void clicked(InputEvent e, float x, float y){
//                     Gdx.app.exit();//            
//		 });
		 

		 btnStart.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
                     //game.setScreen(new GameScreen(game,hud));
            	 game.setState(GameState.PLAYING);
             }
            
		 });
		 btnOptions.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
                     //game.setScreen(new OptionsScreen(game));
            	 game.setState(GameState.OPTIONS);
             }
            
		 });
		 btnLevels.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
                   //  game.setScreen(new LevelSelectScreen(game));
            	 game.setState(GameState.LEVEL_SELECT);
             }
            
		 });
		 Gdx.input.setInputProcessor(stage);
//        Gdx.input.setInputProcessor(new InputMultiplexer(new InputAdapter() {
//			public boolean keyDown (int keycode) {
//				if (keycode == Input.Keys.SPACE) {
//					label.setText("Label");
//					stage.setViewport(viewport);
//					resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//				}
//				return false;
//			}
//		}, stage));

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
    /*
     * render only runs if it is the active screen
     */
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        game.font.draw(game.batch, "Welcome to MonkeyFish!!! ", 100, 150);
        game.font.draw(game.batch, "Tap anywhere to begin!", 100, 100);
        game.batch.end();
        stage.draw();

    }

	public void show() {
		
	}

	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
		
	}

	public void pause() {
		
	}

	public void resume() {
		
	}

	public void hide() {
		
	}

	public void dispose() {
		stage.dispose();
		
	}
}