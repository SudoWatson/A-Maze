package main;

import java.util.ArrayList;
import java.util.List;

import node.Node;
import processing.core.*;

public class Main extends PApplet{

    public static boolean DEBUG = true;

    /** Main processing sketch */
    public static Main sketch;

    /** Size of Window */
    private static final int WIDTH = 1000, HEIGHT = 1000;

    /** Node dimensions */
    private static int rows = 25, cols = 25;

    /** Grid of nodes */
    private static Node[][] grid = new Node[rows][cols];

    /** Collection of nodes available to be checked */
    private static List<Node> openSet = new ArrayList<Node>();
    /** Collection of nodes already checked, and may need to be rechecked */
    private static List<Node> closedSet = new ArrayList<Node>();

    /** Starting/ending node */
    private static Node startNode, endNode;

    private static int nodeWidth = WIDTH/rows;
    private static int nodeHeight = HEIGHT/cols;

    public void settings() {
        size(WIDTH, HEIGHT);
        
    }

    public void setup() {

        // Initialize grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Node(i, j, nodeWidth, nodeHeight);
                if (sketch.random(1) < 0.3) grid[i][j].isWall = true;
            }
        }

        // Assign start and end positions
        assignStartNode(grid[0][0]);
        assignEndNode(grid[rows-1][cols-1]);
        
    }

    public void draw() {
        //TODO Move AStar Algorithm to proper class

        background(33);

        strokeWeight(2);

        // Draw all nodes in grid
        for (Node[] nodeRow : grid) {
            for (Node node : nodeRow) {
                node.draw();
            }
        }


        if (openSet.size() > 0) {  // Still nodes to check, continue working
            
            /** Index of the node hueristically closest to the end point */
            int closestIndex = 0;
            /** Node hueristically closest to the end */
            Node closestNode;

            for (int i = 0; i < openSet.size(); i++) {  // Go through nodes we need to check and check the one closest to the end
                if (openSet.get(i).f < openSet.get(closestIndex).f) {
                    closestIndex = i;
                }
            }

            closestNode = openSet.get(closestIndex);

            if (closestNode == endNode) {  // Node is the finish node, we found the solution
                drawSolution(endNode);
                return;  // Don't bother with doing anything else
            }


            // Add neighbors to open set and update their 'g'
            for (Node neighbor : neighborsOf(closestNode)) {
                if (!closedSet.contains(neighbor) && !neighbor.isWall) {  // Neighbor has already been checked before
                    double tempG;
                    
                    // Calculate potential new g score
                    if (closestNode.getI() == neighbor.getI() || closestNode.getJ() == neighbor.getJ()) {  // Neighbor is inline
                        tempG = closestNode.g + 1;
                    } else {  // Neighbor is diagonal
                        //tempG = closestNode.g + (double) Math.sqrt(2);
                        tempG = closestNode.g + 1;
                    }

                    if (tempG < neighbor.g) {
                        
                        neighbor.g = tempG;  // New G score is less than previous, this is better path
                        neighbor.prevNode = closestNode;
                    }

                    if (!openSet.contains(neighbor)) {  // Add neighbor to open set
                        openSet.add(neighbor);
                        neighbor.prevNode = closestNode;
                        neighbor.setColor(new int[]{0,255,0});
                    }
                    
                    neighbor.h = hueristicOf(neighbor);
                    neighbor.f = neighbor.h + neighbor.g;
                }


            }

            // Node is no longer a part of nodes that need to be checked, add it to already checked nodes
            closedSet.add(closestNode);
            closestNode.setColor(new int[]{255,0,0});
            openSet.remove(closestNode);

        } else {  // No solution found
            System.out.println("No solution found!");
            noLoop();
            return;
        }

    }

    public void mousePressed() {
        background(256,0,0);
    }


    /**
     * Begin Sketch. Use 'setup()' for anything that normally would go here
     */
    public static void main(String[] args) {
        sketch = new Main();
        String[] processingArgs = {"App"};
        PApplet.runSketch(processingArgs, sketch);
    }

    public static List<Node> neighborsOf(Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        int i = node.getI();
        int j = node.getJ();

        // Get valid neighbors
        if (i > 0) neighbors.add(grid[i-1][j]);
        if (i < rows-1) neighbors.add(grid[i+1][j]);
        if (j > 0) neighbors.add(grid[i][j-1]);
        if (j < cols-1) neighbors.add(grid[i][j+1]);

        // Get valid diagonal neighbors
        if (i > 0 && j > 0) neighbors.add(grid[i-1][j-1]);
        if (i > 0 && j < cols-1) neighbors.add(grid[i-1][j+1]);
        if (i < rows-1 && j > 0) neighbors.add(grid[i+1][j-1]);
        if (i < rows-1 && j < cols-1) neighbors.add(grid[i+1][j+1]);

        return neighbors;
    }

    public static double hueristicOf(Node node) {
        return PApplet.dist(node.getI(), node.getJ(), endNode.getI(), endNode.getJ());
    }

    public static void drawSolution(Node lastNode) {
        Node tempNode = endNode;
        // Draw path
        while (tempNode != null) {
            tempNode.setColor(new int[]{0,0,255});
            tempNode.draw();
            if (DEBUG && tempNode.prevNode != null) {  // Traces a path from start to finish. yes this is ugly
                sketch.line(tempNode.getI()*nodeWidth+nodeWidth/2, tempNode.getJ()*nodeHeight+nodeHeight/2, tempNode.prevNode.getI()*nodeWidth+nodeWidth/2, tempNode.prevNode.getJ()*nodeHeight+nodeHeight/2);
            }
            tempNode = tempNode.prevNode;
        }
    }

    public void assignStartNode(Node node) {
        startNode = node;
        startNode.setColor(new int[]{0,255,0});
        openSet.add(startNode);
        startNode.g = 0;
        startNode.isWall = false;
    }

    public void assignEndNode(Node node) {
        endNode = node;
        endNode.setColor(new int[]{0,255,255});
        endNode.isWall = false;
    }
}
