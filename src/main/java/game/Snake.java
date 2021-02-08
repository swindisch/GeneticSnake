package game;

import lombok.*;
import neuralnetwork.activationfunctions.ActivationEnum;
import neuralnetwork.structure.NetworkModel;
import simulation.Simulation;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

@Data
public class Snake {
    private SnakeHead head;
    private ArrayList<SnakeBody> bodyList;
    private boolean alive = false;
    private boolean moveBody = true;

    private NetworkModel brain;
    private int lifeSteps;
    private int allSteps;
    private int appleSteps;
    private int applesCollected;

    private double[] distanceWall;
    private double[] distanceApple;
    private double[] distanceBody;

    public void createSnake(int startX, int startY, int lifeSteps) {
        head = new SnakeHead();
        head.setPosX(startX);
        head.setPosY(startY);
        head.setDir(Direction.RIGHT);
        bodyList = new ArrayList<>();

        this.lifeSteps = lifeSteps;
        allSteps = 0;
        appleSteps = 0;
        applesCollected = 0;
        distanceWall = new double[Perspective.sights];
        distanceApple = new double[Perspective.sights];
        distanceBody = new double[Perspective.sights];

        int[] hiddenLayers = new int[2];
        hiddenLayers[0] = 64;
        hiddenLayers[1] = 32;

        ActivationEnum[] hiddenAcFun = new ActivationEnum[2];
        hiddenAcFun[0] = ActivationEnum.SIGMOID;
        hiddenAcFun[1] = ActivationEnum.SIGMOID;

        brain = new NetworkModel(28, hiddenLayers, hiddenAcFun, 4, ActivationEnum.LINEAR);
        brain.createModel();

        alive = true;
    }

    public void addBody() {
        SnakeBody body = new SnakeBody();
        body.setPosX(head.getPosX());
        body.setPosY(head.getPosY());
        bodyList.add(0, body);
        moveBody = false;
    }


    public void calcNextStep() {

        double[] inputArray = new double[24 + 4];
        double[] directionOneHot = new double[4];
        double[] output = null;

        switch (head.getDir()) {
            case UP:
                directionOneHot[0] = 1.0;
                break;
            case RIGHT:
                directionOneHot[1] = 1.0;
                break;
            case DOWN:
                directionOneHot[2] = 1.0;
                break;
            case LEFT:
                directionOneHot[3] = 1.0;
                break;
        }

        System.arraycopy(distanceWall, 0, inputArray, 0, 8);
        System.arraycopy(distanceApple, 0, inputArray, 8, 8);
        System.arraycopy(distanceBody, 0, inputArray, 16, 8);
        System.arraycopy(directionOneHot, 0, inputArray, 24, 4);

        output = brain.feedforward(inputArray);
        int max = 0;

        for (int i = 1; i < output.length; i++) {
            if (output[i] > max) max = i;
        }

        switch (max) {
            case 0:
                head.setDir(Direction.UP);
                break;
            case 1:
                head.setDir(Direction.RIGHT);
                break;
            case 2:
                head.setDir(Direction.DOWN);
                break;
            case 3:
                head.setDir(Direction.LEFT);
                break;
        }
    }

    public void move(Simulation sim)
    {
        lookAround(sim);


        calcNextStep();


        if (moveBody) {
            if (bodyList.size() > 1) {
                for (int i = bodyList.size() - 1;  i >= 1 ; i--) {
                    bodyList.get(i).setPosX(bodyList.get(i - 1).getPosX());
                    bodyList.get(i).setPosY(bodyList.get(i - 1).getPosY());
                }
            }
            if (bodyList.size() > 0) {
                bodyList.get(0).setPosX(head.getPosX());
                bodyList.get(0).setPosY(head.getPosY());
            }
        }
        else {
            moveBody = true;
        }

        // move head
        switch (head.getDir()) {
            case UP:
                head.setPosY(head.getPosY() - 1);
                break;
            case RIGHT:
                head.setPosX(head.getPosX() + 1);
                break;
            case DOWN:
                head.setPosY(head.getPosY() + 1);
                break;
            case LEFT:
                head.setPosX(head.getPosX() - 1);
                break;
        }

        allSteps++;
        appleSteps++;
    }

    private void lookAround(Simulation sim) {

        Perspective pers = new Perspective();
        int sightStep = 0;

        for (Point sight: pers.getSightsList()) {
            Point startPoint = new Point(getHead().getPosX(), getHead().getPosY());
            int distance = 0;
            int distanceToApple = 0;
            int distanceToBody = 0;

            boolean bodyFound = false;
            boolean appleFound = false;

            startPoint.x += sight.x;
            startPoint.y += sight.y;

            while (isInGrid(sim, startPoint)) {
                distance++;

                if (!appleFound && isInApple(sim, startPoint)) {
                    distanceToApple = distance;
                    appleFound = true;
                }
                if (!bodyFound && isInBody(sim, startPoint)) {
                    distanceToBody = distance;
                    bodyFound = true;
                }
                startPoint.x += sight.x;
                startPoint.y += sight.y;
            }

            distanceWall[sightStep] = 1.0 / (double) distance;
            if (appleFound) distanceApple[sightStep] = 1.0 / (double) distanceToApple;
            if (bodyFound) distanceBody[sightStep] = 1.0 / (double) distanceToBody;

            sightStep++;
        }
    }

    private boolean isInGrid(Simulation sim, Point pos) {
        if (pos.x < 0) return false;
        if (pos.x >= sim.getGridWith()) return false;

        if (pos.y < 0) return false;
        if (pos.y >= sim.getGridHeight()) return false;

        return true;
    }

    public boolean isInApple(Simulation sim, Point pos) {
        return (pos.x == sim.getApple().getPosX() && pos.y == sim.getApple().getPosY());
    }

    public boolean isInBody(Simulation sim, Point pos) {
        for (SnakeBody body: sim.getSnake().getBodyList()) {
            if (body.getPosX() == pos.x && body.getPosY() == pos.y) {
                return true;
            }
        }

        return false;
    }




    public double getFitness() {
        return (allSteps * 2 + applesCollected * 100 - appleSteps);
    }


}
