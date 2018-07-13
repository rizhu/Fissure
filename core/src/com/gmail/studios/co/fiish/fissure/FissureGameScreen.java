package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import java.math.BigDecimal;

/*
    Screen handling all gameplay
 */

public class FissureGameScreen extends ScreenAdapter {
    private Viewport mViewport;
    private ActionResolver mActionResolver;
    private SpriteBatch mBatch;
    private Preferences mData;

    private FissureTitleUI mTitleUI;
    private FissureLogo mLogo;
    private TapPrompt mTapPrompt;
    private CreditsButton mCredits;

    private CreditsUI mCreditsUI;
    private CreditsBG mCreditsBG;
    private BackLeftButton mBackLeftButton;

    private HelpUI mHelpUI;
    private HelpPrompt mHelpPrompt;

    private FissureWorld mWorld;
    private Miner mMiner;
    private Array<Tile> mTiles;
    private Array<Integer> mIntegers;
    private int mBreakCount;

    private FissureGameUI mGameUI;
    private GameOverPrompt mGameOverPrompt;
    private ScoreBG mScoreBG;
    private ReplayButton mReplay;
    private HomeButton mHome;

    private FreeTypeFontGenerator mGenerator;
    private FreeTypeFontParameter mParam;
    private BitmapFont mFont;
    private GlyphLayout mLayout;
    private BigDecimal mScore;
    private float mElapsedTime;
    private float mPixelX, mPixelY; // The graphics in Fissure simulate a retro gaming environment and so the size of pixels on screen do not correspond to the
                                    // native resolution of the screen. These 2 variables offer a conversion between the retro pixels to the device's native resolution.
                                    // Separate conversions exists for the X and Y axes because not every device is perfectly 16:9.

    private boolean mShowAdOnBackToHome;
    private int mAdCounter = 0;

    private final float TIME_BETWEEN_FISSURES = 2.3f;
    private final int NUM_TILES = 144;
    private final float MINER_SPEED_MULTIPLIER = 0.00165f;

    public FissureGameScreen(TextureAtlas atlas) {
        mViewport = new ScreenViewport();

        mMiner = new Miner(mViewport, atlas);
        mTiles = new Array<Tile>(true, NUM_TILES);
        mIntegers = new Array<Integer>(true, NUM_TILES);
        for (int i = 0; i < NUM_TILES; i++) mIntegers.add(new Integer(i));
        for (int i = 0; i < NUM_TILES; i++) mTiles.add(new Tile(mViewport, atlas, i));

        mLogo = new FissureLogo(mViewport, atlas);
        mTapPrompt = new TapPrompt(mViewport, atlas);
        mCredits = new CreditsButton(mViewport, atlas);

        mCreditsBG = new CreditsBG(mViewport, atlas);
        mBackLeftButton = new BackLeftButton(mViewport, atlas);

        mHelpPrompt = new HelpPrompt(mViewport, atlas);

        mGameOverPrompt = new GameOverPrompt(mViewport, atlas);
        mScoreBG = new ScoreBG(mViewport, atlas);
        mReplay = new ReplayButton(mViewport, atlas);
        mHome = new HomeButton(mViewport, atlas);
    }

