package io.github.farmageddon;

public class Item {
    private final String itemName ;
    private final String itemPrice;

    public Item(String itemName, String itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getName(){

        return itemName;
    }

    public String getPrice(){

        return itemPrice;
    }
}
