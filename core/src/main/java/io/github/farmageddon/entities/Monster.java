package io.github.farmageddon.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.CollisionHandling;
import io.github.farmageddon.ultilites.GridNode;

import java.util.Optional;

public class Monster extends Entity {
    private final Animator animation;
    private Animator.MonsterActivity currentActivity;

    private boolean isDead;
    private boolean isDying;
    private float deathTimer;
    private boolean markedForRemoval;
    // Movement and Path
    private float speed; // Pixels per second

    private int typeTarget;
    private ProtectPlant targetPlant;
    private Player targetPlayer;
    private Entity targetEntity;
    private float range = 0;
    private float maxTimeForPlayer = 5f;
    private float timeSinceTargetPlayer;
    private float damagePoint;

    public Monster(float x, float y, float speed, int maxHealth) {
        super(x, y, speed, true, maxHealth); // Assuming Entity constructor: (x, y, speed, isActive, maxHealth)9
        this.speed = speed;
        animation = new Animator();
        typeTarget = -1;
        maxTimeForPlayer = 5f;
        damagePoint = 10;
        timeSinceTargetPlayer = 0f;
        currentActivity = Animator.MonsterActivity.IDLE_DOWN;
        // Adjust based on sprite size
        this.path = null;
        this.range = 20;
        this.currentPathIndex = 0; // Start at the first node in the path
        this.monsterBounds = new Rectangle(x + 7, y + 9, 14, 16);
    }
    public ProtectPlant getTargetPlant() {
        return targetPlant;
    }
    public int getTypeTarget () {return typeTarget;}
    public void setTypeTarget (int typeTarget) {this.typeTarget = typeTarget;}
    public float getRange () {return this.range;}
    public void setRange (float range) {this.range = range;}
    public boolean isNowTargetPlayer () {
        return false;
//        return timeSinceTargetPlayer <= maxTimeForPlayer;
    }
    public void setTimeSinceTargetPlayer (float timeSinceTargetPlayer) {this.timeSinceTargetPlayer = timeSinceTargetPlayer;}

    public void setTargetPlant(ProtectPlant targetPlant) {
        this.typeTarget = 0;
        this.targetPlant = targetPlant;
    }
    public void setTargetEntity(Entity entity) {
        this.typeTarget = 1;
        this.targetEntity = entity;
    }
    public void setTargetPlayer(Player player) {
        this.typeTarget = 2;
        this.targetPlayer = player;
    }
    public Entity getTargetEntity () {return this.targetEntity;}
    public float getTargetHealth () {
        switch (typeTarget) {
            case 0:
                return targetPlant.getHealth();
            case 1:
                return targetEntity.getHealth();
            case 2:
                return targetPlayer.getHealth();
        }
        return 0;
    }
    public Player getTargetPlayer () {return targetPlayer;}



    public boolean isDifferentTarget (ProtectPlant targetPlant) {
        if(targetPlant != this.targetPlant) return true;
        return false;
    }
    public boolean isDifferentTarget (Entity targetEntity) {
        if(targetEntity != this.targetEntity) return true;
        return false;
    }
    public boolean isDifferentTarget (Player player){
        if(player != this.targetPlayer)return false;
        return true;
    }

    public Vector2 getTargetPosition () {
        switch (typeTarget) {
            case -1:
                return null;
            case 0:
                return targetPlant.getPosition();
            case 1:
                return targetEntity.getPosition();
            case 2:
                return targetPlayer.getPosition();
        }
        System.out.println("No target position\n\n\n");
        return null;
    }
    // Constants
    private static final float DEATH_ANIMATION_DURATION = 0.5f; // Seconds
    private Array<GridNode> path; // The path to follow
    private int currentPathIndex = 0; // Index of the current node in the path
    public Array<GridNode> getPath() {
        return path;  // Get the current path
    }
    private Rectangle monsterBounds;



    public Rectangle getMonsterBounds() {
        return monsterBounds;
    }
    public void setPath(Array<GridNode> path) {
        this.path = path;
        this.currentPathIndex = 1; // Reset path to start from the beginning
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

    public boolean isNearEnough () {
        if(position.dst(getTargetPosition()) <= Math.sqrt(200)) return true;
        return false;
    }

    public void applyDamageToTarget () {
        switch (typeTarget) {
            case -1:
                break;
            case 0:
                targetPlant.takeDamage(damagePoint);
                break;
            case 1:

                break;
            case 2:
                targetPlayer.takeDamage(damagePoint);
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
        timeSinceTargetPlayer += delta;

        if (isDying) {
            deathTimer += delta;
            if (deathTimer >= DEATH_ANIMATION_DURATION) {
                isDying = false;
                markForRemoval();
            }
            return; // Skip other updates while dying
        }
        if (isNearEnough()) {
            applyDamageToTarget();
            return;
        }
//        if(typeTarget == -1 || typeTarget == 2)return;
//        System.out.println(typeTarget);
//        System.out.print(getTargetPosition().x);
//        System.out.print(' ');
//        System.out.println(getTargetPosition().y);
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
        animation.render(batch, position.x, position.y, currentActivity, GameScreen.stateTime);
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
