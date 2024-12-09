package io.github.farmageddon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.screens.MainMenuScreen;

public final class UI {
    public Stage stage;
    private Texture UI_sheet;
    private OptionScreen optionScreen;
    private GameScreen gameScreen;
    private Main game;
    // option
    private Button optionsButton;
    public Button upButton;
    public Button downButton;
    private TextureRegion UpbuttonTextureActive;
    private TextureRegion UpbuttonTextureInactive;
    private TextureRegion DownbuttonTextureActive;
    private TextureRegion DownbuttonTextureInactive;
    private TextureRegion optionsTextureActive;
    private TextureRegion optionsTextureInactive;
    public static boolean isOptionVisible = false;

    public UI() {
        this.game = new Main();
        this.gameScreen = new GameScreen(game);
        this.stage = new Stage(new ScreenViewport());
        this.UI_sheet = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\UI_Buttons.png"));
        UpbuttonTextureActive = new TextureRegion(UI_sheet,(UI_sheet.getWidth() / 39) * 35, (UI_sheet.getHeight() / 19),(UI_sheet.getWidth() / 39),(UI_sheet.getHeight() / 19));
        UpbuttonTextureInactive = new TextureRegion(UI_sheet,(UI_sheet.getWidth() / 39) * 34,(UI_sheet.getHeight() / 19),(UI_sheet.getWidth() / 39),(UI_sheet.getHeight() / 19));
        DownbuttonTextureActive = new TextureRegion(UI_sheet,(UI_sheet.getWidth() / 39) * 38,(UI_sheet.getHeight() / 19),(UI_sheet.getWidth() / 39),(UI_sheet.getHeight() / 19));
        DownbuttonTextureInactive = new TextureRegion(UI_sheet,(UI_sheet.getWidth() / 39) * 37,(UI_sheet.getHeight() / 19),(UI_sheet.getWidth() / 39),(UI_sheet.getHeight() / 19));
        upButton = createButton(UpbuttonTextureActive,UpbuttonTextureInactive,"volume louder");
        upButton.setPosition(Gdx.graphics.getWidth()/2 - 110,Gdx.graphics.getHeight()/2 + 10);
        upButton.setSize(upButton.getWidth() * 3, upButton.getHeight() * 3);

        downButton = createButton(DownbuttonTextureActive,DownbuttonTextureInactive,"volume quite");
        downButton.setPosition(Gdx.graphics.getWidth()/2 - 110,Gdx.graphics.getHeight()/2 + 80);
        downButton.setSize(downButton.getWidth() * 3, downButton.getHeight() * 3);
        // option
        optionsTextureActive = new TextureRegion(UI_sheet,(UI_sheet.getWidth() / 39) * 16,(UI_sheet.getHeight() / 19),(UI_sheet.getWidth() / 39),(UI_sheet.getHeight() / 19));
        optionsTextureInactive =new TextureRegion(UI_sheet,(UI_sheet.getWidth() / 39) * 15,(UI_sheet.getHeight() / 19),(UI_sheet.getWidth() / 39),(UI_sheet.getHeight() / 19));
        Button optionButton = createButton(optionsTextureActive,optionsTextureInactive, "options");
        // Đặt vị trí
        optionButton.setSize(optionsTextureActive.getRegionWidth() * 3, optionsTextureActive.getRegionHeight() * 3); // Đặt kích thước nút
        optionButton.setPosition(Gdx.graphics.getWidth()-(UI_sheet.getWidth() / 39) - 30, Gdx.graphics.getHeight()-(UI_sheet.getHeight() / 19) - 30);
        stage.addActor(optionButton);

        optionScreen = new OptionScreen();
    }

    public void create() {
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render() {
        Gdx.input.setInputProcessor(stage);
        float delta = Gdx.graphics.getDeltaTime();
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();

    }

    private Button createButton(TextureRegion active, TextureRegion inactive, String action) {
        // Tạo ButtonStyle
        Button.ButtonStyle buttonStyle = new Button.ButtonStyle();
        buttonStyle.up = new TextureRegionDrawable(inactive); // Trạng thái không được nhấn
        buttonStyle.down = new TextureRegionDrawable(active); // Trạng thái được nhấn
        Button button = new Button(buttonStyle);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("action");
                isOptionVisible = true;
            }
        });
        return button;
    }
}
