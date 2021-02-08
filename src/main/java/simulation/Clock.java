package simulation;

public class Clock {

    public static boolean running = true;
    public static int tick = 200;
    private Simulation sim;

    public Clock(Simulation sim) {
        this.sim = sim;
    }


    public void startClock() {

        new Thread(new Runnable() {
            private long simulationLastMillis;

            public void run() {
                simulationLastMillis = System.currentTimeMillis() + 100; // initial

                while (running) {
                    if (System.currentTimeMillis() - simulationLastMillis > tick) {
                        simulationLastMillis += tick;

                        synchronized (sim) {
                            sim.tickSimulation();
                        }
                    }
                }
            }
        }).start();
    }
}
