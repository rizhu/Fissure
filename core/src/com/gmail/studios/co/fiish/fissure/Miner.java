package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.scenes.scene2d.Touchable;

public class Miner extends Actor {
    public boolean isAlive;
    public boolean isDeathDone;

    private Viewport mViewport;
    private float mTileX, mTileY, mTileWidth, mTileHeight;

    private Animation<TextureRegion> mRunningAnimation;
    private Animation<TextureRegion> mFallingAnimation;

    private Sound mDeath;
    private boolean hasPlayed;
    private float mElapsedTime, mDeadTime, mPixelsPerTileX, mPixelsPerTileY, mPixelX, mPixelY;

    public Miner(Viewport viewport, TextureAtlas atlas) {
        this.mViewport = viewport;
        mRunningAnimation = new Animation(1f/10f, atlas.findRegions("miner"));
        mFallingAnimation = new Animation(1f/10f, atlas.findRegions("falling"));
        mDeath = Gdx.audio.newSound(Gdx.files.internal("death.wav"));
    }

    public void init() {
        this.clearActions();
        mPixelsPerTileX = mViewport.getScreenWidth() / 16f;
        mPixelsPerTileY = mViewport.getScreenHeight() / 9f;
        mPixelX = mViewport.getScreenWidth() / 16f / 32f;
        mPixelY = mViewport.getScreenHeight() / 9f / 32f;

        /*
            Sets hitbox to width and height of legs
         */
        this.setWidth(mPixelX * 10f);
        this.setHeight(mPixelY * 4f);

        this.setX(mViewport.getScreenWidth() / 2f - getWidth() / 2f);
        this.setY(mViewport.getScreenHeight() / 2f - getHeight() / 2f);

        this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        this.setTouchable(Touchable.disabled);

        mTileWidth = getWidth() / mPixelsPerTileX;
        mTileHeight = getHeight() / mPixelsPerTileY;

        isDeathDone = false;
        isAlive = true;
        hasPlayed = false;

        mElapsedTime = 0.0f;
        mDeadTime = 0.0f;
    }

    public void reset() {
        this.clearActions();
        this.setX(mViewport.getScreenWidth() / 2f - getWidth() / 2f);
        this.setY(mViewport.getScreenHeight() / 2f - getHeight() / 2f);

        isDeathDone = false;
        isAlive = true;
        hasPlayed = false;

        mElapsedTime = 0.0f;
        mDeadTime = 0.0f;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        /*
            Prevents player from exiting playable space
         */
        if (getX() - 4 * mPixelX < 0) {
            clearActions();
            this.setX(0.0f + 4 * mPixelX);
        }
        if (getX() + getWidth() + 4 * mPixelX > mViewport.getScreenWidth()) {
            clearActions();
            this.setX(mViewport.getScreenWidth() - getWidth() - 4 * mPixelX);
        }
        if (getY() < 0) {
            clearActions();
            this.setY(0.0f);
        }
        if (getY() + getHeight() + 28 * mPixelY > mViewport.getScreenHeight()) {
            clearActions();
            this.setY(mViewport.getScreenHeight() - getHeight() - 28 * mPixelY);
        }

        mElapsedTime += delta;

        if (!isAlive) mDeadTime += delta;

        if (isAlive) {
            mTileX = getX() / mPixelsPerTileX;
            mTileY = getY() / mPixelsPerTileY;
        }
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * alpha);
       if (isAlive) {
           batch.draw(mRunningAnimation.getKeyFrame(mElapsedTime, true),
                   this.getX() - 4 * mPixelX, this.getY(), 20 * mPixelX, 32 * mPixelY);
        }

       if(!isAlive) {
            batch.draw(mFallingAnimation.getKeyFrame(mDeadTime, false),
                    MathUtils.floor(mTileX) * mPixelsPerTileX, MathUtils.floor(mTileY) * mPixelsPerTileY, 32 * mPixelX,
                    32 * mPixelY);

            if (!hasPlayed) {
                mDeath.play();
                hasPlayed = true;
            }

            if (mFallingAnimation.isAnimationFinished(mDeadTime)) {
                isDeathDone = true;
            }
       }
    }

    public void dispose() {
        mDeath.dispose();
    }

    public void checkSafe(Array<Tile> tiles) {
        int id1, id2, id3, id4;

        /*
            Finds ID's of 4 potential tiles that the hitbox occupies and checks each tile's state to
         */
        id1 = MathUtils.floor(mTileY) * 16 + MathUtils.floor(mTileX);
        id2 = MathUtils.floor(mTileY + mTileHeight) * 16 + MathUtils.floor(mTileX);
        id3 = MathUtils.floor(mTileY) * 16 + MathUtils.floor(mTileX + mTileWidth);
        id4 = MathUtils.floor(mTileY + mTileHeight) * 16 + MathUtils.floor(mTileX + mTileWidth);
        if (id1 >= 0 && id1 <=143 && tiles.get(id1).isFissure) {
            kill();
        } else if (id2 >= 0 && id2 <=143 && tiles.get(id2).isFissure) {
            mTileY += mTileHeight;  //draws death animation in correct tile
            kill();
        }else if (id3 >= 0 && id3 <=143 && tiles.get(id3).isFissure) {
            mTileX += mTileWidth;
            kill();
        } else if (id4 >= 0 && id4 <=143 && tiles.get(id4).isFissure) {
            mTileX += mTileWidth;
            mTileY += mTileHeight;
            kill();
        }
    }


    public void kill() {
        clearActions();
        this.isAlive = false;
    }
}
