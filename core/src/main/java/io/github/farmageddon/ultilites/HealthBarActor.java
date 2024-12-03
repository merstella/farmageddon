package io.github.farmageddon.ultilites;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.awt.*;

// For Scene2D actors, create a HealthBarActor
public class HealthBarActor extends Actor {
    private HealthBar healthBar;
    private Actor target;  // The actor this health bar belongs to
    private ShapeRenderer shapeRenderer;
    public HealthBarActor(Actor target, float maxHealth) {
        this.target = target;
        this.healthBar = new HealthBar(maxHealth);
    }

    public void setShapeRenderer(ShapeRenderer shapeRenderer) {
        this.shapeRenderer = shapeRenderer;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Position the health bar above the target actor
        float x = target.getX() + target.getWidth()/2;
        float y = target.getY() + target.getHeight();
        healthBar.draw((SpriteBatch) batch, shapeRenderer, x, y);
    }

    public void setHealth(float health) {
        healthBar.setHealth(health);
    }
}
