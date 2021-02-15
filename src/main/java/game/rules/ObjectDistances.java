package game.rules;

import game.Grid;
import game.Snake;
import lombok.Data;

import java.awt.*;

@Data
public class ObjectDistances {

    private double[][] distanceField = new double[8][3];
    private double distanceApple = 0.0;
    private int distApple = 0;

    public ObjectDistances() {
    }

    public void calculateDistances(Grid grid, Snake snake) {
        Perspective perspective = new Perspective();
        int sightStep = 0;

        int appleSteps = Math.abs(grid.getApple().getPosition().x - snake.getHead().getPosition().x)
                + Math.abs(grid.getApple().getPosition().y - snake.getHead().getPosition().y);
        if (appleSteps > 0) {
            distApple = appleSteps;
            distanceApple = 1.0 / (double) appleSteps;  // / (double) grid.getWidth();
        }

        for (Point sight: perspective.getSightsList()) {
            Point startPoint = snake.getHead().getPosition().getLocation();
            int distance = 1;
            int distanceToApple = 0;
            int distanceToBody = 0;

            boolean bodyFound = false;
            boolean appleFound = false;

            startPoint.translate(sight.x, sight.y);

            while (!Collisions.checkWall(grid, startPoint)) {
                distance++;

                if (!appleFound && Collisions.checkApple(grid.getApple().getPosition(), startPoint)) {
                    distanceToApple = distance;
                    appleFound = true;
                }
                if (!bodyFound && Collisions.checkSnake(snake, snake.getHead().getPosition())) {
                    distanceToBody = distance;
                    bodyFound = true;
                }
                startPoint.translate(sight.x, sight.y);
            }

            distanceField[sightStep][0] = 1.0 / (double) distance;
            if (appleFound) distanceField[sightStep][1] = 1.0;// / (double) distanceToApple;
            if (bodyFound) distanceField[sightStep][2] = 1.0;// / (double) distanceToBody;

            sightStep++;
        }
    }
}
