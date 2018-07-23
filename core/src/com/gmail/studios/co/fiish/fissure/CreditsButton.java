package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
    When tapped, brings player to Credits menu.
 */

public class CreditsButton extends BasicButton {
    public CreditsButton(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("credits");
    }

    @Override
    public void init() {
        mXDef = mViewport.getScreenWidth() / 16f / 32f * 5;
        mYDef = mViewport.getScreenHeight() / 9f / 32f * 5;

        mWidthDef = mViewport.getScreenWidth() / 16f;
        mHeightDef = mViewport.getScreenHeight() / 9f;

        mTouchableDef = Touchable.enabled;

        mAlphaDef = 1;
        super.init();
    }
}
