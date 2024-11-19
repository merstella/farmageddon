package io.github.farmageddon.markets;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int Money;
    private List<Item> invenItems;

    private Stage stage;
    private Window invenWindow;
    private Table invenTable;
    private boolean invenVisible = true;
    private Skin skin;

    public Inventory(int myMoney, Stage stage, Skin skin) {
        this.Money = myMoney;
        this.invenItems = new ArrayList<Item>();
        this.skin = skin;
        //this.invenItems = new ArrayList<>();


        invenWindow = new Window("Inventory", skin);
        invenWindow.setMovable(false);
        invenWindow.setResizable(false);
        invenWindow.setPosition(400, 500);
        invenWindow.setSize(500, 500);
        invenWindow.setVisible(false); // Đặt inventory ban đầu ở trạng thái ẩn
        stage.addActor(invenWindow);
    }
    // các thành phần của Inven
    public int getMoney() {
        return Money;
    }
    public void addMoney(int amount) {
        Money += amount;
    }
    public void subMoney(int amount){
        Money -= amount;
    }

    public void addItem(Item item){
        invenItems.add(item);
    }
    public void removeItem(Item item){
        invenItems.remove(item);
    }
    public List<Item> getItems(){
        return invenItems;
    }
    public void toggleInventory() {
        invenWindow.setVisible(!invenWindow.isVisible());
    }

    // vẽ Inven
    public void createInventoryWindow() {

        // tạo table bên trong invenWindow
        for (Item item : invenItems) {
            Image image = new Image(item.getImage().getDrawable());
            Label name = new Label(item.getName(), skin);
            Label quantity = new Label("x" + item.getQuantity(), skin);


            invenTable.add(image).size(20, 20).pad(10);
            invenTable.add(name).pad(10);
            invenTable.add(quantity).pad(10);
            invenTable.row();
        }
        invenWindow.add(invenTable).expandX().fillX().row();
    }

//    public void invenHandleInput(){
//        if (Gdx.input.isKeyJustPressed(Input.Keys.B)){
//            invenVisible = !invenVisible;
//            invenWindow.setVisible(invenVisible);
//            if (invenVisible){
//                Gdx.app.log("MyTag", "Da chuyen sang inven Screen!");
//                Gdx.input.setInputProcessor(stage);
//            }
//            else {
//                Gdx.input.setInputProcessor(null);
//            }
//        }
//    }
}
