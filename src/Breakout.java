/*
This file will eventually implement the game of Breakout.
 */
import acm.program.*;
import acm.graphics.*;
import acm.util.RandomGenerator;

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
    private static final int BALL_RADIUS = 15;

    /** Offset of the top brick row from the top */
    private static final int BRICK_Y_OFFSET = 70;

    //dellay of timer
    private static final int DELAY = 30;

    /** Number of turns */
    private static final int NTURNS = 3;
    // runs the program
    public void run() {
        setup();
        while (brickCount > 0 && ball.getY() < HEIGHT - 2 * BALL_RADIUS) {
            moveBall();
            checkForCollision();
            pause(DELAY);
        }
        ball.setVisible(false);
        if (brickCount == 0) {
            GLabel label = new GLabel("You Won , Congratulations");
            label.setFont("Helvetica-24");
            label.setColor(Color.BLACK);
            add(label, (WIDTH - label.getWidth()) / 2, (HEIGHT - label.getAscent()) / 2);
        } else {
            GLabel label = new GLabel("Game over");
            label.setFont("Helvetica-24");
            label.setColor(Color.BLACK);
            add(label, (WIDTH - label.getWidth()) / 2, (HEIGHT - label.getAscent()) / 2);
        }

    }
    // initial setup
    private void setup() {
        // init of vx and vy
        vx = rgen.nextDouble(3,6);
        if (rgen.nextBoolean(0.5)) vx = -vx;
        vy = 4;
        makeBricks();
        makePaddle();
        makeBall();
        addMouseListeners();
    }

    private void makeBall() {
        ball = new GOval((getWidth() - BALL_RADIUS ) / 2, (getHeight() - BALL_RADIUS) / 2, BALL_RADIUS, BALL_RADIUS);
        ball.setFilled(true);
        add(ball);
    }
    // making paddle
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
            // all bricks counting
            brickCount = NBRICK_ROWS * NBRICKS_PER_ROW;
        }
    }
    //find if there is collision and update velocities
    private void checkForCollision() {
        // if ball is touched with edges
        if (ball.getY() > HEIGHT - 2 * BALL_RADIUS) {
            vy = - vy;
        } else if (ball.getX() > WIDTH - 2 * BALL_RADIUS) {
            vx = - vx;
        } else if (ball.getX() < 0) {
            vx = -vx;
        } else if (ball.getY() < 0) {
            vy = -vy;
        }
        // if ball touched paddle
        GObject collider = getCollidingObject();
        if (collider == paddle) {
            vy = -1.01 * vy;
        } else if (collider != null) {
            vy = -vy;
            remove(collider);
            brickCount -= 1;
        }

    }
    // returns the object which was touched with ball
    private GObject getCollidingObject() {
        // cordinates of balls upper left corner
        double x = ball.getX();
        double y = ball.getY();
        GObject element = getElementAt(x,y);

        if (element == null) {
            element = getElementAt(x + 2 * BALL_RADIUS,y);
            if (element == null) {
                element = getElementAt(x,2 * BALL_RADIUS + y);
                if (element == null) {
                    element = getElementAt(x + 2 * BALL_RADIUS,2 * BALL_RADIUS + y);
                }
            }
        }

        return  element;
    }

    //moving ball
    private void moveBall() {
        ball.move(vx, vy);
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
            if (paddle.getX() > (WIDTH - PADDLE_WIDTH)) {
                paddle.setLocation(paddle.getX() - 5, paddle.getY());
            } else if (paddle.getX() < 0) {
                paddle.setLocation( 5, paddle.getY());
            } else {
                paddle.move(mouseEvent.getX() - currentPosition.getX(), 0);
                currentPosition = new GPoint(mouseEvent.getPoint());
            }

        }
    }

    // instance variables
    private GRect paddle;
    private GOval ball;

    // x and y velocities
    private double vx, vy;

    // quantity of bricks
    private int brickCount;

    // random generator
    private RandomGenerator rgen = RandomGenerator.getInstance();

    private GPoint currentPosition;
}
