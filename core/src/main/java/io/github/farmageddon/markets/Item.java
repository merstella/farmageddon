package io.github.farmageddon.markets;


import com.badlogic.gdx.scenes.scene2d.ui.Image;

class Item {
    private final String itemName ;
    private final int itemPrice;
    private Image itemImage;
    private int itemQuantity;


    public Item(String itemName, int itemPrice, int itemQuantity, Image itemImage) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.itemImage = itemImage;
    }

    public String getName(){
        return itemName;
    }
    public int getPrice(){
        return itemPrice;
    }
    public int getQuantity(){
        return itemQuantity;
    }
    public Image getImage(){
        return itemImage;
    }

    public void setQuantity(int quantity){
        itemQuantity = quantity;
    }
    public void setImage(Image image){
        itemImage = image;
    }
    public void setItemName(String itemName){
        itemName = itemName;
    }
    public void setItemPrice(int itemPrice){
        itemPrice = itemPrice;
    }

}
