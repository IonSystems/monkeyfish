package com.ionsystems.monkeyfish;

public enum LevelSettings {
	
	LEVEL_NAME("lvlName"),
	LEVEL_NUMBER("lvlNum"),
	
	LEVEL_INITIAL_MOVEMENT("lvlInitMovement"),
	LEVEL_GRAVITY("lvlGravity"),
	
	LEVEL_VELOCITY_X("lvlVelocityX"),
	LEVEL_VELOCITY_Y("lvlVelocityY"),
	
	LEVEL_DIFFICULTY("lvlDifficulty"),
	LEVEL_DISTANCE("lvlDistance");
	
	private final String id;
	
	private LevelSettings(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
