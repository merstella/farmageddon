package io.github.farmageddon;

import io.github.farmageddon.ultilites.Items;

import java.util.ArrayList;

public class Market extends Entity {
    public ArrayList<Items> marketItems;

    public Market(float x, float y, float speed) {
        super(x, y, speed,false, 100);
        this.marketItems = new ArrayList<>();
    }

    public void addMarketItem(Items item) {
        marketItems.add(item);
    }

    public void removeMarketItem(Items item) {
        marketItems.remove(item);
    }
}
