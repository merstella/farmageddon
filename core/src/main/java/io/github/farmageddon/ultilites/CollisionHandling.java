package io.github.farmageddon.ultilites;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import io.github.farmageddon.screens.GameScreen;

import static io.github.farmageddon.screens.GameScreen.shapeRenderer;

public class CollisionHandling {
    static GameScreen gameScreen;
    private static final MapObjects collisionLayer = GameScreen.map.getLayers().get("Object Layer 1").getObjects();

    public static boolean isColliding(Rectangle entityBound) {
        for (MapObject object : collisionLayer) {
            if (object instanceof RectangleMapObject) {
                Rectangle rectangle = ((RectangleMapObject) object).getRectangle();
                if (entityBound.overlaps(rectangle)) {
                    return true; // Collision detected
                }
            }
        }
        return false;
    }

    public static void renderCollision() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        for (MapObject object : collisionLayer) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                shapeRenderer.rect(rect.x, rect.y, rect.width, rect.height);
            }
        }
        shapeRenderer.end();
    }
}
