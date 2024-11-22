package io.github.farmageddon.Crops;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Items {

    private TextureRegion textureRegion;
    private ItemType type;
    private Item item;
    private int num;
    public enum ItemType{
        SEED,
        TOOL
    }

    public enum Item{
        RICE,
        TOMATO,
        CARROT,
        CORN,
    }

    public Items(Texture texture, ItemType type, Item item){
        this.type = type;
        this.item = item;
        this.textureRegion = new TextureRegion(texture);
        this.num = 0;
    }
    public Items(TextureRegion textureRegion, ItemType type, Item item){
        this.type = type;
        this.item = item;
        this.textureRegion = new TextureRegion(textureRegion);
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
