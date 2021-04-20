package com.taranjot.mario.items;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.taranjot.mario.Screens.PlayScreen;
import com.taranjot.mario.Sprites.Mario;
import com.taranjot.mario.marioGame;

public abstract class item extends Sprite {
    protected PlayScreen screen;
    protected World world;
    protected Vector2 velocity;
    protected boolean toDestroy;
    protected boolean destroyed;
    protected Body body;




    public item(PlayScreen screen,float x,float y){
        this.screen=screen;
        this.world=screen.getWorld();
        setPosition(x,y);
        setBounds(getX(),getY(),16/ marioGame.PPM,16/marioGame.PPM);


        BodyDef bodyDef=new BodyDef();
        bodyDef.position.set(getX(),getY());
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        body=world.createBody(bodyDef);
        FixtureDef fixtureDef=new FixtureDef();
        CircleShape circleShape =new CircleShape();
        circleShape.setRadius(5/marioGame.PPM);
        fixtureDef.filter.categoryBits=marioGame.ITEM_BIT;
        fixtureDef.filter.maskBits=marioGame.MARIO_BIT|marioGame.OBJECT_BIT|marioGame.GROUND_BIT|marioGame.COIN_BIT|marioGame.BRICK_BIT;

        fixtureDef.shape=circleShape;
        body.createFixture(fixtureDef).setUserData(this);


    }


    public abstract void use(Mario mario);

    public void update(float dt){
        if(toDestroy && !destroyed){
            world.destroyBody(body);
            destroyed=true;
        }
    }

    public void destroy(){
        toDestroy=true;
    }
    public void draw(Batch batch){
        if(!destroyed)
          super.draw(batch);

    }
    public void reverseVelocity(boolean x,boolean y){
        if(x)
            velocity.x=-velocity.x;
        if(y)
            velocity.y=-velocity.y;
    }

}
