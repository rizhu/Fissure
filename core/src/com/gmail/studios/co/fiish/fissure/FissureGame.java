package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

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
		if (getScreen().equals(mFiishCoScreen) && mFiishCoScreen.mElapsedTime > 2.5f) {
			setScreen(mFissureGameScreen);
		}
		super.render();
	}

	@Override
	public void dispose() {
		mAtlas.dispose();
	}
}
