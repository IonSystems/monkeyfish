package com.ionsystems.monkeyfish;

import java.util.ArrayList;
import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen implements Screen {

	final MonkeyFishGame game;

	Stage stage;
	Texture birdImage, bobImage, treeImage, cloudImage, cloud2Image, cloud3Image, cloud4Image;
	Texture groundImage, heart, background, textureUp, textureDown, blimpImage, planeImage, moonImage;
	Sound birdSong;
	Music gameMusic;
	OrthographicCamera camera;
	ArrayList<Rectangle> birds, trees, clouds, clouds2, clouds3, clouds4, hearts, grounds;
	ArrayList<AnimationSprite> flappies;
	Rectangle plane, blimp, moon;
	int dropsGathered, frameHeight, frameWidth, movement, acceleration, lives, initMovement;
	long lastTreeTime, lastCloudTime, lastBirdTime, lastPlaneTime, lastBlimpTime, lastFlappyTime, lastMoonTime;
	float btnPauseSx, btnPauseSy, verticalVelocity;
	private boolean touch, antipodean, lockedHeight;
	AnimationSprite player;
	TextButton btnPause;
	TextureAtlas pauseAtlas;
	TextButtonStyle pauseStyle;
	BitmapFont font;
	Skin pauseSkin;
	Viewport viewport;
	Table hud;
	public GameScreen(final MonkeyFishGame gam, Table hud) {
		antipodean = false;
		this.game = gam;
		player = new AnimationSprite(this.game.batch, 5, 1,"mario(half).png", antipodean);
		this.hud = hud;
		stage = new Stage();
		setupImageTextures();
		birdSong = setupSoundSetting();
		gameMusic = setupMusicSetting();
		
		frameHeight = Gdx.graphics.getHeight();
		frameWidth = Gdx.graphics.getWidth();
		initMovement = frameHeight/2;
		movement = initMovement;
		acceleration = 12;
		verticalVelocity = 0;
		lives = 5;

		lockedHeight = true;  //When set to false allows infinite height.
		
		// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		camera.setToOrtho(false, frameWidth, frameHeight);
		viewport = new ExtendViewport(Gdx.graphics.getWidth(), Gdx.graphics.getWidth(), camera);//TODO: get width and height from somewhere
		// create a Rectangle to logically represent the bob
		plane = new Rectangle(frameWidth*MathUtils.random(50, 100), frameHeight - planeImage.getHeight() - (int)MathUtils.random(0, frameHeight/3), planeImage.getWidth(), planeImage.getHeight());
		blimp = new Rectangle(frameWidth*MathUtils.random(20,40), frameHeight - blimpImage.getHeight() - (int)MathUtils.random(0, frameHeight/4), blimpImage.getWidth(), blimpImage.getHeight());
		moon = new Rectangle(frameWidth*MathUtils.random(10,20), frameHeight - moonImage.getHeight() - (int)MathUtils.random(0, frameHeight/4), moonImage.getWidth(), moonImage.getHeight());
		lastFlappyTime = TimeUtils.nanoTime();
		flappies = new ArrayList<AnimationSprite>();
		hearts = new ArrayList<Rectangle>();
		birds = new ArrayList<Rectangle>();
		trees = new ArrayList<Rectangle>();
		clouds = new ArrayList<Rectangle>();
		clouds2 = new ArrayList<Rectangle>();
		clouds3 = new ArrayList<Rectangle>();
		clouds4 = new ArrayList<Rectangle>();
		grounds = new ArrayList<Rectangle>();
		for(int i = 0; i < 5; i++){
			trees.add(new SpawnObject(treeImage,(int)MathUtils.random(0, frameWidth), frameHeight/6));
		}
		clouds.add(new SpawnObject(cloudImage, (int)MathUtils.random(0, frameWidth), frameHeight - cloudImage.getHeight() - (int)MathUtils.random(0,frameHeight/2)));
		clouds2.add(new SpawnObject(cloud2Image,(int)MathUtils.random(0, frameWidth), frameHeight - cloudImage.getHeight() - (int)MathUtils.random(0,frameHeight/2)));
		clouds3.add(new SpawnObject(cloud3Image, (int)MathUtils.random(0, frameWidth), frameHeight - cloudImage.getHeight() - (int)MathUtils.random(0,frameHeight/2)));
		clouds4.add(new SpawnObject(cloud4Image, (int)MathUtils.random(0, frameWidth), frameHeight - cloudImage.getHeight() - (int)MathUtils.random(0,frameHeight/2)));

		initialiseGround();
		
		Gdx.app.setLogLevel(Application.LOG_DEBUG);
		
		//Pause Button creation
		font = new BitmapFont(Gdx.files.internal("fonts/Arial.fnt"), false);
		pauseStyle = new TextButtonStyle();
		pauseSkin = new Skin();
		pauseStyle.font = font;
		btnPause = new TextButton("", pauseStyle);
		pauseAtlas = new TextureAtlas("buttons/pauseOut/pauseButton.pack");
		
		btnPause.setPosition((frameWidth-(0.12f*frameHeight)) , (0.88f*frameHeight));
		btnPause.setSize((0.1f*frameHeight),(0.1f*frameHeight));
		
		stage.setViewport(viewport);
		stage.addActor(btnPause);
		stage.addActor(this.hud);
		InputMultiplexer inputMultiplexer = new InputMultiplexer();
		inputMultiplexer.addProcessor(stage);
		Gdx.input.setInputProcessor(inputMultiplexer);
		
		pauseSkin.addRegions(pauseAtlas);
		pauseStyle.up = pauseSkin.getDrawable("pause_button_up");
		pauseStyle.down = pauseSkin.getDrawable("pause_button_down");
		
		btnPause.addListener(new ClickListener() {
        	public void clicked(InputEvent e, float x, float y){
        		Gdx.app.debug("gesture", "inside touchUp GameScreen");
        		game.state = GameState.PAUSED;
        	}
			});
		hud.addListener(new ClickListener() {
        	public void clicked(InputEvent e, float x, float y){
        		Gdx.app.debug("gesture", "inside ssasa GameScreen");
        		game.state = GameState.PAUSED;
        	}
			});
		
		
		player.create();
		player.x = frameWidth/2-player.width/2;
		player.y = (int)(0.7*player.height);

		//flappy.create();
		
		//end Pause Button
	}

	private void setupImageTextures() {
		moonImage = new Texture(Gdx.files.internal("moon.png"));
		birdImage = new Texture(Gdx.files.internal("bird.png"));
		treeImage = new Texture(Gdx.files.internal ("tree.png"));
		cloudImage = new Texture(Gdx.files.internal ("cloud.png"));
		cloud2Image = new Texture(Gdx.files.internal ("cloud2.png"));
		cloud3Image = new Texture(Gdx.files.internal ("cloud3.png"));
		cloud4Image = new Texture(Gdx.files.internal ("cloud4.png"));
		planeImage = new Texture(Gdx.files.internal ("plane.png"));
		blimpImage = new Texture(Gdx.files.internal("blimp.png"));
		groundImage = new Texture(Gdx.files.internal ("ground1.png"));
		heart = new Texture(Gdx.files.internal ("heart.png"));
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
		return music;
	}

	private void initialiseGround(){
		for(int i = 0; i < (int)(frameWidth/groundImage.getWidth())+2; i++){
			grounds.add(new SpawnObject(groundImage, i * groundImage.getWidth(), 0));
			}
		}
	
	private void spawnPlane(){
		plane = new SpawnObject(planeImage, frameWidth, frameHeight - planeImage.getHeight() - (int)MathUtils.random(0, frameHeight/3));
		lastPlaneTime = TimeUtils.millis();
	}
	
	private void spawnBlimp(){
		blimp = new SpawnObject(blimpImage, frameWidth, frameHeight - blimpImage.getHeight() - (int)MathUtils.random(0, frameHeight/4));
		lastBlimpTime = TimeUtils.millis();
	}
	
	private void spawnMoon(){
		moon = new SpawnObject(moonImage, frameWidth, (int)(frameHeight - moon.height ));
		lastMoonTime = TimeUtils.millis();
	}
	
	private void spawnFlappy(){
		AnimationSprite flappy = new AnimationSprite(this.game.batch, 3, 1, "flappy(half).png", antipodean);
		flappy.create();
		flappy.x = frameWidth;
		flappy.y = (int)MathUtils.random(player.height, frameHeight - flappy.getHeight());
		flappies.add(flappy);
		lastFlappyTime = TimeUtils.nanoTime();
	}
	
	private void spawnTree() {
		trees.add(new SpawnObject(treeImage, frameWidth+ (int)MathUtils.random(0, frameWidth), frameHeight/6));
		lastTreeTime = TimeUtils.nanoTime();
	}
	
	private void spawnCloud(){
		clouds.add(new SpawnObject(cloudImage, frameWidth+ (int)MathUtils.random(0, frameWidth), frameHeight - cloudImage.getHeight() - (int)MathUtils.random(0,frameHeight/2)));
		clouds2.add(new SpawnObject(cloud2Image, frameWidth + (int)MathUtils.random(0, frameWidth), frameHeight - cloud2Image.getHeight() - (int)MathUtils.random(0,frameHeight/3)));
		clouds3.add(new SpawnObject(cloud3Image, frameWidth + (int)MathUtils.random(0, frameWidth), frameHeight - cloud3Image.getHeight() - (int)MathUtils.random(0,frameHeight/4)));
		clouds4.add(new SpawnObject(cloud4Image, frameWidth + (int)MathUtils.random(0, frameWidth), frameHeight - cloud4Image.getHeight() - (int)MathUtils.random(0,frameHeight/5)));
		lastCloudTime = TimeUtils.nanoTime();
	}
	
	private void spawnHearts(){
		hearts.clear();
		for (int i = 0; i < lives; i++){
			hearts.add(new SpawnObject(heart, 5+30*i, (frameHeight -50)));
		}
	}
	
	public float setAntipodean(float height, float y){
		if(antipodean){
			return frameHeight-y-height;
		}
		else return y;
	}
	
	public void render(float delta) {

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

		game.batch.draw(moonImage, moon.x, setAntipodean(moonImage.getHeight(), moon.y), moonImage.getWidth(), moonImage.getHeight(), 0, 0, moonImage.getWidth(), moonImage.getHeight(), false, antipodean);
		drawSprites(grounds, groundImage, false, antipodean);
		drawSprites(trees , treeImage, false, antipodean);
		drawSprites(clouds, cloudImage, false, antipodean);
		drawSprites(clouds2, cloud2Image, false, antipodean);
		drawSprites(clouds3, cloud3Image, false, antipodean);
		drawSprites(clouds4, cloud4Image, false, antipodean);
		drawSprites(flappies);
		
		game.batch.draw(planeImage, plane.x, setAntipodean(planeImage.getHeight(), plane.y), planeImage.getWidth(), planeImage.getHeight(), 0, 0, planeImage.getWidth(), planeImage.getHeight(), false, antipodean);
		game.batch.draw(blimpImage, blimp.x, setAntipodean(blimpImage.getHeight(), blimp.y), blimpImage.getWidth(), blimpImage.getHeight(), 0, 0, blimpImage.getWidth(), blimpImage.getHeight(), false, antipodean);
		player.render((int)player.x, (int)setAntipodean(player.height,(int)player.y));
		for (Rectangle h : hearts){
			game.batch.draw(heart, h.x, h.y);
		}
		game.font.draw(game.batch, "Birds Destroyed: " + dropsGathered, 10, frameHeight - 8);
		
		game.batch.end();

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			touch = true;
			verticalVelocity = 7;
		}

		if (touch) {
			if (player.y + player.height >= frameHeight && lockedHeight) {
				touch = false;
				verticalVelocity = 0;
			}
			verticalVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
		} 
		else {
			if (player.y > 0.7*player.height) {
				verticalVelocity -= Gdx.graphics.getDeltaTime() * acceleration;
			} else {
				verticalVelocity = 0;
			}
		}
		player.y += verticalVelocity;
		/*
		 * Probably won't use side movements if
		 * (Gdx.input.isKeyPressed(Keys.LEFT)) bob.x -= movement *
		 * Gdx.graphics.getDeltaTime(); if (Gdx.input.isKeyPressed(Keys.RIGHT))
		 * bob.x += movement * Gdx.graphics.getDeltaTime();
		 */

		if (player.y < (float)(0.7*player.height))
			player.y = (float) (0.7*player.height);
		if (player.y >= frameHeight - player.height && lockedHeight)
			player.y = frameHeight - player.height;
		
		ArrayList<Rectangle> toRemove = new ArrayList<Rectangle>();
		ArrayList<AnimationSprite> spritesRemove = new ArrayList<AnimationSprite>();
		
		for(int i = 0; i < grounds.size(); i++){
			 grounds.get(i).x -= movement * 0.5* Gdx.graphics.getDeltaTime();
			 if(grounds.get(i).x + grounds.get(i).width <= 0){
				 grounds.get(i).x += grounds.get(i).width * grounds.size();
			 }
		}
		if (TimeUtils.nanoTime() - lastTreeTime > 500000000)
			spawnTree();

		if ((TimeUtils.nanoTime() - lastCloudTime)/1000 > 5000000)
			spawnCloud();
		
		if (TimeUtils.nanoTime() - lastFlappyTime > 1000000000)
			spawnFlappy();
		
		spawnHearts();

		for(AnimationSprite flappy : flappies){
			flappy.x -= movement * Gdx.graphics.getDeltaTime();
			Rectangle tmp1 = new Rectangle(flappy.x, flappy.y, flappy.width, flappy.height);
			Rectangle tmp2 = new Rectangle(player.x, player.y, player.width, player.height);
			if (flappy.getX() + flappy.getWidth() < 0){
				spritesRemove.add(flappy);
			}
			
			if (tmp2.overlaps(tmp1)) {
				dropsGathered++;////broken!
				if(dropsGathered%10 == 0 && dropsGathered != 0 && lives < 5){
					lives++;
				}
				//birdSong.play();
				spritesRemove.add(flappy);
			}
		}
		for(Rectangle s : trees ){
			s.x -= movement *0.5* Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
		}
		for(Rectangle s : clouds ){
			s.x -= movement * 0.7 *Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
			else movement = initMovement;
		}
		for(Rectangle s : clouds2 ){
			s.x -= movement * 0.6*Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
			else movement = initMovement;
		}
		for(Rectangle s : clouds3 ){
			s.x -= movement * 0.9 *Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
			else movement = initMovement;
		}
		for(Rectangle s : clouds4 ){
			s.x -= movement * 0.65 *Gdx.graphics.getDeltaTime();
			if (s.x + s.width < 0)
				toRemove.add(s);
			else movement = initMovement;
		}
		
		blimp.x -= movement*0.6  * Gdx.graphics.getDeltaTime();
		plane.x -= movement*1.5 * Gdx.graphics.getDeltaTime();
		moon.x -= movement*0.1 *Gdx.graphics.getDeltaTime();
		
		if(plane.x > 2*frameWidth && plane.x < 3*frameWidth){
			lastPlaneTime = TimeUtils.millis();
		}
		if(blimp.x > 2*frameWidth && blimp.x < 3*frameWidth){
			lastBlimpTime = TimeUtils.millis();
		}
		if(moon.x > 2*frameWidth && moon.x < 3*frameWidth){
			lastMoonTime = TimeUtils.millis();
		}
		if (plane.x + plane.width < 0){
			if (TimeUtils.millis() - lastPlaneTime > 100000+lastPlaneTime%100000){
				spawnPlane();
			}		
		}
		if(blimp.x + blimp.width < 0){
			if(TimeUtils.millis() - lastBlimpTime > 300000+lastBlimpTime%300000){
				spawnBlimp();
			}
		}
		if(moon.x + moon.width < 0){
			if(TimeUtils.millis() - lastMoonTime > 1000000+lastMoonTime%1000000){
				spawnMoon();
			}
		}
		flappies.removeAll(spritesRemove);
		birds.removeAll(toRemove);
		trees.removeAll(toRemove);
		clouds.removeAll(toRemove);
		clouds2.removeAll(toRemove);
		clouds3.removeAll(toRemove);
		clouds4.removeAll(toRemove);
		stage.draw();
	}

	private void drawSprites(ArrayList<Rectangle>rects, Texture texture, boolean s, boolean antipodean) {
		for (Rectangle cloud : rects){
			game.batch.draw(texture, cloud.x, setAntipodean(texture.getHeight(), cloud.y), texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight(), s, antipodean);
		}
	}
	
	private void drawSprites(ArrayList<AnimationSprite>sprites) {
		for (AnimationSprite sprite : sprites){
			sprite.render((int)sprite.x, (int)setAntipodean(sprite.height,(int)sprite.y));
		}
	}
	
	

	private void checkSettings() {
		//Sound
		if(!SavedSettings.SETTING_SOUND.getBoolean()){
			birdSong.pause();
			System.out.println("p");
		}else{
			birdSong.resume();
		}
		//Music
		if(!SavedSettings.SETTING_MUSIC.getBoolean()){
			gameMusic.pause();
			System.out.println("p");
		}else if(!gameMusic.isPlaying()){
			//gameMusic.play();
			//Gdx.app.debug("Sound", "Music playing");
		}
	}

	public void resize(int width, int height) {
	}

	public void show() {
		// start the playback of the background music
		// when the screen is shown
		//gameMusic.play();
	}

	public void hide() {
	}

	public void pause() {
	}

	public void resume() {
	}

	public void dispose() {
		birdImage.dispose();
		bobImage.dispose();
		birdSong.dispose();
		gameMusic.dispose();
		groundImage.dispose();		
	}
}