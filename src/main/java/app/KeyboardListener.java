package app;

import game.rules.Direction;
import lombok.Getter;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

@Getter
public class KeyboardListener implements KeyListener {

    private int keyCode = 0;

    public KeyboardListener() {
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        keyCode = e.getKeyCode();

        switch (keyCode) {
            case KeyEvent.VK_W:
                break;
            case KeyEvent.VK_D:
                break;
            case KeyEvent.VK_S:
                break;
            case KeyEvent.VK_A:
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keyCode = 0;
    }
}
