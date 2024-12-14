package io.github.farmageddon.ultilites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// PHẢI SỬA CLASS NÀY
// kh sua
public class ItemList {
    public Texture OneGunSheet = new Texture(Gdx.files.internal("mutatedplants/onegun.png"));

    public static final Items Tomato = new Items(new Texture(Gdx.files.internal("foodicon/Tomato.png")),Items.ItemType.FOOD, Items.Item.TOMATO, 10,1);
    public static final  Items TomatoSeed = new Items(new Texture(Gdx.files.internal("foodicon/TomatoSeed.png")),Items.ItemType.SEED, Items.Item.TOMATO, 10,1);
    public static final Items Radish = new Items(new Texture(Gdx.files.internal("foodicon/radishitem.png")),Items.ItemType.FOOD, Items.Item.RADISH, 10,1);
    public static final Items RadishSeed = new Items(new Texture(Gdx.files.internal("foodicon/radishseed.png")),Items.ItemType.SEED, Items.Item.RADISH, 10,1);
    public static final Items Carrot  = new Items(new Texture(Gdx.files.internal("foodicon/Carrot.png")),Items.ItemType.FOOD, Items.Item.CARROT, 10,1);
    public static final Items CarrotSeed = new Items(new Texture(Gdx.files.internal("foodicon/CarrotSeed.png")),Items.ItemType.SEED, Items.Item.CARROT, 10,1) ;
    public static final Items Rice = new Items(new Texture(Gdx.files.internal("foodicon/Rice.png")),Items.ItemType.FOOD, Items.Item.RICE, 10,1);
    public static final Items RiceSeed = new Items(new Texture(Gdx.files.internal("foodicon/RiceSeed.png")),Items.ItemType.SEED, Items.Item.RICE, 10,1);
    public static final Items Fish = new Items( new Texture(Gdx.files.internal("foodicon/Fish.png")), Items.ItemType.FOOD, Items.Item.FISH,10,1);
    public static final Items Default = new Items(new Texture(Gdx.files.internal("default.png")),Items.ItemType.DEFAULT,Items.Item.DEFAULT, 0, 0);;
    public static final Items Bucket = new Items(new Texture(Gdx.files.internal("toolcutted/bucket.png")), Items.ItemType.TOOL, Items.Item.BUCKET, 10,1);
    public static final Items Sword = new Items(new Texture(Gdx.files.internal("toolcutted/Sword.png")),Items.ItemType.TOOL, Items.Item.SWORD, 0,1);
    public static final Items Hoe = new Items(new Texture(Gdx.files.internal("toolcutted/Hoe.png")),Items.ItemType.TOOL, Items.Item.HOE, 0,1);
    public static final Items Torch = new Items(new Texture(Gdx.files.internal("toolcutted/Torch.png")),Items.ItemType.TOOL, Items.Item.TORCH, 0,1) ;
    public static final Items FishingRod = new Items(new Texture(Gdx.files.internal("toolcutted/FishingRod.png")),Items.ItemType.TOOL, Items.Item.FISHINGROD, 0,1);
    public static final Items OneGunIcon = new Items(new Texture(Gdx.files.internal("mutatedplants/onegun_icon.png")),Items.ItemType.MUTATED_PLANT, Items.Item.ONE_GUN, 0,1);

    public static final Items SmallChicken = new Items(new Texture(Gdx.files.internal("foodicon/eggitem.png")), Items.ItemType.ANIMAL, Items.Item.EGG, 10, 0);
    public static final Items SmallCow = new Items(new Texture(Gdx.files.internal("animalicon/smallcow.png")), Items.ItemType.ANIMAL, Items.Item.COW, 10, 0);
    public static final Items SmallSheep = new Items(new Texture(Gdx.files.internal("sheepicon/smallsheep.png")), Items.ItemType.ANIMAL, Items.Item.SHEEP, 10, 0);
    public static final Items SmallPig = new Items(new Texture(Gdx.files.internal("pigiccon/smallpig.png")), Items.ItemType.ANIMAL, Items.Item.PIG, 10, 0);

    public static final Items Shovel = new Items(new Texture(Gdx.files.internal("toolcutted/shovel.png")), Items.ItemType.TOOL, Items.Item.SHOVEL, 0, 1);


}
