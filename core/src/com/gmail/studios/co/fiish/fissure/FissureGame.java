package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/*
	Game class
 */

public class FissureGame extends Game {
	private TextureAtlas mAtlas;
	FiishCoScreen mFiishCoScreen;
	FissureGameScreen mFissureGameScreen;

	@Override
	public void create() {
		mAtlas = new TextureAtlas(Gdx.files.internal("spritesheet.atlas"));
		mFiishCoScreen = new FiishCoScreen();
		mFissureGameScreen = new FissureGameScreen(mAtlas);
		setScreen(mFiishCoScreen);
	}

	@Override
	public void render() {
		if (getScreen().equals(mFiishCoScreen) && mFiishCoScreen.mElapsedTime > 2.5f) { //Fiish Co logo screen remains active for 2.5 seconds
			setScreen(mFissureGameScreen);
		}
		super.render();
	}

	@Override
	public void dispose() {
		mAtlas.dispose();
	}
}
