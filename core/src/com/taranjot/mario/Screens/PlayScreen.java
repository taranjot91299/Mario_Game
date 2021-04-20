package com.taranjot.mario.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taranjot.mario.Scenes.Controller;
import com.taranjot.mario.Scenes.Hud;
import com.taranjot.mario.Sprites.Enemy;
import com.taranjot.mario.Sprites.Mario;
import com.taranjot.mario.Tools.WorldContactListner;
import com.taranjot.mario.Tools.WorldCreator;
import com.taranjot.mario.items.Mushroom;
import com.taranjot.mario.items.item;
import com.taranjot.mario.items.itemDef;
import com.taranjot.mario.marioGame;

import java.util.concurrent.LinkedBlockingQueue;

public class PlayScreen implements Screen {


    private World world;
    private Box2DDebugRenderer box2DDebugRenderer;


    public marioGame game;
    private TextureAtlas atlas;
    private OrthographicCamera gamecam;
    private Viewport gameport;
    private Hud hud;
    private Controller controller;


    private Mario player;



    private Music music;
    private WorldCreator creator;


    private TmxMapLoader mapLoader;
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;

    private Array<item> items;
    private LinkedBlockingQueue<itemDef> itemsToSpawn;


    public PlayScreen(marioGame game){

        atlas=new TextureAtlas("mario_and_enemy.pack");

        this.game=game;

        gamecam=new OrthographicCamera();
        gameport=new FitViewport(marioGame.V_WIDTH/marioGame.PPM,marioGame.V_HEIGHT/marioGame.PPM,gamecam);
        hud = new Hud(game.batch);
        controller=new Controller(game.batch);


        mapLoader=new TmxMapLoader();
        map=mapLoader.load("level1.tmx");
        renderer=new OrthogonalTiledMapRenderer(map,1/marioGame.PPM);
        gamecam.position.set(gameport.getWorldWidth()/2,gameport.getWorldHeight()/2,0);
        world=new World(new Vector2(0,-10),true);
        box2DDebugRenderer=new Box2DDebugRenderer();

        creator=new WorldCreator(this);
        player=new Mario(this);
        items=new Array<item>();
        itemsToSpawn=new LinkedBlockingQueue<itemDef>();

        world.setContactListener(new WorldContactListner());

        music=marioGame.manager.get(("music/bakcgorundmusic/mario_music.ogg"), Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();



    }
    public Music getMusic() {
        return music;
    }
    public void spawnItem(itemDef idef){
        itemsToSpawn.add(idef);
    }
    public void handleSpawningItems(){
        if(!itemsToSpawn.isEmpty()){
            itemDef idef =itemsToSpawn.poll();
            if(idef.type== Mushroom.class){
                items.add(new Mushroom(this,idef.position.x,idef.position.y));
            }

        }
    }

    public TextureAtlas getAtlas() {
        return atlas;
    }

    @Override
    public void show() {

    }
    public void handleInput(float dt){
        if(player.getCurrentState()!=Mario.state.DEAD){
        if((Gdx.input.isKeyJustPressed(Input.Keys.UP)||controller.isUpPressed())&&player.body.getLinearVelocity().y==0&&player.getCurrentState()!= Mario.state.INAIR)
            player.body.applyLinearImpulse(new Vector2(0,4f),player.body.getWorldCenter(),true);
        if((Gdx.input.isKeyPressed(Input.Keys.RIGHT)||controller.isRightPressed())&&player.body.getLinearVelocity().x<=2)
            player.body.applyLinearImpulse(new Vector2(0.1f,0),player.body.getWorldCenter(),true);
        if((Gdx.input.isKeyPressed(Input.Keys.LEFT)||controller.isLeftPressed())&&player.body.getLinearVelocity().x>=-2)
            player.body.applyLinearImpulse(new Vector2(-0.1f,0),player.body.getWorldCenter(),true);

    }
    }
    public void update(float dt){
        handleInput(dt);
        handleSpawningItems();
        for(Enemy enemy:creator.getGoombaArray()) {
            enemy.update(dt, this);
            if(enemy.getX()<player.getX()+224/marioGame.PPM)
                enemy.body.setActive(true);
        }
        world.step(1/60f,6,2);

        player.update(dt);
        for(item item:items)
            item.update(dt);
        hud.update(dt);
        if(player.getCurrentState()!= Mario.state.DEAD){
        gamecam.position.x=player.body.getPosition().x;}
        gamecam.update();

        renderer.setView(gamecam);
    }

    @Override
    public void render(float delta)
    {
        update(delta);
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        renderer.render();



       // box2DDebugRenderer.render(world,gamecam.combined);

        game.batch.setProjectionMatrix(gamecam.combined);
        game.batch.begin();
        player.draw(game.batch);
        for(item item:items)
            item.draw(game.batch);

        for(Enemy enemy:creator.getGoombaArray())
        {enemy.draw(game.batch);}
        game.batch.end();


        game.batch.setProjectionMatrix(hud.stage.getCamera().combined);
        hud.stage.draw();
        controller.draw();
        if(GameOver()){
            game.setScreen(new GameOverScreen(game));
            dispose();
        }


    }

    public TiledMap getMap(){
        return map;
    }
    public World getWorld(){
        return world;
    }
    @Override
    public void resize(int width, int height) {
        gameport.update(width, height);
        controller.resize(width,height);




    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        map.dispose();
        renderer.dispose();
        box2DDebugRenderer.dispose();
       world.dispose();
        hud.dispose();


    }
    public boolean GameOver()
    {
        if(player.getCurrentState()== Mario.state.DEAD&&player.getStateTimer()>3){
            return true;
        }
        return false;
    }


}
