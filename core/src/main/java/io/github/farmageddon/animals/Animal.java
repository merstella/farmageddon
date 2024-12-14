package io.github.farmageddon.animals;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import io.github.farmageddon.entities.Player;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.ultilites.*;

import java.util.Objects;

import static io.github.farmageddon.screens.GameScreen.*;

public abstract class Animal extends Actor {
    private Vector2 position;

    private float hunger;
    private boolean canProduce;
    private int state;
    private boolean isDead;
    private int age;
    private float currentHealth;
    private final float maxHealth = 100f;
    private Stage stage;

    private float speed = 100f; // Units per second

    protected Texture animalSheet; // The spritesheet
    protected Animation<TextureRegion>[] animations; // Array of animations for each activity
    protected float stateTime;
    protected final int frameWidth = 32;
    protected final int frameHeight = 32;
    protected float minX, maxX, minY, maxY;
    private float activityChangeTimer = 0;
    private float ACTIVITY_CHANGE_DELAY;
    DroppedItem droppedFood;
    public boolean canBeProduced() {
        return canProduce;
    }

    public void checkAndDropFoodIfDead() {
        if (isDead && state == 3) {
            droppedItems.add(droppedFood);
        }
    }

    public abstract void initAnimation();

    public abstract void setBound();

    public enum Activity {
        WALK_LEFT, WALK_RIGHT, SLEEP, IDLE_LEFT, IDLE_RIGHT, HIT_LEFT, HIT_RIGHT
    }

    private Activity currentActivity;
    private Vector2 destination;
    private boolean isMoving = false;

    private boolean canBreed = true;
    private float breedCooldown = 5f;
    private float breedCooldownTimer = 0;

    private float layCooldown = 0;
    private float layCooldownMAX = 10f;

    private HealthBar.HealthBarActor healthBar;
    public boolean isHighlighted;
    protected ZoneManager zoneManager;
    boolean typeCanLay;
    boolean typeCanBreed;

    private boolean isHitAnimationActive = false;
    private float hitAnimationDuration = 0.5f; // Duration of the hit animation
    private float hitAnimationTimer = 0;

