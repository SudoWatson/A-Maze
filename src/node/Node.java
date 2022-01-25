package node;

import static main.Main.sketch;

import processing.core.PConstants;

/**
 * A single node, which has a "distance" from start, "distance" to end, and total "distance" from start to end.
 * Note that "distance" is completely relative to the hueristic function used in the AStarAlgo(rithm) 
 */
public class Node {

    /** Remaining "distance" to end */
    public double h = Double.POSITIVE_INFINITY;
    /** "Distance" from start */
    public double g = Double.POSITIVE_INFINITY;
    /** Total "Distance" */
    public double f = Double.POSITIVE_INFINITY;

    /** Previous node in path to end */
    public Node prevNode = null;

    public boolean isWall = false;

    /** Positional data */
    private int i,j,width,height;


    public Node(int i, int j, int width, int height) {
        this.i = i;
        this.j = j;
        this.width = width;
        this.height = height;
    }

    public void draw(int[] color) {
        sketch.pushMatrix();
        sketch.translate(this.i*this.width, this.j*this.height);
        sketch.stroke(0);
        sketch.fill(color[0],color[1],color[2]);
        if (this.isWall) sketch.fill(0);
        sketch.rect(0, 0, this.width, this.height);
        if (main.Main.DEBUG) {
            sketch.fill(0);
            sketch.textSize(12.0f);
            sketch.textAlign(PConstants.CENTER, PConstants.CENTER);
            sketch.text((float) this.f, this.width/2, this.height/2);

            sketch.textSize(8.0f);
            sketch.text((float) this.g, this.width/4, this.height/4);
            sketch.text((float) this.h, this.width*3/4, this.height*3/4);
        }
        sketch.popMatrix();
    }

    public int getI() {
        return this.i;
    }

    public int getJ() {
        return this.j;
    }


}
