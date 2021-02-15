package genetic;

import java.util.Random;

public class Mutation {
    public static double[] createMutationDNA(double[] dna) {
        double[] newDna = new double[dna.length];
        Random rand = new Random();

        int mutated = 0;

        for (int i = 0; i < dna.length; i++) {

            if (rand.nextDouble() < 0.1) {
                mutated++;
                double nd = rand.nextGaussian() * 0.2;
                newDna[i] = dna[i] + nd;
            } else {
                newDna[i] = dna[i];
            }
        }

        //System.out.println(String.format("DNA: %d - mutated: %d", dna.length, mutated));
        return newDna;
    }
}
