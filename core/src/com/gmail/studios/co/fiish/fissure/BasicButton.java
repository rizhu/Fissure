package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
    Template for all buttons
 */

public abstract class BasicButton extends Actor {
    protected Viewport mViewport;
    protected TextureRegion mRegion;

    protected float mXDef, mYDef, mWidthDef, mHeightDef, mAlphaDef;   //default values for corresponding variable
    protected Touchable mTouchableDef;

    public void init() {
        this.clearActions();
        this.setWidth(mWidthDef);
        this.setHeight(mHeightDef);

        this.setX(mXDef);
        this.setY(mYDef);

        this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        this.setTouchable(mTouchableDef);
        this.setColor(getColor().r, getColor().g, getColor().b, mAlphaDef);
    }

    public void reset() {
        this.clearActions();
        this.setX(mXDef);
        this.setY(mYDef);

        this.setColor(getColor().r, getColor().g, getColor().b, mAlphaDef);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
