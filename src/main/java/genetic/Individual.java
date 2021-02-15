package genetic;

import game.Grid;
import game.Snake;
import game.rules.Collisions;
import game.rules.ObjectDistances;
import lombok.Builder;
import lombok.Getter;

import java.awt.*;
import java.security.InvalidParameterException;

@Getter
@Builder
public class Individual implements Comparable<Individual> {

    private Snake snake;
    private Grid grid;
    private ObjectDistances distances;
    private int fitness;

    public void checkApple() {
        if (!grid.isAppleExist()) {
            if (snake.getVictory() == 0) {
                grid.createApple();
            }
        }
    }

    public void checkEnvironment() {
        if (distances == null) distances = new ObjectDistances();
        distances.calculateDistances(grid, snake);
    }

    public void prepareNextMove() {
        snake.prepareNextMove(distances);
    }

    public void validateNextMove() {
        if (snake.getAppleSteps() == snake.getLife()) {
            snake.setAlive(false);
            return;
        }

        Point lookHead = snake.lookHead();
        if (Collisions.checkWall(grid, lookHead) || Collisions.checkSnake(snake, lookHead)) {
            snake.setAlive(false);
            return;
        }
    }

    public void takeNextMove() {
        if (!snake.isAlive()) return;
        snake.takeNextMove();
        fitness = snake.getFitness();
    }

    public boolean validateMove() {
        if (snake.getAppleSteps() > snake.getLife()) {
            snake.setAlive(false);
            return false;
        }

        if (Collisions.checkWall(grid, snake.getHead().getPosition()) || Collisions.checkSnake(snake, snake.getHead().getPosition())) {
            snake.setAlive(false);
            return false;
        }

        if (Collisions.checkApple(grid.getApple().getPosition(), snake.getHead().getPosition()))
        {
            snake.setApplesCollected(snake.getApplesCollected() + 1);
            snake.addBody();
            snake.setAppleSteps(0);
            grid.deleteApple();
        }

        if (snake.getBodyList().size() == 100) {
            snake.setVictory(1);
            snake.setAlive(false);
            return true;
        }
        return false;
    }

    public void resetIndividual() {
        snake.resetSnake();
        grid.resetGrid();
        fitness = 0;
    }

    @Override
    public int compareTo(Individual o) {
        if (o == null) return 0;

        int myFitness = this.getSnake().getFitness();
        int otherFitness = o.getSnake().getFitness();

        return otherFitness - myFitness;
    }
}
