package app;

import genetic.Population;
import simulation.KeyValue;

import javax.swing.*;
import java.awt.*;

public class PopulationLabel extends JComponent {

    Population population;

    public PopulationLabel(Population population) {
        super();
        this.population = population;
    }

    protected void paintComponent(Graphics g) {

        synchronized (population) {

            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN,12));
            g.drawString("Population: " + population.getSnakesAlive(), 10,20);
            g.drawString("Generation: " + population.getGenerations(), 10,35);

            int bestSteps = population.getBestSteps();
            int bestApple = population.getBestApples();
            int bestFitness = population.getBestFitness();

            KeyValue maxSteps = population.getCurrentSteps();
            KeyValue maxApples = population.getCurrentApples();
            KeyValue maxFitness = population.getCurrentFitness();

            g.drawString(String.format("BestSteps: %d CurrentSteps: %d (%d)", bestSteps, maxSteps.getValueNumber(), maxSteps.getKey()), 10,60);
            g.drawString(String.format("BestApples: %d CurrentApples: %d (%d)", bestApple, maxApples.getValueNumber(), maxApples.getKey()), 10,75);
            g.drawString(String.format("BestFitness: %d CurrentFitness: %d (%d)", bestFitness, maxFitness.getValueNumber(), maxFitness.getKey()), 10,90);
        }
    }
}
