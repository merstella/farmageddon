package io.github.farmageddon.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.ultilites.HealthBar;
import io.github.farmageddon.screens.GameScreen;

public class Entity {

    protected Vector2 position;
    protected float speed;
    protected float health, maxHealth;
    private boolean hasHealthBar;
    private HealthBar.HealthBarEntity healthBar;  // Health bar instance
    protected boolean beingAttacked;
    public float getHealth() {
        return health;
    }
    public void setSpeed(float speed){
        this.speed = speed;
    }
    public Entity(float x, float y, float speed, boolean hasHealthBar, float maxHealth) {
        this.position = new Vector2(x, y);
        this.hasHealthBar = hasHealthBar;
        this.speed = speed;
        this.beingAttacked = false;
        // Initialize health and health bar if necessary
        if (hasHealthBar) {
            this.maxHealth = maxHealth;
            this.health = maxHealth;
            this.healthBar = new HealthBar.HealthBarEntity(maxHealth);  // Initialize health bar with max health
        }
    }

    public void setBeingAttacked(boolean beingAttacked) {
        this.beingAttacked = beingAttacked;
    }

    public boolean isBeingAttacked() {
        return beingAttacked;
    }

    public void update(float delta) {
        // Update any entity-specific logic (like movement, behavior, etc.)
    }

    public Vector2 getPosition() {
        return position;
    }
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    // Method to reduce health (for damage)
    public void takeDamage(float damage) {
        if (hasHealthBar) {
            health = Math.max(0, health - damage);
            healthBar.setHealth(health);
//            System.out.println("Auch!???");
//            System.out.print(health);
//            System.out.print(' ');
//            System.out.println(damage);
        }
    }


    // Method to heal the entity
    public void heal(float amount) {
        health = Math.min(maxHealth, health + amount);
        if (hasHealthBar) {
            healthBar.setHealth(health);  // Update health bar when healing
        }
    }

    // Render the entity (and its health bar if necessary)
    public void render(SpriteBatch batch) {
        // Render the entity's sprite or animation (if any)

        if (hasHealthBar) {
            batch.begin();
            // Render the health bar above the entity
            healthBar.draw(batch, GameScreen.shapeRenderer, position.x, position.y + 32);  // Adjust Y to position health bar
            batch.end();
        }
    }

    public void dispose() {
        // Dispose of resources when done (e.g., textures, animations)
    }
}
