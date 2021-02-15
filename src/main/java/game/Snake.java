package game;

import game.rules.Direction;
import game.rules.ObjectDistances;
import lombok.*;

import java.awt.*;
import java.util.ArrayList;

@Data
public class Snake {

   private Point startPosition;
    private SnakeHead head;
    private ArrayList<SnakeBody> bodyList;
    private boolean alive = false;
    private boolean pauseBody = false;

    private int life;
    private int allSteps;
    private int appleSteps;
    private int applesCollected;
    private int id;
    private int victory;



    @Builder
    public Snake(int startPosX, int startPosY, int life, int id) {
        this.startPosition = new Point(startPosX, startPosY);
        this.life = life;
        this.id = id;
    }


    public void createSnake(boolean randomBrain) {
        head = new SnakeHead();
        resetSnake();
        head.createBrain(randomBrain);
    }

    public void resetSnake() {
        head.setPosition(startPosition.getLocation());
        head.setDir(Direction.random());

        bodyList = new ArrayList<>();
        addBody();

        allSteps = 0;
        appleSteps = 0;
        applesCollected = 0;
        victory = 0;
        alive = true;
    }

    public void addBody() {
        SnakeBody body = new SnakeBody();
        body.setPosition(head.getPosition().getLocation());
        bodyList.add(0, body);
        pauseBody = true;
    }

    public void prepareNextMove(ObjectDistances distances) {
        if (!alive) return;
        head.prepareNextMove(distances, life, appleSteps);
    }

    public void takeNextMove() {
        if (!alive) return;

        moveBody();
        moveHead();

        allSteps++;
        appleSteps++;
    }

    public void moveBody() {
        if (pauseBody) {
            pauseBody = false;
            return;
        }

        if (bodyList.size() > 1) {
            for (int i = bodyList.size() - 1;  i >= 1 ; i--) {
                bodyList.get(i).setPosition(bodyList.get(i - 1).getPosition().getLocation());
            }
        }
        if (bodyList.size() > 0) {
            bodyList.get(0).setPosition(head.getPosition().getLocation());
        }
    }

    public void moveHead() {
        // move head
        switch (head.getDir()) {
            case UP:
                head.getPosition().translate(0, -1);
                break;
            case RIGHT:
                head.getPosition().translate(1, 0);
                break;
            case DOWN:
                head.getPosition().translate(0, 1);
                break;
            case LEFT:
                head.getPosition().translate(-1, 0);
                break;
        }
    }

    public Point lookHead() {
        Point look = head.getPosition().getLocation();
        switch (head.getDir()) {
            case UP:
                look.translate(0, -1);
                break;
            case RIGHT:
                look.translate(1, 0);
                break;
            case DOWN:
                look.translate(0, 1);
                break;
            case LEFT:
                look.translate(-1, 0);
                break;
        }
        return look;
    }

    public int getFitness() {
        return Math.max(allSteps * 2 + applesCollected * 100 - appleSteps - getHead().getDistanceApple() + victory * 1000, 1);
    }
}
