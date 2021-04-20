package com.taranjot.mario.Tools;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.taranjot.mario.Screens.PlayScreen;
import com.taranjot.mario.Sprites.Brick;
import com.taranjot.mario.Sprites.Coin;
import com.taranjot.mario.Sprites.Goomba;
import com.taranjot.mario.marioGame;

public class WorldCreator {
    private Array<Goomba> goombaArray;

    public Array<Goomba> getGoombaArray() {
        return goombaArray;
    }

    public WorldCreator(PlayScreen screen) {
        World world = screen.getWorld();
        TiledMap map = screen.getMap();
        BodyDef bodyDef = new BodyDef();
        PolygonShape shape = new PolygonShape();
        FixtureDef fixtureDef = new FixtureDef();
        Body body;


        for (MapObject object : map.getLayers().get(4).getObjects().getByType(RectangleMapObject.class)) {

            new Coin(screen, object);
        }


        for (MapObject object : map.getLayers().get(5).getObjects().getByType(RectangleMapObject.class)) {


            new Brick(screen, object);
        }

        for (MapObject object : map.getLayers().get(3).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / marioGame.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / marioGame.PPM);
            body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2 / marioGame.PPM, rectangle.getHeight() / 2 / marioGame.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = marioGame.OBJECT_BIT;
            body.createFixture(fixtureDef);
        }

        for (MapObject object : map.getLayers().get(8).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / marioGame.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / marioGame.PPM);
            body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2 / marioGame.PPM, rectangle.getHeight() / 2 / marioGame.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = marioGame.BORDER_BIT;
            body.createFixture(fixtureDef);
        }

        for (MapObject object : map.getLayers().get(2).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();

            bodyDef.type = BodyDef.BodyType.StaticBody;
            bodyDef.position.set((rectangle.getX() + rectangle.getWidth() / 2) / marioGame.PPM, (rectangle.getY() + rectangle.getHeight() / 2) / marioGame.PPM);
            body = world.createBody(bodyDef);
            shape.setAsBox(rectangle.getWidth() / 2 / marioGame.PPM, rectangle.getHeight() / 2 / marioGame.PPM);
            fixtureDef.shape = shape;
            fixtureDef.filter.categoryBits = marioGame.GROUND_BIT;

            body.createFixture(fixtureDef);
        }

        goombaArray = new com.badlogic.gdx.utils.Array<Goomba>();
        for (MapObject object : map.getLayers().get(6).getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
            goombaArray.add(new Goomba(screen,rectangle.getX()/marioGame.PPM,rectangle.getY()/marioGame.PPM));

        }
    }
}
