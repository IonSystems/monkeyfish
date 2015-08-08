package com.ionsystems.monkeyfish.screens;

import java.util.ArrayList;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
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
import com.ionsystems.monkeyfish.File;
import com.ionsystems.monkeyfish.GameState;
import com.ionsystems.monkeyfish.Levels;
import com.ionsystems.monkeyfish.MonkeyFishGame;
import com.ionsystems.monkeyfish.SavedSettings;
import com.ionsystems.monkeyfish.sprites.AnimationSprite;
import com.ionsystems.monkeyfish.sprites.SpawnObject;

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
	float accelX;
	float accelY;
	float accelZ;
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
	FileHandle flappyImage;

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
		accelX = Gdx.input.getAccelerometerX();
		accelY = Gdx.input.getAccelerometerY();
		accelZ = Gdx.input.getAccelerometerZ();
		// Load level settings
		acceleration = 40;//Levels.getCurrentLevel().getGravity();
		verticalVelocity = 1000;// Levels.getCurrentLevel().getVerticalVelocity();
		lives = 2;//Levels.getCurrentLevel().getLives();
		flappySpawnRate = 1000000000;
		treeSpawnRate = 500000000;
		cloudSpawnRate = 5000000;
		fruitSpawnRate = 100000000;
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
		flappyImage = File.getInstance().getFile("flappy" + screenVersion);
		System.out.println("setScreenSize = " + screenSize);
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, frameWidth, frameHeight);
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), camera);
		player = new AnimationSprite(this.game.batch, antipodean, id++);

		player.generateAnimation("mario", File.getInstance().getFile("mario" + screenVersion + ".png"), 5, 1);
		player.generateAnimation("mario_hurt", File.getInstance().getFile("mario_hurt" + screenVersion + ".png"),3, 1);
		player.generateAnimation("mario_backwards", File.getInstance().getFile("mariobackwards" + screenVersion + ".png"), 5, 1);
		player.generateAnimation("mario_hurt_backwards", File.getInstance().getFile("mario_hurt_backwards" + screenVersion + ".png"),3, 1);

		player.setCurrentAnimation("mario");
		sonic = new AnimationSprite(this.game.batch, antipodean, id++);
		sonic.generateAnimation("sonic", File.getInstance().getFile("sonic" + screenVersion), 6, 1);
		sonic.setCurrentAnimation("sonic");
		spawnSonic();
		crash = new AnimationSprite(this.game.batch, antipodean, id++);
		crash.generateAnimation("crash", File.getInstance().getFile("crash" + screenVersion), 8, 1);
		crash.setCurrentAnimation("crash");
		spawnCrash();
		crash = new AnimationSprite(this.game.batch, antipodean, id++);
		crash.generateAnimation("crash", File.getInstance().getFile("crash" + screenVersion), 8, 1);
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

		//btnPause.setPosition((frameWidth - (0.12f * frameHeight)), (0.88f * frameHeight));
		//btnPause.setSize((0.1f * frameHeight), (0.1f * frameHeight));

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
				game.setState(GameState.PAUSED);
			}
		});
		hud.addListener(new ClickListener() {
			public void clicked(InputEvent e, float x, float y) {
				Gdx.app.debug("gesture", "inside ssasa GameScreen");
				game.setState(GameState.PAUSED);
			}
		});

		player.setX(frameWidth / 2 - player.getWidth() / 2);
		player.setY((int) (0.7 * player.getHeight()));

		// end Pause Button
	}

	private void setupImageTextures() {
		moonImage = new Texture(File.getInstance().getFile("moon" + screenVersion));
		treeImage = new Texture(File.getInstance().getFile("tree" + screenVersion));
		for (int i = 0; i < fruitImage.length; i++){
		fruitImage[i] = new Texture(File.getInstance().getFile("fruit" + i + "(" + screenVersion + ")"));
		}
		cloudImage = new Texture(File.getInstance().getFile("cloud" + screenVersion));
		cloud2Image = new Texture(File.getInstance().getFile("cloud2" + screenVersion));
		cloud3Image = new Texture(File.getInstance().getFile("cloud3" + screenVersion));
		cloud4Image = new Texture(File.getInstance().getFile("cloud4" + screenVersion));
		planeImage = new Texture(File.getInstance().getFile("cloud" + screenVersion));
		blimpImage = new Texture(File.getInstance().getFile("blimp" + screenVersion));
		groundImage = new Texture(File.getInstance().getFile("ground" + screenVersion));
		heart = new Texture(File.getInstance().getFile("heart" + screenVersion));
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
		sonic.setX(frameWidth * MathUtils.random(4, 8));
		sonic.setY((int) (0.5 * player.getHeight()));
	}

	private void spawnCrash() {
		crash.setX(frameWidth * MathUtils.random(3, 6));
		crash.setY((int) (0.7 * player.getHeight()));
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
		flappy.generateAnimation("flappy", flappyImage, 3, 1);
		flappy.setCurrentAnimation("flappy");
		flappy.setX(frameWidth);
		flappy.setY((int) MathUtils.random(player.getHeight(), frameHeight - flappy.getHeight()));
		flappies.add(flappy);
		lastFlappyTime = TimeUtils.nanoTime();
	}

	private void spawnTree() {
		trees.add(new SpawnObject(treeImage, frameWidth + (int) MathUtils.random(0, frameWidth), frameHeight / 6));
		lastTreeTime = TimeUtils.nanoTime();
	}

	private void spawnFruit(){			
			int thisFruit = MathUtils.random(0, fruitImage.length-1);
			for (int i = 0; i < fruitImage.length; i++){
				if(i == thisFruit){
					fruits.get(thisFruit).add(new SpawnObject(fruitImage[thisFruit], (int)MathUtils.random(-50, frameWidth), (int)MathUtils.random(-50, frameHeight)));
				}
			}
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
			game.setState(GameState.NEXT_LEVEL);
			game.setScreen(new LevelCompleteScreen(game, hud)); // TODO: Should not need this, should be set from MonkeyFishGame
		}
		if (game.getState() == GameState.PLAYING) {
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

			game.batch.draw(moonImage, moon.getX(), setAntipodean(moonImage.getHeight(), moon.getY()), moonImage.getWidth(),
					moonImage.getHeight(), 0, 0, moonImage.getWidth(), moonImage.getHeight(), false, antipodean);
			drawSprites(grounds, groundImage, false, antipodean);
			drawSprites(trees, treeImage, false, antipodean);
			drawSprites(clouds, cloudImage, false, antipodean);
			drawSprites(clouds2, cloud2Image, false, antipodean);
			drawSprites(clouds3, cloud3Image, false, antipodean);
			drawSprites(clouds4, cloud4Image, false, antipodean);
			drawSprites(flappies);

			game.batch.draw(planeImage, plane.getX(), setAntipodean(planeImage.getHeight(), plane.getY()), planeImage.getWidth(),
					planeImage.getHeight(), 0, 0, planeImage.getWidth(), planeImage.getHeight(), false, antipodean);
			game.batch.draw(blimpImage, blimp.getX(), setAntipodean(blimpImage.getHeight(), blimp.getY()), blimpImage.getWidth(),
					blimpImage.getHeight(), 0, 0, blimpImage.getWidth(), blimpImage.getHeight(), false, antipodean);
			player.render();
			sonic.render();
			crash.render();
			//drawSprites(fruit, fruitImage[fruit.size()%12], false, antipodean);
			drawSprites(fruits, fruitImage, false, antipodean);
			for (SpawnObject h : hearts) {
				game.batch.draw(heart, h.getX(), h.getY());
			}
			game.font.draw(game.batch, "Birds Destroyed: " + dropsGathered, 10, frameHeight - 8);
			game.font.draw(game.batch, "Level: " + Levels.getCurrentLevel().getName() + "Time: " + elapsedTime / 1000,
					frameWidth / 2, frameHeight - 8);

			game.batch.end();

			if (Gdx.input.isButtonPressed(Buttons.LEFT)|| Gdx.input.isKeyPressed(Keys.SPACE)) {
				touch = true;
				verticalVelocity = 10;
			}

			if (touch) {
				if (player.getY() + player.getHeight() >= frameHeight && lockedHeight) {
					touch = false;
					verticalVelocity = 0;
				}
				verticalVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
			} else {
				if (player.getY() > 0.7 * player.getHeight()) {
					verticalVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
				} else {
					verticalVelocity = 0;
				}
			}
			player.setY(player.getY() + verticalVelocity);
			
			if (player.x + player.getWidth() < 15){
				player.x = -player.getWidth() + 15;
			}
			if (player.x > frameWidth - 15){
				player.x = frameWidth - 15;
			}
			
			 // Probably won't use side movements if
<<<<<<< HEAD:core/src/com/ionsystems/monkeyfish/GameScreen.java
			if(Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
				float currentY = Gdx.input.getAccelerometerY();
				if(currentY <= 0.1){
					player.x -= movement * Gdx.graphics.getDeltaTime(); 
					player.setCurrentAnimation("mario_backwards");
				}
				else if (currentY > 0.9){
					player.x += movement * Gdx.graphics.getDeltaTime();
					player.setCurrentAnimation("mario");
					}
			}
			else 
			  if(Gdx.input.isKeyPressed(Keys.LEFT)) {
				  player.setCurrentAnimation("mario_backwards");
				  player.x -= 1.2f * movement * Gdx.graphics.getDeltaTime();
				 // movement  *= 0.5;
			  }
			  if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
				  player.setCurrentAnimation("mario");
				  player.x += 0.7 * movement * Gdx.graphics.getDeltaTime();
			  }
				 
			if (player.y < (float) (0.7 * player.getHeight()))
				player.y = (float) (0.7 * player.getHeight());
			if (player.y >= frameHeight - player.getHeight() && lockedHeight)
				player.y = frameHeight - player.getHeight();
