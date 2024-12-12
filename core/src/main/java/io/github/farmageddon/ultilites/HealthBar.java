package io.github.farmageddon.ultilites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HealthBar {
    private float maxHealth;
    private float currentHealth;
    private float width = 10;  // width of health bar
    private float height = 0.5f;  // height of health bar
    private Color backgroundColor = Color.RED;
    private Color foregroundColor = Color.GREEN;

    public HealthBar(float maxHealth) {
        this.maxHealth = maxHealth;
        this.currentHealth = maxHealth;
    }

    public void setHealth(float health) {
        this.currentHealth = MathUtils.clamp(health, 0, maxHealth);
    }

    // Draw method for non-Scene2D entities
    public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float x, float y) {
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

        batch.end(); // Must end SpriteBatch before using ShapeRenderer

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(backgroundColor);
        shapeRenderer.rect(x + width, y + 25, width, height);

        // Draw foreground (filled health bar)
        shapeRenderer.setColor(foregroundColor);
        float healthWidth = (currentHealth / maxHealth) * width;
        shapeRenderer.rect(x + width, y + 25, healthWidth, height);
        shapeRenderer.end();

        batch.begin(); // Resume SpriteBatch
    }



    public static class HealthBarEntity {
        private float maxHealth;
        private float currentHealth;
        private float width = 16;  // Width of the health bar
        private float height = 2;   // Height of the health bar
        private Color backgroundColor = Color.RED;
        private Color foregroundColor = Color.GREEN;

        public HealthBarEntity(float maxHealth) {
            this.maxHealth = maxHealth;
            this.currentHealth = maxHealth;
        }

        // Set the current health, clamping it between 0 and maxHealth
        public void setHealth(float health) {
            this.currentHealth = MathUtils.clamp(health, 0, maxHealth);
        }

        // Draw the health bar at a specific position
        public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float x, float y) {
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

            batch.end(); // End SpriteBatch before using ShapeRenderer

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            // Draw the background of the health bar
            shapeRenderer.setColor(backgroundColor);
            shapeRenderer.rect(x + 8, y, width, height);

            // Draw the foreground (the health bar itself)
            shapeRenderer.setColor(foregroundColor);
            float healthWidth = (currentHealth / maxHealth) * width;
            shapeRenderer.rect(x + 8, y, healthWidth, height);

            shapeRenderer.end();

            batch.begin(); // Resume SpriteBatch
        }

        public void draw(SpriteBatch batch, ShapeRenderer shapeRenderer, float x, float y, float width, float height) {
            shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());

            batch.end(); // Must end SpriteBatch before using ShapeRenderer

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(backgroundColor);
            shapeRenderer.rect(x + width, y + 25, width, height);

            // Draw foreground (filled health bar)
            shapeRenderer.setColor(foregroundColor);
            float healthWidth = (currentHealth / maxHealth) * width;
            shapeRenderer.rect(x + width, y + 25, healthWidth, height);
            shapeRenderer.end();

            batch.begin(); // Resume SpriteBatch
        }

        // Optionally, you can add getter methods if needed
        public float getMaxHealth() {
            return maxHealth;
        }

        public float getCurrentHealth() {
            return currentHealth;
        }

        public void setMaxHealth(float maxHealth) {
            this.maxHealth = maxHealth;
        }
    }

    // For Scene2D actors, create a HealthBarActor
    public static class HealthBarActor extends Actor {
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
}
