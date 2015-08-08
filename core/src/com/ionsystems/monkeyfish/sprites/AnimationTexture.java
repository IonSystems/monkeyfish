package com.ionsystems.monkeyfish.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class AnimationTexture extends Rectangle {
	private static final long serialVersionUID = 5576084630436892954L;
	private int        				cols;        
	private int        				rows;
	Animation                       animation;          
    Texture                         texture;            
    TextureRegion[]                 textureRegion;  
    String name;
	boolean 						antipodean;
    
	public AnimationTexture(String name, int cols, int rows, FileHandle file, boolean antipodean) {
		super();
		this.name = name;
		this.cols = cols;
		this.rows = rows;		
		this.texture = new Texture(file); //TODO: Refactor, should pass in texture as parameter, not file.
		this.antipodean = antipodean;
		create();
	}

	public void create() { 
        TextureRegion[][] tmp = TextureRegion.split(texture, texture.getWidth()/cols, texture.getHeight()/rows);              
        textureRegion = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
            	tmp[i][j].flip(false, antipodean);
            	textureRegion[index++] = tmp[i][j];  
            }
        }
        animation = new Animation(0.05f, textureRegion);        
        this.setWidth(texture.getWidth() / cols);
        this.setHeight(texture.getHeight() / rows);
    }

	public TextureRegion getKeyFrame(float stateTime, boolean b) {
		return animation.getKeyFrame(stateTime, b);
	}
	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
