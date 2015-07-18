package com.ionsystems.monkeyfish;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Game extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	Preferences preferences = Gdx.app.getPreferences("My Options");
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("bard.png");
		
		preferences.putBoolean("sound", true);
		preferences.putBoolean("music", true);
		preferences.putBoolean("antipeeedeeeeean", false);
		preferences.putString("username", "Donald Duck");
		preferences.flush();
		
	}

	//Hello everyone
	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 3, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
		batch.draw(img, 0, 100);
		batch.end();
	}
}
