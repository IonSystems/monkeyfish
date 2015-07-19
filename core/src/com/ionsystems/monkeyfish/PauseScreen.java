package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class PauseScreen implements Screen{

	final MonkeyFishGame game;
	Stage stage;
	
	TextButton btnResume;
	TextButtonStyle resumeStyle;
	TextButton btnOptions;
	TextButtonStyle optionsStyle;
	TextButton btnQuit;
	TextButtonStyle quitStyle;
	TextButton btnRestart;
	TextButtonStyle restartStyle;
	
	BitmapFont font;
	TextureAtlas pauseAtlas;

	Skin pauseSkin;
	Table table;
	
	
	public PauseScreen(final MonkeyFishGame g){
		
		this.game = g;
		stage = new Stage();
		table = new Table(pauseSkin);
		
		table.setFillParent(true);
		
		pauseSkin = new Skin();
		
		font = new BitmapFont(Gdx.files.internal("fonts/Arial.fnt"), false);
		resumeStyle = new TextButtonStyle();
		
		resumeStyle.font = font;
		
		btnResume = new TextButton("Resume", resumeStyle);
		pauseAtlas = new TextureAtlas("buttons/btnOut/buttons.pack");
		pauseSkin.addRegions(pauseAtlas);
		resumeStyle.up = pauseSkin.getDrawable("black_button_up");
		resumeStyle.down = pauseSkin.getDrawable("black_button_down");
		
		btnResume.addListener(new ClickListener() {
			@Override
        	public void touchUp(InputEvent e, float x, float y, int pointer, int button){
        		Gdx.app.debug("gesture", "inside touchUp resume PauseScreen");
        		
        		game.setScreen(new GameScreen(game));
        	}
		});
		
		restartStyle = new TextButtonStyle();
		restartStyle.font = font;
		btnRestart = new TextButton("Restart", restartStyle);
		restartStyle.up = pauseSkin.getDrawable("purple_button_up");
		restartStyle.down = pauseSkin.getDrawable("purple_button_down");
		
		btnRestart.addListener(new ClickListener() {
			@Override
        	public void touchUp(InputEvent e, float x, float y, int pointer, int button){
        		Gdx.app.debug("gesture", "inside touchUp restart PauseScreen");
        		
        		game.setScreen(new GameScreen(game));
        	}
		});
		
		optionsStyle = new TextButtonStyle();
		optionsStyle.font = font;
		btnOptions = new TextButton("Options", optionsStyle);
		optionsStyle.up = pauseSkin.getDrawable("lightblue_button_up");
		optionsStyle.down = pauseSkin.getDrawable("darkblue_button_down");
		
		btnOptions.addListener(new ClickListener() {
			@Override
        	public void touchUp(InputEvent e, float x, float y, int pointer, int button){
        		Gdx.app.debug("gesture", "inside touchUp restart PauseScreen");
        		
        		game.setScreen(new OptionsScreen(game));
        	}
		});
		
		
		table.add(btnResume).row();
		table.add(btnRestart).row();
		table.add(btnOptions).row();
		
		stage.addActor(table);
		
		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(float delta) {
		// TODO Auto-generated method stub
		//Gdx.gl.glClearColor(0, 0.3f, 0.5f, 1.5f);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
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
