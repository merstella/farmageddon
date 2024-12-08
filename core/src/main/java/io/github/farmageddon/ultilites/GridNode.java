package io.github.farmageddon.ultilites;

/**
 * Represents a single cell in the grid for pathfinding.
 */
public class GridNode {
    public enum GridType {
        PASSABLE,
        UNPASSABLE,
        START,
        END
    }

    public float X, Y; // Position in pixels
    public float Width, Height; // Size in pixels
    public GridType type;

    public GridNode Parent; // Parent node in the path
    public float G; // Cost from start node
    public float H; // Heuristic cost to end node
    public float F; // Total cost (G + H)

    /**
     * Constructs a GridNode with specified position and size.
     *
     * @param x      The X position in pixels.
     * @param y      The Y position in pixels.
     * @param width  The width in pixels.
     * @param height The height in pixels.
     */
    public GridNode(float x, float y, float width, float height) {
        this.X = x;
        this.Y = y;
        this.Width = width;
        this.Height = height;
        this.type = GridType.PASSABLE;
    }

    /**
     * Resets the node's pathfinding properties.
     */
    public void Reset() {
        this.Parent = null;
        this.G = 0;
        this.H = 0;
        this.F = 0;
        // Optionally reset type to PASSABLE if needed
        // this.type = GridType.PASSABLE;
    }

    /**
     * Initializes or updates node properties during pathfinding.
     *
     * @param parent The parent node.
     * @param end    The end node.
     */
    public void CalculateNode(GridNode parent, GridNode end) {
        this.Parent = parent;
        if (parent != null) {
            this.G = parent.G + ((this.X != parent.X && this.Y != parent.Y) ? 14 : 10);
        } else {
            this.G = 0;
        }
        this.H = calculateHeuristic(end);
        this.F = this.G + this.H;
    }

    /**
     * Calculates the heuristic (Octile distance) to the end node.
     *
     * @param end The end GridNode.
     * @return The heuristic value.
     */
    private float calculateHeuristic(GridNode end) {
        float dx = Math.abs(this.X - end.X) / gridSizeX();
        float dy = Math.abs(this.Y - end.Y) / gridSizeY();
        float D = 10;  // Cost for orthogonal movement
        float D2 = 14; // Cost for diagonal movement
        return D * (dx + dy) + (D2 - 2 * D) * Math.min(dx, dy);
    }

    /**
     * Retrieves the grid size in the X direction.
     *
     * @return The grid size X in pixels.
     */
    private float gridSizeX() {
        return this.Width; // Assuming uniform grid sizes
    }

    /**
     * Retrieves the grid size in the Y direction.
     *
     * @return The grid size Y in pixels.
     */
    private float gridSizeY() {
        return this.Height; // Assuming uniform grid sizes
    }
}
