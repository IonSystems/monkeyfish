package com.ionsystems.monkeyfish.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class SpawnObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private float x;
	private float y;
	private int width;
	int height;
	Texture Image;
	
	
	public SpawnObject(Texture Image, float x, float y){
		this.setX(x);
		this.setY(y);
		this.setWidth(Image.getWidth());
		this.height = Image.getHeight();
	}


	public float getX() {
		return x;
	}


	public void setX(float x) {
		this.x = x;
	}


	public float getY() {
		return y;
	}


	public void setY(float y) {
		this.y = y;
	}



	public int getWidth() {
		return width;
	}


	public void setWidth(int width) {
		this.width = width;
	}
}
