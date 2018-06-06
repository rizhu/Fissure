package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TapPrompt extends Actor {
    private Viewport mViewport;
    private Texture mTexture;
    private float mPixelX, mPixelY;

    public TapPrompt(Viewport viewport) {
        this.mViewport = viewport;
        mTexture = new Texture(Gdx.files.internal("tapprompt.png"));
    }

    public void init() {
        mPixelX = mViewport.getScreenWidth() / 16f / 32f;
        mPixelY = mViewport.getScreenHeight() / 9f / 32f;

        this.setWidth(93f * mPixelX * 1.75f);
        this.setHeight(12f * mPixelY * 1.75f);

        this.setX(mViewport.getScreenWidth() / 2 - getWidth() / 2);
        this.setY(48f * mPixelY);

        this.setColor(getColor().r, getColor().g, getColor().b, 1);
        this.clearActions();
        this.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f),Actions.fadeIn(1f))));
    }

    public void reset() {
        this.clearActions();
        this.setColor(getColor().r, getColor().g, getColor().b, 1);
        this.addAction(Actions.forever(Actions.sequence(Actions.fadeOut(1f),Actions.fadeIn(1f))));
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mTexture, getX(), getY(), getWidth(), getHeight());
    }

    public void dispose() {
        mTexture.dispose();
    }

}
