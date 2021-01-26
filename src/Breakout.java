/*
This file will eventually implement the game of Breakout.
 */
import acm.program.*;
import acm.graphics.*;

import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {
    /** Width and height of application window in pixels */
    public static final int APPLICATION_WIDTH = 400;
    public static final int APPLICATION_HEIGHT = 600;

    /** Dimensions of game board (usually the same) */
    private static final int WIDTH = APPLICATION_WIDTH;
    private static final int HEIGHT = APPLICATION_HEIGHT;

    /** Dimensions of the paddle */
    private static final int PADDLE_WIDTH = 60;
    private static final int PADDLE_HEIGHT = 10;

    /** Offset of the paddle up from the bottom */
    private static final int PADDLE_Y_OFFSET = 30;

    /** Number of bricks per row */
    private static final int NBRICKS_PER_ROW = 10;

    /** Number of rows of bricks */
    private static final int NBRICK_ROWS = 10;

    /** Separation between bricks */
    private static final int BRICK_SEP = 4;

    /** Width of a brick */
    private static final int BRICK_WIDTH =
            (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

    /** Height of a brick */
    private static final int BRICK_HEIGHT = 8;

    /** Radius of the ball in pixels */
    private static final int BALL_RADIUS = 10;

    /** Offset of the top brick row from the top */
    private static final int BRICK_Y_OFFSET = 70;

    /** Number of turns */
    private static final int NTURNS = 3;
    // runs the program
    public void run() {
        setup();
    }
    // initial setup
    private void setup() {
        makeBricks();
        makePaddle();
        addMouseListeners();
    }
    // makeing paddle
    private void makePaddle() {
        paddle = new GRect((WIDTH / 2) - (PADDLE_WIDTH / 2),HEIGHT - PADDLE_Y_OFFSET, PADDLE_WIDTH, PADDLE_HEIGHT);
        paddle.setFilled(true);
        add(paddle);
    }

    // makes bricks
    private void makeBricks() {
        Color color;
        for (int i = 0; i < NBRICK_ROWS; i++) {
            // changing color of each 2 rows
            if (i == 0 || i == 1) {
                color = Color.RED;
            } else if (i == 2 || i == 3) {
                color = Color.ORANGE;
            } else if (i == 4 || i == 5) {
                color = Color.YELLOW;
            } else if (i == 6 || i == 7) {
                color = Color.GREEN;
            } else {
                color = Color.BLUE;
            }
            // making all bricks
            for (int j = 0; j < NBRICKS_PER_ROW; j++) {
                // x position of brick
                int x = j * (BRICK_WIDTH + BRICK_SEP);
                // y position of brick
                int y = i * (BRICK_HEIGHT + BRICK_SEP) + BRICK_Y_OFFSET;

                //making a brick
                GRect brick = new GRect(x , y, BRICK_WIDTH, BRICK_HEIGHT);
                // setting color to brick
                brick.setFilled(true);
                brick.setColor(color);
                // adding to canvas
                add(brick);
            }
        }
    }
    // Called on mouse press to record the coordinates of the click */
    public void mousePressed(MouseEvent mouseEvent) {
        if (paddle.contains(mouseEvent.getX(), mouseEvent.getY())) {
            currentPosition = new GPoint(mouseEvent.getPoint());
        } else {
            currentPosition = null;
        }
    }
    // Called on mouse drag to reposition the object
    public void mouseDragged(MouseEvent mouseEvent) {
        if (currentPosition != null) {
            if (currentPosition.getX() > (WIDTH - PADDLE_WIDTH)) {
                currentPosition = new GPoint(paddle.getX() + PADDLE_WIDTH, paddle.getY());
            } else {
                paddle.move(mouseEvent.getX() - currentPosition.getX(), 0);
                currentPosition = new GPoint(mouseEvent.getPoint());
            }

        }
    }

    // instance variables
    private GRect paddle;
    private GPoint currentPosition;
}
