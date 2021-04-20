package com.taranjot.mario.Sprites;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.taranjot.mario.Screens.PlayScreen;



public abstract class Enemy extends Sprite {
    protected World world;
    protected Screen screen;
    public Body body;
    public Vector2 velocity;

    public Enemy(PlayScreen screen,float x,float y){
        this.world=screen.getWorld();
        this.screen=screen;
        setPosition(x,y);
        defineEnemy();
        velocity=new Vector2(.4f,0);
        body.setActive(false);
    }
    public abstract void defineEnemy();
    public abstract void update(float dt,PlayScreen screen);
    public abstract void hitOnHead();
    public void reverseVelocity(boolean x,boolean y){
        if(x)
            velocity.x=-velocity.x;
        if(y)
            velocity.y=-velocity.y;
    }
}
