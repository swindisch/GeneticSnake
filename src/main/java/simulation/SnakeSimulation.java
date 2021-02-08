package simulation;

import game.Apple;
import game.Direction;
import game.Snake;
import game.SnakeBody;
import game.rules.Collisions;

import java.util.Random;

public class SnakeSimulation implements Simulation {

    int gridWidth;
    int gridHeight;
    int snakeStartX;
    int snakeStartY;

    Apple apple;
    Snake snake;
    Random rand;

    int lifeSteps;

    int deadCount;
    int applesMax;


    public SnakeSimulation(int gridWidth, int gridHeight, int lifeSteps) {
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.lifeSteps = lifeSteps;
        this.apple = null;
        rand = new Random(42);

        this.snakeStartX = gridWidth / 2;
        this.snakeStartY = gridHeight / 2;

        deadCount = 0;
        applesMax = 0;
    }


    @Override
    public Apple getApple() {
        return apple;
    }

    @Override
    public Snake getSnake() {
        return snake;
    }

    @Override
    public int getGridWith() {
        return gridWidth;
    }

    @Override
    public int getGridHeight() {
        return gridHeight;
    }

    @Override
    public void setDirection(Direction dir) {

        switch (snake.getHead().getDir()) {
            case UP:
                if (dir == Direction.DOWN) return;
                break;
            case RIGHT:
                if (dir == Direction.LEFT) return;
                break;
            case DOWN:
                if (dir == Direction.UP) return;
                break;
            case LEFT:
                if (dir == Direction.RIGHT) return;
                break;
        }
        snake.getHead().setDir(dir);
    }

    @Override
    public void startSimulation() {

        snake = new Snake();
        snake.createSnake(snakeStartX, snakeStartY, lifeSteps);
        snake.setApplesCollected(0);
        apple = getNewApple();
    }

    public Apple getNewApple() {
        int startX;
        int startY;

        boolean inSnake = false;

        do {
            inSnake = false;
            startX = rand.nextInt(gridWidth);
            startY = rand.nextInt(gridHeight);

            if (startX == snake.getHead().getPosX() && startY == snake.getHead().getPosY()) {
                inSnake = true;
                continue;
            }
            for (SnakeBody body: snake.getBodyList()) {
                if (startX == body.getPosX() && startY == body.getPosY()) {
                    inSnake = true;
                    continue;
                }
            }
        } while (inSnake);

        return (new Apple(startX, startY));
    }

    @Override
    public void tickSimulation() {
        if (!getSnake().isAlive()) return;

        getSnake().move(this);

        if (snake.getAllSteps() > lifeSteps)
        {
            deadCount++;
            if (snake.getApplesCollected() > applesMax) {
                applesMax = snake.getApplesCollected();
                getSnake().setAlive(false);
            }
            startSimulation();
            return;
        }

        if (Collisions.checkWall(this) || Collisions.checkSnake(this)) {
            deadCount++;
            if (snake.getApplesCollected() > applesMax) {
                applesMax = snake.getApplesCollected();
                getSnake().setAlive(false);
            }
            startSimulation();
            return;
        }

        if (Collisions.checkApple(this))
        {
            snake.setApplesCollected(snake.getApplesCollected() + 1);
            apple = getNewApple();
            getSnake().addBody();
            getSnake().setAppleSteps(0);
        }




    }

    @Override
    public int getAppleCollected() {
        return snake.getApplesCollected();
    }

    @Override
    public int getApplesMax() {
        return applesMax;
    }

    @Override
    public int getDeadCount() {
        return deadCount;
    }
}
