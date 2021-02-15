package simulation;

import lombok.Builder;

public class Clock {

    enum TickType {
        FAST,
        SLOW
    }

    private boolean running;
    private int tickFast;
    private int tickSlow;
    private int tick;
    private TickType tickType;
    private HeartBeat heartBeat;

    @Builder
    public Clock(HeartBeat heartBeat, int tickFast, int tickSlow) {
        this.heartBeat = heartBeat;
        this.tickFast = tickFast;
        this.tickSlow = tickSlow;

        this.tick = tickFast;
        this.tickType = TickType.FAST;
    }

    public void startClock() {
        System.out.println("startClock()");
        running = true;

        new Thread(new Runnable() {

            public void run() {
                long simulationLastMillis = System.currentTimeMillis() + tick; // initial

                while (running) {
                    if (System.currentTimeMillis() - simulationLastMillis > tick) {
                        simulationLastMillis += tick;

                        synchronized (heartBeat) {
                            heartBeat.tick();
                        }
                    }
                }
                System.out.println("thread finished");
            }
        }).start();
    }

    public void stopClock() {
        System.out.println("stopClock()");
        running = false;
    }

    public void switchSpeed () {
        if (tickType == TickType.FAST) {
            tickType = TickType.SLOW;
            tick = tickSlow;
        } else {
            tickType = TickType.FAST;
            tick = tickFast;
        }
    }
}
