package com.taranjot.mario.items;

import com.badlogic.gdx.math.Vector2;
import com.taranjot.mario.Screens.PlayScreen;
import com.taranjot.mario.Sprites.Mario;

public class Mushroom extends item {
    public Mushroom(PlayScreen screen,float x,float y) {
        super(screen,x,y);
        setRegion(screen.getAtlas().findRegion("mushroom"),0,0,16,16);
        velocity=new Vector2(0.7f,0);

    }

    @Override
    public void use(Mario mario) {

        if(!mario.isBig())
            mario.grow();
        destroy();

    }

    @Override
    public void update(float dt) {

            super.update(dt);
            setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
            velocity.y=body.getLinearVelocity().y;
            body.setLinearVelocity(velocity);


        }


    }

