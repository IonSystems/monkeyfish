package com.ionsystems.monkeyfish;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class SpawnObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	float x;
	float y;
	int width;
	int height;
	Texture Image;
	
	
	public SpawnObject(Texture Image, float x, float y){
		this.x = x;
		this.y = y;
		this.width = Image.getWidth();
		this.height = Image.getHeight();
	}
}
