package com.taranjot.mario.Scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taranjot.mario.marioGame;

public class Controller{

    private Stage stage;
    public boolean upPressed,downPressed,leftPressed,rightPressed;
    private Viewport gameport;
    public Controller(SpriteBatch batch){

        gameport = new FitViewport(marioGame.V_WIDTH,marioGame.V_HEIGHT,new OrthographicCamera());
    stage=new Stage(gameport,batch);
        Gdx.input.setInputProcessor(stage);

        Table table =new Table();
        table.left().bottom();
        Image upImg=new Image(new Texture("up-button.png"));
        upImg.setSize(32,32);


        upImg.addListener(new InputListener(){


        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            upPressed=true;

            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            upPressed=false;
        }
    });
    Image leImg=new Image(new Texture("left-button.png"));
        leImg.setSize(32,32);
        leImg.addListener(new InputListener(){


        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            leftPressed=true;
            System.out.println(button);
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            leftPressed=false;
        }
    });

    Image riImg=new Image(new Texture("right-button.png"));
        riImg.setSize(32,32);
        riImg.addListener(new InputListener(){


        @Override
        public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
            rightPressed=true;
            return true;
        }

        @Override
        public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
            rightPressed=false;
        }
    });



        table.add();
        table.row().pad(10,10,10,10);
        table.add();
        table.add(leImg).size(leImg.getWidth(),leImg.getHeight());
        table.add();
        table.add(riImg).size(riImg.getWidth(),riImg.getHeight());
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add();
        table.add(upImg).size(upImg.getWidth(),upImg.getHeight());
        table.row().padBottom(5);

        stage.addActor(table);
}
public void draw(){
        stage.draw();
}

    public boolean isUpPressed() {
        return upPressed;
    }

    public boolean isLeftPressed() {
        return leftPressed;
    }

    public boolean isRightPressed() {
        return rightPressed;
    }
    public void resize(int width,int height){
        gameport.update(width,height);
    }


}
