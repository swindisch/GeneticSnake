package genetic;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelectionTest {

    @Test
    void createNewGeneration() {
        Population population = Population.builder()
                .populationSize(10)
                .gridWidth(10)
                .gridHeight(10)
                .tickFast(10)
                .tickSlow(200)
                .build();

        population.createFirstPopulation();

        Selection selection = new Selection();
        try {
            selection.createNewGeneration(population);
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(10, population.getIndividuals().size());
    }
}