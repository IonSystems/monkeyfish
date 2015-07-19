package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public enum SavedSettings {
	
	SETTING_SOUND("stgSound",Type.BOOLEAN),
	SETTING_MUSIC("stgMusic",Type.BOOLEAN),
	
	SETTING_UPSIDE_DOWN("stgUpsideDown", Type.BOOLEAN), //Defualt off
	SETTING_USER_NAME("stgUsername",Type.STRING),
	
	SETTING_LEVEL_UNLOCKED("lvlVelocityX",Type.INTEGER);
	
	enum Type{
		BOOLEAN,STRING,INTEGER;
	}
	private final String id;
	private String settingsFileName;
	private Preferences storage;
	private Object objectType;
	private SavedSettings(String id, Type objectType) {
		this.id = id;
		this.objectType = objectType;
		this.settingsFileName = "My Options";
		this.storage = Gdx.app.getPreferences(settingsFileName);
		initialiseFields();
		
		
	}
	
	public boolean getBoolean(){
		return storage.getBoolean(id);
	}
	
	public void setBoolean(boolean value){
		storage.putBoolean(id, value);
		storage.flush();
	}
	
	public String getString(){
		return storage.getString(id);
	}
	
	public void setString(String value){
		storage.putString(id, value);
		storage.flush();
	}
	
	public int getInteger(){
		return storage.getInteger(id);
	}
	
	public void setInteger(int value){
		storage.putInteger(id, value);
		storage.flush();
	}
	
	private void initialiseFields(){
			if(objectType == Type.BOOLEAN){
				if(!storage.contains(this.id)){
					this.setBoolean(true);
				}
			}else if(objectType == Type.INTEGER){
				if(!storage.contains(this.id)){
					this.setInteger(0);
				}
			}else if(objectType == Type.STRING){
				if(!storage.contains(this.id)){
					this.setString("");
				}
			}
		
		
	
}
}
