package com.taranjot.mario.Sprites;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.utils.Array;
import com.taranjot.mario.Scenes.Hud;
import com.taranjot.mario.Screens.PlayScreen;
import com.taranjot.mario.marioGame;

public class Goomba extends Enemy {
    private float stateTime;
    private Animation<TextureRegion> walkAnimation;
    private Array<TextureRegion> frames;
    private boolean setToDestroy;
    private boolean destroyed;

    public Goomba(PlayScreen screen, float x, float y) {
        super(screen, x, y);
        frames=new Array<TextureRegion>();
        for(int i=0;i<2;i++)
        {
            frames.add(new TextureRegion(screen.getAtlas().findRegion("goomba"),i*16,0,16,16));
        }
        walkAnimation=new Animation(0.4f,frames);
        stateTime=0;
        destroyed=false;
        setToDestroy=false;
        setBounds(getX(),getY(),16/marioGame.PPM,16/marioGame.PPM);
    }


    public void update(float dt,PlayScreen screen) {

        stateTime += dt;
        if (setToDestroy && !destroyed) {
            Hud.addScore(200);
            marioGame.manager.get("music/sounds/stomp.wav", Sound.class).play();
            world.destroyBody(body);
            destroyed = true;
            setRegion(new TextureRegion(screen.getAtlas().findRegion("goomba"), 32, 0, 16, 16));
            stateTime=0;


        } else if (!destroyed) {
            body.setLinearVelocity(velocity);
            setPosition(body.getPosition().x - getWidth() / 2, body.getPosition().y - getHeight() / 2);
            setRegion((TextureRegion) walkAnimation.getKeyFrame(stateTime, true));
        }
    }



    @Override
    public void defineEnemy() {
        BodyDef bodyDef=new BodyDef();
        bodyDef.position.set(getX(),getY());
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        body=world.createBody(bodyDef);

        FixtureDef fixtureDef=new FixtureDef();
        CircleShape circleShape =new CircleShape();
        circleShape.setRadius(5/marioGame.PPM);

        fixtureDef.filter.categoryBits=marioGame.ENEMY_BIT;
        fixtureDef.filter.maskBits=marioGame.GROUND_BIT |marioGame.COIN_BIT|marioGame.BRICK_BIT|marioGame.ENEMY_BIT|marioGame.OBJECT_BIT|marioGame.MARIO_BIT;

        fixtureDef.shape=circleShape;
        body.createFixture(fixtureDef).setUserData(this);


        PolygonShape head =new PolygonShape();
        Vector2[] vetice=new Vector2[4];
        vetice[0]=new Vector2(-5,8).scl(1/marioGame.PPM);
        vetice[1]=new Vector2(5,8).scl(1/marioGame.PPM);
        vetice[2]=new Vector2(-3,3).scl(1/marioGame.PPM);
        vetice[3]=new Vector2(3,3).scl(1/marioGame.PPM);
        head.set(vetice);

        fixtureDef.shape=head;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits=marioGame.ENEMY_HEAD_BIT;
        body.createFixture(fixtureDef).setUserData(this);


    }




    public void draw(Batch batch){
        if(!destroyed||stateTime<1)
            super.draw(batch);
    }


    @Override
    public void hitOnHead() {
        setToDestroy=true;


    }



}
