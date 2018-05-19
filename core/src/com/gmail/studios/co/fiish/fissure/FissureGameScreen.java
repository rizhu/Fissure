package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.math.BigDecimal;

import javax.swing.ImageIcon;

public class FissureGameScreen extends ScreenAdapter {
    public Viewport mViewport;
    public Miner mMiner;
    public FissureWorld mWorld;
    public Array<Tile> mTiles;
    public Array<Integer> mIntegers;
    public int mBreakCount;
    public SpriteBatch mBatch;

    private FreeTypeFontGenerator mGenerator;
    private FreeTypeFontParameter mParam;
    private BitmapFont mFont;
    private GlyphLayout mLayout;
    private BigDecimal mScore;

    private final float m_DELTA_FISSURE = 2.3f;
    private float mElapsedTime;

    @Override
    public void show() {
        mViewport = new ScreenViewport();
        mMiner = new Miner(mViewport);
        mTiles = new Array<Tile>(true, 144);
        mIntegers = new Array<Integer>(true, 144);
        for (int i = 0; i < 144; i++) mIntegers.add(new Integer(i));
        for (int i = 0; i < 144; i++) mTiles.add(new Tile(mViewport, i));

        mBatch = new SpriteBatch();
        mWorld = new FissureWorld(mViewport, mBatch);

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

        mGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        mParam = new FreeTypeFontParameter();
        mLayout = new GlyphLayout();

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

        mParam.size = (int) (0.9 * (mViewport.getScreenHeight() / 9));
        mParam.color = Color.WHITE;
        mFont = mGenerator.generateFont(mParam);

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

        mScore = round(mElapsedTime, 2);
        mBatch.begin();
        mLayout.setText(mFont, "" + mScore);
        mFont.draw(mBatch, mLayout, mViewport.getScreenWidth() - 10 - mLayout.width, mViewport.getScreenHeight() - 10 - mLayout.height / 3);
        mBatch.end();

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
        mGenerator.dispose();
        mFont.dispose();
        mBatch.dispose();
    }

    public void resetGame() {
        mMiner.init();
        for (Tile tile : mTiles) tile.init();

        mElapsedTime = 0;
        mBreakCount = 0;
    }

    private BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

}
