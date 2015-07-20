package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
    
    GameScreen mms;
    OptionsScreen os;
    GameScreen gs;
    GameState state;
  //  Music music;
    MusicPlaying mp = MusicPlaying.MENU;
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
    //    music = setupMusicSetting();
        mms = new GameScreen(this, hudTable);
       // music = Gdx.audio.newMusic(Gdx.files.internal("sound/sample.mp3"));
      //  music.stop();
        //os = new OptionsScreen(this, hudTable);
        //gs = new GameScreen(this, hudTable);
        //this.setScreen(mms);
    }
    GameState oldState;
    GameState backToState = GameState.NOP;
    public void render() {
    	//System.out.println("sjdpasjdpas");
    	checkSettings();
    	if(state != oldState){
    		switch(state){
        	case MAINMENU:
        		this.setScreen(new MainMenuScreen(this, hudTable));
        		
//        		if(!music.isPlaying() || backToState == GameState.PAUSED){
//        			music = setupMainMenuMusic();
//        			music.play();
//        		}
        		backToState = oldState;
        		break;
        	case OPTIONS:
        		this.setScreen(new OptionsScreen(this, hudTable));
        		backToState = oldState;
        		break;
        	case PLAYING:
        		this.setScreen(new GameScreen(this, hudTable));
//        		music = setupPlayingMusic();
//        		music.play();
        		break;
        	case PAUSED:
        		this.setScreen(new PauseScreen(this, hudTable));
        		break;
        	case LEVEL_SELECT:
        		this.setScreen(new LevelSelectScreen(this, hudTable));
        		backToState = oldState;
        		break;
        	case NEXT_LEVEL:
        		this.setScreen(new LevelCompleteScreen(this, hudTable));
        		//music.pause();
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

//    private Music setupPlayingMusic() {
//    	music.stop();
//    	music = Gdx.audio.newMusic(Gdx.files.internal("sound/sample.mp3"));
//    	return music;
//	}

//	private Music setupMainMenuMusic() {
//		music.stop();
//    	music = Gdx.audio.newMusic(Gdx.files.internal("sound/title.mp3"));
//    	return music;
//	}

	public void dispose() {
        batch.dispose();
        font.dispose();
    }
    
    private Music setupMusicSetting() {
		Music music;
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/sample.mp3"));
		music.setLooping(true);
		music.play();
		return music;
	}
    
    private void checkSettings() {
		//System.out.println(gameMusic.isPlaying());
		// Sound
		// Music
//		if (!SavedSettings.SETTING_MUSIC.getBoolean()) {
//			music.pause();
//		}
	}
    
    public ImageButton getButton(){
    	return this.imageButton;
    }
    
}

