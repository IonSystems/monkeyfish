package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class MonkeyFishGame extends Game {

    public SpriteBatch batch;
    public BitmapFont font;
    
    ImageButton imageButton;
    ImageButtonStyle imageButtonStyle;
    Skin skin;
    Stage stage;
    Label label;
    Viewport viewport;
    OrthographicCamera camera;
    Screen exitButtonScreen;
    Table hudTable;
    
    MainMenuScreen mms;
    OptionsScreen os;
    GameScreen gs;
    GameState state;
    public void create() {
    	state = GameState.MAINMENU;
        batch = new SpriteBatch();
        //Use LibGDX's default Arial font.
        exitButtonScreen = getScreen();
        font = new BitmapFont();
        skin = new Skin(Gdx.files.internal("skins/uiskin.json"));
        imageButtonStyle = new ImageButtonStyle();  //Instaciate
        imageButtonStyle.up = skin.getDrawable("check-off");  //Set image for not pressed button 
        imageButtonStyle.down = skin.getDrawable("check-on");  //Set image for pressed
        imageButtonStyle.pressedOffsetX = 1; 
        imageButtonStyle.pressedOffsetY = -1;
        imageButton = new ImageButton(skin);
        camera = new OrthographicCamera();
        stage = new Stage();
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), camera);//TODO: get width and height from somewhere
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
        hudTable = new Table(skin);
  		hudTable.setFillParent(false);
  		hudTable.setBackground(skin.getDrawable("default-pane"));
  		hudTable.add(imageButton).row();
  		hudTable.setBounds(Gdx.graphics.getWidth() - 20, Gdx.graphics.getHeight() - 20, 20, 20);
  		imageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent e, float x, float y){
                    Gdx.app.exit();
            }
           
		 });
        stage.addActor(hudTable);
        Gdx.input.setInputProcessor(stage);
        mms = new MainMenuScreen(this, hudTable);
        //os = new OptionsScreen(this, hudTable);
        //gs = new GameScreen(this, hudTable);
        //this.setScreen(mms);
    }
    GameState oldState;
    GameState backToState = GameState.MAINMENU;
    public void render() {
    	if(state != oldState){
    		switch(state){
        	
        	case MAINMENU:
        		this.setScreen(new MainMenuScreen(this, hudTable));
        		backToState = oldState;
        		break;
        	case OPTIONS:
        		this.setScreen(new OptionsScreen(this, hudTable));
        		backToState = oldState;
        		break;
        	case PLAYING:
        		this.setScreen(new GameScreen(this, hudTable));
        		break;
        	case PAUSED:
        		this.setScreen(new PauseScreen(this, hudTable));
        		break;
        	case LEVEL_SELECT:
        		this.setScreen(new LevelSelectScreen(this, hudTable));
        		backToState = oldState;
        		break;
    		default:
    			break;
        		
        	}
    	}
    	
        super.render(); //important!
        camera.update();
        //stage.act();
        oldState  = state;
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
    
    public ImageButton getButton(){
    	return this.imageButton;
    }
    
}

