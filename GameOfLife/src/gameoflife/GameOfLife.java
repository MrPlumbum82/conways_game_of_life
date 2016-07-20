package gameoflife;

/**
 * A sort of ecological simulation played on a 2D grid. Each cell is in one of
 * two possible states -- living or dead. The state of a cell in the next
 * generation is based on a simple local rule:
 *
 * 1. Fewer the 2 living neighbors = death 2. More then 2 living neighbors =
 * death 3. Exactly 2 living neighbors = remain in current state 4. Exactly 3
 * living neighbors = life
 *
 * @author Sadan Mallhi
 * @version Apr 10, 2014
 */
public class GameOfLife {

    private boolean[][] world; // true = alive

    /**
     * Initializes the world at all dead cells
     *
     * @param n size of the grid
     */
    public GameOfLife(int n) {
        world = new boolean[n][n];
    }

    /**
     * Update the world to the next generation
     */
    public void nextGeneration() {
        int n = world.length;
        boolean[][] temp = new boolean[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int k = numberLivingNeighbors(i, j);
                switch (k) {
                    case 2:
                        temp[i][j] = world[i][j];
                        break;
                    case 3:
                        temp[i][j] = true;
                        break;
                    default:
                        temp[i][j] = false;
                }
            }
        }
        world = temp;
    }

    /**
     * @param x a row
     * @param y a column
     * @return the number of living neighbors of cell (x,y)
     */
    private int numberLivingNeighbors(int x, int y) {
        int count = 0;
        for (int i = x - 1; i <= x + 1; i++) {
            for (int j = y - 1; j <= y + 1; j++) {
                if (i == x && j == y) {
                    continue;
                }
                if (isAlive(i, j)) {
                    count++;
                }
            }
        }
        return count;
    }

    // the following methods are needed for GUI
    /**
     *
     * @return the number of rows/columns
     */
    public int getSize() {
        return world.length;
    }

    /**
     * Destroys all life
     */
    public void clear() {
        randomize(0);
    }

    /**
     * Set each cell's state at random
     *
     * @param p probability of a cell being alive
     */
    public void randomize(double p) {
        for (boolean[] world1 : world) {
            for (int j = 0; j < world1.length; j++) {
                world1[j] = Math.random() < p;
            }
        }
    }

    /**
     * @param x a row
     * @param y a column
     * @return true if cell (x,y) is alive
     */
    public boolean isAlive(int x, int y) {
        int n = world.length;
        if (x < 0 || x >= n || y < 0 || y >= n) {
            return false;
        }

        return world[x][y];
    }

    /**
     * Toggles a cell.
     *
     * @param x the row
     * @param y the column
     */
    public void toggle(int x, int y) {
        world[x][y] = !world[x][y];
    }

    /**
     * Sets a cell's state to living
     *
     * @param x the row
     * @param y the column
     */
    public void setLiving(int x, int y) {
        world[x][y] = true;
    }

    /**
     * Sets a cell's state to dead.
     *
     * @param x
     * @param y
     */
    public void setDead(int x, int y) {
        world[x][y] = false;
    }
}
