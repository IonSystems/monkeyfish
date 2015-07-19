package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Levels {
	private static Levels instance = null;
	private int levelNumber = 0;
	private int numberOfLevels = 0;
	private Level level = new Level(Gdx.app.getPreferences("Level 1"));
	
	protected Levels() {
		setupLevels();
	}
	
	private Preferences setupLevels() {
		Preferences level1 = makeLevel("Level 1", 1, 100, 0, 10, 200);
		return level1;
		
	}

	private Preferences makeLevel(String name, int levelNumber, int distance, int difficulty, int gravity, int speed) {
		Preferences level1 = Gdx.app.getPreferences("Level 1");
		level1.putString("name", name);
		
		level1.putString("cloudImage", "!Not Set!"); //TODO: Set these to image dir
		level1.putString("treeImage", "!Not Set!");
		level1.putString("groundImage", "!Not Set!");
		
		level1.putInteger("levelNumber", levelNumber);
		level1.putInteger("distance", distance);
		level1.putInteger("difficulty", difficulty);
		level1.putInteger("gravity", gravity);
		level1.putInteger("speed", speed);
		level1.flush();
		return level1;
	}

	public static Levels getInstance() {
	      if(instance == null) {
	         instance = new Levels();
	      }
	      return instance;
	}
	
	public static Level getCurrentLevel(){
		return getInstance().level;
	}
	
	public boolean nextLevel(){
		if(getInstance().levelNumber < getInstance().numberOfLevels){
			getInstance().levelNumber++;
			return true;
		}else{
			return false;
		}
	}
	
	public boolean previousLevel(){
		if(getInstance().levelNumber < getInstance().numberOfLevels){
			getInstance().levelNumber--;
			return true;
		}else{
			return false;
		}
	}
	
	public boolean gotoLevel(int levelNumber){
		if(levelNumber < getInstance().numberOfLevels){
			getInstance().levelNumber = levelNumber;
			return true;
		}else{
			return false;
		}
	}
	
	public static Level getJsonLevels(){
		return getInstance().level;
	}
}

