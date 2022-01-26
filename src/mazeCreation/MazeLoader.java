package mazeCreation;

import node.Node;

import static main.Main.sketch;
import static main.Main.nodeWidth;
import static main.Main.nodeHeight;

public class MazeLoader {
    
    public MazeLoader() {

    }

    public void randomMess(int xSize, int ySize) {
        Node[][] grid = new Node[xSize][ySize];
    }

    public void randomMess(Node[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new Node(i, j, nodeWidth, nodeHeight);
                if (sketch.random(1) < 0.5) grid[i][j].isWall = true;
            }
        }
    }

    public Node[][] generateMaze() {
        return null;
    }
    
    public Node[][] mazeFromImage() {
        return null;
    }

}
