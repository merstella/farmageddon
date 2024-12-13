package io.github.farmageddon.ultilites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class ZoneManager {
    // Define activity zones as rectangles (you can adjust the size and position as needed)
    private Rectangle farmingZone1;
    private Rectangle farmingZone2;
    public Rectangle chickenZone;
    public Rectangle pigZone;
    public Rectangle cowZone;
    public Rectangle sheepZone;

    public ZoneManager() {
        // Define the zones for each activity (this can be expanded as needed)
        farmingZone1 = new Rectangle(199, 102, 183, 115); // (x, y, width, height)
        farmingZone2 = new Rectangle(775, 261, 145, 85); // (x, y, width, height)
        chickenZone = new Rectangle(553, 550, 81, 46);
        pigZone = new Rectangle(631, 165, 72, 106);
        cowZone = new Rectangle(741, 519, 105, 113);
        sheepZone = new Rectangle(384, 457, 77, 97); //!!!
    }

    // Method to check if the position is within a valid zone for farming activity
    public boolean isValidFarmingPosition(Vector2 position) {
        return farmingZone1.contains(position.x, position.y) || farmingZone2.contains(position.x, position.y);
    }

    // Method to check if the position is within a valid zone for chicken raising
    public boolean isValidChickenPosition(Vector2 position) {
        return chickenZone.contains(position.x, position.y);
    }

    // Method to check if the position is within a valid zone for pig raising
    public boolean isValidPigPosition(Vector2 position) {
        return pigZone.contains(position.x, position.y);
    }

    // Method to check if the position is within a valid zone for cow raising
    public boolean isValidCowPosition(Vector2 position) {
        return cowZone.contains(position.x, position.y);
    }

    // Getters for zone positions (optional)
    public Rectangle getFarmingZone1() {
        return farmingZone1;
    }

    public Rectangle getFarmingZone2() {
        return farmingZone2;
    }


    public Rectangle getChickenZone() {
        return chickenZone;
    }

    public Rectangle getPigZone() {
        return pigZone;
    }

    public Rectangle getCowZone() {
        return cowZone;
    }
    public Rectangle getSheepZone() {
        return sheepZone;
    }
    public void render(SpriteBatch batch) {
        // Vẽ các khu vực
        batch.draw(new Texture(Gdx.files.internal("border.png")), farmingZone1.x, farmingZone1.y, farmingZone1.width, farmingZone1.height);
        batch.draw(new Texture(Gdx.files.internal("border.png")), farmingZone2.x, farmingZone2.y, farmingZone2.width, farmingZone2.height);
        batch.draw(new Texture(Gdx.files.internal("border.png")), chickenZone.x, chickenZone.y, chickenZone.width, chickenZone.height);
        batch.draw(new Texture(Gdx.files.internal("border.png")), pigZone.x, pigZone.y, pigZone.width, pigZone.height);
        batch.draw(new Texture(Gdx.files.internal("border.png")), cowZone.x, cowZone.y, cowZone.width, cowZone.height);
        batch.draw(new Texture(Gdx.files.internal("border.png")), sheepZone.x, sheepZone.y, sheepZone.width, sheepZone.height);
    }
}
