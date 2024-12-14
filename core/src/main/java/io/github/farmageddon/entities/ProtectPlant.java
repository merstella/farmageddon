package io.github.farmageddon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.CollisionHandling;
import io.github.farmageddon.ultilites.Items;
import java.util.ArrayList;
import java.util.Objects;


public class ProtectPlant extends Entity{
    private float cooldown, fromLastShoot, range;
    private float timeMul;
    private boolean isShooting;
    private int typePlant;
    private float additionState;
    private float opacity;
    private boolean isPlanted;
    private final Animator animation;
    private Animator.PlantActivity currentActivity;
    private int plantType;
    private Vector2 targetPosition;

    public void setTargetPosition(Vector2 targetPosition) {
        this.targetPosition = targetPosition;
    }

    private boolean facingRight; // Track the current facing direction
    private boolean swapping; // Track if currently swapping
    private float swapDuration; // Duration of the swap animation
    private float swapTimer; // Timer to track swap progress

    public void setTypePlant (int typePlant) {this.typePlant = typePlant;}
    public void setAdditionState (float additionState) {this.additionState = additionState;}
    public int getTypePlant () {return typePlant;}
    public float getAdditionState () {return additionState;}

    public float getRange () {
        return range;
    }
    public float getFromLastShoot () {
        return fromLastShoot;
    }
    public boolean getIsShooting () {return isShooting && isPlanted;}

    public void shooted () {this.isShooting = false;}

    public float getCooldown () {
        return cooldown;
    }

    public void setTimeMul (float timeMul) {this.timeMul = timeMul;}
    public void setFromLastShoot (float fromLastShoot) {this.fromLastShoot = fromLastShoot;}
    public Animator.PlantActivity getCurrentActivity(){
        return currentActivity;
    }
    private float attackStateTimer;
    @Override
    public void setBeingAttacked(boolean beingAttacked) {
        super.setBeingAttacked(beingAttacked);
        if (beingAttacked) {
            attackStateTimer = 0.4f;
        }
    }
    @Override
    public void takeDamage(float damage) {
        super.takeDamage(damage);
        setBeingAttacked(true);
    }
    public ProtectPlant (int plantType, float x, float y, float maxHealth) {
        super(x, y, 0f, true, maxHealth);
        cooldown = 1f;
        facingRight = true;
        swapping = false;
        swapDuration = 0.5f; // Adjust swap animation duration
        swapTimer = 0;
        range = 100f;
        fromLastShoot = 0f;
        isShooting = false;
        timeMul = 1f;
        this.plantType = plantType;
        typePlant = 0;
        opacity = 0f;
        isPlanted = false;
        animation = new Animator();
        currentActivity = Animator.PlantActivity.IDLE_RIGHT;
    }

    public boolean isPlanted() {
        return isPlanted;
    }

    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }
    public void setPlanted(boolean isPlanted) {
        this.isPlanted = isPlanted;
    }

    public void update(float delta) {
        System.out.println(targetPosition);

        // Update timers
        fromLastShoot += delta * timeMul;

        // Check if the plant is ready to shoot based on cooldown
        if (fromLastShoot >= cooldown) {
            // Determine if animation is at frame 5
            int currentFrameIndex = animation.getCurrentFrameIndex(currentActivity, GameScreen.stateTime);
            if (currentFrameIndex == 5) { // Assuming 5 is the frame index for the bullet release
                isShooting = true;
                fromLastShoot = 0; // Reset the cooldown
            }
        }

        // Handle swap logic
        if (swapping) {
            swapTimer += delta;
            if (swapTimer >= swapDuration) {
                // Swap animation completed
                swapping = false;
                currentActivity = facingRight ? Animator.PlantActivity.SHOOT_RIGHT : Animator.PlantActivity.SHOOT_LEFT;
            }
        }

        // Update animation based on target position
        if (!swapping) {
            updateAnimationBasedOnTarget(targetPosition);
        }

        // Reset attack state if needed
        if (beingAttacked) {
            attackStateTimer -= delta;
            if (attackStateTimer <= 0) {
                beingAttacked = false;
            }
        }
    }


    private void updateAnimationBasedOnTarget(Vector2 targetPosition) {
        if (targetPosition == null || (targetPosition.x == -1 && targetPosition.y == -1)) {
            currentActivity = facingRight ? Animator.PlantActivity.IDLE_RIGHT : Animator.PlantActivity.IDLE_LEFT;
            return;
        }

        boolean targetIsRight = targetPosition.x > position.x;

        if (targetIsRight != facingRight) {
            // Target is on the opposite side, start swapping
            facingRight = targetIsRight;
            swapping = true;
            swapTimer = 0;
            currentActivity = facingRight ? Animator.PlantActivity.SWAP_RIGHT : Animator.PlantActivity.SWAP_LEFT;
        } else {
            // Target is on the current facing side, start shooting
            currentActivity = facingRight ? Animator.PlantActivity.SHOOT_RIGHT : Animator.PlantActivity.SHOOT_LEFT;
        }
    }


    @Override
    public void render (SpriteBatch batch) {
        super.render(batch);
        batch.begin();
        batch.setColor(1f, 1f, 1f, opacity);
//        if (isBeingAttacked()) {
//            animation.render(batch, position.x, position.y, Animator.MonsterActivity.HIT_DOWN, GameScreen.stateTime);
//        } else {
            animation.render(batch, plantType, position.x, position.y, currentActivity, GameScreen.stateTime);
//        }
        batch.setColor(1f, 1f, 1f, 1f);
        batch.end();
    }


    @Override
    public void dispose()  {
        super.dispose();
        animation.dispose();
    }

    public void incrementOpacity(float delta) {
        this.opacity += delta;  // Increment opacity by delta (gradually fade in)
        this.opacity = Math.min(this.opacity, 1f);  // Ensure opacity doesn't exceed 1
    }

}
