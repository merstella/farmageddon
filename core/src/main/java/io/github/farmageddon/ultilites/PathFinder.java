package io.github.farmageddon.ultilites;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Implements the Theta* pathfinding algorithm.
 */
public class PathFinder {
    private Array<Array<GridNode>> grid = new Array<>(); // The grid of nodes
    private int gridSizeX, gridSizeY; // Width and Height for each GridNode in pixels
    private int gridWidth, gridHeight; // Number of nodes horizontally and vertically

    public static final int FOUND = 1;
    public static final int NON_EXISTENT = 2;

    /**
     * Initializes the PathFinder with uniform grid sizes.
     *
     * @param screenWidth  The width of the screen in pixels.
     * @param screenHeight The height of the screen in pixels.
     * @param gridSize     The size of each grid node (both width and height) in pixels.
     */
    public PathFinder(int screenWidth, int screenHeight, int gridSize) {
        this.gridSizeX = gridSize;
        this.gridSizeY = gridSize;
        this.gridWidth = screenWidth / gridSizeX;
        this.gridHeight = screenHeight / gridSizeY;

        initializeGrid(gridWidth, gridHeight, gridSizeX, gridSizeY);
    }

    /**
     * Initializes the PathFinder with separate grid sizes for X and Y axes.
     *
     * @param screenWidth  The width of the screen in pixels.
     * @param screenHeight The height of the screen in pixels.
     * @param gridSizeX    The width of each grid node in pixels.
     * @param gridSizeY    The height of each grid node in pixels.
     */
    public PathFinder(int screenWidth, int screenHeight, int gridSizeX, int gridSizeY) {
        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;
        this.gridWidth = screenWidth / gridSizeX;
        this.gridHeight = screenHeight / gridSizeY;

        initializeGrid(gridWidth, gridHeight, gridSizeX, gridSizeY);
    }

    /**
     * Initializes the grid with GridNode instances.
     *
     * @param width     Number of nodes horizontally.
     * @param height    Number of nodes vertically.
     * @param gridSizeX Width of each node.
     * @param gridSizeY Height of each node.
     */
    private void initializeGrid(int width, int height, int gridSizeX, int gridSizeY) {
        for (int y = 0; y < height; y++) {
            Array<GridNode> row = new Array<>();
            for (int x = 0; x < width; x++) {
                row.add(new GridNode(x * gridSizeX, y * gridSizeY, gridSizeX, gridSizeY));
            }
            grid.add(row);
        }
    }

    /**
     * Comparator for the priority queue based on F score.
     */
    private Comparator<GridNode> nodeComparator = new Comparator<GridNode>() {
        @Override
        public int compare(GridNode n1, GridNode n2) {
            return Float.compare(n1.F, n2.F);
        }
    };

