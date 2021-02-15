package genetic;

import neuralnetwork.structure.NetworkModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class Selection {
    double[] bestDNA;
    Random rand = new Random();

    public void createNewGeneration(Population population) throws Exception {

        ArrayList<Individual> oldIndividuals = population.getIndividuals();
        ArrayList<Individual> newIndividuals = new ArrayList<>(population.getIndividuals().size());

        int populationSize = population.getPopulationSize();
        int killed = 0;

        try {
            Collections.sort(oldIndividuals);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (population.isBestFitnessSet()) {
            System.out.println(String.format("new best Fitness: %d - apples: %d - steps: %d",
                    oldIndividuals.get(0).getSnake().getFitness(),
                    population.getBestApples(),
                    population.getBestSteps()));
            bestDNA = oldIndividuals.get(0).getSnake().getHead().getBrain().getDNA();
        }

        for (int i = 0; i < populationSize / 2; i++) {
            oldIndividuals.remove(oldIndividuals.size() -1);
            killed++;
        }

        if (population.isBestFitnessSet()) {
            for (int id = 0; id < killed; id++) {
                double[] createDna;
                int newId = selectIndividual(oldIndividuals, 0);
                Individual newParent = oldIndividuals.get(newId);
                NetworkModel newBrain = newParent.getSnake().getHead().getBrain();
                double[] newDna = newBrain.getDNA();
                createDna = Crossover.createCrossoverDNA(bestDNA, newDna);

                if (rand.nextDouble() < 0.5) {
                    createDna = Mutation.createMutationDNA(createDna);
                }
                newIndividuals.add(population.getNewIndividual(createDna, id));
            }
        } else {
            for (int id = 0; id < killed; id++) {
                double[] createDna;
                int indId1 = selectIndividual(oldIndividuals, -1);
                int indId2 = selectIndividual(oldIndividuals, indId1);

                Individual newParent1 = oldIndividuals.get(indId1);
                Individual newParent2 = oldIndividuals.get(indId2);

                NetworkModel newBrain1 = newParent1.getSnake().getHead().getBrain();
                NetworkModel newBrain2 = newParent2.getSnake().getHead().getBrain();

                double[] newDna1 = newBrain1.getDNA();
                double[] newDna2 = newBrain2.getDNA();

                createDna = Crossover.createCrossoverDNA(newDna1, newDna2);

                if (rand.nextDouble() < 0.5) {
                    createDna = Mutation.createMutationDNA(createDna);
                }

                newIndividuals.add(population.getNewIndividual(createDna, id));
            }
        }

        // add old best individuals to new child generation
        for (Individual individual: oldIndividuals) {
            individual.resetIndividual();
            newIndividuals.add(individual);
        }
        population.setNewPopulation(newIndividuals);
    }

    public int selectIndividual(ArrayList<Individual> individuals, int firstInd) throws Exception {
        int result = 0;

        int fitnessSum = 0;
        int idx = 0;

        for (Individual individual: individuals) {
            if (idx != firstInd) {
                fitnessSum += Math.abs(individual.getSnake().getFitness());
            }
            idx++;
        }

        if (fitnessSum < 1) {
            throw new Exception("fitnessSum error: " + fitnessSum);
        }

        Random rand = new Random();
        int randSelect = rand.nextInt(fitnessSum);

        int runningSum = 0;
        idx = 0;
        for (int i = 0; i < individuals.size(); i++) {
            if (idx != firstInd) {
                runningSum += Math.abs(individuals.get(i).getSnake().getFitness());
                if (runningSum > randSelect) {
                    result = i;
                    break;
                }
            }
            idx++;
        }

        return result;
    }

    public void printDNA(double[] dna) {

        if (dna == null) return;

        StringBuilder str = new StringBuilder();
        str.append(String.format("DNA size: %d - ", dna.length));

        for (int i = 0; i < dna.length; i += 60) {
            str.append(String.format("%.2f ", dna[i]));
        }
        System.out.println(str.toString());
    }
}
