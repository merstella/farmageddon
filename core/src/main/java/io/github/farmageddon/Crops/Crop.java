package io.github.farmageddon.Crops;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.ultilites.Items;

public class Crop extends Sprite {
    // max 4 stages
    private int growthStage;
    private int growthStageDuration;
    private int daysOld;
    private int price;
    private int daysNotWatered;
    private boolean isDead;
    private boolean isWatered;
    private float centerX;
    private float centerY;
    private Vector2 position;
    private Texture texture;
    private Sprite frameSprite;
    private TextureRegion currentFrame;
    private TextureRegion[][] textureFrames;
    private Array<TextureRegion> cropFrames;
    Items.Item item;
    public Crop(Items.Item item, float x, float y) {
        this.position = new Vector2(x, y);
        this.growthStage = 0;
        this.daysOld = 0;
        this.isWatered = false;
        this.daysNotWatered = 0;
        this.isDead = false;
        centerX = x - 8;
        centerY = y - 8;
        texture = new Texture("Crops/Crops.png");
        textureFrames = TextureRegion.split(texture, 16, 16);
        frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, 16, 16);
        frameSprite.setX(Math.round(centerX/16)*16);
        frameSprite.setY(Math.round(centerY/16)*16);
        loadinfo(item);
    }

    private void loadinfo(Items.Item type) {
        this.item = type;
        switch (type) {
            case RICE:
                cropFrames = new Array<TextureRegion>(4);
                for(int i = 2; i <= 5; i++)
                    cropFrames.insert(i-2, textureFrames[0][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 3;
                this.price = 10;
                break;
            case TOMATO:
                cropFrames = new Array<TextureRegion>(4);
                for(int i = 2; i <= 5; i++)
                    cropFrames.insert(i-2, textureFrames[3][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 3;
                this.price = 20;
                break;
            case CARROT:
                cropFrames = new Array<TextureRegion>(4);
                for(int i = 2; i <= 5; i++)
                    cropFrames.insert(i-2, textureFrames[5][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 2;
                this.price = 8;
                break;
            case CORN:
                cropFrames = new Array<TextureRegion>(4);
                for(int i = 2; i <= 5; i++)
                    cropFrames.insert(i-2, textureFrames[9][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 4;
                this.price = 30;
                break;
            default:
                break;
        }
    }

    public void addDay(){
        this.daysOld++;
        if(isWatered) {
            daysNotWatered = 0;
        }
        if(!isWatered)
            daysNotWatered++;
        if(daysNotWatered == 2)
            isDead = true;
        this.isWatered = false;
        checkGrowth();
    }

    private void checkGrowth() {
        if(daysOld%growthStageDuration == 0 && growthStage != 3) {
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

    public int getPrice() {
        return price;
    }

    public int getGrowthStage() {
        return growthStage;
    }

    public void setWatered(boolean watered) {
        isWatered = watered;
    }
    public boolean isDead() {
        return isDead;
    }

    public boolean isWatered() {
        return isWatered;
    }

    public Items.Item getItemType() {
        return item;
    }
}
