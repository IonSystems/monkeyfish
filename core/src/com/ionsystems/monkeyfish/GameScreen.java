package com.ionsystems.monkeyfish;

import java.util.ArrayList;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

	final MonkeyFishGame game;

	Stage stage;
	Texture birdImage;
	Texture bobImage;
	Texture treeImage;
	Texture cloudImage;
	Texture groundImage;
	Texture heart;
	Sound birdSong;
	Music gameMusic;
	OrthographicCamera camera;
	Rectangle bob;
	ArrayList<Rectangle> raindrops, trees, clouds, hearts, ground;
	Texture grounds;
	Texture textureUp;
	Texture textureDown;
	Texture background;
	long lastDropTime, lastTreeTime, lastCloudTime;
	float btnPauseSx = 200;
	float btnPauseSy = 200;
	int dropsGathered;
	int frameHeight = 480;
	int frameWidth = 800;
	int movement = 200;
	float yVelocity = 0;
	int acceleration = 10;
	boolean antipodean = false;
	
	//pause button variables
	TextButton btnPause;
	TextureAtlas pauseAtlas;
	TextButtonStyle pauseStyle;
	BitmapFont font;
	Skin pauseSkin;
	int lives = 5;

	private boolean touch;

	private boolean overlapping = false;

	private boolean init = true;

	public GameScreen(final MonkeyFishGame gam) {
		this.game = gam;

		stage = new Stage();
		birdImage = new Texture(Gdx.files.internal("bird.png"));
		bobImage = new Texture(Gdx.files.internal("bob.png"));
		treeImage = new Texture(Gdx.files.internal ("tree.png"));
		cloudImage = new Texture(Gdx.files.internal ("cloud.png"));
		groundImage = new Texture(Gdx.files.internal ("ground1.png"));
		heart = new Texture(Gdx.files.internal ("heart.png"));
		birdSong = Gdx.audio.newSound(Gdx.files.internal("sound/sample2.wav"));
		gameMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/sample.mp3"));
		gameMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, frameWidth, frameHeight);

		// create a Rectangle to logically represent the bob
		bob = new Rectangle();
		bob.width = bobImage.getWidth();
		bob.height = bobImage.getHeight();

		bob.x = frameWidth / 2 - (bob.width) / 2; // center the bob
														// horizontally
		bob.y = 20; // bottom left corner of the bob is 20 pixels above
						// the bottom screen edge

		// create the raindrops array and spawn the first raindrop
		hearts = new ArrayList<Rectangle>();
		raindrops = new ArrayList<Rectangle>();
		trees = new ArrayList<Rectangle>();
		clouds = new ArrayList<Rectangle>();
		ground = new ArrayList<Rectangle>();
		spawnRaindrop();

		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		//Pause Button creation
		font = new BitmapFont(Gdx.files.internal("fonts/Arial.fnt"), false);
		pauseStyle = new TextButtonStyle();
		pauseSkin = new Skin();
		pauseStyle.font = font;
		btnPause = new TextButton("A", pauseStyle);
		pauseAtlas = new TextureAtlas("Buttons/pauseButton.pack");
		
		btnPause.setPosition(100, 100);
		btnPause.setSize(20, 20);
		
		
		pauseSkin.addRegions(pauseAtlas);
		pauseStyle.up = pauseSkin.getDrawable("Pause_button_up");
		pauseStyle.down = pauseSkin.getDrawable("pause_button_down");
		
		btnPause.addListener(new ClickListener() {
			@Override
        	public boolean touchDown(InputEvent e, float x, float y, int pointer, int button){
        		Gdx.app.debug("gesture", "inside touchDown");
				return false;
        	}
        	@Override
        	public void touchUp(InputEvent e, float x, float y, int pointer, int button){
        		Gdx.app.debug("gesture", "inside touchUp");
        	}
		});
		
		textureUp = new Texture(Gdx.files.internal("pause_button_up.png"));
		textureDown = new Texture(Gdx.files.internal("pause_button_down.png"));
		background = new Texture(Gdx.files.internal("pause_button_background.png"));
		btnPause.setPosition(frameWidth, 400);
		btnPause.setSize(50, 50);

		stage.addActor(btnPause);
		//end Pause Button
	}

	
	private void spawnRaindrop() {
		raindrops.add(new spawnObject(birdImage, frameWidth, (int)MathUtils.random(0, frameHeight - bob.height)));
		lastDropTime = TimeUtils.nanoTime();
	}
	
	private void spawnTree() {
		trees.add(new spawnObject(treeImage, frameWidth, 150));
		lastTreeTime = TimeUtils.nanoTime();
	}
	
	private void spawnCloud(){
		clouds.add(new spawnObject(cloudImage, frameWidth, ((int)frameHeight/2 + (int)MathUtils.random(0, frameHeight/2 - cloudImage.getHeight()))));
		lastCloudTime = TimeUtils.nanoTime();
	}
	private void initialiseGround(){
		ground.add(new spawnObject(groundImage, 0, 0));
		ground.add(new spawnObject(groundImage, groundImage.getWidth(), 0));
		init = false;
		}
	
	private void spawnGround(){
		ground.add(new spawnObject(groundImage, frameWidth, 0));
	}
	
	private void spawnHearts(){
		hearts.clear();
		for (int i = 0; i < lives; i ++){
			hearts.add(new spawnObject(heart, 10+30*i, (frameHeight -50)));
		}
	}
	
	
	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0.3f, 0.5f, 1.5f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		stage.act();

		// tell the camera to update its matrices.
		camera.update();

		// tell the SpriteBatch to render in the
		// coordinate system specified by the camera.
		game.batch.setProjectionMatrix(camera.combined);

		// begin a new batch and draw the bob and all drops
		game.batch.begin();
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, frameHeight);
				
		for (Rectangle r : ground){
			game.batch.draw(groundImage, r.x, r.y);
		}
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(birdImage, raindrop.x, raindrop.y);
		}
		for (Rectangle tree : trees){
			game.batch.draw(treeImage, tree.x, tree.y);
		}
		for (Rectangle cloud : clouds){
			game.batch.draw(cloudImage, cloud.x, cloud.y);
		}
		for (Rectangle h : hearts){
			game.batch.draw(heart, h.x, h.y);
		}
		game.batch.draw(bobImage, bob.x, bob.y);

		game.batch.end();

		// process user input
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			// Vector3 touchPos = new Vector3();
			// Don't need most of this as based on one button press.
			// touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			// camera.unproject(touchPos);
			// bob.y = touchPos.y - bob.height / 2;
			touch = true;
			yVelocity = 7;
		}
		if (touch) {
			if (bob.y + bob.height >= frameHeight) {

				touch = false;
				yVelocity = 0;
			}

			yVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
		} else {
			if (bob.y > 1) {
				yVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
			} else {
				yVelocity = 0;
			}
		}
		bob.y += yVelocity;
		/*
		 * Probably won't use side movements if
		 * (Gdx.input.isKeyPressed(Keys.LEFT)) bob.x -= movement *
		 * Gdx.graphics.getDeltaTime(); if (Gdx.input.isKeyPressed(Keys.RIGHT))
		 * bob.x += movement * Gdx.graphics.getDeltaTime();
		 */

		// make sure the bob stays within the screen bounds
		if (bob.x < 0)
			bob.x = 0;
		if (bob.x > frameWidth - bob.width)
			bob.x = frameWidth - bob.width;

		if (bob.y < 0)
			bob.y = 0;
		if (bob.y > frameHeight - bob.height)
			bob.y = frameHeight - bob.height;

		
		ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
		
		if (init){
			initialiseGround();
		}
		for(Rectangle gr : ground){
			 gr.x -= movement * Gdx.graphics.getDeltaTime();
			 if(gr.x + gr.width < 0){
				 toRemove.add(gr);
			 }
			 if(gr.x + gr.width == frameWidth){
				 spawnGround();
			 }
		}
		if (TimeUtils.nanoTime() - lastTreeTime > 2000000000)
			spawnTree();

		if (TimeUtils.nanoTime() - lastCloudTime > 1700000000)
			spawnCloud();
		
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();
		
		spawnHearts();
		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bob. In the later case we increase the
		// value our drops counter and add a sound effect.

		for(Rectangle s : raindrops ){
			s.x -= movement * Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
			if (s.overlaps(bob)) {
				dropsGathered++;
				if(dropsGathered%10 == 0 && dropsGathered != 0 && lives <= 5){
					lives++;
				}
				//birdSong.play();
				toRemove.add(s);
			}
		}
		for(Rectangle s : trees ){
			s.x -= movement * Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
			/*if (s.overlaps(bob)){
				if(!overlapping) {
					if(lives == 0){
						movement = 0;
						//dispose();
					}
					else{
					overlapping = true;
					hearts.remove(0);
					lives--;
					System.out.println("Hearts: " + hearts.size());
					System.out.println("lives: " + lives);
					}
			}
			}*/
			else overlapping = false;		
		}
		
		for(Rectangle s : clouds ){
			s.x -= movement * Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
			if (s.overlaps(bob)){
				movement = 100;
			}
			else movement = 200;
		}
		ground.removeAll(toRemove);
		raindrops.removeAll(toRemove);
		trees.removeAll(toRemove);
		clouds.removeAll(toRemove);
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		//gameMusic.play();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		birdImage.dispose();
		bobImage.dispose();
		birdSong.dispose();
		gameMusic.dispose();
		groundImage.dispose();
		
		
	}

}