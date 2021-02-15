package genetic;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import app.Updatable;
import game.Grid;
import game.Snake;
import simulation.Clock;
import simulation.HeartBeat;
import simulation.KeyValue;

@Getter
@Slf4j
public class Population implements HeartBeat {

    private int populationSize;
    private int gridWidth;
    private int gridHeight;
    private boolean crossoverBest;

    private boolean bestFitnessSet;
    private int bestFitness;
    private int bestSteps;
    private int bestApples;

    private KeyValue currentFitness;
    private KeyValue currentSteps;
    private KeyValue currentApples;

    private int generations;
    private int snakesAlive;

    private ArrayList<Individual> individuals;

    private Clock clock;
    private int tickFast;
    private int tickSlow;
    private boolean simulationActive;

    private Updatable updatableObj;
    private Selection selection;

    @Builder
    public Population(int populationSize, int gridWidth, int gridHeight, boolean crossoverBest, int tickFast, int tickSlow) {
        this.populationSize = populationSize;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        this.crossoverBest = crossoverBest;
        this.tickFast = tickFast;
        this.tickSlow = tickSlow;

        individuals = new ArrayList<>(populationSize);
        selection = new Selection();
    }

    public void createFirstPopulation() {
        stopSimulation();
        individuals.clear();

        for (int i = 0; i < populationSize; i++) {
            individuals.add(getNewIndividual(null, i));
        }
    }

    public Individual getNewIndividual(double[] dna, int id) {
        Snake snake = Snake.builder()
                .startPosX(gridWidth / 2)
                .startPosY(gridHeight / 2)
                .life(50)
                .id(id)
                .build();

        if (dna != null) {
            snake.createSnake(false);
            snake.getHead().getBrain().setDNA(dna);
        } else {
            snake.createSnake(true);
        }

        Grid grid = Grid.builder()
                .width(gridWidth)
                .height(gridHeight)
                .snake(snake)
                .build();

        Individual individual = Individual.builder()
                .grid(grid)
                .snake(snake)
                .build();

        return individual;
    }

    public void setNewPopulation(ArrayList<Individual> individuals) {
        this.individuals.clear();
        this.individuals = individuals;
    }


    public void createHeartBeat () {
        clock = Clock.builder()
                .heartBeat(this)
                .tickFast(tickFast)
                .tickSlow(tickSlow)
                .build();
        clock.startClock();
        generations = 1;
    }

    @Override
    public void tick() {
        log.debug("tick() called");
        synchronized (this) {
            snakesAlive = 0;

            for (Individual individual : individuals) {
                individual.checkApple();
                if (!individual.getSnake().isAlive()) {
                    continue;
                }
                snakesAlive++;

                individual.checkEnvironment();
                individual.prepareNextMove();
                individual.validateNextMove();
                individual.takeNextMove();
                individual.validateMove();
            }
        }
        if (updatableObj != null) updatableObj.update();
        validateGeneration();

    }

    public void validateGeneration() {
        if (snakesAlive > 0) return;

        synchronized (this) {

            calcMaxValues();
            try {
                selection.createNewGeneration(this);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < populationSize; i++) {
                individuals.get(i).getSnake().setId(i);
            }
            generations++;
        }
     }

    public void setUpdatableObj (Updatable updatableObj) {
        this.updatableObj = updatableObj;
    }

    public void startSimulation() {
        stopSimulation();
        createFirstPopulation();
        createHeartBeat();
        simulationActive = true;
    }

    public void stopSimulation() {
        simulationActive = false;
        if (clock != null) clock.stopClock();

        bestFitness = 0;
        bestSteps = 0;
        bestApples = 0;

        currentFitness = new KeyValue();
        currentSteps = new KeyValue();
        currentApples = new KeyValue();

        snakesAlive = 0;
        generations = 0;
    }

    private void calcMaxValues() {
        bestFitnessSet = false;
        currentSteps = new KeyValue();
        currentApples = new KeyValue();
        currentFitness = new KeyValue();

        for (Individual individual : individuals) {
            if (individual.getSnake().getApplesCollected() > currentApples.getValueNumber()) {
                currentApples.setKey(individual.getSnake().getId());
                currentApples.setValueNumber(individual.getSnake().getApplesCollected());
            }
            if (individual.getSnake().getAllSteps() > currentSteps.getValueNumber()) {
                currentSteps.setKey(individual.getSnake().getId());
                currentSteps.setValueNumber(individual.getSnake().getAllSteps());
            }
            if (individual.getSnake().getFitness() > currentFitness.getValueNumber()) {
                currentFitness.setKey(individual.getSnake().getId());
                currentFitness.setValueNumber(individual.getSnake().getFitness());
            }
        }
        if (currentApples.getValueNumber() > bestApples) bestApples = currentApples.getValueNumber();
        if (currentSteps.getValueNumber() > bestSteps) bestSteps = currentSteps.getValueNumber();
        if (currentFitness.getValueNumber() > bestFitness) {
            bestFitness = currentFitness.getValueNumber();
            bestFitnessSet = true;
            dumpPopulation();
        }
    }

    public void switchSpeed() {
        clock.switchSpeed();
    }

    private void dumpPopulation() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        String strDate = dateFormat.format(date);

        String fileName = String.format("target/dump/dump_%s_%010d.pop", strDate, generations);
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName))));

            for (Individual individual: individuals) {
                double[] dna = individual.getSnake().getHead().getBrain().getDNA();
                int size = dna.length;

                StringBuilder str = new StringBuilder();
                str.append(dna[0]);
                for(int i = 1; i < size; i++) {
                    str.append(",");
                    str.append(dna[i]);
                }
                bw.write(str.toString());
                bw.newLine();
            }
            System.out.println(String.format("dump generation: %d", generations));
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadAndStartPopulation() {
        String fileName = String.format("target/dump/load_best.pop");
        ArrayList<Individual> individuals = new ArrayList<>(getPopulationSize());

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName))));

            String line = br.readLine();
            while (line != null && line.length() > 1) {
                String[] str = line.split(",");
                double[] doubleValues = Arrays.stream(str)
                        .mapToDouble(Double::parseDouble)
                        .toArray();

                Individual newIndividual = getNewIndividual(doubleValues, 0);
                individuals.add(newIndividual);
                line = br.readLine();
            }
            br.close();

            stopSimulation();
            setNewPopulation(individuals);
            createHeartBeat();
            simulationActive = true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
