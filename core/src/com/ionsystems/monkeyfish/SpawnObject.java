package com.ionsystems.monkeyfish;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class SpawnObject extends Rectangle{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SpawnObject(Texture Image, int x, int y){
		this.x = x;
		this.y = y;
		this.width = Image.getWidth();
		this.height = Image.getHeight();
	}
}
