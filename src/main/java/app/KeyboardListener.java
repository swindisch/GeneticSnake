package app;

import game.Direction;
import lombok.Getter;
import simulation.Simulation;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Getter
public class KeyboardListener implements KeyListener {

    private int keyCode = 0;
    private Simulation sim;

    public KeyboardListener(Simulation sim) {
        this.sim = sim;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_W:
                sim.setDirection(Direction.UP);
                break;
            case KeyEvent.VK_D:
                sim.setDirection(Direction.RIGHT);
                break;
            case KeyEvent.VK_S:
                sim.setDirection(Direction.DOWN);
                break;
            case KeyEvent.VK_A:
                sim.setDirection(Direction.LEFT);
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyCode = 0;
    }
}
