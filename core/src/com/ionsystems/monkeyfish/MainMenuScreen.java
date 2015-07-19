package com.ionsystems.monkeyfish;

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

public class MainMenuScreen implements Screen {

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
	
	Label label;

    public MainMenuScreen(final MonkeyFishGame game) {
    	screenWidth = 800;
    	screenHeight = 480;
        this.game = game;
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
       // atlas = new TextureAtlas(Gdx.files.internal("textures/Textures1.png"));
       // skin = new Skin(Gdx.files.internal("skins/uiskin.png"));
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        skin.add("logo", new Texture("alpha.png"));
        
        red = skin.getColor("red");
        label = new Label("", skin);
        //Buttons
       //System.out.println(Levels.getJsonLevels().prettyPrint(new PrettyPrintSettings()));
       TextButton btnStart = new TextButton("Start",skin);
       TextButton btnOptions = new TextButton("Options", skin);
       TextButton btnHelp = new TextButton("Help", skin);
       TextButton btnAbout = new TextButton("About", skin);
       
        logo = new Texture(Gdx.files.internal("badlogic.jpg"));
        imgLogo = new Image(logo);
        Table root = new Table(skin);
		root.setFillParent(true);
		root.setBackground(skin.getDrawable("default-pane"));
		root.add(imgLogo).row();
		root.add(btnStart).row();
		root.add(btnOptions).row();
		root.add(btnHelp).row();
		root.add(btnAbout).row();
		root.add(label).row();
		stage.addActor(root);
       
        viewport = getViewport((Camera)camera);

		stage.setViewport(viewport);
		label.setText("Creators: Cameron Craig, Euan Mutch, Andrew Rigg, Stuart Thain");
        
		 Gdx.input.setInputProcessor(stage);
		 
		 btnStart.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
                     game.setScreen(new GameScreen(game));
             }
            
		 });
		 btnOptions.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
                     game.setScreen(new OptionsScreen(game));
             }
            
		 });

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