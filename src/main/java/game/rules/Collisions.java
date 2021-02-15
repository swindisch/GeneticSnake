package game.rules;

import game.Grid;
import game.Snake;
import game.SnakeBody;
import game.SnakeHead;

import java.awt.*;

public class Collisions {
    public static boolean checkWall(Grid grid, Point position) {
        if (position.x < 0) return true;
        if (position.x >= grid.getWidth()) return true;

        if (position.y < 0) return true;
        if (position.y >= grid.getHeight()) return true;

        return false;
    }

    public static boolean checkApple(Point apple, Point position) {
        return (apple.equals(position));
    }

    public static boolean checkSnake(Snake snake, Point position) {
        for (SnakeBody body: snake.getBodyList()) {
            if (body.getPosition().equals(position)) return true;
        }
        return false;
    }
}
