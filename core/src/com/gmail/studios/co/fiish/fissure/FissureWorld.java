package com.gmail.studios.co.fiish.fissure;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.Viewport;

public class FissureWorld extends Stage {
    protected Array<Tile> mTiles;

    public FissureWorld(Viewport viewport) {
        super.setViewport(viewport);
    }

}