    @Override
    public void show() {
        mData = Gdx.app.getPreferences("Data");

        mBatch = new SpriteBatch();

        mAdCounter = 0;

        mTitleUI = new FissureTitleUI(mViewport, mBatch);
        mCreditsUI = new CreditsUI(mViewport, mBatch);
        mHelpUI = new HelpUI(mViewport, mBatch);
        mWorld = new FissureWorld(mViewport, mBatch);
        mGameUI = new FissureGameUI(mViewport, mBatch);

        /*
            Moves the middle of the Miner's feet to the location of a touch on screen
         */

        mWorld.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mMiner.clearActions();
                mMiner.addAction(moveTo(x - mMiner.getWidth() / 2, y,
                        (Math.abs((x - mMiner.getX()) / mPixelX) + Math.abs((y - mMiner.getY()) / mPixelY)) * MINER_SPEED_MULTIPLIER));
                return true;
            }
        });

        mGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
        mParam = new FreeTypeFontParameter();
        mLayout = new GlyphLayout();

        Gdx.gl.glClearColor(0, 0, 0, 1);

        Gdx.input.setInputProcessor(mTitleUI);
    }

    @Override
    public void resize(int width, int height) {
        mViewport.update(width, height);

        mPixelX = mViewport.getScreenWidth() / 16f / 32f; // Each Tile is a square of 32 x 32 retro tiles and the playing area is a 16 x 9 grid of Tiles
        mPixelY = mViewport.getScreenHeight() / 9f / 32f;

        mMiner.init();
        for (int i = 0; i < mTiles.size; i++){
            mTiles.get(i).init();
            mWorld.addActor(mTiles.get(i));
        }

        mGameOverPrompt.init();
        mLogo.init();
        mTapPrompt.init();
        mCredits.init();

        /*
            Brings player to Credits menu by fading out all children of Title UI
         */

        mCredits.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mCredits.addAction(sequence(moveTo(mCredits.getX(), mCredits.getY() - 3f * mPixelY, 0.1f),
                        moveTo(mCredits.getX(), mViewport.getScreenHeight() / 9f / 32f * 5, 0.1f),
                        run(new Runnable() {
                            @Override
                            public void run() {
                                mTapPrompt.clearActions();
                                mTapPrompt.addAction(fadeOut(0.3f));
                                mLogo.addAction(fadeOut(0.3f));
                                mCredits.addAction(fadeOut(0.3f));
                            }
                        }),
                        delay(0.3f, run(new Runnable() {
                            @Override
                            public void run() {
                                Gdx.input.setInputProcessor(mCreditsUI);
                                mCreditsBG.addAction(fadeIn(0.3f));
                                mBackLeftButton.addAction(fadeIn(0.3f));
                            }
                        }))));
                return true;
            }
        });

        /*
            Allows the player to touch to begin game sequence
         */

        mTitleUI.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if (event.isHandled()) return true; // Only starts game sequence if the user touched in an area that other buttons do not occupy
                mLogo.addAction(sequence(
                        run(new Runnable() {
                                @Override
                                public void run() {
                                if (!mData.getBoolean("firstPlay", true)) {
                                    mTapPrompt.clearActions();
                                    mTapPrompt.addAction(fadeOut(0.3f));
                                } else {
                                    mHelpUI.addActor(mTapPrompt);
                                }
                                mLogo.addAction(fadeOut(0.3f));
                                mCredits.addAction(fadeOut(0.3f));
                            }
                        }),
                        delay(0.2f, run(new Runnable() {
                                @Override
                                public void run() {
                                    if (mData.getBoolean("firstPlay", true)) {
                                        mHelpPrompt.init();
                                        mHelpUI.addActor(mHelpPrompt);
                                        mHelpUI.addListener(new InputListener(){
                                                @Override
                                                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                                    mHelpPrompt.clearActions();
                                                    mTapPrompt.clearActions();
                                                    mTapPrompt.addAction(fadeOut(0.3f));
                                                    mHelpPrompt.addAction(sequence(
                                                    fadeOut(0.25f),
                                                    delay(0.25f),
                                                    Actions.run(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                        mData.putBoolean("firstPlay", false);
                                                        mData.flush();
                                                        mHelpUI.clear();
                                                        mTitleUI.addActor(mTapPrompt);
                                                        Gdx.input.setInputProcessor(mWorld);
                                                        }
                                                    })
                                                ));
                                                return true;
                                            }
                                        });
                                        mHelpPrompt.addAction(fadeIn(0.25f));
                                        Gdx.input.setInputProcessor(mHelpUI);
                                    } else {
                                        Gdx.input.setInputProcessor(mWorld);
                                    }
                                }
                        }))));
                return true;
            }
        });

        mCreditsBG.init();
        mBackLeftButton.init();

        mBackLeftButton.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mBackLeftButton.addAction(sequence(moveTo(mBackLeftButton.getX(), mBackLeftButton.getY() - 3f * mPixelY, 0.1f),
                        moveTo(mBackLeftButton.getX(), mViewport.getScreenHeight() / 9f / 32f * 5, 0.1f),
                        run(new Runnable() {
                            @Override
                            public void run() {
                            mBackLeftButton.setTouchable(Touchable.disabled);
                            mBackLeftButton.addAction(fadeOut(0.3f));
                            mCreditsBG.addAction(fadeOut(0.3f));
                            }
                        }),
                        delay(0.25f, run(new Runnable() {
                            @Override
                            public void run() {
                                backToHome();
                            }
                        }))));
                return true;
            }
        });

        mScoreBG.init();
        mReplay.init();
        mHome.init();

        mReplay.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mScoreBG.setTouchable(Touchable.disabled);
                mReplay.setTouchable(Touchable.disabled);
                mHome.setTouchable(Touchable.disabled);

                mReplay.addAction(sequence(moveTo(mReplay.getX(), mReplay.getY() - 3f * mPixelY, 0.1f),
                        moveTo(mReplay.getX(), mReplay.getY() + 3f * mPixelY, 0.1f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                mGameOverPrompt.addAction(sequence(fadeOut(0.25f), moveTo(mGameOverPrompt.getX(), mGameOverPrompt.getY() + 16f * mPixelY, 0.25f)));
                                mReplay.addAction(fadeOut(0.2f));
                                mHome.addAction(fadeOut(0.2f));
                                mScoreBG.addAction(delay(0.2f, sequence(moveTo(mScoreBG.getX(), mViewport.getScreenHeight() + 10, 0.15f),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                resetGame();
                                            }}))));
                            }
                        })));

                return  true;
            }
        });

        mHome.addListener(new InputListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                mScoreBG.setTouchable(Touchable.disabled);
                mReplay.setTouchable(Touchable.disabled);
                mHome.setTouchable(Touchable.disabled);

                mHome.addAction(sequence(moveTo(mHome.getX(), mHome.getY() - 3f * mPixelY, 0.1f),
                        moveTo(mHome.getX(), mHome.getY() + 3f * mPixelY, 0.1f),
                        Actions.run(new Runnable() {
                            @Override
                            public void run() {
                                mGameOverPrompt.addAction(sequence(fadeOut(0.25f), moveTo(mGameOverPrompt.getX(), mGameOverPrompt.getY() + 16f * mPixelY, 0.25f)));
                                mHome.addAction(fadeOut(0.2f));
                                mReplay.addAction(fadeOut(0.2f));
                                mScoreBG.addAction(delay(0.2f, sequence(moveTo(mScoreBG.getX(), mViewport.getScreenHeight() + 10, 0.15f),
                                        Actions.run(new Runnable() {
                                            @Override
                                            public void run() {
                                                backToHome();
                                            }
                                        }))));
                            }
                        })));
                return  true;
            }
        });

        mTitleUI.addActor(mLogo);
        mTitleUI.addActor(mTapPrompt);
        mTitleUI.addActor(mCredits);

        mCreditsUI.addActor(mCreditsBG);
        mCreditsUI.addActor(mBackLeftButton);

        mWorld.addActor(mMiner);

        mGameUI.addActor(mGameOverPrompt);
        mGameUI.addActor(mScoreBG);
        mGameUI.addActor(mReplay);
        mGameUI.addActor(mHome);

        mParam.size = (int) (0.65 * (mViewport.getScreenHeight() / 9));
        mParam.color = Color.WHITE;
        mFont = mGenerator.generateFont(mParam);

        mElapsedTime = 0f;
        mBreakCount = 0;

        mShowAdOnBackToHome = true;

        Gdx.input.setInputProcessor(mTitleUI);
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.getInputProcessor().equals(mWorld)) {
            mElapsedTime += delta;
        }
        mViewport.apply(true);

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        if (Gdx.input.getInputProcessor().equals(mWorld) && mMiner.isAlive && mElapsedTime % TIME_BETWEEN_FISSURES < delta) {
            breakTiles();
        }

        if (mMiner.isAlive || (!mMiner.isAlive && !mMiner.isDeathDone)) {
            mWorld.act(delta);
        }

        if (!mMiner.isAlive && !mTiles.get(143).isFrozen) {
            for (int i = 0; i < mTiles.size; i++) mTiles.get(i).toggleFreeze();
        }

        mWorld.draw();

        if (Gdx.input.getInputProcessor().equals(mGameUI) && !mMiner.isAlive) {
            mGameUI.act();
            mGameUI.draw();
        }

        if (Gdx.input.getInputProcessor().equals(mTitleUI)) {
            mTitleUI.act();
            mTitleUI.draw();
        }

        if (Gdx.input.getInputProcessor().equals(mCreditsUI)) {
            mCreditsUI.act();
            mCreditsUI.draw();
        }

        if (Gdx.input.getInputProcessor().equals(mHelpUI)) {
            mHelpUI.act();
            mHelpUI.draw();
        }

        if ((Gdx.input.getInputProcessor().equals(mWorld) || Gdx.input.getInputProcessor().equals(mHelpUI))
                && mMiner.isAlive) {
            mScore = round(mElapsedTime, 2);
        }

        if (!Gdx.input.getInputProcessor().equals(mTitleUI) && !Gdx.input.getInputProcessor().equals(mCreditsUI) && mMiner.isAlive || (!mMiner.isAlive && !mMiner.isDeathDone)) {
            drawScore();
        }

        mMiner.checkSafe(mTiles);

       if (!mMiner.isAlive && mMiner.isDeathDone && !Gdx.input.getInputProcessor().equals(mGameUI)) {
           doMinerDeathSequence();
       }
    }

    @Override
    public void dispose() {
        mTitleUI.dispose();
        mCreditsUI.dispose();
        mHelpUI.dispose();
        mMiner.dispose();
        mWorld.dispose();
        mScoreBG.dispose();
        mGameUI.dispose();
        mGenerator.dispose();
        mFont.dispose();
        mBatch.dispose();
    }

    public void setActionResolver(ActionResolver actionResolver) {
        this.mActionResolver = actionResolver;
    }

    private void breakTiles() {
        mIntegers.shuffle();
        if (mBreakCount < 2) {
            for (int i = 0; i < 36; i++) {
                mTiles.get(mIntegers.get(i)).breakTile();
            }
        } else if (mBreakCount < 3) {
            for (int i = 0; i < 54; i++) {
                mTiles.get(mIntegers.get(i)).breakTile();
            }
        } else if (mBreakCount < 5) {
            for (int i = 0; i < 72; i++) {
                mTiles.get(mIntegers.get(i)).breakTile();
            }
        } else if (mBreakCount < 7) {
            for (int i = 0; i < 90; i++) {
                mTiles.get(mIntegers.get(i)).breakTile();
            }
        }else if (mBreakCount < 8) {
            for (int i = 0; i < 108; i++) {
                mTiles.get(mIntegers.get(i)).breakTile();
            }
        }else if (mBreakCount < 18) {
            for (int i = 0; i < 128; i++) {
                mTiles.get(mIntegers.get(i)).breakTile();
            }
        } else {
            for (int i = 0; i < 130; i++) {
                mTiles.get(mIntegers.get(i)).breakTile();
            }
        }
        mBreakCount++;
    }

    private boolean doAdCheck() {
        mAdCounter += MathUtils.random(1, 2);
        if (mAdCounter >= 5) {
            mAdCounter = 0;
            mActionResolver.showInterstitial();
            mShowAdOnBackToHome = false;
            backToHome();
            return true;
        }
        return false;
    }

    private void doMinerDeathSequence() {
        Gdx.input.setInputProcessor(mGameUI);
        mActionResolver.showBanner(true);

        mScoreBG.mScore = mScore;

        if (mScore.setScale(2, BigDecimal.ROUND_UP).floatValue() > mData.getFloat("highScore")) {
            mData.putFloat("highScore", mScore.setScale(2, BigDecimal.ROUND_UP).floatValue());
            mData.flush();
        }

        mScoreBG.updateHighScore();

        mGameOverPrompt.addAction(sequence(fadeIn(0.25f),
                moveTo(mGameOverPrompt.getX(), mGameOverPrompt.getY() - 16f * mPixelY, 0.25f)));
        mScoreBG.addAction(delay(0.25f,
                moveTo(mViewport.getScreenWidth() / 2 - mScoreBG.getWidth() / 2, mViewport.getScreenHeight() / 2 - mScoreBG.getHeight() / 2,
                        0.25f)));
        mReplay.addAction(delay(0.5f, sequence(fadeIn(0.2f),
                Actions.run(new Runnable() {
                    @Override
                    public void run() {
                        mReplay.setTouchable(Touchable.enabled);
                        mHome.setTouchable(Touchable.enabled);
                    }
                }))));
        mHome.addAction(delay(0.5f, fadeIn(0.2f)));
    }

    private void drawScore() {
        mBatch.begin();
        mLayout.setText(mFont, "" + mScore);
        mFont.draw(mBatch, mLayout, mViewport.getScreenWidth() - 10 - mLayout.width, mViewport.getScreenHeight() - 10 - mLayout.height / 3);
        mBatch.end();
    }

    private void resetGame() {
        mActionResolver.showBanner(false);
        if (doAdCheck()) {
            return;
        }

        for (int i = 0; i < mTiles.size; i++) mTiles.get(i).reset();
        mMiner.reset();

        mHome.reset();
        mReplay.reset();

        mGameOverPrompt.reset();
        mScoreBG.reset();

        mElapsedTime = 0;
        mBreakCount = 0;

        Gdx.input.setInputProcessor(mWorld);
    }

    private void backToHome() {
        mActionResolver.showBanner(false);
        if (mShowAdOnBackToHome && doAdCheck()) {
            return;
        }
        mShowAdOnBackToHome = true;

        mLogo.reset();
        mTapPrompt.reset();
        mCredits.reset();

        mCreditsBG.reset();
        mBackLeftButton.reset();

        for (int i = 0; i < mTiles.size; i++) mTiles.get(i).reset();
        mMiner.reset();

        mHome.reset();
        mReplay.reset();

        mGameOverPrompt.reset();
        mScoreBG.reset();

        mElapsedTime = 0;
        mBreakCount = 0;

        Gdx.input.setInputProcessor(mTitleUI);
    }

    private BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

}
