package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

public class HelpPrompt extends Actor {
    private Viewport mViewport;
    private TextureRegion mRegion;

    public HelpPrompt(Viewport viewport, TextureAtlas atlas) {
        this.mViewport = viewport;
        this.mRegion = atlas.findRegion("helpprompt");
    }

    public void init() {
        this.clearActions();

        this.setWidth(mViewport.getScreenWidth() / 16f * 6f);
        this.setHeight(mViewport.getScreenHeight() / 9f * 4f);

        this.setX(mViewport.getScreenWidth() / 2 - getWidth() / 2);
        this.setY(mViewport.getScreenHeight() / 2 - getHeight() / 2);

        this.setTouchable(Touchable.disabled);
        this.setColor(getColor().r, getColor().g, getColor().b, 0f);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
