package io.github.farmageddon.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.DroppedItem;
import io.github.farmageddon.ultilites.HealthBarActor;
import io.github.farmageddon.ultilites.Items;

import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.actor;
import static io.github.farmageddon.screens.GameScreen.droppedItems;
import static io.github.farmageddon.screens.GameScreen.shapeRenderer;

public class Animal extends Actor {
    private Vector2 position;

    private String type;
    private float hunger;
    private boolean canProduce;
    private int state;
    private boolean isDead;
    private int age;
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
    private float ACTIVITY_CHANGE_DELAY;

    public boolean canBeProduced() {
        return canProduce;
    }

    public enum Activity {
        WALK_LEFT, WALK_RIGHT, SLEEP, IDLE_LEFT, IDLE_RIGHT
    }

    private Activity currentActivity;
    private Vector2 destination;
    private boolean isMoving = false;

    private boolean canBreed = true;
    private float breedCooldown = 5f;
    private float breedCooldownTimer = 0;

    private float layCooldown = 0;
    private float layCooldownMAX = 10f;

    private HealthBarActor healthBar;
    public boolean isHighlighted;



    public Animal(float x, float y, String type, Stage stage) {

        this.state = 1;
        setBounds(x, y, frameWidth * 0.333f, frameHeight * 0.333f);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);
        this.age = 0;
        position = new Vector2(getX(), getY());
        this.type = type;
        this.hunger = 0;
        this.canProduce = false;

        this.isDead = false;
        this.currentHealth = 100;
        setHighlighted(false);
        initAnimation();
        this.currentActivity = Activity.IDLE_LEFT;
        randomizeActivityChangeDelay();
        healthBar = new HealthBarActor(this, maxHealth);
        healthBar.setShapeRenderer(shapeRenderer);
        this.setTouchable(Touchable.enabled);
        this.stage = stage;
        stage.addActor(healthBar);
        stage.addActor(this);

