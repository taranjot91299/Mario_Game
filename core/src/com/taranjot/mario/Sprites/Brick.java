package com.taranjot.mario.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.World;
import com.taranjot.mario.Scenes.Hud;
import com.taranjot.mario.Screens.PlayScreen;
import com.taranjot.mario.marioGame;


public class Brick extends InteractiveTileObject{

    public Brick(PlayScreen screen, MapObject object){
        super(screen,object);
        fixture.setUserData(this);
        setCategoryFilter(marioGame.BRICK_BIT);

    }

    @Override
    public void onHeadHit(Mario mario) {
        if(mario.isBig()){
        Gdx.app.log("Brick","Collision");
        setCategoryFilter(marioGame.DESTROYED_BIT);
        getCell().setTile(null);
        Hud.addScore(200);
        marioGame.manager.get("music/sounds/breakblock.wav", Sound.class).play();
    }marioGame.manager.get("music/sounds/bump.wav", Sound.class).play();
    }
}
