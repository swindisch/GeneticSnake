package genetic;

import java.util.Random;

public class Crossover {
    public static double[] createCrossoverDNA(double[] dna1, double[] dna2) {
        double[] newDna = new double[dna1.length];
        Random rand = new Random();

        for (int i = 0; i < dna1.length; i++) {
            double select = rand.nextDouble();

            if (select >= 0.5) {
                newDna[i] = dna1[i];
            } else {
                newDna[i] = dna2[i];
            }
        }
        return newDna;
    }
}
