package gameoflife;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * Displays Game of Life in a bounded grid. States of cells can be toggled via
 * mouse clicks or by mouse dragging.
 *
 * @author Sadan Mallhi
 * @version Apr 10, 2014
 */
public class LifeComponent extends JComponent {

    private GameOfLife game;

    // fields for continuouse updating
    private boolean inContinuouseMode;
    private int delay;
    public static final int SLOW = 500;
    public static final int MEDIUM = 250;
    public static final int FAST = 1;

    // for drawing
    private static final int LINE_THICKNESS = 1;
    private static final int OFFSET = 15;
    private static final Color bgColor = Color.BLACK;
    private static final Color livingColor = Color.RED;
    private static final Color lineColor = Color.GRAY;

    private int cellWidth;
    private int cellHeight;

    /**
     * Creates a new game and installs a mouse listener to toggle the states of
     * cells.
     *
     * @param n
     */
    public LifeComponent(int n) {
        game = new GameOfLife(n);
        inContinuouseMode = false;
        delay = MEDIUM;

        MouseAdapter myAdapter = getMyMouseAdapter();
        addMouseListener(myAdapter);
        addMouseMotionListener(myAdapter);
    }

    private MouseAdapter getMyMouseAdapter() {
        class Listener extends MouseAdapter {

            boolean turnOnWhenDragging = true;

            // find the (row, col) of the cell where the
            // mouse cursor is.
            /**
             *
             * @param cursor the current mouse cursor position
             * @return the position of the corresponding game square
             */
            private Point getCellContainingCursor(Point cursor) {
                final int N = game.getSize();
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        // find top=left corner of cell (i,j)
                        int x = OFFSET + j * cellWidth;
                        int y = OFFSET + i * cellHeight;
                        Rectangle r = new Rectangle(x, y, cellWidth, cellHeight);
                        if (r.contains(cursor)) {
                            // row-i col-j of game selected
                            return new Point(i, j);

                        }
                    }
                }
                return null;
            }

            @Override
            public void mousePressed(MouseEvent e) {
                Point currentPoint = new Point(e.getX(), e.getY());
                Point p = getCellContainingCursor(currentPoint);
                if (p == null) {
                    return;
                }
                int i = (int) p.getX();
                int j = (int) p.getY();
                turnOnWhenDragging = !game.isAlive(i, j);
                game.toggle(i, j);
                inContinuouseMode = false;
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point currentPoint = new Point(e.getX(), e.getY());
                Point p = getCellContainingCursor(currentPoint);
                if (p == null) {
                    return;
                }
                int i = (int) p.getX();
                int j = (int) p.getY();
                if (turnOnWhenDragging) {
                    game.setLiving(i, j);

                } else {
                    game.setDead(i, j);
                }
                inContinuouseMode = false;
                repaint();
            }
        }
        return new Listener();
    }

    /**
     * Draws the Life grid.
     *
     * @param g the current graphics content
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        
        // fill in background
        g2.setColor(bgColor);
        int w = getWidth();
        int h = getHeight();
        g2.fill(new Rectangle(w, h));

        // draw the grid
        g2.setStroke(new BasicStroke(LINE_THICKNESS));
        final int N = game.getSize();
        cellWidth = (w - 2 * OFFSET) / N;
        cellHeight = (h - 2 * OFFSET) / N;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {

                // find top-left corner of cell(i,j)
                int a = OFFSET + j * cellWidth;
                int b = OFFSET + i * cellHeight;

                if (game.isAlive(i, j)) {
                    g2.setColor(livingColor);

                } else {
                    g2.setColor(bgColor);
                }
                g2.fillRect(a, b, cellWidth, cellHeight);
                g2.setColor(lineColor);
                g2.drawRect(a, b, cellWidth, cellHeight);

            }
        }

        if (inContinuouseMode) {
            try {
                game.nextGeneration();
                Thread.sleep(delay);
                repaint();
            } catch (InterruptedException ex) {
                Logger.getLogger(
                        LifeComponent.class.getName()).log(Level.SEVERE,
                                null, ex);
            }
        }
    }

    /**
     * Destroy all life.
     */
    public void clear() {
        game.clear();
        inContinuouseMode = false;
        repaint();
    }

    /**
     * Toggles the state of a cell
     *
     * @param row
     * @param col
     */
    public void toggle(int row, int col) {
        game.toggle(row, col);
    }

    /**
     * Randomize each cell.
     *
     * @param p probability of a cell being alive
     */
    public void randomize(double p) {
        game.randomize(p);
        inContinuouseMode = false;
        repaint();
    }

    /**
     * go into continues updating mode
     */
    public void run() {
        inContinuouseMode = true;
        game.nextGeneration();
        repaint();
    }

    /**
     * Stops continuously updating generations.
     */
    public void stop() {
        inContinuouseMode = false;
    }

    /**
     * Updating the world to the next generation.
     */
    public void nextGeneration() {
        game.nextGeneration();
        inContinuouseMode = false;
        repaint();

    }

    /**
     * Sets the delay between successive generations.
     *
     * @param delay number of milliseconds
     */
    public void setDelay(int delay) {
        this.delay = delay;
    }
    
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 680);
        
        frame.add(new LifeComponent(100));
        
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
    }
}
