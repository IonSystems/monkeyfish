package com.ionsystems.monkeyfish;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

public class spawnObject extends Rectangle{

	public spawnObject(Texture Image, int x, int y){
		this.x = x;
		this.y = y;
		this.width = Image.getWidth();
		this.height = Image.getHeight();
	}
}
