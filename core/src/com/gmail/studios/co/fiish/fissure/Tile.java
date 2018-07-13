package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

/*
    Individual tile on map. Has ID based on location (bottom left = 0, top right = 143) and has coordinates relative
    to other tiles (bottom left = (0, 0), top right = (15, 8)). Frozen state used to isolate cracking sequence to only
    during gameplay
 */

public class Tile  extends Actor {
    public boolean isFissure = false;
    public boolean isFrozen;

    private TextureRegion mRegion;
    private Viewport mViewport;
    private int mId, mCoordX, mCoordY;
    private boolean isBroken = false;

    private Animation<TextureRegion> mCrackAnimation;
    private float mElapsedTime;

    public Tile(Viewport viewport, TextureAtlas atlas, int id) {
        mViewport = viewport;

        int random = MathUtils.random(0, 4);
        switch (random) {
            case 1:
                mRegion = atlas.findRegion("walkable1");
                break;
            case 2:
                mRegion = atlas.findRegion("walkable2");
                break;
            case 3:
                mRegion = atlas.findRegion("walkable3");
                break;
            case 4:
                mRegion = atlas.findRegion("walkable4");
                break;
            case 0:
                mRegion = atlas.findRegion("walkable5");
                break;
        }
        mCrackAnimation = new Animation(1/5f, atlas.findRegions("crack"));

        this.mId = id;
        int temp;
        mCoordX = 0;
        for (temp = mId; temp % 16 != 0; temp--) mCoordX++;

        mCoordY = temp / 16;
    }

    public void init() {
        this.clearActions();
        this.setWidth(mViewport.getScreenWidth() / 16f);
        this.setHeight(mViewport.getScreenHeight() / 9f);

        isFrozen = false;
        isFissure = false;
        isBroken = false;

        mElapsedTime = 0.0f;
    }

    public void reset() {
        this.clearActions();
        isFrozen = false;
        isFissure = false;
        isBroken = false;

        mElapsedTime = 0.0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (!isFrozen) {
            mElapsedTime += delta;
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
        batch.draw(mRegion, mCoordX * this.getWidth(), mCoordY * this.getHeight(), this.getWidth(), this.getHeight());

        if (isBroken) {
            batch.draw(mCrackAnimation.getKeyFrame(mElapsedTime, false),
                    mCoordX * this.getWidth(), mCoordY * this.getHeight(), this.getWidth(), this.getHeight());
            isFissure = mCrackAnimation.getKeyFrameIndex(mElapsedTime) >= 4;
            if (mCrackAnimation.isAnimationFinished(mElapsedTime)) {
                isBroken = false;
                isFissure = false;
            }
        }
    }

    public void breakTile() {
        isBroken = true;
        mElapsedTime = 0;
    }

    public void toggleFreeze() {
        isFrozen = !isFrozen;
    }

}