=======
			  if(Gdx.input.isKeyPressed(Keys.LEFT)) player.setX(player.getX() - movement *
			  Gdx.graphics.getDeltaTime()); 
			  if (Gdx.input.isKeyPressed(Keys.RIGHT)) player.setX(player.getX() + movement *
			  Gdx.graphics.getDeltaTime());
			 

			if (player.getY() < (float) (0.7 * player.getHeight()))
				player.setY((float) (0.7 * player.getHeight()));
			if (player.getY() >= frameHeight - player.getHeight() && lockedHeight)
				player.setY(frameHeight - player.getHeight());
>>>>>>> origin/master:core/src/com/ionsystems/monkeyfish/screens/GameScreen.java

			ArrayList<SpawnObject> toRemove = new ArrayList<SpawnObject>();
			ArrayList<Integer> spritesRemove = new ArrayList<Integer>();

			for (int i = 0; i < grounds.size(); i++) {
				grounds.get(i).setX(grounds.get(i).getX() - movement * 0.5 * Gdx.graphics.getDeltaTime());
				if (grounds.get(i).getX() + grounds.get(i).getWidth() <= 0) {
					grounds.get(i).setX(grounds.get(i).getX() + grounds.get(i).getWidth() * grounds.size());
				}
			}
			if (TimeUtils.nanoTime() - lastTreeTime > treeSpawnRate)
				spawnTree();

			if ((TimeUtils.nanoTime() - lastCloudTime) / 1000 > cloudSpawnRate)
				spawnCloud();

			if (TimeUtils.nanoTime() - lastFlappyTime > flappySpawnRate)
				spawnFlappy();

			spawnHearts();

			if (sonic.getX() + sonic.getWidth() < 0) {
				spawnSonic();
			}

			if (sonic.getX() < player.getX() + player.getWidth() && sonic.getX() > player.getX() && player.getY() <= sonic.getHeight()) {
				player.setY(player.getY() + (sonic.getHeight()-10));
				lives--;
				if(player.currentAnimation.name=="mario"){////Need to add mario walking hurt...
					player.setCurrentAnimation("mario_hurt");
				}
				else if (player.currentAnimation.name=="mario_backwards"){
					player.setCurrentAnimation("mario_hurt_backwards");
				}
				player_hurt_timer = 1.6f;
			}
	
			if (crash.getX() + crash.getWidth() < 0) {
				spawnCrash();
				}

			if (crash.getX() < player.getX() + player.getWidth() && crash.getX() > player.getX() && player.getY() <= crash.getHeight()) {
				player.setY(player.getY() + crash.getHeight());
				lives--;
				if(player.currentAnimation.name=="mario"){
				player.setCurrentAnimation("mario_hurt");
				}
				else if (player.currentAnimation.name=="mario_backwards"){
					player.setCurrentAnimation("mario_hurt_backwards");
				}
				player_hurt_timer = 1.6f;
			}
			if (lives < 0)lives = 0;
			if(lives == 0){		
				//Shake device to get rid of fruit if gyroscope exists otherwise 
				//press the 'x' key on the keyboard.
				float currentX = Gdx.input.getAccelerometerX()-accelX;
				float currentY = Gdx.input.getAccelerometerY()-accelY;
				float currentZ = Gdx.input.getAccelerometerZ()-accelZ;
				
				if(TimeUtils.nanoTime() - lastFruitTime > fruitSpawnRate){
					spawnFruit();
				}
				if(currentY > 0.8 && currentZ > 0.8 || Gdx.input.isKeyPressed(Keys.X)){
					emptyFruit(fruits);
					lives = 2;
					}
				accelX = Gdx.input.getAccelerometerX();
				accelY = Gdx.input.getAccelerometerY();
				accelZ = Gdx.input.getAccelerometerZ();
			}
			
