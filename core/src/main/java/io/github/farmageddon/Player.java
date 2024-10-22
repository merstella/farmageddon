package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Player {

    private Texture player;
    private float x, y;

    public Player() {
        player = new Texture(Gdx.files.internal("player.png"));
        x = 100;
        y = 100;
    }

    public void update(float delta) {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) x -= 200 * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) x += 200 * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) y += 200 * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) y -= 200 * delta;
    }

    public void render(SpriteBatch batch) {
        batch.draw(player, x, y, 100, 100);
    }

    public void dispose() {
        player.dispose();
    }

    public float getX() {
        return x;
    }
    public float getY() {
        return y;
    }
}
