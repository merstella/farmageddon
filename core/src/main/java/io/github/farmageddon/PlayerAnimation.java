package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerAnimation {

    private final Texture playerSheet; // The spritesheet
    private final Animation<TextureRegion>[] animations; // Array of animations for each direction
    private float stateTime;

    // Enum for directions
    public enum Direction {
        UP, UP_RIGHT, RIGHT, DOWN_RIGHT, DOWN, DOWN_LEFT, LEFT, UP_LEFT,
        IDLE_UP, IDLE_RIGHT, IDLE_DOWN, IDLE_LEFT, IDLE_DOWN_RIGHT, IDLE_DOWN_LEFT,
        IDLE_UP_RIGHT, IDLE_UP_LEFT
    }

    public PlayerAnimation() {
        // Load the spritesheet
        playerSheet = new Texture(Gdx.files.internal("player.png"));

        // Define the frame dimensions (assuming each frame is 64x64 pixels)
        int frameWidth = 32;
        int frameHeight = 32;

        // Split the spritesheet into frames
        TextureRegion[][] tmpFrames = TextureRegion.split(playerSheet, frameWidth, frameHeight);

        // Initialize the animations array
        animations = new Animation[16];

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
        animations[Direction.IDLE_DOWN_LEFT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 1);
        animations[Direction.IDLE_UP_RIGHT.ordinal()] = createFlippedAnimation(tmpFrames[4], 0, 1);
        animations[Direction.IDLE_UP_LEFT.ordinal()] = createAnimation(tmpFrames[4], 0, 1);
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
    public void render(SpriteBatch batch, float x, float y, Direction direction) {
        // Update state time
        stateTime += Gdx.graphics.getDeltaTime();

        // Get the current frame of the animation for the specified direction
        TextureRegion currentFrame = animations[direction.ordinal()].getKeyFrame(stateTime, true);

        // Draw the current frame at the specified position
        batch.draw(currentFrame, x, y, Player.WIDTH , Player.HEIGHT);
    }

    public void dispose() {
        playerSheet.dispose();
    }
}
