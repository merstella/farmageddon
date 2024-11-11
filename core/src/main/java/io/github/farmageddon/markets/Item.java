package io.github.farmageddon.markets;

import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Item {
    private String itemName ;
    private int itemPrice;
    private Image itemImage;
    private int itemQuantity;

    public Item(String itemName, int itemPrice, Image itemImage, int itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemImage = itemImage;
        this.itemQuantity = itemQuantity;
    }

    public String getName(){

        return itemName;
    }

    public int getPrice(){

        return itemPrice;
    }

    public Image getImage(){
        return itemImage;
    }

    public int getQuantity(){
        return itemQuantity;
    }

    //setter
    public void setName(String itemName) {
        this.itemName = itemName;
    }
    public void setPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
    public void setImage(Image itemImage) {
        this.itemImage = itemImage;
    }
}
