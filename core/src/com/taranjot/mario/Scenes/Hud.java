package com.taranjot.mario.Scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.taranjot.mario.marioGame;



public class Hud implements Disposable {
    public Stage stage;
    private Viewport viewport;

    private int worldTimer;
    private float timeCount;
    private  static int score;

   private Label countdounLabel;
   private static Label scoreLabel;
   private Label timeLabel;
   private Label levelLabel;
   private Label worldLabel;
   private Label marioLabel;


    @SuppressWarnings("DefaultLocale")
    public Hud(SpriteBatch spriteBatch){
        worldTimer=300;
        timeCount=0;
        score=0;
        viewport = new FitViewport(marioGame.V_WIDTH,marioGame.V_HEIGHT,new OrthographicCamera());
        stage=new Stage(viewport,spriteBatch);

        Table table=new Table();
        table.top();
        table.setFillParent(true);
        countdounLabel=new Label(String.format("%03d",worldTimer),new LabelStyle(new BitmapFont(), Color.WHITE));
        scoreLabel=new Label(String.format("%06d",score),new LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel=new Label("TIME",new LabelStyle(new BitmapFont(), Color.WHITE));
        levelLabel=new Label("1-1",new LabelStyle(new BitmapFont(), Color.WHITE));
        worldLabel=new Label("WORLD",new LabelStyle(new BitmapFont(), Color.WHITE));
        marioLabel=new Label("MARIO",new LabelStyle(new BitmapFont(), Color.WHITE));

        table.add(marioLabel).expandX().padTop(10);
        table.add(worldLabel).expandX().padTop(10);
        table.add(timeLabel).expandX().padTop(10);
        table.row();
        table.add(scoreLabel).expandX();
        table.add(levelLabel).expandX();
        table.add(countdounLabel).expandX();
        stage.addActor(table);

    }

    public void update(float deltTime){
        timeCount+=deltTime;
        if(timeCount>=1){
            worldTimer--;
            countdounLabel.setText(String.format("%03d",worldTimer));
            timeCount=0;
        }
    }
    public static void addScore(int value){
        score+=value;
        scoreLabel.setText(String.format("%06d",score));
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
