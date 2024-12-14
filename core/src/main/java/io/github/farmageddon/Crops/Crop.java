package io.github.farmageddon.Crops;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.ultilites.Items;
import io.github.farmageddon.ultilites.Timer_;

public class Crop extends Sprite {
    // max 4 stages
    private Timer_ timer;
    private int growthStage;
    private int growthStageDuration; // in hours
    private int hoursOld;

    private float centerX;
    private float centerY;
    private Vector2 position;
    private Texture texture;
    private Sprite frameSprite;
    private TextureRegion currentFrame;
    private TextureRegion[][] textureFrames;
    private Array<TextureRegion> cropFrames;
    Items.Item item;

    public Crop(Items.Item item, float x, float y, Timer_ timer) {
        this.timer = timer; // Initialize the timer
        this.position = new Vector2(x, y);
        this.growthStage = 0;
        this.hoursOld = 0;
        centerX = x - 8;
        centerY = y - 8;
        texture = new Texture("Crops/Crops.png");
        textureFrames = TextureRegion.split(texture, 16, 16);
        frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, 16, 16);
        frameSprite.setX(Math.round(centerX / 16) * 16);
        frameSprite.setY(Math.round(centerY / 16) * 16);
        loadinfo(item);
    }

    private void loadinfo(Items.Item type) {
        this.item = type;
        switch (type) {
            case RICE:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 2; i <= 5; i++)
                    cropFrames.insert(i - 2, textureFrames[0][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 6; // 6 hours per stage
                break;
            case TOMATO:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 2; i <= 5; i++)
                    cropFrames.insert(i - 2, textureFrames[2][i]);
                System.out.println(cropFrames.size);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 12; // 12 hours per stage
                break;
            case CARROT:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 2; i <= 5; i++)
                    cropFrames.insert(i - 2, textureFrames[4][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 12; // 12 hours per stage
                break;
            case RADISH:
                cropFrames = new Array<TextureRegion>(4);
                for (int i = 2; i <= 5; i++)
                    cropFrames.insert(i - 2, textureFrames[6][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 18; // 18 hours per stage
                break;
            default:
                break;
        }
    }

    public void update() {
        int elapsedHours = (int) timer.getElapsedInHours();
        if (elapsedHours > hoursOld) {
            addHours(elapsedHours - hoursOld);
        }
    }
    public void addHours(int hours) {
        this.hoursOld += hours;
        checkGrowth();
    }

    private void checkGrowth() {
        if (hoursOld / growthStageDuration > growthStage && growthStage < 3) {
            growthStage++;
            this.currentFrame = cropFrames.get(growthStage);
        }
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public Sprite getFrameSprite() {
        return frameSprite;
    }

    public int getGrowthStage() {
        return growthStage;
    }

    public Items.Item getItemType() {
        return item;
    }
}