    /**
     * Finds a path from the start position to the end position using the Theta* algorithm.
     *
     * @param startX    The starting node's X grid coordinate.
     * @param startY    The starting node's Y grid coordinate.
     * @param endX      The ending node's X grid coordinate.
     * @param endY      The ending node's Y grid coordinate.
     * @param finalPath An empty Array to be filled with the resulting path nodes.
     * @return FOUND if a path is found, NON_EXISTENT otherwise.
     */
    public int findPath(int startX, int startY, int endX, int endY, Array<GridNode> finalPath) {
        // Validate start and end positions
        if (!isValidPosition(startX, startY) || !isValidPosition(endX, endY)) {
            System.out.println("Invalid start or end positions: (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")");
            return NON_EXISTENT;
        }

        GridNode startNode = grid.get(startY).get(startX);
        GridNode endNode = grid.get(endY).get(endX);

        if (startNode == endNode) {
            finalPath.add(startNode);
            System.out.println("Start and end nodes are the same.");
            return FOUND;
        }

        // Reset all nodes
        resetGrid();

        // Initialize open and closed lists
        PriorityQueue<GridNode> openList = new PriorityQueue<>(nodeComparator);
        Array<GridNode> closedList = new Array<>();

        // Initialize start node
        startNode.CalculateNode(null, endNode);
        openList.add(startNode);

        while (!openList.isEmpty()) {
            // Get the node with the lowest F score
            GridNode current = openList.poll();
            closedList.add(current);

            // If we've reached the end node, reconstruct the path
            if (current == endNode) {
                reconstructPath(endNode, finalPath);
                System.out.println("Path found from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ") with " + finalPath.size + " nodes.");
                return FOUND;
            }

            // Get all passable neighbors
            Array<GridNode> neighbors = getNeighbors(current);
            for (GridNode neighbor : neighbors) {
                if (neighbor.type == GridNode.GridType.UNPASSABLE || closedList.contains(neighbor, true)) {
                    continue; // Skip unpassable or already evaluated nodes
                }

                GridNode parent = current.Parent;
                if (parent == null) {
                    parent = current;
                }

                // Check line-of-sight from parent to neighbor
                if (hasLineOfSight(parent, neighbor)) {
                    float tentativeG = parent.G + distance(parent, neighbor);
                    if (tentativeG < neighbor.G || !openList.contains(neighbor)) {
                        neighbor.Parent = parent;
                        neighbor.G = tentativeG;
                        neighbor.H = calculateHeuristic(neighbor, endNode);
                        neighbor.F = neighbor.G + neighbor.H;

                        if (!openList.contains(neighbor)) {
                            openList.add(neighbor);
//                            System.out.println("Adding node (" + getGridX(neighbor) + ", " + getGridY(neighbor) + ") to open list with F=" + neighbor.F);
                        }
                    }
                } else {
                    // If no line-of-sight, proceed with standard A* logic
                    float tentativeG = current.G + distance(current, neighbor);
                    if (tentativeG < neighbor.G || !openList.contains(neighbor)) {
                        neighbor.Parent = current;
                        neighbor.G = tentativeG;
                        neighbor.H = calculateHeuristic(neighbor, endNode);
                        neighbor.F = neighbor.G + neighbor.H;

                        if (!openList.contains(neighbor)) {
                            openList.add(neighbor);
//                            System.out.println("Adding node (" + getGridX(neighbor) + ", " + getGridY(neighbor) + ") to open list with F=" + neighbor.F);
                        }
                    }
                }
            }
        }

        // No path found
//        System.out.println("No path found from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")");
        return NON_EXISTENT;
    }