    public boolean isDead() {
        return isDead;
    }
    protected Items.Item favFood;
    protected GameScreen gameScreen;
    public boolean getTypeCanBreed() {
        return typeCanBreed;
    }
    public Animal(float x, float y, Stage stage, GameScreen gameScreen) {
        this.state = 1;
        setBounds(x, y, frameWidth * 0.333f, frameHeight * 0.333f);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        this.age = 0;
        position = new Vector2(getX(), getY());
        this.hunger = 0;
        this.canProduce = false;
        this.gameScreen = gameScreen;
        this.isDead = false;
        this.currentHealth = 100;
        setHighlighted(false);
        initAnimation();
        this.currentActivity = Activity.IDLE_LEFT;
        randomizeActivityChangeDelay();
        healthBar = new HealthBar.HealthBarActor(this, maxHealth);
        healthBar.setShapeRenderer(shapeRenderer);
        this.setTouchable(Touchable.enabled);
        this.stage = stage;
        stage.addActor(healthBar);
        stage.addActor(this);
        zoneManager = new ZoneManager();
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
                Vector3 touchPosition = new Vector3(x, y, 0);
                camera.unproject(touchPosition);
                Vector2 touchPosition2D = new Vector2(touchPosition.x, touchPosition.y);
                if (button == Input.Buttons.LEFT) {
                    System.out.println(gameScreen.getSlotCursorHandler().getAction());
                    if (Animal.this instanceof Chicken) {
                        if (gameScreen.getSlotCursorHandler().getSeed() != null) {
                            System.out.println("Seed: " + gameScreen.getSlotCursorHandler().getSeed());
                            player.eqipInventory.get(player.slotCursor).remove(player.eqipInventory.get(player.slotCursor));
                            if (player.eqipInventory.get(player.slotCursor).getNum() == 0){
                                player.setEquipItem(ItemList.Default,player.slotCursor);
                            }
                            feed();
                        }
                    }
                    System.out.println("FavFood: " + favFood);
                    System.out.println("need: " + gameScreen.getSlotCursorHandler().getFood());
                    System.out.println("action: " + gameScreen.getSlotCursorHandler().getAction());
                    if (favFood != null && gameScreen.getSlotCursorHandler().getFood() == favFood) {
                        player.eqipInventory.get(player.slotCursor).remove(player.eqipInventory.get(player.slotCursor));
                        if (player.eqipInventory.get(player.slotCursor).getNum() == 0){
                            player.setEquipItem(ItemList.Default,player.slotCursor);
                        }
                        feed();
                    } else if (player.getPosition().dst(Animal.this.getPosition()) <= 20f && Objects.equals(gameScreen.getSlotCursorHandler().getAction(), "sword")) {
                        player.updateActivityAnimation(gameScreen.getSlotCursorHandler().getAction(), touchPosition2D);
                        takeDamage(Player.attackDamage);
                    }
                }
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                player.stopActivity();
            }
        });
    }
    // New method to handle taking damage
    public void takeDamage(float damage) {
        if (!isDead) {
            currentHealth -= damage;
            if (currentHealth <= 0) {
                currentHealth = 0;
                die(); // Trigger death if health reaches 0
            }
            playHitAnimation();
        }
    }

    // Trigger the hit animation depending on the direction
    private void playHitAnimation() {
        if (!isHitAnimationActive) { // Play hit animation only if it's not already active
            if (currentActivity == Activity.WALK_LEFT || currentActivity == Activity.IDLE_LEFT) {
                currentActivity = Activity.HIT_LEFT;
            } else if (currentActivity == Activity.WALK_RIGHT || currentActivity == Activity.IDLE_RIGHT) {
                currentActivity = Activity.HIT_RIGHT;
            }
            stateTime = 0; // Reset animation timer to play hit animation immediately
            isHitAnimationActive = true; // Activate hit animation flag
            hitAnimationTimer = 0; // Reset hit animation timer
        }
    }

    // Method to handle the death of the animal
    private void die() {
        isDead = true;
        healthBar.remove();
        this.remove();
        checkAndDropFoodIfDead();
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
        this.ACTIVITY_CHANGE_DELAY = 1f + (float) (Math.random() * 4f);
    }

    Animation<TextureRegion> createAnimation(TextureRegion[] frames, int startFrame, int frameCount, float frameDuration) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        System.arraycopy(frames, startFrame, directionFrames, 0, frameCount);
        return new Animation<>(frameDuration, directionFrames);
    }

    Animation<TextureRegion> createFlippedAnimation(TextureRegion[] frames, int startFrame, int frameCount, float frameDuration) {
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
        if (hunger > 80) {
            currentHealth -= delta * 2;
        }

        // Clamp current health between 0 and maxHealth
        currentHealth = Math.max(0, Math.min(maxHealth, currentHealth));
        if (currentHealth == 0) {
            die();
        }
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
            position.x + (float) (Math.random() * 2 * maxDistance - maxDistance),
            position.y + (float) (Math.random() * 2 * maxDistance - maxDistance)
        );

        // Clamp destination within bounds
        destination.x = Math.max(minX, Math.min(maxX, destination.x));
        destination.y = Math.max(minY, Math.min(maxY, destination.y));
    }

    public boolean isEligibleForBreeding(Animal other) {
//        System.out.println(this.canBreed +";"+ other.canBreed);
        return this.getClass() == other.getClass() // Same type
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
        Animal offspring = null;
        if (this instanceof Chicken) {
            offspring = new Chicken(offspringX, offspringY, stage, gameScreen);  // Creates a Chicken offspring
        } else if (this instanceof Pig) {
            offspring = new Pig(offspringX, offspringY, stage, gameScreen);  // Creates a Pig offspring
        } else if (this instanceof Cow) {
            offspring = new Cow(offspringX, offspringY, stage, gameScreen);  // Base case for Animal
        } else if (this instanceof Sheep) {
            offspring = new Sheep(offspringX, offspringY, stage, gameScreen);
        }
        offspring.setBound();
        return offspring;
    }


    public Vector2 getPosition() {
        return position;
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        update(delta);
        if (healthBar != null) healthBar.setHealth(currentHealth);
        if (typeCanBreed) {
            if (!canBreed) {
                breedCooldownTimer -= delta;
                if (breedCooldownTimer <= 0) {
                    canBreed = true; // Reset breeding ability
                }
            }
        }
        stateTime += delta;
        activityChangeTimer += delta;

        // Cycle through activities in a specific order
        if (activityChangeTimer >= ACTIVITY_CHANGE_DELAY && !isHitAnimationActive) {
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
        if (typeCanLay && canProduce) {
            DroppedItem drop = drop();
            if (drop != null) {
                droppedItems.add(drop);
            }
        }

        // Handle hit animation timeout
        if (isHitAnimationActive) {
            hitAnimationTimer += delta;
            if (hitAnimationTimer >= hitAnimationDuration) {
                isHitAnimationActive = false; // Reset hit animation state
                // After the hit animation, return to previous state (Idle or Walking)
                if (currentActivity == Activity.HIT_LEFT) {
                    currentActivity = Activity.IDLE_LEFT;
                } else if (currentActivity == Activity.HIT_RIGHT) {
                    currentActivity = Activity.IDLE_RIGHT;
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Ensure correct projection matrix for the batch
        batch.end();
        batch.setProjectionMatrix(getStage().getCamera().combined);
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

        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        batch.setColor(Color.WHITE);
        batch.end();


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

    public DroppedItem drop() {
        if (canProduce) {
            DroppedItem egg = new DroppedItem(position.x, position.y, Items.Item.EGG, Items.ItemType.FOOD);
            canProduce = false;
            System.out.println("Animal laid an egg at: " + position);
            return egg;
        }
        return null;
    }

}
