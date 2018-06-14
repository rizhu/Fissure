package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameOverPrompt extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;

    private float mPixelX, mPixelY;

    public GameOverPrompt(Viewport viewport, TextureAtlas atlas) {
        this.mViewport = viewport;
        mRegion = atlas.findRegion("gameover");
    }

    public void init() {
        this.clearActions();
        mPixelX = mViewport.getScreenWidth() / 16f / 32f;
        mPixelY = mViewport.getScreenHeight() / 9f / 32f;

        this.setWidth(70f * mPixelX * 3f);
        this.setHeight(12f * mPixelY * 3f);

        this.setX(mViewport.getScreenWidth() / 2 - getWidth() / 2);
        this.setY(mViewport.getScreenHeight() - 24f * mPixelY - getHeight());

        this.setTouchable(Touchable.disabled);
        this.setColor(getColor().r, getColor().g, getColor().b, 0);
    }

    public void reset() {
        this.clearActions();
        this.setX(mViewport.getScreenWidth() / 2 - getWidth() / 2);
        this.setY(mViewport.getScreenHeight() - 24f * mPixelY - getHeight());
        this.setColor(getColor().r, getColor().g, getColor().b, 0);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, getX(), getY(), getWidth(), getHeight());
    }

}
