package io.github.farmageddon.Crops;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;


public class Crop extends Sprite {


    private int growthStage;
    private int growthStageDuration;
    private int daysOld;
    private int price; // gia tien cua crop
    private int daysNotWatered;
    private boolean isDead;
    private boolean isWatered;

    private float centerX;
    private float centerY;

    private Vector2 position;

    private Texture texture;
    private Sprite frameSprite;
    private TextureRegion currentFrame; // counting variable
    private TextureRegion[][] textureFrames;
    private Array<TextureRegion> cropFrames;
    private TextureRegion dirtFrame;

    final int CROP_HEIGHT = 32;
    final int CROP_WIDTH = 16;
    public Crop(Items.Item item, float x, float y) {
        this.position = new Vector2(x, y);
        this.growthStage = 0;
        this.daysOld = 0;
        this.isWatered = false;
        this.daysNotWatered = 0;
        this.isDead = false;
        centerX = x - (float) CROP_WIDTH /2;
        centerY = y - (float) CROP_HEIGHT /2;
        texture = new Texture(Gdx.files.internal("Crops.png"));
        textureFrames = TextureRegion.split(texture, CROP_WIDTH, CROP_HEIGHT);
        frameSprite = new Sprite(textureFrames[0][0].getTexture(), 0, 0, CROP_WIDTH, CROP_HEIGHT);
        frameSprite.setX(Math.round(centerX/CROP_WIDTH)*CROP_WIDTH);
        frameSprite.setY(Math.round(centerY/CROP_HEIGHT)*CROP_HEIGHT);
        // dirtFrame = textureFrames[5][2];
        loadinfo(item);
    }

    private void loadinfo(Items.Item type) {
        switch (type) {
            case RICE:
                cropFrames = new Array<>(4);
                for(int i = 2; i <= 5; i++)
                    cropFrames.insert(i-2, textureFrames[0][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 2;
                this.price = 10;
                break;
            case TOMATO:
                cropFrames = new Array<>(4);
                for(int i = 2; i <= 5; i++)
                    cropFrames.insert(i-2, textureFrames[1][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 2;
                this.price = 20;
                break;
            case CARROT:
                cropFrames = new Array<>(4);
                for(int i = 2; i <= 5; i++)
                    cropFrames.insert(i-2, textureFrames[2][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 2;
                this.price = 30;
                break;
            case CORN:
                cropFrames = new Array<>(4);
                for(int i = 2; i <= 5; i++)
                    cropFrames.insert(i-2, textureFrames[4][i]);
                currentFrame = cropFrames.get(0);
                this.growthStageDuration = 2;
                this.price = 40;
                break;
            default:
                break;

        }
    }

    public void addDay(){
        if(isWatered) {
            daysNotWatered = 0;
            this.daysOld++;
        }
        if(!isWatered && growthStage != 3)
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
}
