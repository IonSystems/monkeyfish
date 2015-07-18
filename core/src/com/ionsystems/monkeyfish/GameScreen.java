package com.ionsystems.monkeyfish;

import java.util.Iterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {
	final MonkeyFishGame game;

	Stage stage;
	Texture dropImage;
	Texture bucketImage;
	Sound dropSound;
	Music rainMusic;
	OrthographicCamera camera;
	Rectangle bucket;
	Array<Rectangle> raindrops;
	Texture textureUp;
	Texture textureDown;
	Texture background;
	MyButton myButton;
	MyButton btnPause;
	long lastDropTime;
	int dropsGathered;
	int frameHeight = 480;
	int frameWidth = 800;
	int movement = 200;
	float yVelocity = 0;
	int acceleration = 10;

	private boolean touch;

	public GameScreen(final MonkeyFishGame gam) {
		this.game = gam;

		stage = new Stage();
		// load the images for the droplet and the bucket, 64x64 pixels each
		dropImage = new Texture(Gdx.files.internal("badlogic.jpg"));
		bucketImage = new Texture(Gdx.files.internal("badlogic.jpg"));

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

		/* Testing */
		System.out.println("Width: " + bucket.width + ", Height: " + bucket.height);
		bucket.x = frameWidth / 2 - (bucket.width) / 2; // center the bucket
														// horizontally
		bucket.y = 20; // bottom left corner of the bucket is 20 pixels above
						// the bottom screen edge

		// create the raindrops array and spawn the first raindrop
		raindrops = new Array<Rectangle>();
		spawnRaindrop();

		textureUp = new Texture(Gdx.files.internal("pause_button_up.png"));
		textureDown = new Texture(Gdx.files.internal("pause_button_down.png"));
		background = new Texture(Gdx.files.internal("pause_button_background.png"));
		btnPause = new MyButton(textureUp, textureDown, background);
		btnPause.setPosition(400, 400);
		btnPause.setSize(50, 50);

		stage.addActor(btnPause);

	}

	private void spawnRaindrop() {
		Rectangle raindrop = new Rectangle();
		raindrop.y = MathUtils.random(0, frameHeight - bucket.height);
		raindrop.x = frameWidth;
		raindrop.width = dropImage.getWidth();
		raindrop.height = dropImage.getHeight();
		raindrops.add(raindrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render(float delta) {
		// clear the screen with a dark blue color. The
		// arguments to glClearColor are the red, green
		// blue and alpha component in the range [0,1]
		// of the color to be used to clear the screen.
		Gdx.gl.glClearColor(0, 0, 0.2f, 1);
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
		if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
			spawnRaindrop();

		// move the raindrops, remove any that are beneath the bottom edge of
		// the screen or that hit the bucket. In the later case we increase the
		// value our drops counter and add a sound effect.
		Iterator<Rectangle> iter = raindrops.iterator();
		while (iter.hasNext()) {
			Rectangle raindrop = iter.next();
			raindrop.x -= movement * Gdx.graphics.getDeltaTime();
			if (raindrop.x + raindrop.width < 0)
				iter.remove();
			if (raindrop.overlaps(bucket)) {
				dropsGathered++;
				dropSound.play();
				iter.remove();
			}
		}
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		rainMusic.play();
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