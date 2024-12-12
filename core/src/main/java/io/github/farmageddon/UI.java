package io.github.farmageddon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.farmageddon.entities.Player;
import io.github.farmageddon.screens.GameScreen;
import io.github.farmageddon.screens.MainMenuScreen;
import io.github.farmageddon.ultilites.HealthBar;
import io.github.farmageddon.ultilites.Timer_;

public final class UI {
    private Player player;
    public Stage stage;
    public SpriteBatch batch;
    private Texture UI_sheet;
    private OptionScreen optionScreen;
    // option
    public Button upButton;
    public Button downButton;
    private TextureRegion UpbuttonTextureActive;
    private TextureRegion UpbuttonTextureInactive;
    private TextureRegion DownbuttonTextureActive;
    private TextureRegion DownbuttonTextureInactive;
    private TextureRegion optionsTextureActive;
    private TextureRegion optionsTextureInactive;
    public static boolean isOptionVisible = false;
    // avatar
    private Texture avatar;
    private Texture healthSheet;
    private TextureRegion[] healthBar;
    private Texture dayBar;
    private BitmapFont font;
    private int[][] checkRanges;

    public UI() {
        this.batch = new SpriteBatch();
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
        Gdx.input.setInputProcessor(stage);
        optionScreen = new OptionScreen();

        this.avatar = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\ui_player_icon.png"));
        this.healthSheet = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\ui_player_heathBar.png"));
        this.dayBar = new Texture(Gdx.files.internal("Cute_Fantasy_UI\\moneyBar.png"));
        TextureRegion[][] regions = TextureRegion.split(healthSheet, healthSheet.getWidth() / 5, healthSheet.getHeight() / 2);
        this.healthBar = new TextureRegion[regions.length * regions[0].length];
        this.font = new BitmapFont();
        font.getData().setScale(1.5f);
        font.setColor(Color.BLACK);

// Dùng vòng lặp để khởi tạo healthBar
        int index = 0;
        for (int i = 0; i < regions.length; i++) {
            for (int j = 0; j < regions[i].length; j++) {
                healthBar[index++] = regions[i][j];
            }
        }
        checkRanges = new int[][]{{90, 99}, {80, 89}, {70, 79}, {60, 69}, {50, 59}, {40, 49}, {30, 39}, {20, 29}, {10, 19}, {0, 9}};
    }

    public void create() {
        Gdx.input.setInputProcessor(stage);
    }

    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render() {
        System.out.println("heckRanges.length"+ checkRanges.length);
        batch.begin();
        font.getCache().clear(); // Xóa bộ đệm trước khi vẽ
        CheckHealth((int) Player.currentHealth,checkRanges);
        batch.draw(avatar,20,Gdx.graphics.getHeight()-95,avatar.getWidth() * 4,avatar.getHeight() * 4);
        batch.draw(dayBar,20,Gdx.graphics.getHeight() - 140,dayBar.getWidth() * 4,dayBar.getHeight() * 4);
        font.draw(batch,"Day: " + String.valueOf(GameScreen.currentDays),43,Gdx.graphics.getHeight() - 98);
        batch.end();
        float delta = Gdx.graphics.getDeltaTime();
        stage.act(delta);
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        batch.dispose();

    }

    public void CheckHealth(int currentHealth, int[][] checkRanges){
        for (int index = 0; index < checkRanges.length; index++) {
            int start = checkRanges[index][0];
            int end = checkRanges[index][1];
            if (currentHealth >= start && currentHealth <= end) {
                batch.draw(healthBar[index],83, Gdx.graphics.getHeight() - 50, healthBar[0].getRegionWidth()*7,healthBar[0].getRegionHeight()*4);
                return;
            }
        }
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
