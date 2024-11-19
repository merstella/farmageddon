package io.github.farmageddon;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;


public class AnimalUI {
    private Stage stage;
    private Label animalStatusLabel;
    private ImageButton feedButton;
    private Animal animal;

    public AnimalUI(Stage stage, Animal animal) {
        this.stage = stage;
        this.animal = animal;
        createUI();
    }

    private void createUI() {
        Table table = new Table();
        table.setFillParent(true);

        animalStatusLabel = new Label("Animal Health: " + animal.getHealth(), new Label.LabelStyle());
        feedButton = new ImageButton(new TextureRegionDrawable(new TextureRegion()));

    }
}
