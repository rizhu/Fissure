package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.math.BigDecimal;

public class ScoreBG extends Actor {
    public Viewport mViewport;
    public BigDecimal mScore;

    private Texture mTexture;

    private FreeTypeFontGenerator mGenerator;
    private FreeTypeFontGenerator.FreeTypeFontParameter mParam;
    private BitmapFont mFont;
    private GlyphLayout mLayout;

    public ScoreBG(Viewport viewport) {
        this.mViewport = viewport;
        mTexture = new Texture(Gdx.files.internal("scorebg.png"));

        mGenerator = new FreeTypeFontGenerator(Gdx.files.internal("font.ttf"));
    }

    public void init() {
        this.setWidth(mViewport.getScreenWidth() / 16f * 6);
        this.setHeight(mViewport.getScreenHeight() / 9 * 7);

        this.setX(mViewport.getScreenWidth() / 2 - this.getWidth() / 2);
        this.setY(mViewport.getScreenHeight() + 10);

        this.setBounds(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        this.setTouchable(Touchable.disabled);

        mParam = new FreeTypeFontGenerator.FreeTypeFontParameter();
        mLayout = new GlyphLayout();
        mParam.size = (int) (1.1f * (mViewport.getScreenHeight() / 9));
        mParam.color = Color.WHITE;
        mFont = mGenerator.generateFont(mParam);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float alpha) {
        batch.draw(mTexture, this.getX(), this.getY(), this.getWidth(), this.getHeight());

        mLayout.setText(mFont, "Time:");
        mFont.draw(batch, mLayout, getX() + getWidth() / 2 - mLayout.width / 2, getY() + getHeight() - 20 - mLayout.height / 2);

        mLayout.setText(mFont, "" + mScore);
        mFont.draw(batch, mLayout, getX() + getWidth() / 2 - mLayout.width / 2, getY() + getHeight() - 40 - (1.5f * mLayout.height));
    }

    public void dispose() {
        mTexture.dispose();
        mGenerator.dispose();
        mFont.dispose();
    }
}
