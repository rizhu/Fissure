package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FissureLogo extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;
    private float mPixelX, mPixelY;

    public FissureLogo(Viewport viewport, TextureAtlas atlas) {
        this.mViewport = viewport;
        mRegion = atlas.findRegion("fissure");
    }

    public void init() {
        this.clearActions();
        mPixelX = mViewport.getScreenWidth() / 16f / 32f;
        mPixelY = mViewport.getScreenHeight() / 9f / 32f;

        this.setWidth(208f * mPixelX * 1.75f);
        this.setHeight(34f * mPixelY * 1.75f);

        this.setX(mViewport.getScreenWidth() / 2 - getWidth() / 2);
        this.setY(mViewport.getScreenHeight() - 32f * mPixelY - getHeight());

        this.setTouchable(Touchable.disabled);
        this.setColor(getColor().r, getColor().g, getColor().b, 1);
    }

    public void reset() {
        this.clearActions();
        this.setX(mViewport.getScreenWidth() / 2 - getWidth() / 2);
        this.setY(mViewport.getScreenHeight() - 32f * mPixelY - getHeight());
        this.setColor(getColor().r, getColor().g, getColor().b, 1);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, getX(), getY(), getWidth(), getHeight());
    }
}
