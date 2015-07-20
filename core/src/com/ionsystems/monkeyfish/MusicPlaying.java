package com.ionsystems.monkeyfish;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public enum MusicPlaying {
	
	GAMEPLAY("sound/sample.mp3"),
	MENU("sound/title.mp3"),
	NONE("sound/none.mp3");
	
	MusicPlaying(String filename){
		this.filename = filename;
		this.playing = true;
		if(filename.equals("sound/none.mp3")){
			music = null;
		}else{
			music = Gdx.audio.newMusic(Gdx.files.internal(filename));
			music.play();
		}
	}
	
	void stop(){
		if(music != null){
			music.stop();
		playing = false;
		}
	}
	Music music;
	String filename;
	boolean playing;
}
