//package io.github.farmageddon.screens;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//
//import java.util.List;
//
//public class Minimap {
//    private final OrthographicCamera camera;
//    private final ShapeRenderer shapeRenderer;
//    private final float scale;
//
//    public Minimap(float worldWidth, float worldHeight, float scale) {
//        this.scale = scale;
//        this.camera = new OrthographicCamera();
//        this.camera.setToOrtho(false, worldWidth * scale, worldHeight * scale);
//        this.shapeRenderer = new ShapeRenderer();
//    }
//
//    public void render(Player player, List<Zombie> zombies, List<Plant> plants) {
//        camera.update();
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//
//        // Draw zombies
//        shapeRenderer.setColor(Color.RED);
//        for (Zombie zombie : zombies) {
//            shapeRenderer.circle(zombie.x * scale, zombie.y * scale, 2);
//        }
//
//        // Draw plants
//        shapeRenderer.setColor(Color.GREEN);
//        for (Plant plant : plants) {
//            shapeRenderer.rect(plant.x * scale, plant.y * scale, 2, 2);
//        }
//
//        // Draw player
//        shapeRenderer.setColor(Color.BLUE);
//        shapeRenderer.circle(player.x * scale, player.y * scale, 3);
//
//        shapeRenderer.end();
//    }
//
//    public void dispose() {
//        shapeRenderer.dispose();
//    }
//}
