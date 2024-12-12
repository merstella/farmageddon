package io.github.farmageddon.Crops;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.farmageddon.ultilites.Items;


public class Seeds {
    private Texture texture;
    private Rectangle boundingRect;
    private Items.Item item;
    private int price;


    public Seeds(String string, Rectangle rectangle){
        this.texture = new Texture("foodicons/" + string + "s.png");
        setSeedPrice(string);

    }

    public void setSeedPrice(String string){
        if(string.equals("riceseed")) {
            this.price = 4;
            this.item = Items.Item.RICE;
        }
        else if(string.equals("cornseed")) {
            this.price = 5;
            this.item = Items.Item.CORN;
        }
        else if(string.equals("carrotseed")) {
            this.price = 2;
            this.item = Items.Item.CARROT;
        }
        else if(string.equals("tomatoseed")) {
            this.price = 4;
            this.item = Items.Item.TOMATO;
        }
    }

    public int getPrice() {
        return price;
    }

    public Texture getTexture() {
        return texture;
    }

    public Rectangle getBoundingRect() {
        return boundingRect;
    }

    public Items.Item getItem() {
        return item;
    }
}
