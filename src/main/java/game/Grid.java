package game;

import lombok.Builder;
import lombok.Getter;

import java.awt.*;
import java.util.Random;

@Getter
@Builder
public class Grid {
    private int width;
    private int height;
    private Snake snake;


    private Apple apple;
    private boolean appleExist;

    public void createApple() {
        Random rand = new Random();
        boolean inSnake;
        Point position;

        do {
            inSnake = false;
            position = new Point(rand.nextInt(width), rand.nextInt(height));

            if (position.equals(snake.getHead().getPosition())) {
                inSnake = true;
                continue;
            }
            for (SnakeBody body: snake.getBodyList()) {
                if (position.equals(body.getPosition())) {
                    inSnake = true;
                    continue;
                }
            }
        } while (inSnake);

        apple = new Apple(position);
        appleExist = true;
    }

    public void deleteApple() {
        apple = null;
        appleExist = false;
    }

    public void resetGrid() {
        deleteApple();
    }
}
