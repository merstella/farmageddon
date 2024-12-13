package io.github.farmageddon.ultilites;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import io.github.farmageddon.Crops.Land;
import io.github.farmageddon.animals.*;
import io.github.farmageddon.entities.Monster;
import io.github.farmageddon.entities.Player;
import io.github.farmageddon.screens.GameScreen;

public class SlotCursorHandler {
    private Player player;
    private String action;
    private Items.Item seed, food, mutatedPlant, animal;
    private Array<Monster> monsters;

    public SlotCursorHandler(Player player, Array<Monster> monsters) {
        this.player = player;
        this.monsters = monsters;
        this.seed = null;
        this.food = null;
        this.mutatedPlant = null;
        this.action = null;
        this.animal = null;
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

    public Items.Item getAnimal() {
        return animal;
    }
    public Animal getAnimal(int x, int y, Stage stage, GameScreen gameScreen) {
        switch (animal.toString().toLowerCase()) {
            case "egg":
                return new Chicken(x, y, stage, gameScreen);
            case "cow":
                return new Cow(x, y, stage, gameScreen);
            case "pig":
                return new Pig(x, y, stage, gameScreen);
            case "sheep":
                return new Sheep(x, y, stage, gameScreen);
            default:
                return null;
        }
    }
    public void update() {
        // Get the current item in the slot cursor
        Items.Item currentItem = player.eqipInventory.get(player.slotCursor).getItem();
//        System.out.println(currentItem);
        Items.ItemType currentItemType = player.eqipInventory.get(player.slotCursor).getType();

        // Reset all items and action before processing the current item type
        action = null;
        seed = null;
        food = null;
        mutatedPlant = null;
        animal = null;
        // Handle the current item type and update corresponding fields
        if (currentItem == Items.Item.TORCH) {
            // If the item is a torch, reset action and set item holding to "torch"
            Player.itemHolding = "torch";
        } else {
            // Handle specific item types
            Player.itemHolding = "none"; // Reset item holding for non-torch items

            if (currentItemType == Items.ItemType.SEED) {
                seed = currentItem;
            } else if (currentItemType == Items.ItemType.TOOL) {
                // Set the action based on the tool
                action = currentItem.toString().toLowerCase();
            } else if (currentItemType == Items.ItemType.FOOD) {
                food = currentItem;
            } else if (currentItemType == Items.ItemType.MUTATED_PLANT) {
                mutatedPlant = currentItem;
            } else if (currentItemType == Items.ItemType.ANIMAL) {
                animal = currentItem;
            }
        }
    }

    // Perform the action when the user initiates it
    public void startAction(Land land) {
        if (action == null) {
            return; // If no action is set, do nothing
        }

        switch (action) {
            case "hoe":
                // Perform hoe action on the land
                land.hoe();
                break;

            case "bucket":
                // Perform watering action
                land.water();
                break;

            case "sword":
                // Perform sword action and attack nearby monsters
                if (Player.timeSinceLastAttack >= Player.attackCooldown) {
                    Player.timeSinceLastAttack = 0f;
                }

                // Attack all nearby monsters within a certain distance (20f)
                for (Monster enemy : monsters) {
                    if (player.getPosition().dst(enemy.getPosition()) < 20f) {
                        enemy.takeDamage(player.getAttackDamage());
                    }
                }
                break;
        }
    }
}
