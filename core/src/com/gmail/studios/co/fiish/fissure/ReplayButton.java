package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ReplayButton extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;

    public ReplayButton(Viewport viewport, TextureAtlas atlas) {
        this.mViewport = viewport;
        mRegion = atlas.findRegion("replay");
    }

    public void init() {
        this.clearActions();
        this.setWidth(mViewport.getScreenWidth() / 16f / 32f * 84f);
        this.setHeight(mViewport.getScreenHeight() / 9f / 32f * 42f);

        this.setX(mViewport.getScreenWidth() / 2 - mViewport.getScreenWidth() / 16 * 3);
        this.setY(mViewport.getScreenHeight() * 0.1f);

        this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        this.setTouchable(Touchable.disabled);
        this.setColor(getColor().r, getColor().g, getColor().b, 0f);
    }

    public void reset() {
        this.clearActions();
        this.setX(mViewport.getScreenWidth() / 2 - mViewport.getScreenWidth() / 16 * 3);
        this.setY(mViewport.getScreenHeight() * 0.1f);

        this.setColor(getColor().r, getColor().g, getColor().b, 0f);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
