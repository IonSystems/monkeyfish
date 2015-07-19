package com.ionsystems.monkeyfish;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationSprite implements ApplicationListener {

	private int        				cols;         // #1
    private int        				rows;         // #2
    Animation                       walkAnimation;          // #3
    Texture                         walkSheet;              // #4
    TextureRegion[]                 walkFrames;             // #5
    SpriteBatch                     spriteBatch;            // #6
    TextureRegion                   currentFrame;           // #7
    String 							file;
    float stateTime;                                        // #8

    
    public AnimationSprite(SpriteBatch sb, int cols, int rows, String file){
    	this.spriteBatch = sb;
	    this.cols = cols;
	    this.rows = rows;
	    this.file = file;
    }
    
    
    public void create() {
        walkSheet = new Texture(Gdx.files.internal(file)); // #9
        TextureRegion[][] tmp = TextureRegion.split(walkSheet, walkSheet.getWidth()/cols, walkSheet.getHeight()/rows);              // #10
        walkFrames = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                walkFrames[index++] = tmp[i][j];
            }
        }
        walkAnimation = new Animation(0.025f, walkFrames);      // #11
        //spriteBatch = new SpriteBatch();                // #12
        stateTime = 0f;                         // #13
    }

    public void render(int x, int y) {
        stateTime += Gdx.graphics.getDeltaTime();           // #15
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);  // #16
        spriteBatch.draw(currentFrame, x, y);             // #17
    }

	public void resize(int width, int height) {	
	}

	public void pause() {	
	}

	public void resume() {
	}

	public void dispose() {
	}

	public void render() {
		
	}
}