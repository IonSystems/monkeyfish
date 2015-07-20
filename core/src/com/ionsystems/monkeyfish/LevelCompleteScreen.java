package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class LevelCompleteScreen extends DefaultScreen implements Screen{
	float frameWidth;
	float frameHeight;
	
	TextButton btnNextLevel;
	TextButtonStyle resumeStyle;
	TextButton btnMainMenu;
	TextButtonStyle optionsStyle;
	TextButton btnQuit;
	TextButtonStyle quitStyle;
	TextButton btnRestart;
	TextButtonStyle restartStyle;
	
	BitmapFont font;
	TextureAtlas pauseAtlas;

	Skin pauseSkin;
	Table table;
	
	public LevelCompleteScreen(final MonkeyFishGame g, Table hud){
		super(g,hud);
		this.hud = hud;

		table = new Table(pauseSkin);
		table.setFillParent(true);
		
		pauseSkin = new Skin();
		font = new BitmapFont(Gdx.files.internal("fonts/Arial.fnt"), false);
		resumeStyle = new TextButtonStyle();
		resumeStyle.font = font;
		
		btnNextLevel = new TextButton("Next Level", resumeStyle);
		pauseAtlas = new TextureAtlas("buttons/btnOut/buttons.pack");
		pauseSkin.addRegions(pauseAtlas);
		resumeStyle.up = pauseSkin.getDrawable("black_button_up");
		resumeStyle.down = pauseSkin.getDrawable("black_button_down");
		
		btnNextLevel.addListener(new ClickListener() {
			@Override
        	public void touchUp(InputEvent e, float x, float y, int pointer, int button){
        		Levels.nextLevel();
        		game.state = GameState.PLAYING;
        		
        		//game.setScreen(new GameScreen(game));
        	}
		});
		
		restartStyle = new TextButtonStyle();
		restartStyle.font = font;
		btnRestart = new TextButton("Restart", restartStyle);
		restartStyle.up = pauseSkin.getDrawable("purple_button_up");
		restartStyle.down = pauseSkin.getDrawable("purple_button_down");
		
		btnRestart.addListener(new ClickListener() {
        	public void touchUp(InputEvent e, float x, float y, int pointer, int button){
        		Gdx.app.debug("gesture", "inside touchUp restart PauseScreen");
        		game.state = GameState.PLAYING; //TODO:Possibl bug here, may not restart as no change in state if restarting form PLAYING
        	}
		});
		
		optionsStyle = new TextButtonStyle();
		optionsStyle.font = font;
		btnMainMenu = new TextButton("Main Menu", optionsStyle);
		optionsStyle.up = pauseSkin.getDrawable("lightblue_button_up");
		optionsStyle.down = pauseSkin.getDrawable("darkblue_button_down");
		
		btnMainMenu.addListener(new ClickListener() {
			
        	public void touchUp(InputEvent e, float x, float y, int pointer, int button){
        		game.state = GameState.MAINMENU;
        	}
		});
		
		table.add(btnNextLevel).row();
		table.add(btnRestart).row();
		table.add(btnMainMenu).row();
		
		stage.addActor(table);
		stage.addActor(hud);
		
		Gdx.input.setInputProcessor(stage);
	}

	public void show() {
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
		// TODO Auto-generated method stub
		//Gdx.gl.glClearColor(0, 0.3f, 0.5f, 1.5f);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		
	}
}
