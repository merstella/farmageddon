package io.github.farmageddon.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.HealthBar;

import java.util.ArrayList;
import java.util.List;

public class House {
    private List<HouseEntity> entitiesOnBorder;  // List of entities on the border
    // Total health of the house (sum of health of entities)
    private float currentHealth;  // Current health of the house
    float x, y;
    private HealthBar.HealthBarEntity healthBar;
    public House(float x, float y) {
        this.x = x;
        this.y = y;
        this.entitiesOnBorder = new ArrayList<>();
        this.currentHealth = 1000;
        healthBar = new HealthBar.HealthBarEntity(currentHealth);
    }

    // Add a HouseEntity (entity on the border) to the house
    public void addEntity(HouseEntity entity) {
        entitiesOnBorder.add(entity);
    }

    // Deal damage to the house (through an entity)
    public void takeDamage(float damage) {
        currentHealth -= damage;
        if (currentHealth <= 0) {
            currentHealth = 0;
            destroyHouse();
        }
        healthBar.setHealth(currentHealth);
    }

    // Destroy the house when health reaches 0
    private void destroyHouse() {
        // Handle house destruction logic (e.g., remove house, notify other systems)
//        System.out.println("House destroyed!");
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public List<HouseEntity> getEntitiesOnBorder() {
        return entitiesOnBorder;
    }
    public void render(SpriteBatch batch) {
        batch.begin();
        // Render the health bar above the entity
        healthBar.draw(batch, GameScreen.shapeRenderer, x - 50, y + 20, 50, 3);
        batch.end();
    }
}
