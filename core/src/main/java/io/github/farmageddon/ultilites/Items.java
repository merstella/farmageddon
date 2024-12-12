package io.github.farmageddon.ultilites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static io.github.farmageddon.ultilites.Items.ItemType.*;

public class Items {

    private TextureRegion textureRegion;
    private ItemType type;
    private Item item;
    private int num;
    private int cost;


    public ItemType getItemType() {
        return null;
    }


    public enum ItemType{
        NULL,
        DEFAULT,
        SEED,
        TOOL,
        FOOD,
        OTHER, MUTATED_PLANT,
    }

    public enum Item{
        DEFAULT,
        NULL,// default Item
        RICE,
        TOMATO,
        CARROT,
        CORN,
        BUCKET,
        HOE,SWORD,TORCH,FISHINGROD,
        EGG,
        FISH, COIN, RADISH,
        CHICKEN_MEAT, COW_MEAT, SHEEP_MEAT, ONE_GUN, PIG_MEAT
    }

    public Items(Texture texture, ItemType type, Item item, int cost, int num){
        this.type = type;
        this.item = item;
        this.textureRegion = new TextureRegion(texture);
        this.num = 0;
        this.cost = cost;
        this.num = num;

    }
    public Items(TextureRegion textureRegion, ItemType type, Item item, int cost){
        this.type = type;
        this.item = item;
        this.textureRegion = new TextureRegion(textureRegion);
        this.num = 0;
        this.cost = cost;
    }

    public ItemType getType() {
        return type;
    }
    public Item getItem() {
            return item;
    }
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    public int getCost() {
        return cost;
    }
    public void add(Items item){
        num++;
    }
    public void remove(Items item){
        num--;
    }

    public int getNum() {
        return num;
    }

}

