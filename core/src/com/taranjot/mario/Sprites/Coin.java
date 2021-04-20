package com.taranjot.mario.Sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.math.Vector2;
import com.taranjot.mario.Scenes.Controller;
import com.taranjot.mario.Scenes.Hud;
import com.taranjot.mario.Screens.PlayScreen;
import com.taranjot.mario.items.Mushroom;
import com.taranjot.mario.items.itemDef;
import com.taranjot.mario.marioGame;

public class Coin extends InteractiveTileObject {
    private static TiledMapTileSet tileSet;
    private final int BLANK_COIN=28;
    private final int COIN=58;


    public Coin(PlayScreen screen, MapObject object) {
        super(screen, object);

        tileSet=map.getTileSets().getTileSet("tileset_gutter");

        fixture.setUserData(this);
        setCategoryFilter(marioGame.COIN_BIT);

    }

    @Override
    public void onHeadHit(Mario mario) {
        Gdx.app.log("Coin", "collision");
        if(getCell().getTile().getId()==BLANK_COIN){

            marioGame.manager.get("music/sounds/bump.wav", Sound.class).play();}

        else{

            if(object.getProperties().containsKey("mushroom")){
                marioGame.manager.get("music/sounds/powerup_spawn.wav", Sound.class).play();
            screen.spawnItem(new itemDef(new Vector2(body.getPosition().x,body.getPosition().y+16/marioGame.PPM), Mushroom.class));}
            else
            marioGame.manager.get("music/sounds/coin.wav", Sound.class).play();
            Hud.addScore(200);
        }
        getCell().setTile(tileSet.getTile(BLANK_COIN));


    }

}
