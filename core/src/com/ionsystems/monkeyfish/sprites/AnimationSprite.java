package com.ionsystems.monkeyfish.sprites;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationSprite {

	HashMap<String, AnimationTexture> animations;

	private AnimationTexture currentAnimation;
	SpriteBatch spriteBatch;
	TextureRegion currentFrame;

	float stateTime;
	private float x;
	private float y;
	boolean antipodean;
	private int id;

	public AnimationSprite(SpriteBatch sb, boolean antipodean, int id) {
		animations = new HashMap<String, AnimationTexture>();
		this.spriteBatch = sb;
		this.setX(0);
		this.setY(0);
		this.antipodean = antipodean;
		this.setId(id);
		stateTime = 0f;
	}

	public float getHeight() {
		return getCurrentAnimation().getHeight();
	}

	public float getWidth() {
		return getCurrentAnimation().getWidth();
	}

	public void generateAnimation(String name, FileHandle file, int cols, int rows) {
		animations.put(name, new AnimationTexture(name, cols, rows, file, antipodean));
	}

	public void setCurrentAnimation(String name) {
			currentAnimation = animations.get(name);

	}

	public void render() {
		if (getCurrentAnimation() != null) {
			stateTime += Gdx.graphics.getDeltaTime();
			currentFrame = getCurrentAnimation().getKeyFrame(stateTime, true);
			spriteBatch.draw(currentFrame, getX(), setAntipodean(getCurrentAnimation().getHeight(), getY()));
		}
	}
	
	public float setAntipodean(float height, float y) {
		if (antipodean) {
			return Gdx.graphics.getHeight() - y - height;
		} else
			return y;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AnimationTexture getCurrentAnimation() {
		return currentAnimation;
	}

	public void setCurrentAnimation(AnimationTexture currentAnimation) {
		this.currentAnimation = currentAnimation;
	}

}