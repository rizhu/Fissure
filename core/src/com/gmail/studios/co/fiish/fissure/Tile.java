package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Tile  extends Actor {
    public boolean isFissure = false;
    public boolean isFrozen;

    private Texture mTexture;
    private Viewport mViewport;
    private int mId, mCoordX, mCoordY;
    private boolean isBroken = false;

    private TextureAtlas mAtlas;
    private Animation<TextureRegion> mCrackAnimation;
    private float mElapsedTime;

    public Tile(Viewport viewport, int id) {
        mViewport = viewport;

        this.mId = id;
        int temp;
        mCoordX = 0;
        for (temp = mId; temp % 16 != 0; temp--) mCoordX++;

        mCoordY = temp / 16;

        mAtlas = new TextureAtlas(Gdx.files.internal("spritesheets/cracksheet.atlas"));
        mCrackAnimation = new Animation(1/5f, mAtlas.getRegions());
    }

    public void init() {
        this.setWidth(mViewport.getScreenWidth() / 16f);
        this.setHeight(mViewport.getScreenHeight() / 9f);

        int random = MathUtils.random(0, 4);
        switch (random) {
            case 1:
                mTexture = new Texture(Gdx.files.internal("walkable_1.png"));
                break;
            case 2:
                mTexture = new Texture(Gdx.files.internal("walkable_2.png"));
                break;
                case 3:
                mTexture = new Texture(Gdx.files.internal("walkable_3.png"));
                break;
            case 4:
                mTexture = new Texture(Gdx.files.internal("walkable_4.png"));
                break;
            case 0:
                mTexture = new Texture(Gdx.files.internal("walkable_5.png"));
                break;
        }

        isFrozen = false;
        isFissure = false;
        isBroken = false;

        mElapsedTime = 0.0f;
    }

    public void reset() {
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
        batch.draw(mTexture, mCoordX * this.getWidth(), mCoordY * this.getHeight(), this.getWidth(), this.getHeight());

        if (isBroken) {
            batch.draw(mCrackAnimation.getKeyFrame(mElapsedTime, false),
                    mCoordX * this.getWidth(), mCoordY * this.getHeight(), this.getWidth(), this.getHeight());
            if (mCrackAnimation.getKeyFrameIndex(mElapsedTime) >= 4) {
                isFissure = true;
            } else {
                isFissure = false;
            }

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

    public void dispose() {
        mTexture.dispose();
    }

    public void toggleFreeze() {
        isFrozen = !isFrozen;
    }

}
