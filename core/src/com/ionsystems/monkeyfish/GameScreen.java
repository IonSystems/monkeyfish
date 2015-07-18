package com.ionsystems.monkeyfish;

import java.util.ArrayList;
import java.util.Iterator;
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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

	final MonkeyFishGame game;

	Stage stage;
	Texture dropImage;
	Texture bucketImage;
	Texture treeImage;
	Texture cloudImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	ArrayList<Rectangle> raindrops, trees, clouds;
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
	
	//pause button variables
	TextButton btnPause;
	TextureAtlas pauseAtlas;
	TextButtonStyle pauseStyle;
	BitmapFont font;
	Skin pauseSkin;

	private boolean touch;

	public GameScreen(final MonkeyFishGame gam) {
		this.game = gam;

		stage = new Stage();
		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("bird.png"));
		bucketImage = new Texture(Gdx.files.internal("bobargb8888-32x32.png"));
		treeImage = new Texture(Gdx.files.internal ("tree.png"));
		cloudImage = new Texture(Gdx.files.internal ("cloud.png"));
		// load the drop sound effect and the rain background "music"
		dropSound = Gdx.audio.newSound(Gdx.files.internal("sound/sample2.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/sample.mp3"));
		rainMusic.setLooping(true);

		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, frameWidth, frameHeight);

		// create a Rectangle to logically represent the bucket
		bucket = new Rectangle();
		bucket.width = bucketImage.getWidth();
		bucket.height = bucketImage.getHeight();

		bucket.x = frameWidth / 2 - (bucket.width) / 2; // center the bucket
														// horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge

		// create the raindrops array and spawn the first raindrop
		raindrops = new ArrayList<Rectangle>();
		trees = new ArrayList<Rectangle>();
		clouds = new ArrayList<Rectangle>();
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
		
		stage.addActor(btnPause);
		//end Pause Button
	}

	private void spawnRaindrop() {
		raindrops.add(new spawnObject(dropImage, frameWidth, (int)MathUtils.random(0, frameHeight - bucket.height)));
		lastDropTime = TimeUtils.nanoTime();
	}
	
	private void spawnTree() {
		trees.add(new spawnObject(treeImage, frameWidth, 0));
		lastTreeTime = TimeUtils.nanoTime();
	}
	
	private void spawnCloud(){
		clouds.add(new spawnObject(cloudImage, frameWidth, ((int)frameHeight/2 + (int)MathUtils.random(0, frameHeight/2 - cloudImage.getHeight()))));
		lastCloudTime = TimeUtils.nanoTime();
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

		// begin a new batch and draw the bucket and
		// all drops
		game.batch.begin();
		game.font.draw(game.batch, "Drops Collected: " + dropsGathered, 0, frameHeight);
		game.batch.draw(bucketImage, bucket.x, bucket.y);
		for (Rectangle raindrop : raindrops) {
			game.batch.draw(dropImage, raindrop.x, raindrop.y);
		}
		for (Rectangle tree : trees){
			game.batch.draw(treeImage, tree.x, tree.y);
		}
		for (Rectangle cloud : clouds){
			game.batch.draw(cloudImage, cloud.x, cloud.y);
		}
		game.batch.end();

		// process user input
		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			// Vector3 touchPos = new Vector3();
			// Don't need most of this as based on one button press.
			// touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			// camera.unproject(touchPos);
			// bucket.y = touchPos.y - bucket.height / 2;
			touch = true;
			yVelocity = 7;
		}
		if (touch) {
			if (bucket.y + bucket.height >= frameHeight) {

				touch = false;
				yVelocity = 0;
			}

			yVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
		} else {
			if (bucket.y > 1) {
				yVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
			} else {
				yVelocity = 0;
			}
		}
		bucket.y += yVelocity;
		/*
		 * Probably won't use side movements if
		 * (Gdx.input.isKeyPressed(Keys.LEFT)) bucket.x -= movement *
		 * Gdx.graphics.getDeltaTime(); if (Gdx.input.isKeyPressed(Keys.RIGHT))
		 * bucket.x += movement * Gdx.graphics.getDeltaTime();
		 */

		// make sure the bucket stays within the screen bounds
		if (bucket.x < 0)
			bucket.x = 0;
		if (bucket.x > frameWidth - bucket.width)
			bucket.x = frameWidth - bucket.width;

		if (bucket.y < 0)
			bucket.y = 0;
		if (bucket.y > frameHeight - bucket.height)
			bucket.y = frameHeight - bucket.height;

		// check if we need to create a new raindrop
		if (TimeUtils.nanoTime() - lastTreeTime > 2000000000)
			spawnTree();

		if (TimeUtils.nanoTime() - lastCloudTime > 1700000000)
			spawnCloud();
		
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();
		
		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we increase the
		// value our drops counter and add a sound effect.
		ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
		for(Rectangle s : raindrops ){
			s.x -= movement * Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
			if (s.overlaps(bucket)) {
				dropsGathered++;
				//dropSound.play();
				toRemove.add(s);
			}
		}
		for(Rectangle s : trees ){
			s.x -= movement * Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
		}
		for(Rectangle s : clouds ){
			s.x -= movement * Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
		}
		
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
		//rainMusic.play();
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
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.dispose();
	}

}