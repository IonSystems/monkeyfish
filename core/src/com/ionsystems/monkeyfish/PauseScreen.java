package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
	TextButton btnOptions;
	TextButton btnQuit;
	
	BitmapFont font;
	TextureAtlas pauseAtlas;
	TextButtonStyle pauseStyle;
	Skin pauseSkin;
	Table table;
	
	
	public PauseScreen(final MonkeyFishGame g){
		
		this.game = g;
		stage = new Stage();
		table = new Table(pauseSkin);
		
		table.setFillParent(true);
		
		font = new BitmapFont(Gdx.files.internal("fonts/Arial.fnt"), false);
		pauseStyle = new TextButtonStyle();
		pauseSkin = new Skin();
		pauseStyle.font = font;
		
		btnResume = new TextButton("Resume", pauseStyle);
		pauseAtlas = new TextureAtlas("buttons/btnOut/buttons.pack");
		pauseSkin.addRegions(pauseAtlas);
		pauseStyle.up = pauseSkin.getDrawable("black_button_up");
		pauseStyle.down = pauseSkin.getDrawable("black_button_down");
		
		btnResume.addListener(new ClickListener() {
			@Override
        	public void touchUp(InputEvent e, float x, float y, int pointer, int button){
        		Gdx.app.debug("gesture", "inside touchUp PauseScreen");
        		
        		game.setScreen(new GameScreen(game));
        	}
		});
		
		table.add(btnResume).row();
		
		
		
		
		
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
