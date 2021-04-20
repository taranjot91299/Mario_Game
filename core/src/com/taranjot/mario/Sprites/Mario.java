package com.taranjot.mario.Sprites;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.taranjot.mario.Scenes.Hud;
import com.taranjot.mario.Screens.PlayScreen;
import com.taranjot.mario.items.Mushroom;
import com.taranjot.mario.marioGame;

public class Mario extends Sprite {
    public enum state{FALLING,JUMPING,STANDING,RUNNING,INAIR,GROWING,DEAD};
    public state currentState;
    public state previousState;
    public World world;
    public Body body;
    private TextureRegion marioStand;
    private TextureRegion marioDead;
    private Animation<TextureRegion> marioRun;
    private TextureRegion marioJump;
    private float stateTimer;
    private boolean runningRight;
    private TextureRegion bigMarioStand;
    private TextureRegion bigMarioJump;
    private Animation<TextureRegion> bigMairioRun;
    private Animation<TextureRegion> growMario;
    public boolean marioIsBig;
    private boolean runGrowAnimation;
    private boolean timeToDefineBigMario;
    private boolean timeToReefineMario;
    private boolean marioIsDead;
    private boolean death;
    private int check;


    public Mario(PlayScreen screen){
        //super(screen.getAtlas().findRegion("little_mario"));

        this.world=screen.getWorld();
        currentState=state.STANDING;
        previousState=state.STANDING;
        stateTimer=0;
        runningRight=true;
        Array<TextureRegion> frames=new Array<TextureRegion>();
        for(int i=1;i<4;i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("little_mario"),i*16,0,16,16));
            }
        marioRun=new Animation(0.1f,frames);
        frames.clear();

        for(int i=1;i<4;i++){
            frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),i*16,0,16,32));
        }
        bigMairioRun=new Animation(0.1f,frames);

        frames.clear();

        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),240,0,16,32));
        frames.add(new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32));
        growMario=new Animation(0.2f,frames);

        marioJump=new TextureRegion(screen.getAtlas().findRegion("little_mario"),80,0,16,16);
        bigMarioJump=new TextureRegion(screen.getAtlas().findRegion("big_mario"),80,0,16,32);
        frames.clear();
        marioDead=new TextureRegion(screen.getAtlas().findRegion("little_mario"),96,0,16,16);

        marioStand=new TextureRegion(screen.getAtlas().findRegion("little_mario"),0,0,16,16);
        bigMarioStand=new TextureRegion(screen.getAtlas().findRegion("big_mario"),0,0,16,32);

        defineMario();


        setBounds(0,0,16/marioGame.PPM,16/marioGame.PPM);
        setRegion(marioStand);
        setRegion(bigMarioStand);

    }
    public void update(float deltaTime)
    {   if(marioIsBig)
            setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2-6/marioGame.PPM);
    else
        setPosition(body.getPosition().x-getWidth()/2,body.getPosition().y-getHeight()/2);
    setRegion(getFrame(deltaTime));

    if(timeToDefineBigMario)
            defineBigMario();

    if(timeToReefineMario)
        redineMario();





    }
    public TextureRegion getFrame(float deltaTime) {
        currentState = getState();
        TextureRegion region;
        switch (currentState) {
            case DEAD:
                region=marioDead;
                break;
            case GROWING:
                region= growMario.getKeyFrame(stateTimer);
                 if(growMario.isAnimationFinished(stateTimer))
                    runGrowAnimation=false;
                break;
            case JUMPING:
                region = marioIsBig?bigMarioJump:marioJump;
                break;
            case RUNNING:
                region = marioIsBig? bigMairioRun.getKeyFrame(stateTimer, true): marioRun.getKeyFrame(stateTimer, true);
                break;
            case FALLING:
            case STANDING:
            default:
                region = marioIsBig?bigMarioStand:marioStand;
                break;

        }
        if ((body.getLinearVelocity().x < 0 || !runningRight) && !region.isFlipX())
        {       region.flip(true, false);
                runningRight=false;
        }
        else if((body.getLinearVelocity().x>0||runningRight) && region.isFlipX())
        {
            region.flip(true,false);
            runningRight=true;
        }
        stateTimer=currentState==previousState ? stateTimer+deltaTime:0;
        previousState=currentState;
        return region;

    }

    public state getCurrentState() {
        return currentState;
    }
    public boolean isBig(){
        return marioIsBig;
    }

    public void grow(){
        runGrowAnimation=true;
        marioIsBig=true;
        timeToDefineBigMario=true;
        setBounds(getX(),getY(),getWidth(),getHeight()*2);
        marioGame.manager.get("music/sounds/powerup.wav", Sound.class).play();

    }

    public state getState(){
        if(marioIsDead)
            return state.DEAD;
        if(runGrowAnimation)
            return state.GROWING;
        if(previousState==state.STANDING&&currentState==state.STANDING)
            return state.INAIR;
        if(body.getLinearVelocity().y>0||(body.getLinearVelocity().y<0&&previousState==state.JUMPING))
            return state.JUMPING;
        if(body.getLinearVelocity().y<0)
            return state.FALLING;
        else
            if(body.getLinearVelocity().x!=0)
                return state.RUNNING;
            else
                return state.STANDING;
    }
    public void defineBigMario(){
        Vector2 currentPosition=body.getPosition();
        world.destroyBody(body);

        BodyDef bodyDef=new BodyDef();
        bodyDef.position.set(currentPosition.add(0,10/marioGame.PPM));
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        body=world.createBody(bodyDef);

        FixtureDef fixtureDef=new FixtureDef();
        CircleShape circleShape =new CircleShape();
        circleShape.setRadius(5/marioGame.PPM);

        fixtureDef.filter.categoryBits=marioGame.MARIO_BIT;
        fixtureDef.filter.maskBits=marioGame.GROUND_BIT |marioGame.COIN_BIT|marioGame.BRICK_BIT|marioGame.OBJECT_BIT|marioGame.ENEMY_BIT|marioGame.ENEMY_HEAD_BIT|marioGame.ITEM_BIT|marioGame.BORDER_BIT;

        fixtureDef.shape=circleShape;
        body.createFixture(fixtureDef).setUserData(this);
        circleShape.setPosition(new Vector2(0,-14/marioGame.PPM));
        body.createFixture(fixtureDef).setUserData(this);

        EdgeShape head=new EdgeShape();
        head.set(new Vector2(-2/marioGame.PPM,6/marioGame.PPM),new Vector2(2/marioGame.PPM,6/marioGame.PPM));
        fixtureDef.filter.categoryBits=marioGame.MARIO_HEAD_BIT;
        fixtureDef.shape=head;
        fixtureDef.isSensor=true;
        body.createFixture(fixtureDef).setUserData(this);
        timeToDefineBigMario=false;

    }
    public void defineMario(){
        BodyDef bodyDef=new BodyDef();
        bodyDef.position.set(32/marioGame.PPM,32/marioGame.PPM);
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        body=world.createBody(bodyDef);

        FixtureDef fixtureDef=new FixtureDef();
        CircleShape circleShape =new CircleShape();
        circleShape.setRadius(5/marioGame.PPM);

        fixtureDef.filter.categoryBits=marioGame.MARIO_BIT;
        fixtureDef.filter.maskBits=marioGame.GROUND_BIT |marioGame.COIN_BIT|marioGame.BRICK_BIT|marioGame.OBJECT_BIT|marioGame.ENEMY_BIT|marioGame.ENEMY_HEAD_BIT|marioGame.ITEM_BIT|marioGame.BORDER_BIT;

        fixtureDef.shape=circleShape;
        body.createFixture(fixtureDef).setUserData(this);


        EdgeShape head=new EdgeShape();
        head.set(new Vector2(-2/marioGame.PPM,6/marioGame.PPM),new Vector2(2/marioGame.PPM,6/marioGame.PPM));
        fixtureDef.filter.categoryBits=marioGame.MARIO_HEAD_BIT;
        fixtureDef.shape=head;
        fixtureDef.isSensor=true;
        body.createFixture(fixtureDef).setUserData(this);

    }
    public void hit()
    {
        if(marioIsBig)
        {   marioGame.manager.get("music/sounds/powerdown.wav", Sound.class).play();
            marioIsBig=false;
            timeToReefineMario=true;
            setBounds(getX(),getY(),getWidth(),getHeight()/2);
        }
        else
            death();

    }
    public void death(){

        marioGame.manager.get("music/sounds/mariodie.wav", Sound.class).play();
        marioGame.manager.get("music/bakcgorundmusic/mario_music.ogg", Music.class).stop();
        marioIsDead=true;
        Filter filter=new Filter();
        filter.maskBits=marioGame.NOTHING_BIT;
        for(Fixture fixture:body.getFixtureList())
            fixture.setFilterData(filter);
        body.applyLinearImpulse(new Vector2(0,4f),body.getWorldCenter(),true);
    }
    public void redineMario(){

        Vector2 position=body.getPosition();
        world.destroyBody(body);
        BodyDef bodyDef=new BodyDef();
        bodyDef.position.set(position);
        bodyDef.type=BodyDef.BodyType.DynamicBody;
        body=world.createBody(bodyDef);

        FixtureDef fixtureDef=new FixtureDef();
        CircleShape circleShape =new CircleShape();
        circleShape.setRadius(5/marioGame.PPM);

        fixtureDef.filter.categoryBits=marioGame.MARIO_BIT;
        fixtureDef.filter.maskBits=marioGame.GROUND_BIT |marioGame.COIN_BIT|marioGame.BRICK_BIT|marioGame.OBJECT_BIT|marioGame.ENEMY_BIT|marioGame.ENEMY_HEAD_BIT|marioGame.ITEM_BIT|marioGame.BORDER_BIT;

        fixtureDef.shape=circleShape;
        body.createFixture(fixtureDef).setUserData(this);


        EdgeShape head=new EdgeShape();
        head.set(new Vector2(-2/marioGame.PPM,6/marioGame.PPM),new Vector2(2/marioGame.PPM,6/marioGame.PPM));
        fixtureDef.filter.categoryBits=marioGame.MARIO_HEAD_BIT;
        fixtureDef.shape=head;
        fixtureDef.isSensor=true;
        body.createFixture(fixtureDef).setUserData(this);
        timeToReefineMario=false;
    }
    public boolean isDead(){
        return marioIsDead;
    }
    public float getStateTimer(){
        return stateTimer;
    }



}
