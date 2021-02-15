package genetic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PopulationTest {

    @Test
    void createFirstPopulation() {
        Population population = Population.builder()
                .populationSize(100)
                .gridWidth(10)
                .gridHeight(10)
                .crossoverBest(false)
                .tickFast(1)
                .tickSlow(200)
                .build();

        population.createFirstPopulation();
        population.startSimulation();

        while (population.getGenerations() < 10000) {
            population.tick();
        }
    }

    @Test
    void loadAndStartPopulation() {
        Population population = Population.builder()
                .populationSize(100)
                .gridWidth(10)
                .gridHeight(10)
                .crossoverBest(false)
                .tickFast(1)
                .tickSlow(200)
                .build();

        population.createFirstPopulation();

        population.loadAndStartPopulation();

        while (population.getGenerations() < 30000) {
            population.tick();
        }
    }
}