package com.taranjot.mario.Tools;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.taranjot.mario.Sprites.Enemy;
import com.taranjot.mario.Sprites.InteractiveTileObject;
import com.taranjot.mario.Sprites.Mario;
import com.taranjot.mario.items.item;
import com.taranjot.mario.marioGame;

public class WorldContactListner implements ContactListener {

    @Override
    public void beginContact(Contact contact) {
        Fixture fixA=contact.getFixtureA();
        Fixture fixB=contact.getFixtureB();
        Fixture ob1=fixA;
        Fixture ob2=fixB;

        int cDef=fixA.getFilterData().categoryBits|fixB.getFilterData().categoryBits;
//        if(fixA.getUserData()=="head"||fixB.getUserData()=="head")
//        {
//            Fixture head =fixA.getUserData()=="head"?fixA:fixB;
//            Fixture object=head==fixA?fixB:fixA;
//
//            if(object.getUserData() != null && InteractiveTileObject.class.isAssignableFrom(object.getUserData().getClass())){
//                ((InteractiveTileObject) object.getUserData()).onHeadHit();
//            }
//        }
        switch (cDef){
            case marioGame.MARIO_HEAD_BIT|marioGame.BRICK_BIT:
            case marioGame.MARIO_HEAD_BIT|marioGame.COIN_BIT:
                if(fixA.getFilterData().categoryBits==marioGame.MARIO_HEAD_BIT)
                    ((InteractiveTileObject) fixB.getUserData()).onHeadHit((Mario) fixA.getUserData());
                else
                    ((InteractiveTileObject) fixA.getUserData()).onHeadHit((Mario) fixB.getUserData());
                break;
            case marioGame.ENEMY_HEAD_BIT|marioGame.MARIO_BIT:
                if(fixA.getFilterData().categoryBits==marioGame.ENEMY_HEAD_BIT)

                    ((Enemy) fixA.getUserData()).hitOnHead();

                else if(fixB.getFilterData().categoryBits==marioGame.ENEMY_HEAD_BIT)
                    ((Enemy) fixB.getUserData()).hitOnHead();
                break;
            case marioGame.ENEMY_HEAD_BIT|marioGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits==marioGame.ENEMY_HEAD_BIT)

                    ((Enemy) fixA.getUserData()).reverseVelocity(true,false);

                else
                    ((Enemy) fixB.getUserData()).reverseVelocity(true,false);
                break;

            case marioGame.ENEMY_BIT|marioGame.ENEMY_BIT:

                    ((Enemy) fixA.getUserData()).reverseVelocity(true,false);
                    ((Enemy) fixB.getUserData()).reverseVelocity(true,false);
                    break;

            case marioGame.ITEM_BIT|marioGame.OBJECT_BIT:
                if(fixA.getFilterData().categoryBits==marioGame.ITEM_BIT)

                    ((item) fixA.getUserData()).reverseVelocity(true,false);

                else
                    ((item) fixB.getUserData()).reverseVelocity(true,false);
                break;

            case marioGame.ITEM_BIT|marioGame.MARIO_BIT:
                if(fixA.getFilterData().categoryBits==marioGame.ITEM_BIT) {

                    ((item) fixA.getUserData()).use((Mario) ob2.getUserData());

                }
                else
                    ((item) fixB.getUserData()).use((Mario) ob1.getUserData());

                break;

             case marioGame.MARIO_BIT|marioGame.ENEMY_BIT:
                 if(fixA.getFilterData().categoryBits==marioGame.MARIO_BIT)
                     ((Mario) fixA.getUserData()).hit();
                 else
                     ((Mario) fixB.getUserData()).hit();
                 break;
            case marioGame.MARIO_BIT|marioGame.BORDER_BIT:
                if(fixA.getFilterData().categoryBits==marioGame.MARIO_BIT)
                    ((Mario) fixA.getUserData()).death();
                else
                    ((Mario) fixB.getUserData()).death();
                break;

        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
