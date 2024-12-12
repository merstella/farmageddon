package io.github.farmageddon.ultilites;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.Crops.Land;
import io.github.farmageddon.entities.Monster;
import io.github.farmageddon.entities.Player;

import java.util.Locale;

public class SlotCursorHandler {
    private Player player;
    private String action;
    private Items.Item seed, food, mutatedPlant;
    private Array<DroppedItem> droppedItems;
    private Array<Monster> monsters;
    public SlotCursorHandler(Player player, Array<DroppedItem> droppedItems, Array<Monster> monsters) {
        this.player = player;
        this.action = null;
        this.droppedItems = droppedItems;
        this.monsters = monsters;
        seed = null; food = null; mutatedPlant = null;
    }

    public Items.Item getFood() {
        return food;
    }
    public Items.Item getSeed() {
        return seed;
    }
    public String getAction() {
        return action;
    }
    public Items.Item getMutatedPlant() {
        return mutatedPlant;
    }

    public void update(){
        // Get the current item in the slot cursor
        Items.Item currentItem = player.eqipInventory.get(player.slotCursor).getItem();
        Items.ItemType currentItemType = player.eqipInventory.get(player.slotCursor).getType();

        if (currentItem == Items.Item.TORCH) {
            action = null; seed = null; food = null; mutatedPlant = null;
            Player.itemHolding = "torch";
            return;
        }
        Player.itemHolding = "none";
        if (currentItemType == Items.ItemType.SEED) {
            seed = currentItem;
            action = null; food = null; mutatedPlant = null;
            return;
        }
        if (currentItemType == Items.ItemType.TOOL){
            action = currentItem.toString().toLowerCase();
            seed = null; food = null; mutatedPlant = null;
            return;
        }
        if (currentItemType == Items.ItemType.FOOD) {
            food = currentItem;
            action = null; seed = null; mutatedPlant = null;
            return;
        }
        if (currentItemType == Items.ItemType.MUTATED_PLANT) {
            mutatedPlant = currentItem;
            action = null; seed = null; food = null;
            return;
        }

    }
    public void startAction(Land land) {
        switch (action) {
            case "hoe": land.hoe(); break;
            case "bucket": {
                land.water();
                if (!land.isEmpty()) {
                    land.getCrop().setWatered(true);
                }
                break;
            }
            case "sword": {
                if (Player.timeSinceLastAttack >= Player.attackCooldown) {
                    Player.timeSinceLastAttack = 0f;
                }

                for (Monster enemy : monsters) {
                    if (player.getPosition().dst(enemy.getPosition())<20f) {
                        enemy.takeDamage(player.getAttackDamage());
                    }
                }
                break;
            }
        }
    }
}
