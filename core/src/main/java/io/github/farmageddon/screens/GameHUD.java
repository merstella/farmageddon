//package io.github.farmageddon.screens;
//
//import com.badlogic.gdx.graphics.Color;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Pixmap;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.maps.tiled.TiledMap;
//import com.badlogic.gdx.maps.tiled.TmxMapLoader;
//import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import io.github.farmageddon.entities.Player;
//public class GameHUD {
//    TiledMap map = new TmxMapLoader().load("mapok.tmx");
//    OrthogonalTiledMapRenderer mapRenderer = new OrthogonalTiledMapRenderer(map);
//    private Stage stage;
//    private OrthographicCamera minimapCamera;
//    private TiledMap minimapTiledMap;
//    private OrthogonalTiledMapRenderer minimapRenderer;
//    private Image minimapImage;
//    public GameHUD(TiledMap map) {
//        stage = new Stage();
//        minimapCamera = new OrthographicCamera();
//        minimapTiledMap = map; // Use the same map for the minimap
//        minimapRenderer = new OrthogonalTiledMapRenderer(minimapTiledMap);
//
//    }
//
//    public void update(Player player/*, List<Plant> plants, List<Zombie> zombies*/) {
//        // Update the minimap camera and render the minimap
//        updateMinimapCamera(player);
//        renderMinimap();
//    }
//
//    private void updateMinimapCamera(Player player) {
//        // Set the camera position to the player's position
//        minimapCamera.position.set(player.getX(), player.getY(), 0);
//        minimapCamera.update();
//    }
//
//    private void renderMinimap() {
//        // Set the viewport for the minimap
//        minimapCamera.setToOrtho(false);
//        minimapRenderer.setView(minimapCamera);
//        minimapRenderer.render();
//
//        // Optionally, draw the player and other entities here
//        drawEntities();
//    }
//
//    private void drawEntities(Player player/*, List<Plant> plants, List<Zombie> zombies*/) {
//        // Use a Pixmap to draw entities on top of the minimap
//        Pixmap pixmap = new Pixmap(minimapRenderer.getMap().getProperties().get("width", Integer.class) * 32,
//            minimapRenderer.getMap().getProperties().get("height", Integer.class) * 32,
//            Pixmap.Format.RGBA8888);
//        pixmap.setColor(Color.BLACK);
//        pixmap.fill(); // Clear the pixmap
//
//        // Draw player
//        pixmap.setColor(Color.BLUE);
//        pixmap.fillCircle((int)(player.getPosition().x / 32), (int)(player.getPosition().y / 32), 2);
//
////        // Draw plants
////        pixmap.setColor(Color.GREEN);
////        for (Plant plant : plants) {
////            pixmap.fillCircle((int)(plant.getX() / tileWidth), (int)(plant.getY() / tileHeight), 2);
////        }
////
////        // Draw zombies
////        pixmap.setColor(Color.RED);
////        for (Zombie zombie : zombies) {
////            pixmap.fillCircle((int)(zombie.getX() / tileWidth), (int)(zombie.getY() / tileHeight), 2);
////        }
//
//        // Update the texture with the new pixmap
//        TextureRegion minimapTextureRegion = new TextureRegion(new Texture(pixmap));
//        // Add a drawable to your minimap image here
//        minimapImage.setDrawable(new TextureRegionDrawable(minimapTextureRegion));
//
//        // Dispose of the pixmap
//        pixmap.dispose();
//    }
//
//    public void render() {
//        stage.draw();
//    }
//}
