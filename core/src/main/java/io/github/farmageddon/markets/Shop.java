package io.github.farmageddon.markets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

import java.util.ArrayList;

public class Shop {
    private Item item;
    private Table shopTable;
    private ArrayList<Item> shopItems;
    private Window shopWindow;
    private Inventory inventory;
    private Button sellButton;
    private Skin skin;
    private Stage stage;
    private boolean Visible;

    public Shop(Stage stage, Skin skin, Inventory inventory) {
        this.inventory = inventory;
        this.shopItems = new ArrayList<>();
        this.skin = new Skin();

        shopWindow = new Window("Shop", skin);
        shopWindow.setMovable(false);
        shopWindow.setPosition(800,800);
        shopWindow.setSize(500,500);
        shopWindow.setResizable(false);
        shopWindow.setVisible(false);// ban đầu ở trạng thái ẩn
        stage.addActor(shopWindow);
    }

    //các thành phần của shop



    public void createShopWindow(){
        //Tạo table bên trong window
        for(Item item : shopItems){
            Image image = new Image(item.getImage().getDrawable());
            Label name = new Label(item.getName(), skin);
            Label quantity = new Label("x" + item.getQuantity(), skin);
            Label price = new Label("$" + item.getPrice(), skin);
            TextButton buyButton = new TextButton("Buy", skin);

            // khi bấm nút buy
            buyButton.addListener(event -> {
                if (inventory.getMoney() >= item.getPrice()){
                    inventory.addItem(new Item(item.getName(), item.getPrice(), item.getImage(),item.getQuantity())); // Thêm item vào Inventory
                    inventory.subMoney(item.getPrice());
                    return true;
                } else{
                    System.out.println("You don't have enough money to buy this item");
                }
                return false;
            });


            shopTable.add(image).size(20, 20).pad(10);
            shopTable.add(name).pad(10);
            shopTable.add(quantity).pad(10);
            shopTable.add(price).pad(10);
            shopTable.row();
        }
        shopWindow.add(shopTable).expandX().fillX().row();

    }


    public void handleInput(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            Visible = !Visible;
            shopWindow.setVisible(Visible);
            if (Visible){
                Gdx.app.log("MyTag", "Da chuyen sang market Screen!");
                Gdx.input.setInputProcessor(stage);
            }
            else {
                Gdx.input.setInputProcessor(null);
            }
        }
    }

    public void toggleShop() {
        shopWindow.setVisible(!shopWindow.isVisible());
    }
}
