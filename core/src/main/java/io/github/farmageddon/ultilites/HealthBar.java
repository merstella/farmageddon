package io.github.farmageddon.ultilites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;

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

}
