package io.github.farmageddon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Entity {
    protected float x, y;
    protected float speed;

    public Entity(float x, float y, float speed) {
        this.x = x;
        this.y = y;
        this.speed = speed;
    }

    public void render(SpriteBatch batch) {

    }

    public void update(float delta) {

    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }

    public void dispose() {

    }
}
