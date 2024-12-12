package io.github.farmageddon.ultilites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// PHẢI SỬA CLASS NÀY
public class ItemList {

    public static final Items Tomato = new Items(new Texture(Gdx.files.internal("foodicon/Tomato.png")),Items.ItemType.FOOD, Items.Item.TOMATO, 10,1);
    public static final  Items TomatoSeed = new Items(new Texture(Gdx.files.internal("foodicon/TomatoSeed.png")),Items.ItemType.SEED, Items.Item.TOMATO, 10,1);
    public static final Items Corn = new Items(new Texture(Gdx.files.internal("foodicon/Corn.png")),Items.ItemType.FOOD, Items.Item.CORN, 10,1);
    public static final Items CornSeed = new Items(new Texture(Gdx.files.internal("foodicon/CornSeed.png")),Items.ItemType.SEED, Items.Item.CORN, 10,1);
    public static final Items Carrot  = new Items(new Texture(Gdx.files.internal("foodicon/Carrot.png")),Items.ItemType.SEED, Items.Item.CARROT, 10,1);
    public static final Items CarrotSeed = new Items(new Texture(Gdx.files.internal("foodicon/CarrotSeed.png")),Items.ItemType.SEED, Items.Item.CARROT, 10,1) ;
    public static final Items Rice = new Items(new Texture(Gdx.files.internal("foodicon/Rice.png")),Items.ItemType.FOOD, Items.Item.RICE, 10,1);
    public static final Items RiceSeed = new Items(new Texture(Gdx.files.internal("foodicon/RiceSeed.png")),Items.ItemType.SEED, Items.Item.RICE, 10,1);
    public static final Items Fish = new Items( new Texture(Gdx.files.internal("foodicon/Meat.png")), Items.ItemType.FOOD, Items.Item.FISH,10,1);
    public static final Items Default = new Items(new Texture(Gdx.files.internal("default.png")),Items.ItemType.DEFAULT,Items.Item.DEFAULT, 0, 0);;
    public static final Items Bucket = new Items(new Texture(Gdx.files.internal("toolcutted/bucket.png")), Items.ItemType.TOOL, Items.Item.BUCKET, 10,1);
    public static final Items Sword = new Items(new Texture(Gdx.files.internal("toolcutted/Sword.png")),Items.ItemType.TOOL, Items.Item.SWORD, 0,1);
    public static final Items Hoe = new Items(new Texture(Gdx.files.internal("toolcutted/Hoe.png")),Items.ItemType.TOOL, Items.Item.HOE, 0,1);
    public static final Items Torch = new Items(new Texture(Gdx.files.internal("toolcutted/Torch.png")),Items.ItemType.TOOL, Items.Item.TORCH, 0,1) ;
    public static final Items FishingRod = new Items(new Texture(Gdx.files.internal("toolcutted/FishingRod.png")),Items.ItemType.TOOL, Items.Item.FISHINGROD, 0,1);

}
