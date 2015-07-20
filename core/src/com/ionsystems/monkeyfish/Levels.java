package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class Levels {
	private static Levels instance = null;
	private int levelNumber = 0;
	private int numberOfLevels = 0;
	public int getNumberOfLevels() {
		return numberOfLevels;
	}

	public void setNumberOfLevels(int numberOfLevels) {
		this.numberOfLevels = numberOfLevels;
	}

	private Level level = new Level(Gdx.app.getPreferences("Level 1"));
	
	protected Levels() {
		setupLevels();
	}
	
	private void setupLevels() {
		Preferences level1 = makeLevel("Level 1", 1, 200, 0, 10, 200);
		Preferences level2 = makeLevel("Level 2", 2, 300, 0, 5, 100);
	}

	
	private Preferences makeLevel(String name, int levelNumber, int distance, int difficulty, int gravity, int speed) {
		Preferences level = Gdx.app.getPreferences("Level " + levelNumber);
		level.putString("name", name);
		
		level.putString("cloudImage", "!Not Set!"); //TODO: Set these to image dir
		level.putString("treeImage", "!Not Set!");
		level.putString("groundImage", "!Not Set!");
		
		level.putInteger("levelNumber", levelNumber);
		level.putInteger("distance", distance);
		level.putInteger("difficulty", difficulty);
		level.putInteger("gravity", gravity);
		level.putInteger("speed", speed);
		level.flush();
		numberOfLevels++;
		return level;
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
	
	public static boolean nextLevel(){
		if(getInstance().levelNumber < getInstance().numberOfLevels){
			getInstance().levelNumber++;
			getInstance().level = getInstance().getSpecificLevel(getInstance().levelNumber);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean previousLevel(){
		if(getInstance().levelNumber < getInstance().numberOfLevels){
			getInstance().levelNumber--;
			getInstance().level = getInstance().getSpecificLevel(levelNumber);
			return true;
		}else{
			return false;
		}
	}
	
	public boolean gotoLevel(int levelNumber){
		if(levelNumber < getInstance().numberOfLevels){
			getInstance().levelNumber = levelNumber;
			getInstance().level = getInstance().getSpecificLevel(levelNumber);
			return true;
		}else{
			return false;
		}
	}
	
	public static Level getJsonLevels(){
		return getInstance().level;
	}
	/*
	 * Do not use zero as index , 1 is level 1, no level before level 1.
	 */
	public Level getSpecificLevel(int index) {
		if(index <= getInstance().numberOfLevels){
			getInstance().level = new Level(Gdx.app.getPreferences("Level " + index));
		}
		return getInstance().level;
		
	}
}

