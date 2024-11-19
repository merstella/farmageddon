package io.github.farmageddon;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class Entity {
    protected Vector2 position;
    protected float speed;

    public Entity() {
        position = new Vector2();
        speed = 1;
    }

    public Entity(float x, float y, float speed) {
        this.position = new Vector2(x, y);
        this.speed = speed;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public Vector2 getPosition() {
        return position;
    }
    public void render(SpriteBatch batch) {

    }

    public void update(float delta) {

    }

    public void dispose() {

    }
}
