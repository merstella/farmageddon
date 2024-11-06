package io.github.farmageddon;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.ArrayList;
import java.util.List;

public class Market {
    private Stage stage;
    private Table table;
    private List<Item> items;
    private Skin skin;
    private boolean Visible;

    public Market(Skin skin) {
        this.skin = skin;
        stage = new Stage();
        table = new Table();
        // kich thuoc cua bang duoc dieu chinh phu hop voi kich thuoc cua man hinh
        table.setFillParent(true);
        items = new ArrayList<>();
        stage.addActor(table);
        populateMarket();
        Visible = false;
    }

    // dien du lieu vao cho
    private void populateMarket() {
        for (Item item : items) {
            addItemToMarket(item);
        }
    }

    private void addItemToMarket(Item item) {
        Label itemLabel = new Label(item.getName() + " - $" + item.getPrice(), skin);
        TextButton buyButton = new TextButton("Buy", skin);
        // chua code phan thay doi inventory va money
        buyButton.addListener(event -> {
            System.out.println("Bought: " + item.getName());
            return true;
        });

        TextButton sellButton = new TextButton("Sell", skin);
        sellButton.addListener(event -> {
            // chua code phan thay doi inventory va money
            System.out.println("Sold: " + item.getName());
            return true;
        });

        table.add(itemLabel);
        table.add(buyButton);
        table.add(sellButton);
        table.row();
    }
/*
    // kiem tra input M de bat tat market
    public void Input(){
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)){
            Visible = !Visible;
            if (Visible){
                Gdx.input.setInputProcessor(stage);
            }
            else {
                Gdx.input.setInputProcessor(null);
            }
        }
    }

    // ve market ra nhe' !!!
*/

    public Stage getStage() {
        return stage;
    }
}
