package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Animal extends Actor {
    private Vector2 position;

    private String type;
    private float hunger;
    private boolean canProduce;
    private int state;
    private boolean isDead;

    private float currentHealth;
    private final float maxHealth = 100f;
    private Stage stage;

    private float speed = 100f; // Units per second

    private Texture animalSheet; // The spritesheet
    private Animation<TextureRegion>[] animations; // Array of animations for each activity
    private float stateTime;
    private final int frameWidth = 32;
    private final int frameHeight = 32;
    private float minX, maxX, minY, maxY;
    private float activityChangeTimer = 0;
    private final float ACTIVITY_CHANGE_DELAY = 3f;

    public enum Activity {
        WALK_LEFT, WALK_RIGHT, SLEEP, IDLE
    }

    private Activity currentActivity;
    private Vector2 destination;
    private boolean isMoving = false;

    public Animal(float x, float y, String type, Stage stage) {
        position = new Vector2(x, y);
        setPosition(x, y);

        this.type = type;
        this.hunger = 0;
        this.canProduce = false;
        this.state = 1;
        this.isDead = false;
        this.currentHealth = 100;

        initAnimation();
        this.currentActivity = Activity.IDLE;

        stage.addActor(this);
    }

    private void initAnimation() {
        animalSheet = new Texture(Gdx.files.internal("Animals\\Chicken\\Chicken_01.png"));
        TextureRegion[][] tmpFrames = TextureRegion.split(animalSheet, frameWidth, frameHeight);
        animations = new Animation[4];

        // Walk Left animation (original frames)
        animations[Activity.WALK_LEFT.ordinal()] = createAnimation(tmpFrames[1], 0, 6, 0.1f);

        // Walk Right animation (flipped frames)
        animations[Activity.WALK_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[1], 0, 6);

        animations[Activity.SLEEP.ordinal()] = createAnimation(tmpFrames[3], 0, 3, 0.1f);
        animations[Activity.IDLE.ordinal()] = createAnimation(tmpFrames[0], 0, 2, 0.3f);
        stateTime = 0f;
    }

    private Animation<TextureRegion> createAnimation(TextureRegion[] frames, int startFrame, int frameCount, float frameDuration) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        System.arraycopy(frames, startFrame, directionFrames, 0, frameCount);
        return new Animation<>(frameDuration, directionFrames);
    }

    private Animation<TextureRegion> createFlippedAnimation(TextureRegion[] frames, int startFrame, int frameCount) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            directionFrames[i] = new TextureRegion(frames[startFrame + i]);
            directionFrames[i].flip(true, false); // Flip horizontally
        }
        return new Animation<>(0.1f, directionFrames);
    }
    public void update(float delta, int currentDays) {
        // Existing hunger and health logic
        hunger += delta * 5;
        if (hunger > 100) hunger = 100;
        if (currentHealth == 0) isDead = true;
        if (hunger > 80) {
            currentHealth -= delta * 2;
        }
        if (currentHealth >= 80 && state == 3) {
            canProduce = true;
        } else canProduce = false;


    }

    private void moveTowardsDestination(float delta) {
        // If no destination, choose a new one
        if (destination == null || position.dst(destination) < 5) {
            chooseNewDestination();
        }

        // Move towards destination
        float moveSpeed = 10f;
        Vector2 direction = destination.cpy().sub(position).nor();
        position.add(direction.scl(moveSpeed * delta));

        // Update activity based on movement direction
        if (destination.x > position.x) {
            currentActivity = Activity.WALK_RIGHT;
        } else if (destination.x < position.x) {
            currentActivity = Activity.WALK_LEFT;
        }

        // Boundary checks
        if (position.x < minX) position.x = minX;
        if (position.x > maxX) position.x = maxX;
        if (position.y < minY) position.y = minY;
        if (position.y > maxY) position.y = maxY;

        setPosition(position.x, position.y);
    }

    private void chooseNewDestination() {
        // Choose a random destination within bounds
        float rangeX = maxX - minX;
        float rangeY = maxY - minY;

        // Random destination near current position
        float maxDistance = Math.min(rangeX, rangeY) * 0.3f; // 30% of range

        destination = new Vector2(
            position.x + (float)(Math.random() * 2 * maxDistance - maxDistance),
            position.y + (float)(Math.random() * 2 * maxDistance - maxDistance)
        );

        // Clamp destination within bounds
        destination.x = Math.max(minX, Math.min(maxX, destination.x));
        destination.y = Math.max(minY, Math.min(maxY, destination.y));
    }


    @Override
    public void act(float delta) {
        super.act(delta);
        stateTime += delta;
        activityChangeTimer += delta;

        // Cycle through activities in a specific order
        if (activityChangeTimer >= ACTIVITY_CHANGE_DELAY) {
            switch (currentActivity) {
                case IDLE:
                    chooseNewDestination();
                    if (destination.x < position.x) {
                        currentActivity = Activity.WALK_LEFT;
                    } else {
                        currentActivity = Activity.WALK_RIGHT;
                    }
                    break;
                case WALK_LEFT:
                case WALK_RIGHT:
                    currentActivity = Activity.IDLE;
                    break;
            }

            // Reset timers and state time
            activityChangeTimer = 0;
            stateTime = 0;
        }

        // Movement logic for walk states
        if (currentActivity == Activity.WALK_LEFT || currentActivity == Activity.WALK_RIGHT) {
            moveTowardsDestination(delta);
        }
    }
    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Get current frame and check if animation is finished
        Animation<TextureRegion> currentAnimation = animations[currentActivity.ordinal()];
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        // If the current animation is finished, randomly choose a new activity
        if (currentAnimation.isAnimationFinished(stateTime)) {
            // Only change if enough time has passed
            if (activityChangeTimer >= ACTIVITY_CHANGE_DELAY) {
                Activity[] activities = Activity.values();
                currentActivity = activities[(int)(Math.random() * activities.length)];
                stateTime = 0; // Reset state time for new animation
                activityChangeTimer = 0; // Reset timer
            }
        }
        float scale = (0.333f * state);
        float width = frameWidth * scale;
        float height = frameHeight * scale;
        float offsetX = (width - frameWidth) / 2;
        float offsetY = (height - frameHeight) / 2;

        batch.draw(currentFrame, getX() - offsetX, getY() - offsetY, width, height);
    }


    private TextureRegion getCurrentFrame() {
        return animations[currentActivity.ordinal()].getKeyFrame(stateTime, true);
    }


    public void incState() {
        state += 1;
        if (state > 3) state = 3;
    }

    public void feed() {
        hunger = 0;
        currentHealth += 10;
        if (currentHealth > 100) currentHealth = 100;
    }

    public float getHunger() {
        return hunger;
    }

    public float getHealth() {
        return currentHealth;
    }

    public int getState() {
        return state;
    }

    public boolean canBeProduced() {
        return canProduce;
    }

    public String getType() {
        return type;
    }

    public void produce() {
        System.out.println("Has produced!");
    }

    public void setBound(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
}
