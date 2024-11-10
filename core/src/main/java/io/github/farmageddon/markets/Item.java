package io.github.farmageddon.markets;

class Item {
    private final String itemName ;
    private final int itemPrice;

    public Item(String itemName, int itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getName(){

        return itemName;
    }

    public int getPrice(){

        return itemPrice;
    }
}
