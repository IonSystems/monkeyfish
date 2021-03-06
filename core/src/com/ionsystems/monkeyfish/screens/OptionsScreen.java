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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ionsystems.monkeyfish.MonkeyFishGame;
import com.ionsystems.monkeyfish.SavedSettings;

public class OptionsScreen extends DefaultScreen implements Screen {
    Texture logo;
    Image imgLogo;
    Viewport viewport;
    
    CheckBox chkSound, chkMusic, chkAntipeeedeeeeean;
    TextButton btnBack, btnSave;
	
	Label label, lblSaved;
	ClickListener lnrCheckBox;

    public OptionsScreen(final MonkeyFishGame game, Table hud) {
    	super(game, hud);
    	lnrCheckBox = new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y){
        		lblSaved.setText("");
            }
           
		 };

    	 label = new Label("Configure game options, click save to save.\n"
         		+ "Options are saved for the next time.", skin);
    	 lblSaved = new Label("", skin);
        //Buttons
        chkSound = new CheckBox("Sound", skin);
        chkSound.setChecked(SavedSettings.SETTING_SOUND.getBoolean());
        chkMusic = new CheckBox("Music", skin);
        chkMusic.setChecked(SavedSettings.SETTING_MUSIC.getBoolean());
        chkAntipeeedeeeeean = new CheckBox("Antipodean", skin);
        chkAntipeeedeeeeean.setChecked(SavedSettings.SETTING_UPSIDE_DOWN.getBoolean());
        btnBack = new TextButton("Back", skin);
        btnSave = new TextButton("Save", skin);
       
        logo = new Texture(Gdx.files.internal("IONsystems.png"));
        imgLogo = new Image(logo);
        Table root = new Table(skin);
		root.setFillParent(true);
		root.setBackground(skin.getDrawable("default-pane"));
		root.add(label).row();
		root.add(chkSound).row();
		root.add(chkMusic).row();
		root.add(chkAntipeeedeeeeean).row();
		root.add(btnBack).row();
		root.add(btnSave).row();
		root.add(lblSaved).row();
		stage.addActor(root);

		 Gdx.input.setInputProcessor(stage);
		 //System.out.println("Stage set");
		 chkAntipeeedeeeeean.addListener(lnrCheckBox);
		 chkMusic.addListener(lnrCheckBox);
		 chkSound.addListener(lnrCheckBox);
		 btnBack.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
            	 game.setState(game.getBackToState());
            	 
             }
            
		 });
		 btnSave.addListener(new ClickListener() {
             @Override
             public void clicked(InputEvent e, float x, float y){
            	Preferences preferences = Gdx.app.getPreferences("My Options");
            	SavedSettings.SETTING_SOUND.setBoolean(chkSound.isChecked());
            	SavedSettings.SETTING_MUSIC.setBoolean(chkMusic.isChecked());
            	SavedSettings.SETTING_UPSIDE_DOWN.setBoolean(chkAntipeeedeeeeean.isChecked());
            	SavedSettings.SETTING_USER_NAME.setString("Donald Duck");
         		lblSaved.setText("Saved");
                //game.setScreen(new MainMenuScreen(game));
             }
            
		 });
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