package com.ionsystems.monkeyfish;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationSprite {

	HashMap<String, AnimationTexture> animations;

	AnimationTexture currentAnimation;
	SpriteBatch spriteBatch;
	TextureRegion currentFrame;

	float stateTime;
	float x;
	float y;
	boolean antipodean;
	int id;

	public AnimationSprite(SpriteBatch sb, boolean antipodean, int id) {
		animations = new HashMap<String, AnimationTexture>();
		this.spriteBatch = sb;
		this.x = 0;
		this.y = 0;
		this.antipodean = antipodean;
		this.id = id;
		stateTime = 0f;
	}

	public float getHeight() {
		return currentAnimation.getHeight();
	}

	public float getWidth() {
		return currentAnimation.getWidth();
	}

	public void generateAnimation(String name, String file, int cols, int rows) {
		animations.put(name, new AnimationTexture(name, cols, rows, file, antipodean));
	}

	public void setCurrentAnimation(String name) {
			currentAnimation = animations.get(name);

	}

	public void render() {
		if (currentAnimation != null) {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = currentAnimation.getKeyFrame(stateTime, true);
			spriteBatch.draw(currentFrame, x, setAntipodean(currentAnimation.getHeight(), y));
		}
	}
	
	public float setAntipodean(float height, float y) {
		if (antipodean) {
			return Gdx.graphics.getHeight() - y - height;
		} else
			return y;
	}

}