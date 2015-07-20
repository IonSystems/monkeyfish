package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Preferences;

public class Level {
	private String name;

	private String cloudImage;
	private String treeImage;
	private String groundImage;

	private int levelNumber;
	private int distance;
	private int difficulty;
	private int gravity; //AKA acceleration (towards monkey earth)
	private int speed; //AKA movement
	private boolean unlocked;

	public Level(Preferences level){
		this.name = level.getString("name");
		
		this.cloudImage = level.getString("cloudImage");
		this.treeImage = level.getString("treeImage");
		this.groundImage = level.getString("groundImage");

		this.levelNumber = level.getInteger("levelNumber");
		this.distance = level.getInteger("distance");
		this.difficulty = level.getInteger("difficulty");
		this.gravity = level.getInteger("gravity");
		this.speed = level.getInteger("speed");
		this.unlocked = level.getBoolean("unlocked");
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCloudImage() {
		return cloudImage;
	}

	public void setCloudImage(String cloudImage) {
		this.cloudImage = cloudImage;
	}

	public String getTreeImage() {
		return treeImage;
	}

	public void setTreeImage(String treeImage) {
		this.treeImage = treeImage;
	}

	public String getGroundImage() {
		return groundImage;
	}

	public void setGroundImage(String groundImage) {
		this.groundImage = groundImage;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public void setDifficulty(int difficulty) {
		this.difficulty = difficulty;
	}

	public int getGravity() {
		return gravity;
	}

	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

	public int getLevelNumber() {
		return levelNumber;
	}

	public void setLevelNumber(int levelNumber) {
		this.levelNumber = levelNumber;
	}

	public float getVerticalVelocity() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getLives() {
		// TODO Auto-generated method stub
		return 5;
	}

	public boolean getInfiniteHeight() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean getUpsideDownMode() {
		// TODO Auto-generated method stub
		return false;
	}
}
