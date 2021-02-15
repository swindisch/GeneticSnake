package game.rules;

import java.util.Random;

public enum Direction {
    NOP,
    UP,
    RIGHT,
    DOWN,
    LEFT;

    public static Direction random() {

        Random rand = new Random();
        int dir = rand.nextInt(4) + 1;
        switch (dir) {
            case 1:
                return UP;
            case 2:
                return RIGHT;
            case 3:
                return DOWN;
            case 4:
                return LEFT;
        }
        return NOP;
    }
}
