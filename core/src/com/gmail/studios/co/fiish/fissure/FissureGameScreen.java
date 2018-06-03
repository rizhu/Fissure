package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
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
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class FissureGameScreen extends ScreenAdapter {
    public Viewport mViewport;
    public SpriteBatch mBatch;
    public Preferences mData;

    public Miner mMiner;
    public FissureWorld mWorld;
    public Array<Tile> mTiles;
    public Array<Integer> mIntegers;
    public int mBreakCount;

    public FissureGameUI mGameUI;
    public ScoreBG mScoreBG;
    public ReplayButton mReplay;

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
        mData = Gdx.app.getPreferences("Data");

        mMiner = new Miner(mViewport);
        mTiles = new Array<Tile>(true, 144);
        mIntegers = new Array<Integer>(true, 144);
        for (int i = 0; i < 144; i++) mIntegers.add(new Integer(i));
        for (int i = 0; i < 144; i++) mTiles.add(new Tile(mViewport, i));

        mScoreBG = new ScoreBG(mViewport);
        mReplay = new ReplayButton(mViewport);

        mBatch = new SpriteBatch();
        mWorld = new FissureWorld(mViewport, mBatch);
        mGameUI = new FissureGameUI(mViewport, mBatch);

        mWorld.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mMiner.clearActions();
                mMiner.addAction(moveTo(x - mMiner.getWidth() / 2, y, (Math.abs(x - mMiner.getX()) + Math.abs(y - mMiner.getY())) * 0.0009f));
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

        mScoreBG.init();
        mReplay.init();

        mReplay.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mScoreBG.setTouchable(Touchable.disabled);
                MoveToAction action = new MoveToAction(){
                    @Override
                    public void end() {
                        Gdx.input.setInputProcessor(mWorld);
                        resetGame();
                    }
                };
                action.setPosition(mScoreBG.getX(), mViewport.getScreenHeight() + 10);
                action.setDuration(0.35f);
                mScoreBG.addAction(action);

                mReplay.addAction(moveTo(mReplay.getX(), 0 - mReplay.getHeight() - 10, 0.35f));
                return  true;
            }
        });

        mWorld.addActor(mMiner);

        mGameUI.addActor(mScoreBG);
        mGameUI.addActor(mReplay);

        mParam.size = (int) (0.65 * (mViewport.getScreenHeight() / 9));
        mParam.color = Color.WHITE;
        mFont = mGenerator.generateFont(mParam);

        mElapsedTime = 0;
        mBreakCount = 0;

        Gdx.input.setInputProcessor(mWorld);
    }

    @Override
    public void render(float delta) {
        mElapsedTime += delta;
        mViewport.apply(true);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (mElapsedTime % m_DELTA_FISSURE < delta && mMiner.isAlive) {
            mIntegers.shuffle();
            if (mBreakCount < 2) {
                for (int i = 0; i < 60; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 3) {
                for (int i = 0; i < 72; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 4) {
                for (int i = 0; i < 84; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 6) {
                for (int i = 0; i < 96; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 8) {
                for (int i = 0; i < 108; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else if (mBreakCount < 9) {
                for (int i = 0; i < 120; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            } else {
                for (int i = 0; i < 132; i++) {
                    mTiles.get(mIntegers.get(i)).breakTile();
                }
            }
            mBreakCount++;
        }

        if (mMiner.isAlive || (!mMiner.isAlive && !mMiner.isDeathDone)) {
            mWorld.act(delta);
        }

        if (!mMiner.isAlive && !mTiles.get(143).isFrozen) {
            for (Tile tile : mTiles) tile.toggleFreeze();
        }

        mWorld.draw();

        if (!mMiner.isAlive) {
            mGameUI.act();
            mGameUI.draw();
        }

        if (mMiner.isAlive) mScore = round(mElapsedTime, 2);

        if (mMiner.isAlive || (!mMiner.isAlive && !mMiner.isDeathDone)) {
            mBatch.begin();
            mLayout.setText(mFont, "" + mScore);
            mFont.draw(mBatch, mLayout, mViewport.getScreenWidth() - 10 - mLayout.width, mViewport.getScreenHeight() - 10 - mLayout.height / 3);
            mBatch.end();
        }

        mMiner.checkSafe(mTiles);

       if (!mMiner.isAlive && mMiner.isDeathDone && !Gdx.input.getInputProcessor().equals(mGameUI)) {
           Gdx.input.setInputProcessor(mGameUI);

           mScoreBG.mScore = mScore;

           if (mScore.setScale(2, BigDecimal.ROUND_UP).floatValue() > mData.getFloat("highScore")) {
               mData.putFloat("highScore", mScore.setScale(2, BigDecimal.ROUND_UP).floatValue());
               mData.flush();
           }

           mScoreBG.updateHighScore();
           mScoreBG.addAction(moveTo(mViewport.getScreenWidth() / 2 - mScoreBG.getWidth() / 2, mViewport.getScreenHeight() / 2 - mScoreBG.getHeight() / 2,
                   0.35f));

           MoveToAction action = new MoveToAction() {
             @Override
             public void end() {
                 mReplay.setTouchable(Touchable.enabled);
             }
           };
           action.setPosition(mReplay.getX(), mViewport.getScreenHeight() * 0.1f);
           action.setDuration(0.35f);
           mReplay.addAction(action);
       }
    }

    @Override
    public void dispose() {
        mMiner.dispose();
        for (Tile tile : mTiles) tile.dispose();
        mWorld.dispose();
        mScoreBG.dispose();
        mReplay.dispose();
        mGenerator.dispose();
        mFont.dispose();
        mBatch.dispose();
    }

    public void resetGame() {
        for (Tile tile : mTiles) tile.init();
        mMiner.init();

        mScoreBG.init();
        mScoreBG.clearListeners();
        mScoreBG.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mScoreBG.setTouchable(Touchable.disabled);
                MoveToAction action = new MoveToAction(){
                    @Override
                    public void end() {
                        Gdx.input.setInputProcessor(mWorld);
                        resetGame();
                    }
                };
                action.setPosition(mScoreBG.getX(), mViewport.getScreenHeight() + 10);
                action.setDuration(0.8f);
                mScoreBG.addAction(action);
                return  true;
            }
        });

        mElapsedTime = 0;
        mBreakCount = 0;
    }

    private BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

}
