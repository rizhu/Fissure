package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
   Used to return to Title Screen from Credits menu.
 */

public class BackLeftButton extends BasicButton {
    public BackLeftButton(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("backleft");
    }

    @Override
    public void init() {
        mXDef = mViewport.getScreenWidth() / 16f / 32f * 5;
        mYDef = mViewport.getScreenHeight() / 9f / 32f * 5;

        mWidthDef = mViewport.getScreenWidth() / 16f;
        mHeightDef = mViewport.getScreenHeight() / 9f;

        mTouchableDef = Touchable.enabled;

        mAlphaDef = 0;
        super.init();
    }
}
