package aStar;

import java.util.ArrayList;
import java.util.List;

import node.Node;
import processing.core.PApplet;

import static main.Main.sketch;

public class AStarAlgo {
    

    /** Collection of nodes available to be checked */
    private List<Node> openSet = new ArrayList<Node>();
    /** Collection of nodes already checked, and may need to be rechecked */
    private List<Node> closedSet = new ArrayList<Node>();

    private Node startNode, endNode;

    private  Node[][] grid;

    private int rows, cols;

    
    public AStarAlgo(Node startNode, Node endNode, Node[][] grid) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.grid = grid;
        this.rows = grid.length;
        this.cols = grid[0].length;
        
        // Assign start and end positions
        assignStartNode(this.startNode);
        assignEndNode(this.endNode);
    }


    // TODO Solve entire grid in one run

    /**
     * Does one step of solving given grid
     * @return node hueristically closest to the end node at current point of solving
     */
    public Node stepSolveGrid() {
        /** Node hueristically closest to the end */
        Node closestNode = null;

        if (this.openSet.size() > 0) {  // Still nodes to check, continue working
            
            /** Index of the node hueristically closest to the end point */
            int closestIndex = 0;

            for (int i = 0; i < this.openSet.size(); i++) {  // Go through nodes we need to check and check the one closest to the end
                if (this.openSet.get(i).f < this.openSet.get(closestIndex).f) {
                    closestIndex = i;
                }
            }

            closestNode = this.openSet.get(closestIndex);

            if (closestNode == this.endNode) {  // Node is the finish node, we found the solution
                //drawSolution(endNode);
                System.out.println("Solution found!");
                sketch.noLoop();
                return closestNode;  // Don't bother with doing anything else
            }
            //drawSolution(closestNode);


            // Add neighbors to open set and update their 'g'
            for (Node neighbor : neighborsOf(closestNode)) {
                if (!closedSet.contains(neighbor) && !neighbor.isWall) {  // Neighbor has already been checked before
                    // TODO move g calc to function
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
                    }
                    
                    neighbor.h = hueristicOf(neighbor);
                    neighbor.f = neighbor.h + neighbor.g;
                }


            }

            // Node is no longer a part of nodes that need to be checked, add it to already checked nodes
            this.closedSet.add(closestNode);
            this.openSet.remove(closestNode);

        } else {  // No solution found
            System.out.println("No solution found!");
            sketch.noLoop();
            return closestNode;
        }
        return closestNode;
    }


    /**
     * Get the neighbors of a given node
     * @param node to find neighbors of
     * @return neighbors of given node in an array list
     */
    private List<Node> neighborsOf(Node node) {
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

    /**
     * Huristic function
     * @param node to calculate hueristic off
     * @return hueristic value of given node
     */
    private double hueristicOf(Node node) {
        return PApplet.dist(node.getI(), node.getJ(), this.endNode.getI(), this.endNode.getJ());
    }

    /**
     * Assigns the starting node and does stuff that it needs
     * @param startingNode 
     */
    private void assignStartNode(Node startingNode) {
        startNode = startingNode;
        openSet.add(startNode);
        startNode.g = 0;
        startNode.isWall = false;
    }

    /**
     * Assigns the starting node and does stuff that it needs
     * @param endingNode
     */
    private void assignEndNode(Node endingNode) {
        endNode = endingNode;
        endNode.isWall = false;
    }

    public List<Node> getOpenSet() {
        return new ArrayList<Node>(this.openSet);
    }

    public List<Node> getClosedSet() {
        return new ArrayList<Node>(this.closedSet);
    }

}
