package io.github.farmageddon.entities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.screens.GameScreen;

public class Entity {
    protected GameScreen gameScreen;
    protected Vector2 position;
    protected float speed;
    protected float health, maxHealth;
    // Kich thuoc thanh mau
    private final int BAR_WIDTH = 100;
    private final int BAR_HEIGHT = 10;
    private boolean hasHealthBar;
    private ShapeRenderer shapeRenderer;
    public Entity(float x, float y, float speed, boolean hasHealthBar, float maxHealth) {
        this.position = new Vector2(x, y);
        this.hasHealthBar = hasHealthBar;
        this.speed = speed;
        if (hasHealthBar) {
            this.maxHealth = maxHealth;
            this.health = maxHealth;
            this.shapeRenderer = new ShapeRenderer();
        }
    }

    public void takeDamage(float damage) {
        if (hasHealthBar) {
            health = Math.max(0, health - damage);
        }
    }

    public void heal(float amount) {
        if (hasHealthBar) {
            health = Math.min(maxHealth, health + amount);
        }
    }


    public void update(float delta) {

    }

    public Vector2 getPosition() {
        return position;
    }



    public void render(SpriteBatch batch) {
        if (hasHealthBar) {
            // Render health bar
            shapeRenderer.setProjectionMatrix(gameScreen.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            // Background bar (gray)
            shapeRenderer.setColor(Color.GRAY);
            shapeRenderer.rect(position.x, position.y + 50, BAR_WIDTH, BAR_HEIGHT);

            // Health bar (green, shrinking based on health)
            shapeRenderer.setColor(Color.GREEN);
            float healthPercentage = health / maxHealth;
            shapeRenderer.rect(position.x, position.y + 50, BAR_WIDTH * healthPercentage, BAR_HEIGHT);

            shapeRenderer.end();
        }
    }

    public void dispose() {
        if (hasHealthBar && shapeRenderer != null) {
            shapeRenderer.dispose();
        }
    }
}
