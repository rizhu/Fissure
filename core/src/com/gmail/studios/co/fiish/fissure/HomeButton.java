package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HomeButton extends BasicButton {
    public HomeButton(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("home");
    }

    @Override
    public void init() {
        mXDef = mViewport.getScreenWidth() / 2 + mViewport.getScreenWidth() / 16 * 3 - getWidth();
        mYDef = mViewport.getScreenHeight() * 0.1f;

        mWidthDef = mViewport.getScreenWidth() / 16f / 32f * 84f;
        mHeightDef = mViewport.getScreenHeight() / 9f / 32f * 42f;

        mTouchableDef = Touchable.disabled;

        mAlphaDef = 0;
        super.init();
    }
}
