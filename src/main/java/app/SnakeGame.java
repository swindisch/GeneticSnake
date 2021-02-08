package app;

import simulation.Clock;
import simulation.Simulation;
import simulation.SnakeSimulation;

import java.util.ArrayList;

public class SnakeGame {

    private static int width = 1200;
    private static int height = 1000;

    private static int xOffset = 120;
    private static int yOffset = 100;

    private static int cellSize = 10;

    private static int numSims = 10;

    public static void main(String[] args) {

        ArrayList<Simulation> simList = new ArrayList<>();

        for (int i = 0; i < numSims; i++) {
            Simulation sim = new SnakeSimulation(10, 10, 50);
            Clock clock = new Clock(sim);
            simList.add(sim);
            sim.startSimulation();
            clock.startClock();
        }

        MainFrame app = new MainFrame(simList);
        app.createWindow(width, height, xOffset, yOffset, cellSize);
    }
}
