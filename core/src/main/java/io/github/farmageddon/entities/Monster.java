package io.github.farmageddon.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.MonsterAnimation;
import io.github.farmageddon.ultilites.CollisionHandling;

public class Monster extends Entity{
    private Vector2 targetPosition;
    private final Animator animation;
    public Animator.MonsterActivity currentActivity;
    Rectangle monsterBounds;
    public float getSpeed() { return speed; }

    public float getHealth() { return health; }

    public void setHealth(float health) { this.health = health; }

    public Vector2 getTargetPosition() { return targetPosition; }
    public void setTargetPosition(Vector2 targetPosition) { this.targetPosition = targetPosition; }
    public Monster(float x, float y, float speed, float targetX, float targetY, int maxHealth) {
        super(x, y, speed, true, maxHealth);
        targetPosition = new Vector2(targetX, targetY);

        animation = new Animator();
        currentActivity = Animator.MonsterActivity.IDLE_DOWN;
        monsterBounds = new Rectangle(x + 7, y + 9, 14, 9);
    }
    @Override
    public void update(float delta) {
        super.update(delta);

        updateDirectionAnimation(delta);
    }

    private void updateDirectionAnimation(float delta) {
        // Vector pointing from current position to the target position
        Vector2 movement = new Vector2(targetPosition.x - position.x, targetPosition.y - position.y);
        // Check if the monster is already at the target position
        if (movement.len() > 0.5f) { // Allow a small tolerance to prevent jitter
            movement.nor().scl(speed * delta); // Normalize and scale by speed and delta time

            // Simulate the next position to check for collisions
            Rectangle testBounds = new Rectangle(monsterBounds);
            testBounds.setPosition(monsterBounds.x + movement.x, monsterBounds.y + movement.y);

            // Check collision only at the new position
            if (!CollisionHandling.isColliding(testBounds)) {
                position.add(movement); // Update position if no collision
                monsterBounds.setPosition(position.x + 7, position.y + 9); // Update bounds position
            }

            // Determine animation direction based on movement vector
            if (movement.x > 0 && movement.y > 0) {
                currentActivity = Animator.MonsterActivity.UP_RIGHT;
            } else if (movement.x < 0 && movement.y > 0) {
                currentActivity = Animator.MonsterActivity.UP_LEFT;
            } else if (movement.x > 0 && movement.y < 0) {
                currentActivity = Animator.MonsterActivity.DOWN_RIGHT;
            } else if (movement.x < 0 && movement.y < 0) {
                currentActivity = Animator.MonsterActivity.DOWN_LEFT;
            } else if (movement.y > 0) {
                currentActivity = Animator.MonsterActivity.UP;
            } else if (movement.y < 0) {
                currentActivity = Animator.MonsterActivity.DOWN;
            } else if (movement.x > 0) {
                currentActivity = Animator.MonsterActivity.RIGHT;
            } else if (movement.x < 0) {
                currentActivity = Animator.MonsterActivity.LEFT;
            }
        } else {
            // If at target, set idle animation based on last movement direction
            currentActivity = getIdleDirection(currentActivity);
        }
    }

    private Animator.MonsterActivity getIdleDirection(Animator.MonsterActivity currentActivity) {
        switch (currentActivity) {
            case UP:
                return Animator.MonsterActivity.IDLE_UP;
            case DOWN:
                return Animator.MonsterActivity.IDLE_DOWN;
            case LEFT:
                return Animator.MonsterActivity.IDLE_LEFT;
            case RIGHT:
                return Animator.MonsterActivity.IDLE_RIGHT;
            case DOWN_LEFT:
                return Animator.MonsterActivity.IDLE_DOWN_LEFT;
            case DOWN_RIGHT:
                return Animator.MonsterActivity.IDLE_DOWN_RIGHT;
            case UP_LEFT:
                return Animator.MonsterActivity.IDLE_UP_LEFT;
            case UP_RIGHT:
                return Animator.MonsterActivity.IDLE_UP_RIGHT;
            default:
                return currentActivity;
        }
    }


    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        animation.render(batch, position.x, position.y, currentActivity);

        batch.end();
    }
    public void dispose() {
        animation.dispose();
    }
}
