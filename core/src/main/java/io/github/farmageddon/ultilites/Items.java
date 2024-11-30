package io.github.farmageddon.ultilites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Items {

    private TextureRegion textureRegion;
    private ItemType type;
    private Item item;
    private int num;
    private int cost;
    public enum ItemType{
        SEED,
        TOOL
    }

    public enum Item{
        RICE,
        TOMATO,
        CARROT,
        CORN,
        BUCKET,
        HOE,
        ;


    }

    public Items(Texture texture, ItemType type, Item item, int cost){
        this.type = type;
        this.item = item;
        this.textureRegion = new TextureRegion(texture);
        this.num = 0;
        this.cost = cost;
    }
    public Items(TextureRegion textureRegion, ItemType type, Item item, int cost){
        this.type = type;
        this.item = item;
        this.textureRegion = new TextureRegion(textureRegion);
        this.cost = cost;
    }

    public ItemType getType() {
        return type;
    }

    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public Item getItem() {
        return item;
    }
    public int getCost() {
        return cost;
    }
    public void add(){
        num++;
    }
    public void remove(){
        num--;
    }

    public int getNum() {
        return num;
    }
}
//package io.github.farmageddon.markets;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//
//public class Items {
//    private String itemName;
//    private Texture texture;
//    public int Cost;
//
//    public Items(String itemName, Texture texture, int Cost) {
//        this.itemName = itemName;
//        this.texture = texture;
//        this.Cost = Cost;
//    }
//
//    public void render(SpriteBatch batch, float x, float y) {
//        batch.draw(texture, x, y);
//    }
//
//    public String getItemName() {
//        return itemName;
//    }
//
//    public int getCost() {
//        return Cost;
//    }
//
//    public Texture getTexture() {
//        return texture;
//    }
//
//    public void dispose() {
//        if (texture != null) {
//            texture.dispose();
//        }
//    }
//
//}
