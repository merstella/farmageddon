package io.github.farmageddon.Crops;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;


public class Seeds {
    private Texture texture;
    private Rectangle boundingRect;
    private cropItems.Item item;
    private int price;


    public Seeds(String string, Rectangle rectangle){
        this.texture = new Texture("ref assets/seeds/" + string + "s.png");
        this.boundingRect = new Rectangle(rectangle.x, rectangle.y, texture.getWidth(), texture.getHeight());
        setSeedPrice(string);

    }

    public void setSeedPrice(String string){
<<<<<<< HEAD
        if(string.equals("riceseed")) {
            this.price = 4;
            this.item = cropItems.Item.RICE;
        }
        else if(string.equals("tomatoseed")) {
            this.price = 5;
            this.item = cropItems.Item.TOMATO;
=======
        if(string.equals("cornseed")) {
            this.price = 5;
            this.item = Items.Item.CORN;
>>>>>>> e7669bef912f70f05928e3235847bf7da3adc572
        }
        else if(string.equals("carrotseed")) {
            this.price = 2;
            this.item = cropItems.Item.CARROT;
        }
<<<<<<< HEAD
        else if(string.equals("cornseed")) {
            this.price = 8;
            this.item = cropItems.Item.CORN;
=======
        else if(string.equals("tomatoseed")) {
            this.price = 4;
            this.item = Items.Item.TOMATO;
        }
        else if(string.equals("riceseed")) {
            this.price = 5;
            this.item = Items.Item.RICE;
>>>>>>> e7669bef912f70f05928e3235847bf7da3adc572
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

    public cropItems.Item getItem() {
        return item;
    }
}
