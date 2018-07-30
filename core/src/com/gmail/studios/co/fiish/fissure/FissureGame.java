package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

/*
	Game class
 */

public class FissureGame extends Game {
	private TextureAtlas mAtlas;
	private FiishCoScreen mFiishCoScreen;
	private FissureGameScreen mFissureGameScreen;
	private ActionResolver mActionResolver;
	private IOSLink mIOSLink;

	public FissureGame() {
		super();
	} // default constructor

	public FissureGame(ActionResolver actionResolver) {
	    this.mActionResolver = actionResolver;
	}

	public FissureGame(IOSLink iosLink) {
		this.mIOSLink = iosLink;
	}

	@Override
	public void create() {
		mAtlas = new TextureAtlas(Gdx.files.internal("spritesheet.atlas"));
		mFissureGameScreen = new FissureGameScreen(mAtlas);
		mFiishCoScreen = new FiishCoScreen();
		if (mActionResolver != null) {
			mFissureGameScreen.setActionResolver(mActionResolver);
		}
		if (mIOSLink != null) {
			mFissureGameScreen.setIOSLink(mIOSLink);
		}
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
