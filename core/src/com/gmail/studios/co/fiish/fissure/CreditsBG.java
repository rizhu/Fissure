package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
    Displays game credits.
 */

public class CreditsBG extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;
    private Animation<TextureRegion> mRichard, mJames;

    private float mElapsedTime, mPixelX, mPixelY;

    public CreditsBG(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("creditsbg");
        mRichard = new Animation<TextureRegion>(1f/10f, atlas.findRegions("richard"));
        mJames = new Animation<TextureRegion>(1f/10f, atlas.findRegions("james"));
        setTouchable(Touchable.disabled);
    }

    public void init() {
        clearActions();

        this.setHeight(mViewport.getScreenHeight() * 9f / 10f);
        this.setWidth(getHeight() * 1.5f);

        mPixelX = getWidth() / 192f;
        mPixelY = getHeight() / 128f;

        this.setX(mViewport.getScreenWidth() / 2f - getWidth() / 2f);
        this.setY(mViewport.getScreenHeight() / 2f - getHeight() / 2f);

        this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        setColor(getColor().r, getColor().g, getColor().b, 0f);

        mElapsedTime = 0.0f;
    }

    public void reset() {
        this.clearActions();
        setColor(getColor().r, getColor().g, getColor().b, 0f);
        mElapsedTime = 0.0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        mElapsedTime += delta;
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, this.getX(), this.getY(), this.getWidth(), this.getHeight());
        batch.draw(mRichard.getKeyFrame(mElapsedTime, true), getX() + 6 * mPixelX,
                getY() + 69f * mPixelY,
                30 * mPixelX, 30 * mPixelY * 9f / 11f);
        batch.draw(mJames.getKeyFrame(mElapsedTime, true), getX() + 1.25f * mPixelX,
                getY() + 31.5f * mPixelY,
                35 * mPixelX, 35 * mPixelY);
    }
}
