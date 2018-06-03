package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ReplayButton extends Actor {
    public Viewport mViewport;

    private Texture mTexture;

    public ReplayButton(Viewport viewport) {
        this.mViewport = viewport;
        mTexture = new Texture(Gdx.files.internal("replay.png"));
    }

    public void init() {
        this.setWidth(mViewport.getScreenWidth() / 16f / 32f * 84f);
        this.setHeight(mViewport.getScreenHeight() / 9f / 32f * 42f);

        this.setX(mViewport.getScreenWidth() / 2 - mViewport.getScreenWidth() / 16 * 3);
        this.setY(0 - getHeight() - 10f);

        this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());;

        this.setTouchable(Touchable.disabled);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(mTexture, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    public void dispose() {
        mTexture.dispose();
    }
}