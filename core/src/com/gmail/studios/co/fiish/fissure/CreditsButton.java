package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CreditsButton extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;

    public CreditsButton(Viewport viewport, TextureAtlas atlas) {
        mViewport = viewport;
        mRegion = atlas.findRegion("credits");
    }

    public void init() {
        clearActions();
        this.setWidth(mViewport.getScreenWidth() / 16f);
        this.setHeight(mViewport.getScreenHeight() / 9f);

        this.setX(mViewport.getScreenWidth() / 16f / 32f * 5);
        this.setY(mViewport.getScreenHeight() / 9f / 32f * 5);

        this.setBounds(getX(), getY(), getWidth(), getHeight());

        setTouchable(Touchable.enabled);
        setColor(getColor().r, getColor().g, getColor().b, 1f);
    }

    public void reset() {
        clearActions();
        setTouchable(Touchable.enabled);
        setColor(getColor().r, getColor().g, getColor().b, 1f);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
