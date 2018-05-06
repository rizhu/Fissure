package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import javax.swing.ImageIcon;

public class FissureGameScreen extends ScreenAdapter {
    public Viewport mViewport;
    public Miner mMiner;
    public FissureWorld mWorld;
    public Array<Tile> mTiles;
    public Array<Integer> mIntegers;
    public int mBreakCount;

    private final float m_DELTA_FISSURE = 2.3f;
    private float mElapsedTime;
    int mi = 0;

    @Override
    public void show() {
        mViewport = new ScreenViewport();
        mMiner = new Miner(mViewport);
        mTiles = new Array<Tile>(true, 144);
        mIntegers = new Array<Integer>(true, 144);
        for (int i = 0; i < 144; i++) mIntegers.add(new Integer(i));
        for (int i = 0; i < 144; i++) mTiles.add(new Tile(mViewport, i));
        mWorld = new FissureWorld(mViewport);

        mWorld.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                MoveToAction moveToAction = new MoveToAction();
                moveToAction.setDuration((Math.abs(x - mMiner.getX()) + Math.abs(y - mMiner.getY())) * 0.0009f);
                moveToAction.setPosition(x - mMiner.getWidth() / 2, y - mMiner.getHeight() / 2);
                mMiner.clearActions();
                mMiner.addAction(moveToAction);
                return true;
            }
        });

        Gdx.input.setInputProcessor(mWorld);
    }

    @Override
    public void resize(int width, int height) {
        mViewport.update(width, height);
        mMiner.init();

        for (Tile tile : mTiles){
            tile.init();
            mWorld.addActor(tile);
        }

        mWorld.addActor(mMiner);

        mElapsedTime = 0;
        mBreakCount = 0;
    }

    @Override
    public void render(float delta) {
        mElapsedTime += delta;
        mViewport.apply(true);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (mElapsedTime % m_DELTA_FISSURE < delta) {
            mIntegers.shuffle();
            if (mBreakCount < 2) {
                for (int i = 0; i < 48; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 4) {
                for (int i = 0; i < 60; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 6) {
                for (int i = 0; i < 72; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 8) {
                for (int i = 0; i < 84; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 10) {
                for (int i = 0; i < 96; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 12) {
                for (int i = 0; i < 108; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else {
                for (int i = 0; i < 120; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            }
            mBreakCount++;
        }

        mWorld.act(delta);
        mWorld.draw();
        mMiner.checkSafe(mTiles);

        if (!mMiner.isAlive) {
            resetGame();
        }
    }

    @Override
    public void dispose() {
        mMiner.dispose();
        for (Tile tile : mTiles) tile.dispose();
        mWorld.dispose();
    }

    public void resetGame() {
        mMiner.init();
        for (Tile tile : mTiles) tile.init();

        mElapsedTime = 0;
        mBreakCount = 0;
    }

}
