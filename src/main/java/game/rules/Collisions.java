package game.rules;

import game.SnakeBody;
import game.SnakeHead;
import simulation.Simulation;
import simulation.SnakeSimulation;

public class Collisions {
    public static boolean checkWall(Simulation sim) {
        SnakeHead head = sim.getSnake().getHead();

        if (head.getPosX() < 0) return true;
        if (head.getPosX() >= sim.getGridWith()) return true;

        if (head.getPosY() < 0) return true;
        if (head.getPosY() >= sim.getGridHeight()) return true;

        return false;
    }

    public static boolean checkApple(SnakeSimulation sim) {
        SnakeHead head = sim.getSnake().getHead();
        return (head.getPosX() == sim.getApple().getPosX() && head.getPosY() == sim.getApple().getPosY());
    }

    public static boolean checkSnake(SnakeSimulation sim) {
        SnakeHead head = sim.getSnake().getHead();

        for (SnakeBody body: sim.getSnake().getBodyList()) {
            if (body.getPosX() == head.getPosX() && body.getPosY() == head.getPosY()) return true;
        }

        return false;
    }
}
