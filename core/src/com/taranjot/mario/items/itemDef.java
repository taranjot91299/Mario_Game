package com.taranjot.mario.items;

import com.badlogic.gdx.math.Vector2;

public class itemDef {
    public Vector2 position;
    public Class<?> type;
    public itemDef(Vector2 position,Class<?> type){
        this.position=position;
        this.type=type;
    }
}