    /**
     * Resets the grid by clearing parents and costs for all nodes.
     */
    private void resetGrid() {
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                grid.get(y).get(x).Reset();
            }
        }
    }

    /**
     * Reconstructs the path from the end node to the start node by following parent links.
     *
     * @param endNode   The end GridNode.
     * @param finalPath An empty Array to be filled with the path nodes.
     */
    private void reconstructPath(GridNode endNode, Array<GridNode> finalPath) {
        GridNode current = endNode;
        while (current != null) {
            finalPath.add(current);
//            System.out.println("Path node: (" + getGridX(current) + ", " + getGridY(current) + ")");
            current = current.Parent;
        }
        finalPath.reverse(); // Reverse the path to start from the beginning
    }



    /**
     * Retrieves all passable neighboring nodes of the given node.
     *
     * @param node The current GridNode.
     * @return An Array of neighboring GridNodes.
     */
    private Array<GridNode> getNeighbors(GridNode node) {
        Array<GridNode> neighbors = new Array<>();

        int x = getGridX(node);
        int y = getGridY(node);

        // Define all 8 possible directions (N, NE, E, SE, S, SW, W, NW)
        int[][] directions = {
            {0, 1}, {1, 1}, {1, 0}, {1, -1},
            {0, -1}, {-1, -1}, {-1, 0}, {-1, 1}
        };

        for (int[] dir : directions) {
            int nx = x + dir[0];
            int ny = y + dir[1];

            if (isValidPosition(nx, ny)) {
                neighbors.add(grid.get(ny).get(nx));
            }
        }

        return neighbors;
    }

    /**
     * Checks if there is a line-of-sight between two nodes.
     *
     * @param from The starting GridNode.
     * @param to   The target GridNode.
     * @return True if line-of-sight exists, false otherwise.
     */
    private boolean hasLineOfSight(GridNode from, GridNode to) {
        int x0 = getGridX(from);
        int y0 = getGridY(from);
        int x1 = getGridX(to);
        int y1 = getGridY(to);

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int x = x0;
        int y = y0;

        int n = 1 + dx + dy;
        int x_inc = (x1 > x0) ? 1 : -1;
        int y_inc = (y1 > y0) ? 1 : -1;
        int error = dx - dy;
        dx *= 2;
        dy *= 2;

        for (; n > 0; --n) {
            if (grid.get(y).get(x).type == GridNode.GridType.UNPASSABLE) {
                return false;
            }

            if (error > 0) {
                x += x_inc;
                error -= dy;
            } else if (error < 0) {
                y += y_inc;
                error += dx;
            } else { // error == 0
                // Move diagonally
                x += x_inc;
                y += y_inc;
                error -= dy;
                error += dx;
                n--; // Extra decrement because we moved diagonally
                if (n <= 0) {
                    break;
                }
                if (grid.get(y).get(x).type == GridNode.GridType.UNPASSABLE) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Calculates the Euclidean distance between two nodes.
     *
     * @param a The first GridNode.
     * @param b The second GridNode.
     * @return The Euclidean distance.
     */
    private float distance(GridNode a, GridNode b) {
        float dx = (a.X - b.X) / gridSizeX;
        float dy = (a.Y - b.Y) / gridSizeY;
        return (float) Math.sqrt(dx * dx + dy * dy) * 10; // Scale by movement cost
    }

    /**
     * Calculates the heuristic (Euclidean distance) between two nodes.
     *
     * @param a The first GridNode.
     * @param b The second GridNode.
     * @return The heuristic value.
     */
    private float calculateHeuristic(GridNode a, GridNode b) {
        float dx = Math.abs(a.X - b.X) / gridSizeX;
        float dy = Math.abs(a.Y - b.Y) / gridSizeY;
        return (float) Math.sqrt(dx * dx + dy * dy) * 10; // Scale by movement cost
    }

    /**
     * Retrieves the grid X coordinate of a node.
     *
     * @param node The GridNode.
     * @return The grid X coordinate.
     */
    public int getGridX(GridNode node) {
        return (int) (node.X / gridSizeX);
    }

    /**
     * Retrieves the grid Y coordinate of a node.
     *
     * @param node The GridNode.
     * @return The grid Y coordinate.
     */
    public int getGridY(GridNode node) {
        return (int) (node.Y / gridSizeY);
    }

    /**
     * Checks if the given grid coordinates are within the grid boundaries.
     *
     * @param x The grid X coordinate.
     * @param y The grid Y coordinate.
     * @return True if valid, false otherwise.
     */
    public boolean isValidPosition(int x, int y) {
        return x >= 0 && x < gridWidth && y >= 0 && y < gridHeight;
    }

    /**
     * Sets the type of the node at the given grid coordinates.
     *
     * @param gridX The grid X coordinate.
     * @param gridY The grid Y coordinate.
     * @param type  The GridType to set.
     */
    public void setGridNode(int gridX, int gridY, GridNode.GridType type) {
        if (isValidPosition(gridX, gridY)) {
            GridNode node = grid.get(gridY).get(gridX);
            node.type = type;
            System.out.println("Node (" + gridX + ", " + gridY + ") set to " + type);
        }
    }


    public void drawGrid(ShapeRenderer shapeRenderer) {
        // Step 1: Draw grid lines
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(Color.LIGHT_GRAY);
        for (int y = 0; y <= gridHeight; y++) {
            shapeRenderer.line(0, y * gridSizeY, gridWidth * gridSizeX, y * gridSizeY);
        }
        for (int x = 0; x <= gridWidth; x++) {
            shapeRenderer.line(x * gridSizeX, 0, x * gridSizeX, gridHeight * gridSizeY);
        }
        shapeRenderer.end();

        // Step 2: Draw obstacles, start, and end nodes
        shapeRenderer.begin(ShapeType.Filled);
        for (int y = 0; y < gridHeight; y++) {
            for (int x = 0; x < gridWidth; x++) {
                GridNode node = grid.get(y).get(x);
                switch (node.type) {
                    case UNPASSABLE:
                        shapeRenderer.setColor(Color.BLACK);
                        shapeRenderer.rect(node.X, node.Y, node.Width, node.Height);
                        break;
                    case START:
                        shapeRenderer.setColor(Color.GREEN);
                        shapeRenderer.rect(node.X, node.Y, node.Width, node.Height);
                        break;
                    case END:
                        shapeRenderer.setColor(Color.RED);
                        shapeRenderer.rect(node.X, node.Y, node.Width, node.Height);
                        break;
                    default:
                        // Optionally, draw passable nodes with a semi-transparent color
                        // shapeRenderer.setColor(new Color(1, 1, 1, 0.1f));
                        // shapeRenderer.rect(node.X, node.Y, node.Width, node.Height);
                        break;
                }
            }
        }
        shapeRenderer.end();

    }
    public void drawPath(ShapeRenderer shapeRenderer, Array<GridNode> path) {
        // Step 3: Draw the path as connected lines (ensure this is after obstacles)
        if (path != null && path.size > 1) {
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(Color.YELLOW);
            for (int i = 0; i < path.size - 1; i++) {
                GridNode from = path.get(i);
                GridNode to = path.get(i + 1);
                float fromX = from.X + from.Width / 2;
                float fromY = from.Y + from.Height / 2;
                float toX = to.X + to.Width / 2;
                float toY = to.Y + to.Height / 2;
                shapeRenderer.line(fromX, fromY, toX, toY);
            }
            shapeRenderer.end();
        }
    }

    /**
     * Retrieves the grid width (number of nodes horizontally).
     *
     * @return The grid width.
     */
    public int getGridWidth() {
        return gridWidth;
    }

    /**
     * Retrieves the grid height (number of nodes vertically).
     *
     * @return The grid height.
     */
    public int getGridHeight() {
        return gridHeight;
    }

    /**
     * Retrieves the grid size in the X direction.
     *
     * @return The grid size X in pixels.
     */
    public int getGridSizeX() {
        return gridSizeX;
    }

    /**
     * Retrieves the grid size in the Y direction.
     *
     * @return The grid size Y in pixels.
     */
    public int getGridSizeY() {
        return gridSizeY;
    }

    /**
     * Marks grid cells as unpassable based on a collision object's position and size.
     *
     * @param objX      The X position of the collision object in pixels.
     * @param objY      The Y position of the collision object in pixels.
     * @param objWidth  The width of the collision object in pixels.
     * @param objHeight The height of the collision object in pixels.
     */
    public void addCollisionObject(float objX, float objY, float objWidth, float objHeight) {
        // Calculate the grid cell indices that the collision object covers
        int startX = (int) (objX / gridSizeX);
        int startY = (int) (objY / gridSizeY);
        int endX = (int) ((objX + objWidth) / gridSizeX);
        int endY = (int) ((objY + objHeight) / gridSizeY);

        // Clamp the indices to grid boundaries
        startX = Math.max(0, startX);
        startY = Math.max(0, startY);
        endX = Math.min(gridWidth - 1, endX);
        endY = Math.min(gridHeight - 1, endY);

        // Mark each overlapping grid cell as unpassable
        for (int y = startY; y <= endY; y++) {
            for (int x = startX; x <= endX; x++) {
                setGridNode(x, y, GridNode.GridType.UNPASSABLE);
            }
        }

        System.out.println("Added collision object covering grid cells from (" + startX + ", " + startY + ") to (" + endX + ", " + endY + ")");
    }

    // Assuming entity's position is given by a Vector2 (x, y)
    public GridNode getGridNodeForEntity(Vector2 position) {
        // Convert the entity's position to grid coordinates
        int gridX = (int) (position.x / gridSizeX);
        int gridY = (int) (position.y / gridSizeY);

        // Ensure that the coordinates are within the grid bounds
        gridX = Math.max(0, Math.min(gridWidth - 1, gridX));
        gridY = Math.max(0, Math.min(gridHeight - 1, gridY));

        return grid.get(gridY).get(gridX); // Get the corresponding grid node
    }


}
