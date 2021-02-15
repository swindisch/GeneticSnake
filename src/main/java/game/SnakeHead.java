package game;

import game.rules.Direction;
import game.rules.ObjectDistances;
import game.rules.Perspective;
import lombok.Data;
import neuralnetwork.activationfunctions.ActivationEnum;
import neuralnetwork.structure.NetworkModel;

import java.awt.*;

@Data
public class SnakeHead {
    private Point position;
    private NetworkModel brain;
    private Direction dir = Direction.NOP;
    private int distanceApple = 0;
    private int inputSize;

    public void createBrain(boolean randomBrain) {
        int[] hiddenLayers = new int[2];
        hiddenLayers[0] = 32;
        hiddenLayers[1] = 16;
//        hiddenLayers[2] = 8;

        ActivationEnum[] hiddenAcFun = new ActivationEnum[2];
        hiddenAcFun[0] = ActivationEnum.RLU;
        hiddenAcFun[1] = ActivationEnum.RLU;
  //      hiddenAcFun[2] = ActivationEnum.RLU;

        inputSize = 30;
        brain = new NetworkModel(inputSize, hiddenLayers, hiddenAcFun, 4, ActivationEnum.SIGMOID);
        brain.createModel(randomBrain, false, 0.0);
    }

    public void prepareNextMove(ObjectDistances distances, int life, int steps) {
        double[] inputArray = new double[inputSize];
        double[] directionOneHot = new double[4];
        double[] output;

        double lifeNorm = ((double)(life - steps)) / ((double) life);

        switch (getDir()) {
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

        int offset = 0;

        for (int i = 0; i < Perspective.sights; i++) {
            double[][] field = distances.getDistanceField();
            inputArray[offset++] = field[i][0];
            inputArray[offset++] = field[i][1];
            inputArray[offset++] = field[i][2];
        }
        System.arraycopy(directionOneHot, 0, inputArray, 24, 4);
        inputArray[28] = distances.getDistanceApple();
        inputArray[29] = lifeNorm;

        distanceApple = distances.getDistApple();

        //output = activation(brain.feedforward(inputArray));
        output = brain.feedforward(inputArray);


        int maxIdx = 0;
        double maxValue = output[0];

        for (int i = 1; i < output.length; i++) {
            if (output[i] > maxValue) {
                maxIdx = i;
                maxValue = output[i];
            }
        }

        switch (maxIdx) {
            case 0:
                setDir(Direction.UP);
                break;
            case 1:
                setDir(Direction.RIGHT);
                break;
            case 2:
                setDir(Direction.DOWN);
                break;
            case 3:
                setDir(Direction.LEFT);
                break;
        }
    }

    public double[] activation(double[] input) {
        double[] exp = new double[input.length];
        double sum = 0;
        for(int neuron = 0; neuron < exp.length; neuron++) {
            exp[neuron] = Math.exp(input[neuron]);
            sum += exp[neuron];
        }

        double[] output = new double[input.length];
        for(int neuron = 0; neuron < output.length; neuron++) {
            output[neuron] = exp[neuron] / sum;
        }

        return output;
    }
}
