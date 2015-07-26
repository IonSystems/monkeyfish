package com.ionsystems.monkeyfish;

import java.util.ArrayList;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen extends DefaultScreen implements Screen {

	final MonkeyFishGame game;
	Stage stage;
	Texture treeImage, cloudImage, cloud2Image, cloud3Image, cloud4Image, groundImage, heart, blimpImage, planeImage, moonImage;
	Texture [] fruitImage = new Texture [12];
	Sound birdSong;
	Music gameMusic;
	OrthographicCamera camera;
	ArrayList<ArrayList<SpawnObject>> fruits;
	ArrayList<SpawnObject>  fruit1, fruit2, fruit3, fruit4, fruit5, fruit6, fruit7, fruit8, fruit9, fruit10, fruit11, fruit12;
	ArrayList<SpawnObject> trees, clouds, clouds2, clouds3, clouds4, hearts, grounds;
	ArrayList<AnimationSprite> flappies;
	SpawnObject plane;
	SpawnObject blimp, moon;
	int dropsGathered, frameHeight, frameWidth, lives;
	long lastTreeTime, lastCloudTime, lastFlappyTime, 
	lastMoonTime, flappySpawnRate, treeSpawnRate, cloudSpawnRate, lastFruitTime, fruitSpawnRate;
	long levelDistance, currentDistance;
	float btnPauseSx, btnPauseSy, verticalVelocity, movement, acceleration, initMovement;
	private boolean touch, antipodean, lockedHeight;
	int screenSize;
	String screenVersion;
	AnimationSprite player, sonic, crash;
	TextButton btnPause;
	TextureAtlas pauseAtlas;
	TextButtonStyle pauseStyle;
	BitmapFont font;
	Skin pauseSkin;
	Viewport viewport;
	Table hud;
	Label levelAnnounce;
	long startTime = TimeUtils.millis();
	int id = 0;

	private float player_hurt_timer;

	public GameScreen(final MonkeyFishGame gam, Table hud) {
		super(gam, hud);
		antipodean = SavedSettings.SETTING_UPSIDE_DOWN.getBoolean();
		this.game = gam;
		this.hud = hud;
		stage = new Stage();
		birdSong = setupSoundSetting();
		gameMusic = setupMusicSetting();
		frameHeight = Gdx.graphics.getHeight();
		frameWidth = Gdx.graphics.getWidth();
		initMovement = frameHeight / 2;
		movement = initMovement;
		// Load level settings
		acceleration = Levels.getCurrentLevel().getGravity();
		verticalVelocity = 100;// Levels.getCurrentLevel().getVerticalVelocity();
		lives = 2;//Levels.getCurrentLevel().getLives();
		flappySpawnRate = 1000000000;
		treeSpawnRate = 500000000;
		cloudSpawnRate = 5000000;
		fruitSpawnRate = 200000000;
		lockedHeight = Levels.getCurrentLevel().getInfiniteHeight(); // When set to false allows infinite height.
		// Overide option value if set, otherwise use the option value.
		antipodean = Levels.getCurrentLevel().getUpsideDownMode() ? true : antipodean;
		levelDistance = Levels.getCurrentLevel().getDistance();
		System.out.println("frameHeight: " + frameHeight);
		if (frameHeight < 480)
			screenVersion = "0.5";
		// need to also change the speeds and gravity accordingly
		else if (frameHeight < 600)
			screenVersion = "";
		else if (frameHeight < 1000)
			screenVersion = "1.5";
		else
			screenVersion = "2.0";
		setupImageTextures();
		System.out.println("setScreenSize = " + screenSize);
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, frameWidth, frameHeight);
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), camera);// TODO: Get width and height from somewhere
		player = new AnimationSprite(this.game.batch, antipodean, id++);
		player.generateAnimation("mario", "mario" + screenVersion + ".png", 5, 1);
		player.generateAnimation("mario_hurt", "mario_hurt" + screenVersion + ".png",3, 1);
		player.setCurrentAnimation("mario");
		sonic = new AnimationSprite(this.game.batch, antipodean, id++);
		sonic.generateAnimation("sonic", "sonic" + screenVersion + ".png", 6, 1);
		sonic.setCurrentAnimation("sonic");
		spawnSonic();
		crash = new AnimationSprite(this.game.batch, antipodean, id++);
		crash.generateAnimation("crash", "crash" + screenVersion + ".png", 8, 1);
		crash.setCurrentAnimation("crash");
		spawnCrash();
		plane = new SpawnObject(planeImage, frameWidth * MathUtils.random(5, 10),
				frameHeight - planeImage.getHeight() - (int) MathUtils.random(0, frameHeight / 3));
		blimp = new SpawnObject(blimpImage, frameWidth * MathUtils.random(2, 4),
				frameHeight - blimpImage.getHeight() - (int) MathUtils.random(0, frameHeight / 4));
		moon = new SpawnObject(moonImage, frameWidth * MathUtils.random(1, 2),
				frameHeight - moonImage.getHeight() - (int) MathUtils.random(0, frameHeight / 8));
		lastFlappyTime = TimeUtils.nanoTime();
		fruits = new ArrayList<ArrayList<SpawnObject>> ();
		//somehow initialise array of arraylists!!!
		//fruit = new ArrayList<SpawnObject> ()[12];
		//for(int i = 0; i < fruitImage.length; i++){
		fruit1 = new ArrayList<SpawnObject>();
		fruit2 = new ArrayList<SpawnObject>();
		fruit3 = new ArrayList<SpawnObject>();
		fruit4 = new ArrayList<SpawnObject>();
		fruit5 = new ArrayList<SpawnObject>();
		fruit6 = new ArrayList<SpawnObject>();
		fruit7 = new ArrayList<SpawnObject>();
		fruit8 = new ArrayList<SpawnObject>();
		fruit9 = new ArrayList<SpawnObject>();
		fruit10= new ArrayList<SpawnObject>();
		fruit11= new ArrayList<SpawnObject>();
		fruit12= new ArrayList<SpawnObject>();
			fruits.add(fruit1);
			fruits.add(fruit2);
			fruits.add(fruit3);
			fruits.add(fruit4);
			fruits.add(fruit5);
			fruits.add(fruit6);
			fruits.add(fruit7);
			fruits.add(fruit8);
			fruits.add(fruit9);
			fruits.add(fruit10);
			fruits.add(fruit11);
			fruits.add(fruit12);
		//}
		flappies = new ArrayList<AnimationSprite>();
		hearts = new ArrayList<SpawnObject>();
		trees = new ArrayList<SpawnObject>();
		clouds = new ArrayList<SpawnObject>();
		clouds2 = new ArrayList<SpawnObject>();
		clouds3 = new ArrayList<SpawnObject>();
		clouds4 = new ArrayList<SpawnObject>();
		grounds = new ArrayList<SpawnObject>();
		for (int i = 0; i < 5; i++) {
			trees.add(new SpawnObject(treeImage, (int) MathUtils.random(0, frameWidth), frameHeight / 6));
		}
		clouds.add(new SpawnObject(cloudImage, (int) MathUtils.random(0, frameWidth),
				frameHeight - cloudImage.getHeight() - (int) MathUtils.random(0, frameHeight / 2)));
		clouds2.add(new SpawnObject(cloud2Image, (int) MathUtils.random(0, frameWidth),
				frameHeight - cloudImage.getHeight() - (int) MathUtils.random(0, frameHeight / 2)));
		clouds3.add(new SpawnObject(cloud3Image, (int) MathUtils.random(0, frameWidth),
				frameHeight - cloudImage.getHeight() - (int) MathUtils.random(0, frameHeight / 2)));
		clouds4.add(new SpawnObject(cloud4Image, (int) MathUtils.random(0, frameWidth),
				frameHeight - cloudImage.getHeight() - (int) MathUtils.random(0, frameHeight / 2)));

		initialiseGround();

		Gdx.app.setLogLevel(Application.LOG_DEBUG);

		// Pause Button creation
		font = new BitmapFont(Gdx.files.internal("fonts/Arial.fnt"), false);
		pauseStyle = new TextButtonStyle();
		pauseSkin = new Skin();
		pauseStyle.font = font;
		btnPause = new TextButton("", pauseStyle);
		pauseAtlas = new TextureAtlas("buttons/pauseOut/pauseButton.pack");

		btnPause.setPosition((frameWidth - (0.12f * frameHeight)), (0.88f * frameHeight));
		btnPause.setSize((0.1f * frameHeight), (0.1f * frameHeight));

		stage.setViewport(viewport);
		stage.addActor(btnPause);
		stage.addActor(this.hud);
		levelAnnounce = new Label(Levels.getCurrentLevel().getName(), skin);
		levelAnnounce.setBounds(50, 50, 50, 20);
		stage.addActor(levelAnnounce);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);

		pauseSkin.addRegions(pauseAtlas);
		pauseStyle.up = pauseSkin.getDrawable("pause_button_up");
		pauseStyle.down = pauseSkin.getDrawable("pause_button_down");

		btnPause.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				Gdx.app.debug("gesture", "inside touchUp GameScreen");
				game.state = GameState.PAUSED;
			}
		});
		hud.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				Gdx.app.debug("gesture", "inside ssasa GameScreen");
				game.state = GameState.PAUSED;
			}
		});

		player.x = frameWidth / 2 - player.getWidth() / 2;
		player.y = (int) (0.7 * player.getHeight());

		// end Pause Button
	}

	private void setupImageTextures() {
		moonImage = new Texture(Gdx.files.internal("moon" + screenVersion + ".png"));
		treeImage = new Texture(Gdx.files.internal("tree" + screenVersion + ".png"));
		for (int i = 0; i < fruitImage.length; i++){
		fruitImage[i] = new Texture(Gdx.files.internal("fruit"+ i + "" + screenVersion + ".png"));
		}
		cloudImage = new Texture(Gdx.files.internal("cloud" + screenVersion + ".png"));
		cloud2Image = new Texture(Gdx.files.internal("cloudtwo" + screenVersion + ".png"));
		cloud3Image = new Texture(Gdx.files.internal("cloudthree" + screenVersion + ".png"));
		cloud4Image = new Texture(Gdx.files.internal("cloudfour" + screenVersion + ".png"));
		planeImage = new Texture(Gdx.files.internal("plane" + screenVersion + ".png"));
		blimpImage = new Texture(Gdx.files.internal("blimp" + screenVersion + ".png"));
		groundImage = new Texture(Gdx.files.internal("ground" + screenVersion + ".png"));
		heart = new Texture(Gdx.files.internal("heart" + screenVersion + ".png"));
	}

	private Sound setupSoundSetting() {
		Sound sound;
		sound = Gdx.audio.newSound(Gdx.files.internal("sound/sample2.wav"));
		return sound;
	}

	private Music setupMusicSetting() {
		Music music;
		music = Gdx.audio.newMusic(Gdx.files.internal("sound/sample.mp3"));
		music.setLooping(true);
		music.play();
		return music;
	}

	private void initialiseGround() {
		for (int i = 0; i < (int) (frameWidth / groundImage.getWidth()) + 2; i++) {
			grounds.add(new SpawnObject(groundImage, i * groundImage.getWidth(), 0));
		}
	}
	
	private void spawnSonic() {
		sonic.x = frameWidth * MathUtils.random(4, 8);
		sonic.y = (int) (0.5 * player.getHeight());
	}

	private void spawnCrash() {
		crash.x = frameWidth * MathUtils.random(3, 6);
		crash.y = (int) (0.7 * player.getHeight());
	}
	
	private void spawnPlane() {
		plane = new SpawnObject(planeImage, frameWidth * MathUtils.random(50, 100),
				frameHeight - planeImage.getHeight() - (int) MathUtils.random(0, frameHeight / 3));
	}

	private void spawnBlimp() {
		blimp = new SpawnObject(blimpImage, frameWidth * MathUtils.random(20, 40),
				frameHeight - blimpImage.getHeight() - (int) MathUtils.random(0, frameHeight / 4));
	}

	private void spawnMoon() {
		moon = new SpawnObject(moonImage, frameWidth * MathUtils.random(10, 20),
				frameHeight - moonImage.getHeight() - (int) MathUtils.random(0, frameHeight / 8));
	}

	private void spawnFlappy() {
		AnimationSprite flappy = new AnimationSprite(this.game.batch, antipodean, id++);
		flappy.generateAnimation("flappy", "flappy" + screenVersion + ".png", 3, 1);
		flappy.setCurrentAnimation("flappy");
		flappy.x = frameWidth;
		flappy.y = (int) MathUtils.random(player.getHeight(), frameHeight - flappy.getHeight());
		flappies.add(flappy);
		lastFlappyTime = TimeUtils.nanoTime();
	}

	private void spawnTree() {
		trees.add(new SpawnObject(treeImage, frameWidth + (int) MathUtils.random(0, frameWidth), frameHeight / 6));
		lastTreeTime = TimeUtils.nanoTime();
	}

	private void spawnFruit(){			
			int thisFruit = MathUtils.random(0, fruitImage.length-1);
			fruits.get(thisFruit).add(new SpawnObject(fruitImage[thisFruit], (int)MathUtils.random(-50, frameWidth), (int)MathUtils.random(-50, frameHeight)));
			//fruit.add(new SpawnObject(fruitImage[thisFruit], (int)MathUtils.random(-50, frameWidth), (int)MathUtils.random(-50, frameHeight)));
			lastFruitTime = TimeUtils.nanoTime();
	}
	
	private void spawnCloud() {
		clouds.add(new SpawnObject(cloudImage, frameWidth + (int) MathUtils.random(0, frameWidth),
				frameHeight - cloudImage.getHeight() - (int) MathUtils.random(0, frameHeight / 2)));
		clouds2.add(new SpawnObject(cloud2Image, frameWidth + (int) MathUtils.random(0, frameWidth),
				frameHeight - cloud2Image.getHeight() - (int) MathUtils.random(0, frameHeight / 3)));
		clouds3.add(new SpawnObject(cloud3Image, frameWidth + (int) MathUtils.random(0, frameWidth),
				frameHeight - cloud3Image.getHeight() - (int) MathUtils.random(0, frameHeight / 4)));
		clouds4.add(new SpawnObject(cloud4Image, frameWidth + (int) MathUtils.random(0, frameWidth),
				frameHeight - cloud4Image.getHeight() - (int) MathUtils.random(0, frameHeight / 5)));
		lastCloudTime = TimeUtils.nanoTime();
	}

	private void spawnHearts() {
		hearts.clear();
		for (int i = 0; i < lives; i++) {
			hearts.add(new SpawnObject(heart, 5 + (3 / 2) * heart.getWidth() * i,
					frameHeight - (3 / 2) * heart.getHeight()));
		}
	}

	public float setAntipodean(float height, float y) {
		if (antipodean) {
			return frameHeight - y - height;
		} else
			return y;
	}

	public void render(float delta) {
		currentDistance++;
		// System.out.println("State: " + game.state + ", " + game.getScreen() +
		// ", currD: " + currentDistance + ", lD" + levelDistance);
		if (currentDistance >= levelDistance) {
			game.state = GameState.NEXT_LEVEL;
			game.setScreen(new LevelCompleteScreen(game, hud)); // TODO: Should not need this, should be set from MonkeyFishGame
		}
		if (game.state == GameState.PLAYING) {
			long elapsedTime = TimeUtils.timeSinceMillis(startTime);
			checkSettings();
			Gdx.gl.glClearColor(0.4f, 0.4f, 0.7f, 1.5f);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			camera.update();
			stage.act();

			// tell the camera to update its matrices.

			// tell the SpriteBatch to render in the
			// coordinate system specified by the camera.
			game.batch.setProjectionMatrix(camera.combined);

			game.batch.begin();

			game.batch.draw(moonImage, moon.x, setAntipodean(moonImage.getHeight(), moon.y), moonImage.getWidth(),
					moonImage.getHeight(), 0, 0, moonImage.getWidth(), moonImage.getHeight(), false, antipodean);
			drawSprites(grounds, groundImage, false, antipodean);
			drawSprites(trees, treeImage, false, antipodean);
			drawSprites(clouds, cloudImage, false, antipodean);
			drawSprites(clouds2, cloud2Image, false, antipodean);
			drawSprites(clouds3, cloud3Image, false, antipodean);
			drawSprites(clouds4, cloud4Image, false, antipodean);
			drawSprites(flappies);

			game.batch.draw(planeImage, plane.x, setAntipodean(planeImage.getHeight(), plane.y), planeImage.getWidth(),
					planeImage.getHeight(), 0, 0, planeImage.getWidth(), planeImage.getHeight(), false, antipodean);
			game.batch.draw(blimpImage, blimp.x, setAntipodean(blimpImage.getHeight(), blimp.y), blimpImage.getWidth(),
					blimpImage.getHeight(), 0, 0, blimpImage.getWidth(), blimpImage.getHeight(), false, antipodean);
			player.render();
			sonic.render();
			crash.render();
			//drawSprites(fruit, fruitImage[fruit.size()%12], false, antipodean);
			drawSprites(fruits, fruitImage, false, antipodean);
			for (SpawnObject h : hearts) {
				game.batch.draw(heart, h.x, h.y);
			}
			game.font.draw(game.batch, "Birds Destroyed: " + dropsGathered, 10, frameHeight - 8);
			game.font.draw(game.batch, "Level: " + Levels.getCurrentLevel().getName() + "Time: " + elapsedTime / 1000,
					frameWidth / 2, frameHeight - 8);

			game.batch.end();

			if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
				touch = true;
				verticalVelocity = 7;
			}

			if (touch) {
				if (player.y + player.getHeight() >= frameHeight && lockedHeight) {
					touch = false;
					verticalVelocity = 0;
				}
				verticalVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
			} else {
				if (player.y > 0.7 * player.getHeight()) {
					verticalVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
				} else {
					verticalVelocity = 0;
				}
			}
			player.y += verticalVelocity;
			
			 // Probably won't use side movements if
			  if(Gdx.input.isKeyPressed(Keys.LEFT)) player.x -= movement *
			  Gdx.graphics.getDeltaTime(); 
			  if (Gdx.input.isKeyPressed(Keys.RIGHT)) player.x += movement *
			  Gdx.graphics.getDeltaTime();
			 

			if (player.y < (float) (0.7 * player.getHeight()))
				player.y = (float) (0.7 * player.getHeight());
			if (player.y >= frameHeight - player.getHeight() && lockedHeight)
				player.y = frameHeight - player.getHeight();

			ArrayList<SpawnObject> toRemove = new ArrayList<SpawnObject>();
			ArrayList<Integer> spritesRemove = new ArrayList<Integer>();

			for (int i = 0; i < grounds.size(); i++) {
				grounds.get(i).x -= movement * 0.5 * Gdx.graphics.getDeltaTime();
				if (grounds.get(i).x + grounds.get(i).width <= 0) {
					grounds.get(i).x += grounds.get(i).width * grounds.size();
				}
			}
			if (TimeUtils.nanoTime() - lastTreeTime > treeSpawnRate)
				spawnTree();

			if ((TimeUtils.nanoTime() - lastCloudTime) / 1000 > cloudSpawnRate)
				spawnCloud();

			if (TimeUtils.nanoTime() - lastFlappyTime > flappySpawnRate)
				spawnFlappy();

			spawnHearts();

			if (sonic.x + sonic.getWidth() < 0) {
				spawnSonic();
			}

			if (sonic.x < player.x + player.getWidth() && sonic.x > player.x && player.y <= sonic.getHeight()) {
				player.y += sonic.getHeight()-10;
				lives--;
				player.setCurrentAnimation("mario_hurt");
				player_hurt_timer = 1.6f;
			}
	
			if (crash.x + crash.getWidth() < 0) {
				spawnCrash();
				}

			if (crash.x < player.x + player.getWidth() && crash.x > player.x && player.y <= crash.getHeight()) {
				player.y += crash.getHeight();
				lives--;
				player.setCurrentAnimation("mario_hurt");
				player_hurt_timer = 1.6f;
			}
			if (lives < 0)lives = 0;
			if(lives == 0){
				// movement = 0;
				
				if(TimeUtils.nanoTime() - lastFruitTime > fruitSpawnRate){
					spawnFruit();
				}
			}
			
			sonic.x -= 2.0 * movement * Gdx.graphics.getDeltaTime();
			crash.x -= 1.5 * movement * Gdx.graphics.getDeltaTime();
			if(player_hurt_timer < 1){
				player.setCurrentAnimation("mario");
			}else{
				player_hurt_timer -= Gdx.graphics.getDeltaTime();
			}
			
			for (AnimationSprite flappy : flappies) {
				Rectangle tmp1 = new Rectangle(flappy.x, flappy.y, flappy.getWidth(), flappy.getHeight());
				Rectangle tmp2 = new Rectangle(player.x, player.y, player.getWidth(), player.getHeight());
				flappy.x -= movement * Gdx.graphics.getDeltaTime();
				if (flappy.x + flappy.getWidth() < 0) {
					spritesRemove.add(flappy.id);
				}
				if (tmp2.overlaps(tmp1)) {
					spritesRemove.add(flappy.id);
					if (dropsGathered % 10 == 0 && dropsGathered != 0 && lives < 5) {
						lives++;
					}
					dropsGathered++;//// broken!

					// birdSong.play();
				}
			}
			for (SpawnObject s : trees) {
				s.x -= movement * 0.5 * Gdx.graphics.getDeltaTime();
				if (s.x + s.width < 0)
					toRemove.add(s);
			}
			for (SpawnObject s : clouds) {
				s.x -= movement * 0.7 * Gdx.graphics.getDeltaTime();
				if (s.x + s.width < 0)
					toRemove.add(s);
				else
					movement = initMovement;
			}
			for (SpawnObject s : clouds2) {
				s.x -= movement * 0.6 * Gdx.graphics.getDeltaTime();
				if (s.x + s.width < 0)
					toRemove.add(s);
				else
					movement = initMovement;
			}
			for (SpawnObject s : clouds3) {
				s.x -= movement * 0.9 * Gdx.graphics.getDeltaTime();
				if (s.x + s.width < 0)
					toRemove.add(s);
				else
					movement = initMovement;
			}
			for (SpawnObject s : clouds4) {
				s.x -= movement * 0.65 * Gdx.graphics.getDeltaTime();
				if (s.x + s.width < 0)
					toRemove.add(s);
				else
					movement = initMovement;
			}

			blimp.x -= movement * 0.6 * Gdx.graphics.getDeltaTime();
			plane.x -= movement * 1.5 * Gdx.graphics.getDeltaTime();
			moon.x -= movement * 0.1 * Gdx.graphics.getDeltaTime();

			if (plane.x + plane.width < 0) {
				spawnPlane();
			}
			if (blimp.x + blimp.width < 0) {
				spawnBlimp();
			}
			if (moon.x + moon.width < 0) {
				spawnMoon();
			}

			// flappies.removeAll(spritesRemove);
			for (int i : spritesRemove) {
				for (int j = 0; j < flappies.size(); j++) {
					if (flappies.get(j).id == i) {
						flappies.remove(j);
					}
				}
			}
			trees.removeAll(toRemove);
			clouds.removeAll(toRemove);
			clouds2.removeAll(toRemove);
			clouds3.removeAll(toRemove);
			clouds4.removeAll(toRemove);
			stage.draw();
		}
	}

	private void drawSprites(ArrayList<SpawnObject> rects, Texture texture, boolean s, boolean antipodean) {
		for (SpawnObject rect : rects) {
			game.batch.draw(texture, rect.x, setAntipodean(texture.getHeight(), rect.y), texture.getWidth(),
					texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), s, antipodean);
		}
	}

	private void drawSprites(ArrayList<AnimationSprite> sprites) {
		for (AnimationSprite sprite : sprites) {
			sprite.render();
		}
	}

	private void drawSprites(ArrayList<ArrayList<SpawnObject>> fruits, Texture texture [], boolean s, boolean antipodean){
		for (ArrayList<SpawnObject>  fruit: fruits){
			for(SpawnObject piece : fruit){
				game.batch.draw(piece.Image, piece.x, setAntipodean(texture[fruits.indexOf(fruit)].getHeight(), piece.y), texture[fruits.indexOf(fruit)].getWidth(),
						texture[fruits.indexOf(fruit)].getHeight(), 0, 0, texture[fruits.indexOf(fruit)].getWidth(), texture[fruits.indexOf(fruit)].getHeight(), s, antipodean);
			}
		}
	}

	private void checkSettings() {
		// System.out.println(gameMusic.isPlaying());
		// Sound
		if (!SavedSettings.SETTING_SOUND.getBoolean()) {
			birdSong.pause();
		} else {
			birdSong.resume();
		}
		// Music
		if (!SavedSettings.SETTING_MUSIC.getBoolean()) {
			gameMusic.pause();
		}
	}

	public void resize(int width, int height) {
	}

	public void show() {
		// start the playback of the background music
		// when the screen is shown
		// gameMusic.play();
	}

	public void hide() {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
		birdSong.dispose();
		gameMusic.dispose();
		groundImage.dispose();
	}
}