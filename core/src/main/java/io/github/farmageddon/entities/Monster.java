package io.github.farmageddon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
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

    private boolean isExist;

    private boolean isDead;
    private boolean isDying;

    public boolean isDying() {
        return isDying;
    }

    private float deathTimer;
    private boolean markedForRemoval;
    // Movement and Path
    private float speed; // Pixels per second
//    private boolean

    private int typeTarget;
    private ProtectPlant targetPlant;
    private Player targetPlayer;
    private Entity targetEntity;
    private float range = 10, attackRange;
    private float rangeForPlayer;
    private float maxTimeForPlayer = 5f;
    private float timeSinceTargetPlayer;
    private float damagePoint;
    private float cooldown, timePassed;
    private int typeMonster;
    private boolean isAttacking;
    public static Music zombiesSound;

    public Monster(float x, float y, float speed, int maxHealth) {
        super(x, y, speed, true, maxHealth); // Assuming Entity constructor: (x, y, speed, isActive, maxHealth)9
        animation = new Animator();
        typeMonster = 0;
        typeTarget = -1;
        maxTimeForPlayer = 5f;
        cooldown = 2;
        damagePoint = 1;
        timeSinceTargetPlayer = 0f;
        currentActivity = Animator.MonsterActivity.IDLE_DOWN;
        isExist = false;
        // Adjust based on sprite size
        this.path = null;
        this.range = 1;
        rangeForPlayer = 10;
        isAttacking = false;
        this.attackRange = 500;
        this.currentPathIndex = 0; // Start at the first node in the path
        this.monsterBounds = new Rectangle(x + 7, y + 9, 14, 16);
        this.isDead = false;
        this.isDying = false;
        this.deathTimer = 0;
        this.markedForRemoval = false;
        zombiesSound = Gdx.audio.newMusic(Gdx.files.internal("Sound\\zombie.mp3"));
        zombiesSound.setLooping(false);
        zombiesSound.setVolume(1f);
    }

    public void setExist (boolean isExist) {this.isExist = isExist;}
    public boolean isExist () {return this.isExist;}

    public float getDamagePoint () {return this.damagePoint;}
    public void setDamagePoint (float damagePoint) {this.damagePoint = damagePoint;}
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
    public void setAnimationByType (int monsterType) {
//        animation.setMonsterAnimationsByType(monsterType);
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
    public boolean isNullTarget () {
        switch (typeTarget) {
            case -1:
                return true;
            case 0:
                if(targetPlant == null || !targetPlant.isPlanted()) return true;
            case 1:
                if(targetEntity == null) return true;
            case 2:
                break;
        }
//        if(position.dst(getTargetPosition()) > attackRange) return true;
        return false;
    }

    public boolean isTargetInAttackRange () {
        return position.dst(getTargetPosition()) < attackRange;
    }

    public void setTypeMonster (int typeMonster) {this.typeMonster = typeMonster;}
    public int getTypeMonster () {return this.typeMonster;}

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
    private int currentPathIndex;// Index of the current node in the path
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

    private float attackStateTimer;
    @Override
    public void setBeingAttacked(boolean beingAttacked) {
        super.setBeingAttacked(beingAttacked);
        if (beingAttacked) {
            attackStateTimer = 0.4f;
        }
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
    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);
        setBeingAttacked(true);
    }

    private Animator.MonsterActivity getHitAnimation(Animator.MonsterActivity currentActivity) {
        String act = currentActivity.toString();
        if (act.contains("UP")) {
            if (act.contains("LEFT")) {
                return Animator.MonsterActivity.HIT_UP_LEFT;
            }
            if (act.contains("RIGHT")){
                return Animator.MonsterActivity.HIT_UP_RIGHT;
            }
            return Animator.MonsterActivity.HIT_UP;
        } else if (act.contains("DOWN")) {
            if (act.contains("LEFT")) {
                return Animator.MonsterActivity.HIT_DOWN_LEFT;
            }
            if (act.contains("RIGHT")){
                return Animator.MonsterActivity.HIT_DOWN_RIGHT;
            }
            return Animator.MonsterActivity.HIT_DOWN;
        } else if (act.contains("LEFT")) {
            return Animator.MonsterActivity.HIT_LEFT;
        } else if (act.contains("RIGHT")) {
            return Animator.MonsterActivity.HIT_RIGHT;
        }
        return currentActivity;
    }

    public boolean isNearEnough () {
        if(getTypeTarget() == -1)return false;
        if(position.dst(getTargetPosition()) <= this.attackRange) return true;
        return false;
    }

    public void setAttackRange (float attackRange) {this.attackRange = attackRange;}

    public void applyDamageToTarget () {
        switch (typeMonster) {
            case 1:
                break;
            case 0:
                switch (typeTarget) {
                    case -1:
                        break;
                    case 0:
                        targetPlant.takeDamage(damagePoint);
                        break;
                    case 1:
                        targetEntity.takeDamage(damagePoint);
                        break;
                    case 2:
                        targetPlayer.takeDamage(damagePoint);
                }
                isAttacking = false;
                break;
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
        timePassed += delta;
        timeSinceTargetPlayer += delta;
        if (beingAttacked) {
            attackStateTimer -= delta;
            if (attackStateTimer <= 0) {
                beingAttacked = false;
            }
        }
        if (isDying) {
            deathTimer += delta;
            zombiesSound.play();
            if (deathTimer >= DEATH_ANIMATION_DURATION) {
                isDying = false;
                markForRemoval();
            }
            return; // Skip other updates while dying
        }
        boolean dontMove = isNearEnough();
        if (isNearEnough() && timePassed >= cooldown) {
//            timePassed = 0;
            currentActivity = getAttackAnimation(currentActivity);
            isAttacking = true;
            timePassed = 0;
//            applyDamageToTarget();
            dontMove = true;
            System.out.println("STOPPPPPPP");

            Player.slashMusic.stop();
            Player.slashMusic.play();
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
//        System.out.println("Speed : " + (speed));

        direction.nor().scl(getSpeed() * delta); // Normalize and scale by speed and delta time


        // Simulate next position to check for collisions
        Vector2 newPosition = position.cpy().add(direction);
        if(dontMove){
            return;
        }
//        direction.set(0, 0);
        updateMovementAnimation(direction);
//        System.out.println("Move a direction " + (direction.x + " " + direction.y));
        position.add(direction);
    }

    private Animator.MonsterActivity getAttackAnimation(Animator.MonsterActivity currentActivity) {
        switch (currentActivity) {
            case UP:
                return Animator.MonsterActivity.ATTACK_UP;
            case DOWN:
                return Animator.MonsterActivity.ATTACK_DOWN;
            case LEFT:
                return Animator.MonsterActivity.ATTACK_LEFT;
            case RIGHT:
                return Animator.MonsterActivity.ATTACK_RIGHT;
            case UP_RIGHT:
                return Animator.MonsterActivity.ATTACK_UP_RIGHT;
            case UP_LEFT:
                return Animator.MonsterActivity.ATTACK_UP_LEFT;
            case DOWN_RIGHT:
                return Animator.MonsterActivity.ATTACK_DOWN_RIGHT;
            case DOWN_LEFT:
                return Animator.MonsterActivity.ATTACK_DOWN_LEFT;
            default:
                return currentActivity;
        }
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
        super.render(batch);
        batch.begin();
        if (isBeingAttacked()) {
//            System.out.println(currentActivity);
            animation.render(batch, position.x, position.y, getHitAnimation(currentActivity), GameScreen.stateTime);
        } else {
            animation.render(batch, position.x, position.y, currentActivity, GameScreen.stateTime);
        }
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
    public void setAttacking (boolean isAttacking) {
//        if(!isAttacking)timePassed = 0;
        this.isAttacking = isAttacking;}
    public boolean isAttacking () {return this.isAttacking;}
}
