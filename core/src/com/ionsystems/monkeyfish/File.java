package com.ionsystems.monkeyfish;

import java.util.HashMap;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public class File {
	private static File instance = null;
	private HashMap<String, String> strings = new HashMap<String, String>();
	private FileHandle defaultImage = Gdx.files.internal("sprites/doge.jpeg");

	protected File() {
		setupStrings();
	}

	private void setupStrings() {
		
		//Blimp
		strings.put("blimp", "sprites/blimp/blimp.png");
		strings.put("blimp0.5", "sprites/blimp/blimp0.5.png");
		strings.put("blimp1.5", "sprites/blimp/blimp1.5.png");
		strings.put("blimp2.0", "sprites/blimp/blimp2.0.png");
		//Crash
		strings.put("crash", "sprites/crash/crash.png");
		strings.put("crash0.5", "sprites/crash/crash0.5.png");
		strings.put("crash1.5", "sprites/crash/crash1.5.png");
		strings.put("crash2.0", "sprites/crash/crash2.0.png");
		
		//Sonic
		strings.put("sonic", "sprites/sonic/sonic.png");
		strings.put("sonic0.5", "sprites/sonic/sonic0.5.png");
		strings.put("sonic1.5", "sprites/sonic/sonic1.5.png");
		strings.put("sonic2.0", "sprites/sonic/sonic2.0.png");
		
		// Clouds
		strings.put("cloud", "sprites/clouds/cloud.png");
		strings.put("cloud0.5", "sprites/clouds/cloud0.5.png");
		strings.put("cloud1.5", "sprites/clouds/cloud1.5.png");
		strings.put("cloud2.0", "sprites/clouds/cloud2.0.png");
		
		strings.put("cloud2", "sprites/clouds/cloudtwo.png");
		strings.put("cloud20.5", "sprites/clouds/cloudtwo0.5.png"); //TODO: Fix naming inconsistencies
		strings.put("cloud21.5", "sprites/clouds/cloudtwo1.5.png");
		strings.put("cloud22.0", "sprites/clouds/cloudtwo2.0.png");
		
		strings.put("cloud3", "sprites/clouds/cloudthree.png");
		strings.put("cloud30.5", "sprites/clouds/cloudthree0.5.png");
		strings.put("cloud31.5", "sprites/clouds/cloudthree1.5.png");
		strings.put("cloud32.0", "sprites/clouds/cloudthree2.0.png");
		
		strings.put("cloud4", "sprites/clouds/cloudfour.png");
		strings.put("cloud40.5", "sprites/clouds/cloudfour0.5.png");
		strings.put("cloud41.5", "sprites/clouds/cloudfour1.5.png");
		strings.put("cloud42.0", "sprites/clouds/cloudfour2.0.png");

		// Moon
		strings.put("moon", "sprites/moon/moon.png");
		strings.put("moon0.5", "sprites/moon/moon0.5");
		strings.put("moon1.5", "sprites/moon/moon1.5.png");
		strings.put("moon2.0", "sprites/moon/moon2.0.png");

		// Mario
		strings.put("mario", "sprites/mario/mario.png");
		strings.put("mario0.5", "sprites/mario/mario0.5.png");
		strings.put("mario1.5", "sprites/mario/mario1.5.png");
		strings.put("mario2.0", "sprites/mario/mario2.0.png");

		strings.put("mariohurt", "sprites/mario/mario_hurt.png");
		strings.put("mariohurt0.5", "sprites/mario/mario_hurt0.5.png");
		strings.put("mariohurt1.5", "sprites/mario/mario_hurt1.5.png");
		strings.put("mariohurt2.0", "sprites/mario/mario_hurt2.0.png");

		// Ground
		strings.put("ground", "sprites/ground/ground.png");
		strings.put("ground0.5", "sprites/ground/ground0.5.png");
		strings.put("ground1.5", "sprites/ground/ground1.5.png");
		strings.put("ground2.0", "sprites/ground/ground2.0.png");

		// Flappy
		strings.put("flappy", "sprites/flappy/flappy.png");
		strings.put("flappy0.5", "sprites/flappy/flappy0.5.png");
		strings.put("flappy1.5", "sprites/flappy/flappy1.5.png");
		strings.put("flappy2.0", "sprites/flappy/flappy2.0.png");

		// Hearts
		strings.put("heart", "sprites/heart/heart.png");
		strings.put("heart0.5", "sprites/heart/heart.png");
		strings.put("heart1.5", "sprites/heart/heart.png");
		strings.put("heart2.0", "sprites/heart/heart.png");

		// Tree
		strings.put("tree", "sprites/tree/tree.png");
		strings.put("tree0.5", "sprites/tree/tree0.5.png");
		strings.put("tree1.5", "sprites/tree/tree1.5.png");
		strings.put("tree2.0", "sprites/tree/tree2.0.png");

		// Tomato
		strings.put("fruit0()", "sprites/tomato/fruit0().png"); // TODO:Fix weird file names.
		strings.put("fruit0(0.5)", "sprites/tomato/fruit0(0.5).png");
		strings.put("fruit0(1.5)", "sprites/tomato/fruit0(1.5).png");
		strings.put("fruit0(2.0)", "sprites/tomato/fruit0(2.0).png");
		
		//Grapes
		strings.put("fruit1()",  "sprites/grapes/fruit1().png");
		strings.put("fruit1(0.5)", "sprites/grapes/fruit1(0.5).png");
		strings.put("fruit1(1.5)", "sprites/grapes/fruit1(1.5).png");
		strings.put("fruit1(2.0)", "sprites/grapes/fruit1(2.0).png");
		
		//Strawberry
		strings.put("fruit2()",  "sprites/strawberry/fruit2().png");
		strings.put("fruit2(0.5)", "sprites/strawberry/fruit2(0.5).png");
		strings.put("fruit2(1.5)", "sprites/strawberry/fruit2(1.5).png");
		strings.put("fruit2(2.0)", "sprites/strawberry/fruit2(2.0).png");
		
		//Pineapple
		strings.put("fruit3()",  "sprites/pineapple/fruit3().png");
		strings.put("fruit3(0.5)", "sprites/pineapple/fruit3(0.5).png");
		strings.put("fruit3(1.5)", "sprites/pineapple/fruit3(1.5).png");
		strings.put("fruit3(2.0)", "sprites/pineapple/fruit3(2.0).png");
		
		//Watermelon
		strings.put("fruit4()",  "sprites/watermelon/fruit4().png");
		strings.put("fruit4(0.5)", "sprites/watermelon/fruit4(0.5).png");
		strings.put("fruit4(1.5)", "sprites/watermelon/fruit4(1.5).png");
		strings.put("fruit4(2.0)", "sprites/watermelon/fruit4(2.0).png");
		
		//Apple
		strings.put("fruit5()",  "sprites/apple/fruit5().png");
		strings.put("fruit5(0.5)", "sprites/apple/fruit5(0.5).png");
		strings.put("fruit5(1.5)", "sprites/apple/fruit5(1.5).png");
		strings.put("fruit5(2.0)", "sprites/apple/fruit5(2.0).png");
		
		//Lemon
		strings.put("fruit6()",  "sprites/lemon/fruit6().png");
		strings.put("fruit6(0.5)", "sprites/lemon/fruit6(0.5).png");
		strings.put("fruit6(1.5)", "sprites/lemon/fruit6(1.5).png");
		strings.put("fruit6(2.0)", "sprites/lemon/fruit6(2.0).png");
		
		//Cherrys
		strings.put("fruit7()",  "sprites/cherrys/fruit7().png");
		strings.put("fruit7(0.5)", "sprites/cherrys/fruit7(0.5).png");
		strings.put("fruit7(1.5)", "sprites/cherrys/fruit7(1.5).png");
		strings.put("fruit7(2.0)", "sprites/cherrys/fruit7(2.0).png");
		
		//Raspberrys
		strings.put("fruit8()",  "sprites/raspberrys/fruit8().png");
		strings.put("fruit8(0.5)", "sprites/raspberrys/fruit8(0.5).png");
		strings.put("fruit8(1.5)", "sprites/raspberrys/fruit8(1.5).png");
		strings.put("fruit8(2.0)", "sprites/raspberrys/fruit8(2.0).png");
		
		//Pomegranate
		strings.put("fruit9()",  "sprites/pomegranate/fruit9().png");
		strings.put("fruit9(0.5)", "sprites/pomegranate/fruit9(0.5).png");
		strings.put("fruit9(1.5)", "sprites/pomegranate/fruit9(1.5).png");
		strings.put("fruit9(2.0)", "sprites/pomegranate/fruit9(2.0).png");
		
		//Banana
		strings.put("fruit10()",  "sprites/banana/fruit10().png");
		strings.put("fruit10(0.5)", "sprites/banana/fruit10(0.5).png");
		strings.put("fruit10(1.5)", "sprites/banana/fruit10(1.5).png");
		strings.put("fruit10(2.0)", "sprites/banana/fruit10(2.0).png");
		
		//Pear
		strings.put("fruit11()",  "sprites/pear/fruit11().png");
		strings.put("fruit11(0.5)", "sprites/pear/fruit11(0.5).png");
		strings.put("fruit11(1.5)", "sprites/pear/fruit11(1.5).png");
		strings.put("fruit11(2.0)", "sprites/pear/fruit11(2.0).png");
	}

	public FileHandle getFile(String key) {
		Gdx.app.debug("File", "Looking for " + key);
		try {
			FileHandle fileLocation = Gdx.files.internal(strings.get(key));
			return fileLocation;
		} catch (NullPointerException e) {
			Gdx.app.debug("File", "Did not find " + key);
			System.out.println("Did not find " + key);
			return defaultImage;
		}
	}

	public static File getInstance() {
		if (instance == null) {
			instance = new File();
		}
		return instance;
	}
}
