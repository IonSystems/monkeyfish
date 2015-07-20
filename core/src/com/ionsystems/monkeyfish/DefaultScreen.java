
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

public class DefaultScreen implements Screen {

    final MonkeyFishGame game;
    
    OrthographicCamera camera;
    Stage stage;
    Skin skin;
    int screenWidth, screenHeight;
    Viewport viewport;
    Table hud;
	


    public DefaultScreen(final MonkeyFishGame game, final Table hud) {
    	screenWidth = Gdx.graphics.getWidth();
    	screenHeight = Gdx.graphics.getHeight();
        this.game = game;
        this.hud = hud;
        stage = new Stage();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, screenWidth, screenHeight);
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
      
		stage.addActor(hud);
        viewport = getViewport((Camera)camera);

		stage.setViewport(viewport);
		 
		 hud.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
                     //game.setScreen(new GameScreen(game,hud));
            	Gdx.app.exit();
             }
            
		 });
		 Gdx.input.setInputProcessor(stage);

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