<<<<<<< HEAD:core/src/com/ionsystems/monkeyfish/GameScreen.java
			sonic.x -= 2.0 * movement * Gdx.graphics.getDeltaTime();
			crash.x -= 1.5 * movement * Gdx.graphics.getDeltaTime();
			if(player_hurt_timer <= 0){
				if(player.currentAnimation.name == "mario_hurt"){
=======
			sonic.setX((float)(sonic.getX() - 2.0 * movement * Gdx.graphics.getDeltaTime()));
			crash.setX((float)(crash.getX() - 1.5 * movement * Gdx.graphics.getDeltaTime()));
			if(player_hurt_timer < 1){
>>>>>>> origin/master:core/src/com/ionsystems/monkeyfish/screens/GameScreen.java
				player.setCurrentAnimation("mario");
				}
				else if(player.currentAnimation.name == "mario_hurt_backwards"){
					player.setCurrentAnimation("mario_backwards");
					}
			}else{
				player_hurt_timer -= Gdx.graphics.getDeltaTime();
			}
			
			for (AnimationSprite flappy : flappies) {
				Rectangle tmp1 = new Rectangle(flappy.getX(), flappy.getY(), flappy.getWidth(), flappy.getHeight());
				Rectangle tmp2 = new Rectangle(player.getX(), player.getY(), player.getWidth(), player.getHeight());
				flappy.setX(flappy.getX() - movement * Gdx.graphics.getDeltaTime());
				if (flappy.getX() + flappy.getWidth() < 0) {
					spritesRemove.add(flappy.getId());
				}
				if (tmp2.overlaps(tmp1)) {
					spritesRemove.add(flappy.getId());
					if (dropsGathered % 10 == 0 && dropsGathered != 0 && lives < 5) {
						lives++;
					}
					dropsGathered++;//// broken!

					// birdSong.play();
				}
			}
			for (SpawnObject s : trees) {
				s.setX(s.getX() - movement * 0.5 * Gdx.graphics.getDeltaTime());
				if (s.getX() + s.getWidth() < 0)
					toRemove.add(s);
			}
			for (SpawnObject s : clouds) {
				s.setX(s.getX() - movement * 0.7 * Gdx.graphics.getDeltaTime());
				if (s.getX() + s.getWidth() < 0)
					toRemove.add(s);
				else
					movement = initMovement;
			}
			for (SpawnObject s : clouds2) {
				s.setX(s.getX() - movement * 0.6 * Gdx.graphics.getDeltaTime());
				if (s.getX() + s.getWidth() < 0)
					toRemove.add(s);
				else
					movement = initMovement;
			}
			for (SpawnObject s : clouds3) {
				s.setX(s.getX() - movement * 0.9 * Gdx.graphics.getDeltaTime());
				if (s.getX() + s.getWidth() < 0)
					toRemove.add(s);
				else
					movement = initMovement;
			}
			for (SpawnObject s : clouds4) {
				s.setX(s.getX() - movement * 0.65 * Gdx.graphics.getDeltaTime());
				if (s.getX() + s.getWidth() < 0)
					toRemove.add(s);
				else
					movement = initMovement;
			}

			blimp.setX(blimp.getX() - movement * 0.6 * Gdx.graphics.getDeltaTime());
			plane.setX(plane.getX() - movement * 1.5 * Gdx.graphics.getDeltaTime());
			moon.setX(moon.getX() - movement * 0.1 * Gdx.graphics.getDeltaTime());

			if (plane.getX() + plane.getWidth() < 0) {
				spawnPlane();
			}
			if (blimp.getX() + blimp.getWidth() < 0) {
				spawnBlimp();
			}
			if (moon.getX() + moon.getWidth() < 0) {
				spawnMoon();
			}

			// flappies.removeAll(spritesRemove);
			for (int i : spritesRemove) {
				for (int j = 0; j < flappies.size(); j++) {
					if (flappies.get(j).getId() == i) {
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
			game.batch.draw(texture, rect.getX(), setAntipodean(texture.getHeight(), rect.getY()), texture.getWidth(),
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
			//for(SpawnObject piece : fruit){
			  for(int i = 0; i < fruit.size(); i++){
				if(!fruit.isEmpty()){
				game.batch.draw(fruitImage[i%12], 
						fruit.get(i).getX(), 
						setAntipodean(texture[i%12].getHeight(), fruit.get(i).getY()), 
						texture[i%12].getWidth(),
						texture[i%12].getHeight(), 0, 0, texture[i%12].getWidth(), texture[i%12].getHeight(), s, antipodean);
				}
			}
		}
	}

	private void emptyFruit(ArrayList<ArrayList<SpawnObject>> fruits){
		for (ArrayList<SpawnObject> fruit: fruits){
			fruit.clear();
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