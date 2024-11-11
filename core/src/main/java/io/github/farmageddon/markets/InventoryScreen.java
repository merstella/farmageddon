package io.github.farmageddon.markets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.farmageddon.Main;

public class InventoryScreen implements Screen {
    private final Main game;
    private Stage stage;
    private Skin skin;
    private Inventory inventory;
    private Shop shop;
    private boolean invenVisible = true;

    public InventoryScreen(Main game) {
        this.game = game;
        // Khởi tạo Stage và Skin
        stage = new Stage(new ScreenViewport());
        skin = new Skin(Gdx.files.internal("uiskin.json"));

        // Khởi tạo Inventory
        inventory = new Inventory(100,stage, skin);
        shop = new Shop(stage, skin, inventory);


        // Đặt stage là input processor để nhận các sự kiện
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        // Hiển thị Inventory khi vào màn hình này
        inventory.toggleInventory();
        shop.toggleShop();
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.B)) {
            inventory.toggleInventory();  // Gọi hàm toggleInventory() để bật/tắt invenWindow
            invenVisible = !invenVisible;
        }
        // Xóa màn hình với màu nền đen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Gọi hàm render của Inventory để cập nhật nội dung
        inventory.createInventoryWindow();
        shop.createShopWindow();

        // Vẽ stage (bao gồm cả Inventory window)
        if (invenVisible == true) {
            // Vẽ `invenWindow` nếu nó đang hiển thị
//            inventory.createInventoryWindow();
//            shop.createShopWindow();
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        // Thay đổi kích thước của stage nếu cần
        //stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        // Ẩn Inventory khi thoát khỏi màn hình này
        //inventory.toggleInventory();
        //shop.toggleShop();
    }

    @Override
    public void dispose() {
        // Giải phóng tài nguyên
        stage.dispose();
        skin.dispose();
    }
}
