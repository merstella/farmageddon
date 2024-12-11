package io.github.farmageddon.ultilites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

// PHẢI SỬA CLASS NÀY
public class ItemList {

    // Static items - khởi tạo các item tĩnh dùng trong game
    public static final Items RICE = new Items(
        new Texture("rice.png"),
        Items.ItemType.SEED,
        Items.Item.RICE,
        10,
        1
    );

    public static final Items HOE = new Items(
        new Texture("hoe.png"),
        Items.ItemType.TOOL,
        Items.Item.HOE,
        50,
        1
    );

    public static final Items SWORD = new Items(
        new Texture("sword.png"),
        Items.ItemType.TOOL,
        Items.Item.SWORD,
        100,
        1
    );

    public static final Items TORCH = new Items(
        new Texture("torch.png"),
        Items.ItemType.OTHER,
        Items.Item.TORCH,
        5,
        3
    );

    public static final Items FISH = new Items(
        new Texture("fish.png"),
        Items.ItemType.FOOD,
        Items.Item.FISH,
        15,
        2
    );

    // Phương thức để truy cập hoặc quản lý các item nếu cần
    public static Items getItemByName(String itemName) {
        switch (itemName.toUpperCase()) {
            case "RICE":
                return RICE;
            case "HOE":
                return HOE;
            case "SWORD":
                return SWORD;
            case "TORCH":
                return TORCH;
            case "FISH":
                return FISH;
            default:
                return null; // Không tìm thấy
        }
    }
}
