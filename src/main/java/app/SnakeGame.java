package app;

import genetic.Population;

public class SnakeGame {

    private static int width = 1200;
    private static int height = 1000;

    private static int xOffset = 30;
    private static int yOffset = 30;

    private static int cellSize = 10;

    private static int populationSize = 10;
    private static boolean crossoverBest = true;

    private static int tickFast = 1;
    private static int tickSlow = 100;


    public static void main(String[] args) {

        Population population = Population.builder()
                .populationSize(populationSize)
                .gridWidth(cellSize)
                .gridHeight(cellSize)
                .crossoverBest(crossoverBest)
                .tickFast(tickFast)
                .tickSlow(tickSlow)
                .build();

        population.createFirstPopulation();

        MainFrame app = new MainFrame(population);
        app.createWindow(width, height, xOffset, yOffset, cellSize);
        population.setUpdatableObj(app);
    }
}
