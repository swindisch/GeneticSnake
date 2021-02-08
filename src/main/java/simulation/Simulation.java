package simulation;

import game.Apple;
import game.Direction;
import game.Snake;
import game.SnakeHead;

public interface Simulation {

    Apple getApple();

    Snake getSnake();

    int getGridWith();

    int getGridHeight();

    void setDirection(Direction dir);

    void startSimulation();

    void tickSimulation();

    int getAppleCollected();

    int getApplesMax();

    int getDeadCount();
}
