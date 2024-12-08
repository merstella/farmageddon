package io.github.farmageddon.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.ultilites.CollisionHandling;
import io.github.farmageddon.ultilites.GridNode;

public class Monster extends Entity {
    private final Animator animation;
    private Animator.MonsterActivity currentActivity;

    private boolean isDead;
    private boolean isDying;
    private float deathTimer;
    private boolean markedForRemoval;
    // Movement and Path
    private float speed; // Pixels per second
    private Plant targetPlant;

    public Plant getTargetPlant() {
        return targetPlant;
    }

    public void setTargetPlant(Plant targetPlant) {
        this.targetPlant = targetPlant;
    }
    // Constants
    private static final float DEATH_ANIMATION_DURATION = 0.5f; // Seconds
    private Array<GridNode> path; // The path to follow
    private int currentPathIndex = 0; // Index of the current node in the path
    public Array<GridNode> getPath() {
        return path;  // Get the current path
    }

    public Monster(float x, float y, float speed, int maxHealth) {
        super(x, y, speed, true, maxHealth); // Assuming Entity constructor: (x, y, speed, isActive, maxHealth)
        this.speed = speed;
        animation = new Animator();
        currentActivity = Animator.MonsterActivity.IDLE_DOWN;
        // Adjust based on sprite size
        this.path = null;
        this.currentPathIndex = 0; // Start at the first node in the path
    }
    public void setPath(Array<GridNode> path) {
        this.path = path;
        this.currentPathIndex = 0; // Reset path to start from the beginning
    }

    // Getters and Setters
    public boolean isDead() {
        return isDead;
    }
    public void setDead(boolean dead) {
        isDead = dead;
    }

    public boolean isMarkedForRemoval() {
        return markedForRemoval;
    }

    private void markForRemoval() {
        markedForRemoval = true;
    }


    /**
     * Marks the monster as dead and initiates the death animation.
     */
    public void die() {
        if (!isDying) {
            setDead(true); // Mark the monster as dead
            isDying = true; // Start the death animation
            deathTimer = 0f; // Reset the death timer
            currentActivity = Animator.MonsterActivity.DEAD; // Set death animation
        }
    }


    /**
     * Updates the monster's state, moving along the path.
     *
     * @param delta Time elapsed since last frame (seconds).
     */
    @Override
    public void update(float delta) {
        super.update(delta); // Assuming Entity.update(delta) handles health reduction, etc.

        if (isDying) {
            deathTimer += delta;
            if (deathTimer >= DEATH_ANIMATION_DURATION) {
                isDying = false;
                markForRemoval();
            }
            return; // Skip other updates while dying
        }

        if (isDead) return; // Skip updates if dead and not yet removed

        // If there's no valid path, the monster should idle
        if (path == null || path.size == 0) {
            updateIdleAnimation(); // No path to follow
            return;
        }

        // Get the current target node from the path (only if valid)
        GridNode currentNode = null;
        if (currentPathIndex < path.size) currentNode =path.get(currentPathIndex);
        if (currentNode == null) {
            updateIdleAnimation(); // No valid node to move to
            return;
        }

        // Calculate movement vector towards the current node
        Vector2 direction = new Vector2(currentNode.X, currentNode.Y).sub(position);
        float distanceToTarget = direction.len();

        // If close enough to the current node, move to the next node
        if (distanceToTarget < 0.3f) {
            currentPathIndex++; // Move to the next node in the path
            if (currentPathIndex >= path.size) {
                updateIdleAnimation(); // If the monster reaches the end of the path, idle
                return;
            }
            currentNode = path.get(currentPathIndex); // Update target node
            direction = new Vector2(currentNode.X, currentNode.Y).sub(position); // Recalculate direction
        }

        direction.nor().scl(speed * delta); // Normalize and scale by speed and delta time

        // Simulate next position to check for collisions
        Vector2 newPosition = position.cpy().add(direction);
        position.add(direction);
        updateMovementAnimation(direction);
    }



    /**
     * Updates the monster's animation based on movement direction.
     *
     * @param direction The movement direction vector.
     */
    private void updateMovementAnimation(Vector2 direction) {
        if (direction.x > 0 && direction.y > 0) {
            currentActivity = Animator.MonsterActivity.UP_RIGHT;
        } else if (direction.x < 0 && direction.y > 0) {
            currentActivity = Animator.MonsterActivity.UP_LEFT;
        } else if (direction.x > 0 && direction.y < 0) {
            currentActivity = Animator.MonsterActivity.DOWN_RIGHT;
        } else if (direction.x < 0 && direction.y < 0) {
            currentActivity = Animator.MonsterActivity.DOWN_LEFT;
        } else if (direction.y > 0) {
            currentActivity = Animator.MonsterActivity.UP;
        } else if (direction.y < 0) {
            currentActivity = Animator.MonsterActivity.DOWN;
        } else if (direction.x > 0) {
            currentActivity = Animator.MonsterActivity.RIGHT;
        } else if (direction.x < 0) {
            currentActivity = Animator.MonsterActivity.LEFT;
        }
    }

    /**
     * Updates the monster's animation to idle based on its current activity.
     */
    public void updateIdleAnimation() {
        switch (currentActivity) {
            case UP:
                currentActivity = Animator.MonsterActivity.IDLE_UP;
                break;
            case DOWN:
                currentActivity = Animator.MonsterActivity.IDLE_DOWN;
                break;
            case LEFT:
                currentActivity = Animator.MonsterActivity.IDLE_LEFT;
                break;
            case RIGHT:
                currentActivity = Animator.MonsterActivity.IDLE_RIGHT;
                break;
            case UP_RIGHT:
                currentActivity = Animator.MonsterActivity.IDLE_UP_RIGHT;
                break;
            case UP_LEFT:
                currentActivity = Animator.MonsterActivity.IDLE_UP_LEFT;
                break;
            case DOWN_RIGHT:
                currentActivity = Animator.MonsterActivity.IDLE_DOWN_RIGHT;
                break;
            case DOWN_LEFT:
                currentActivity = Animator.MonsterActivity.IDLE_DOWN_LEFT;
                break;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        animation.render(batch, position.x, position.y, currentActivity);
        batch.end();
    }

    /**
     * Disposes of resources used by the monster.
     */
    @Override
    public void dispose() {
        super.dispose();
        animation.dispose();
    }
}
