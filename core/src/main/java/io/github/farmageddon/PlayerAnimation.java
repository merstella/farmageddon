package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.awt.*;

public class PlayerAnimation {

    private final Texture playerSheet;
    private final Texture playerActionSheet;
    private final Texture playerFishingSheet;
    private final Animation<TextureRegion>[] animations, actionAnimations;
    private float stateTime;
    private ShapeRenderer shapeRenderer;

    public enum Direction {
        UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT,
        IDLE_UP, IDLE_RIGHT, IDLE_DOWN, IDLE_LEFT, IDLE_DOWN_RIGHT, IDLE_DOWN_LEFT,
        IDLE_UP_RIGHT, WALK, SLEEP, IDLE, IDLE_UP_LEFT
    }

    public enum Activity {
        NONE, // Default state when no activity
        HOE_UP, HOE_DOWN, HOE_LEFT, HOE_RIGHT,
        WATER_UP, WATER_DOWN, WATER_LEFT, WATER_RIGHT, START_FISHING_RIGHT, START_FISHING_LEFT,
        WAIT_FISHING_RIGHT, WAIT_FISHING_LEFT , DONE_FISHING_RIGHT, DONE_FISHING_LEFT
    }

    public PlayerAnimation() {
        shapeRenderer = new ShapeRenderer();
        // Load textures and initialize animations as before
        playerSheet = new Texture(Gdx.files.internal("player.png"));
        playerActionSheet = new Texture(Gdx.files.internal("Player/Player_Actions.png"));
        playerFishingSheet = new Texture(Gdx.files.internal("Player\\Player_Fishing.png"));
        // Define the frame dimensions and split frames (same as original)
        int frameWidth = 32, frameHeight = 32;
        int frameFishingWidth = playerFishingSheet.getWidth() / 9; int frameFishingHeight = playerFishingSheet.getHeight() / 8;
        int frameActionWidth = 48, frameActionHeight = 48;
        TextureRegion[][] tmpFrames = TextureRegion.split(playerSheet, frameWidth, frameHeight);
        TextureRegion[][] tmpFrames2 = TextureRegion.split(playerActionSheet, frameActionWidth, frameActionHeight);
        TextureRegion[][] tmpFrames3 = TextureRegion.split(playerFishingSheet, frameFishingWidth, frameFishingHeight);

        animations = new Animation[19];
        actionAnimations = new Animation[Activity.values().length];

        // Create animations for each direction
        animations[Direction.UP.ordinal()] = createAnimation(tmpFrames[5], 0, 6); // Assuming 4 frames in the first row for UP
        animations[Direction.UP_RIGHT.ordinal()] = createAnimation(tmpFrames[4], 0, 6); // Adjust indices based on layout
        animations[Direction.RIGHT.ordinal()] = createAnimation(tmpFrames[4], 0, 6);
        animations[Direction.DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames[4], 0, 6);
        animations[Direction.DOWN.ordinal()] = createAnimation(tmpFrames[3], 0, 6);
        animations[Direction.DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 6);
        animations[Direction.LEFT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 6);
        animations[Direction.UP_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 6);

        animations[Direction.IDLE_UP.ordinal()] = createAnimation(tmpFrames[2], 0, 6);
        animations[Direction.IDLE_RIGHT.ordinal()] = createAnimation(tmpFrames[1], 0, 6);
        animations[Direction.IDLE_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[1], 0, 6);
        animations[Direction.IDLE_DOWN.ordinal()] = createFlippedAnimation(tmpFrames[0], 0, 6);
        animations[Direction.IDLE_DOWN_RIGHT.ordinal()] = createAnimation(tmpFrames[4], 0, 1);
        animations[Direction.IDLE_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[1], 0, 6);
        animations[Direction.IDLE_UP_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 1);
        animations[Direction.IDLE_UP_LEFT.ordinal()] = createAnimation(tmpFrames[4], 0, 1);

        actionAnimations[Activity.HOE_UP.ordinal()] = createAnimation(tmpFrames2[8], 0, 6);
        actionAnimations[Activity.HOE_DOWN.ordinal()] = createAnimation(tmpFrames2[7], 0, 6);
        actionAnimations[Activity.HOE_LEFT.ordinal()] = createFlippedAnimation(tmpFrames2[6], 0, 6);
        actionAnimations[Activity.HOE_RIGHT.ordinal()] = createAnimation(tmpFrames2[6], 0, 6);
        actionAnimations[Activity.WATER_UP.ordinal()] = createAnimation(tmpFrames2[10], 0, 6);
        actionAnimations[Activity.WATER_DOWN.ordinal()] = createAnimation(tmpFrames2[9], 0, 6);
        actionAnimations[Activity.WATER_LEFT.ordinal()] = createFlippedAnimation(tmpFrames2[11], 0, 6);
        actionAnimations[Activity.WATER_RIGHT.ordinal()] = createAnimation(tmpFrames2[11], 0, 6);

        actionAnimations[Activity.START_FISHING_RIGHT.ordinal()] = createAnimation(tmpFrames3[0], 0, 9);
        actionAnimations[Activity.START_FISHING_LEFT.ordinal()] = createFlippedAnimation(tmpFrames3[0], 0, 9);
        actionAnimations[Activity.WAIT_FISHING_RIGHT.ordinal()] = createAnimation(tmpFrames3[1], 0, 4);
        actionAnimations[Activity.WAIT_FISHING_LEFT.ordinal()] = createFlippedAnimation(tmpFrames3[1], 0, 4);
        actionAnimations[Activity.DONE_FISHING_RIGHT.ordinal()] = createAnimation(tmpFrames3[2], 0, 8);
        actionAnimations[Activity.DONE_FISHING_LEFT.ordinal()] = createFlippedAnimation(tmpFrames3[2], 0, 8);

        stateTime = 0f;
    }
    private Animation<TextureRegion> createAnimation(TextureRegion[] frames, int startFrame, int frameCount) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        System.arraycopy(frames, startFrame, directionFrames, 0, frameCount);
        return new Animation<>(0.1f, directionFrames);
    }

    private Animation<TextureRegion> createFlippedAnimation(TextureRegion[] frames, int startFrame, int frameCount) {
        TextureRegion[] directionFrames = new TextureRegion[frameCount];
        for (int i = 0; i < frameCount; i++) {
            directionFrames[i] = new TextureRegion(frames[startFrame + i]);
            directionFrames[i].flip(true, false); // Flip horizontally
        }
        return new Animation<>(0.1f, directionFrames);
    }

    // Remove or modify this method
    public void render(SpriteBatch batch, float x, float y, Direction direction) {
        // Update state time
        stateTime += Gdx.graphics.getDeltaTime();

        // Get the current frame of the animation for the specified direction
        TextureRegion currentFrame = animations[direction.ordinal()].getKeyFrame(stateTime, true);
        // Draw the current frame at the specified position
        batch.draw(currentFrame, x, y, Player.WIDTH , Player.HEIGHT);


    }
    public void renderActivity(SpriteBatch batch, float x, float y, Activity activity) {
        if (activity == Activity.NONE) return;

        stateTime += Gdx.graphics.getDeltaTime();
        Animation<TextureRegion> activityAnimation = actionAnimations[activity.ordinal()];

        if (activity == Activity.WAIT_FISHING_RIGHT) {
            // Change to true to loop the animation
            TextureRegion currentFrame = activityAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, x - 8, y - 8, 48, 48);
        }
         else {
            TextureRegion currentFrame = activityAnimation.getKeyFrame(stateTime, true);
            batch.draw(currentFrame, x - 8, y - 8, 48, 48);
        }
    }
    public void dispose() {
        playerSheet.dispose();
        playerActionSheet.dispose();
        playerFishingSheet.dispose();
    }
}
