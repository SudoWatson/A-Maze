package main;

import java.util.ArrayList;
import java.util.List;

import aStar.AStarAlgo;
import node.Node;
import processing.core.*;

public class Main extends PApplet{

    public static boolean DEBUG = false;

    /** Main processing sketch */
    public static Main sketch;

    /** Size of Window */
    private static final int WIDTH = 1000, HEIGHT = 1000;

    /** Node dimensions */
    private static int rows = 50, cols = 50;

    /** Grid of nodes */
    private static Node[][] grid = new Node[rows][cols];

    private static AStarAlgo aStar;


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
                if (sketch.random(1) < 0.45) grid[i][j].isWall = true;
            }
        }

        aStar = new AStarAlgo(grid[0][0], grid[rows-1][cols-1], grid);
        
    }

    public void draw() {
        background(33);

        drawGrid();

        Node closestNode = aStar.stepSolveGrid();
        drawSolution(closestNode);

    }

    public void mousePressed() {
        background(256,0,0);
    }

    public void drawGrid() {
        strokeWeight(2);

        // Draw all nodes in grid
        for (Node[] nodeRow : grid) {
            for (Node node : nodeRow) {
                node.draw(new int[] {255,255,255});
            }
        }

        for (Node node : aStar.getOpenSet()) {
            node.draw(new int[] {0,255,0});
        }

        for (Node node : aStar.getClosedSet()) {
            node.draw(new int[] {255,0,0});
        }
    }


    /**
     * Begin Sketch. Use 'setup()' for anything that normally would go here
     */
    public static void main(String[] args) {
        sketch = new Main();
        String[] processingArgs = {"App"};
        PApplet.runSketch(processingArgs, sketch);
    }

    public static void drawSolution(Node lastNode) {
        Node tempNode = lastNode;
        // Draw path
        while (tempNode != null) {
            tempNode.draw(new int[] {0,0,255});

            // vv Debug vv  \\
            if (DEBUG && tempNode.prevNode != null) {  // Traces a path from start to finish. yes this is ugly
                sketch.line(tempNode.getI()*nodeWidth+nodeWidth/2, tempNode.getJ()*nodeHeight+nodeHeight/2, tempNode.prevNode.getI()*nodeWidth+nodeWidth/2, tempNode.prevNode.getJ()*nodeHeight+nodeHeight/2);
            }

            tempNode = tempNode.prevNode;
        }
    }
}