        this.addListener(new InputListener() {
            @Override
            public boolean mouseMoved(InputEvent event, float x, float y) {
                setHighlighted(true);
                return false;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {


                    setHighlighted(false);

            }
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Actor touched at: " + x + ", " + y);
                feed();
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Touch released on actor.");
            }
        });
    }
    public void setHighlighted(boolean highlighted) {
        isHighlighted = highlighted;
    }
    public void incrementAge() {
        this.age++;
    }

    public int getAge() {
        return this.age;
    }

    private void randomizeActivityChangeDelay() {
        // Random delay between 1 and 5 seconds
        this.ACTIVITY_CHANGE_DELAY = 1f + (float)(Math.random() * 4f);
    }
    private void initAnimation() {
        animalSheet = new Texture(Gdx.files.internal("Animals\\Chicken\\Chicken_01.png"));
        TextureRegion[][] tmpFrames = TextureRegion.split(animalSheet, frameWidth, frameHeight);
        animations = new Animation[10];

        // Walk Left animation (original frames)
        animations[Activity.WALK_LEFT.ordinal()] = createAnimation(tmpFrames[1], 0, 6, 0.1f);

        // Walk Right animation (flipped frames)
        animations[Activity.WALK_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[1], 0, 6, 0.1f);

        animations[Activity.SLEEP.ordinal()] = createAnimation(tmpFrames[3], 0, 3, 0.1f);
        animations[Activity.IDLE_LEFT.ordinal()] = createAnimation(tmpFrames[0], 0, 2, 0.3f);
        animations[Activity.IDLE_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[0], 0, 2, 0.3f);

        stateTime = 0f;
    }

    private Animation<TextureRegion> createAnimation(TextureRegion[] frames, int startFrame, int frameCount, float frameDuration) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        System.arraycopy(frames, startFrame, directionFrames, 0, frameCount);
        return new Animation<>(frameDuration, directionFrames);
    }

    private Animation<TextureRegion> createFlippedAnimation(TextureRegion[] frames, int startFrame, int frameCount, float frameDuration) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            directionFrames[i] = new TextureRegion(frames[startFrame + i]);
            directionFrames[i].flip(true, false); // Flip horizontally
        }
        return new Animation<>(frameDuration, directionFrames);
    }
    public void update(float delta) {
        // Existing cooldown and production logic
        layCooldown += delta;
        if (layCooldown > layCooldownMAX) {
            layCooldown = 0;
            if (currentHealth >= 80 && state == 3) {
                canProduce = true;
            }
        } else {
            canProduce = false;
        }

        // Hunger and health logic
        hunger += delta * 5;
        if (hunger > 100) hunger = 100;
        if (currentHealth == 0) isDead = true;
        if (hunger > 80) {
            currentHealth -= delta * 2;
        }

        // Clamp current health between 0 and maxHealth
        currentHealth = Math.max(0, Math.min(maxHealth, currentHealth));
    }

    private void moveTowardsDestination(float delta) {
        // If no destination, choose a new one
        if (destination == null || position.dst(destination) < 5) {
            chooseNewDestination();
            return;
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

    public boolean isEligibleForBreeding(Animal other) {
//        System.out.println(this.canBreed +";"+ other.canBreed);
        return this.type.equals(other.type) // Same type
            && this.currentHealth > 80 && this.hunger < 20 // Health and hunger conditions
            && this.canBreed && other.canBreed && this.state == 3 && other.state == 3;// Both animals can breed
    }
    public Animal breed(Animal other, Stage stage) {
        if (!isEligibleForBreeding(other)) {
            return null; // Breeding not possible
        }

        // Set both animals on cooldown
        this.canBreed = false;
        other.canBreed = false;

        // Reset cooldown timers
        this.breedCooldownTimer = breedCooldown;
        other.breedCooldownTimer = breedCooldown;

        // Calculate offspring position near the parents
        float offspringX = (this.getX() + other.getX()) / 2 + (float) (Math.random() * 20 - 10);
        float offspringY = (this.getY() + other.getY()) / 2 + (float) (Math.random() * 20 - 10);

        // Create offspring with inherited traits
        Animal offspring = new Animal(offspringX, offspringY, this.type, stage);
        offspring.setBound(
            Math.min(this.getMinX(), other.getMinX()),
            Math.max(this.getMaxX(), other.getMaxX()),
            Math.min(this.getMinY(), other.getMinY()),
            Math.max(this.getMaxY(), other.getMaxY())
        );
        return offspring;
    }


    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
        healthBar.setHealth(currentHealth);
        if (!canBreed) {
            breedCooldownTimer -= delta;
            if (breedCooldownTimer <= 0) {
                canBreed = true; // Reset breeding ability
            }
        }

        stateTime += delta;
        activityChangeTimer += delta;

        // Cycle through activities in a specific order
        if (activityChangeTimer >= ACTIVITY_CHANGE_DELAY) {
            switch (currentActivity) {
                case IDLE_LEFT:
                    chooseNewDestination();
                    if (destination.x < position.x) {
                        currentActivity = Activity.WALK_LEFT;
                    } else {
                        currentActivity = Activity.WALK_RIGHT;
                    }
                    break;
                case IDLE_RIGHT:
                    chooseNewDestination();
                    if (destination.x < position.x) {
                        currentActivity = Activity.WALK_LEFT;
                    } else {
                        currentActivity = Activity.WALK_RIGHT;
                    }
                    break;
                case WALK_LEFT:
                    currentActivity = Activity.IDLE_LEFT;
                    break;
                case WALK_RIGHT:
                    currentActivity = Activity.IDLE_RIGHT;
                    break;
            }

            // Reset timers and state time
            activityChangeTimer = 0;
            stateTime = 0;
            randomizeActivityChangeDelay();

        }

        // Movement logic for walk states
        if (currentActivity == Activity.WALK_LEFT || currentActivity == Activity.WALK_RIGHT) {
            moveTowardsDestination(delta);
        }
        // Egg-laying logic
        if (canProduce) {
            DroppedItem egg = layEgg();
            if (egg != null) {
                // Add the egg to a global list or stage
                droppedItems.add(egg);
            }
        }


    }
    @Override
    public void draw(Batch batch, float parentAlpha) {

        // Ensure correct projection matrix for the batch
        batch.setProjectionMatrix(getStage().getCamera().combined);
        batch.end();
        // First, draw the actor's texture
        Animation<TextureRegion> currentAnimation = animations[currentActivity.ordinal()];
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        // If the current animation is finished, randomly choose a new activity
        if (currentAnimation.isAnimationFinished(stateTime)) {
            if (activityChangeTimer >= ACTIVITY_CHANGE_DELAY) {
                Activity[] activities = Activity.values();
                currentActivity = activities[(int) (Math.random() * activities.length)];
                stateTime = 0; // Reset state time for new animation
                activityChangeTimer = 0; // Reset timer
            }
        }

        // Set the batch color for highlighting
        if (isHighlighted) {
            batch.setColor(Color.YELLOW); // Highlight color
        } else {
            batch.setColor(Color.WHITE); // Normal color
        }

//        float scale = (0.333f * state);
//        float width = frameWidth * scale;
//        float height = frameHeight * scale;
//        float offsetX =  // Center the texture within the bounds
//        float offsetY = (height - frameHeight) / 2f;
        batch.begin();
        // Draw the actor's current frame with tight bounds
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        batch.setColor(Color.WHITE); // Reset to default
        batch.end();

        // Now draw the actor bounds for debugging using ShapeRenderer
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());  // Use batch's projection matrix
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line); // Use ShapeType.Line for the bounds
        shapeRenderer.setColor(Color.RED);  // Set the color to red for bounds
        shapeRenderer.rect(getX(), getY(), getWidth(), getHeight());
        shapeRenderer.end();

        batch.begin();
    }


    public void incState() {
        state += 1;
        if (state > 3) state = 3;
        float scale = (0.333f * state);
        float width = frameWidth * scale;
        float height = frameHeight * scale;
        setBounds(getX(), getY(), width, height);
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


    public String getType() {
        return type;
    }

    public void produce() {
        System.out.println("Has produced!");
    }
    public DroppedItem layEgg() {
        if (canProduce) {
            // Create a DroppedItem at the animal's position
            DroppedItem egg = new DroppedItem(position.x, position.y, Items.Item.EGG, Items.ItemType.FOOD);
            canProduce = false; // Reset production capability
            System.out.println("Animal laid an egg at: " + position);
            return egg;
        }
        return null; // No egg produced
    }

    public void setBound(float minX, float maxX, float minY, float maxY) {
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }
    public float getMinX() { return minX; }
    public float getMaxX() { return maxX; }
    public float getMinY() { return minY; }
    public float getMaxY() { return maxY; }
}
