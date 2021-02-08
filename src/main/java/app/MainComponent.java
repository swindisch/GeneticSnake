package app;

import game.Apple;
import game.Snake;
import simulation.Simulation;

import javax.swing.*;
import java.awt.*;

public class MainComponent extends JComponent {

    private int xOffset = 0;
    private int yOffset = 0;
    private int cellSize = 0;

    private Simulation sim;

    public MainComponent(int xOffset, int yOffset, int cellSize, Simulation sim) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.cellSize = cellSize;
        this.sim = sim;
    }

    protected void paintComponent(Graphics g) {
        Point point;

        synchronized (sim) {

            super.paintComponent(g);
            Graphics2D g2D = (Graphics2D) g;
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2D.setColor(Color.LIGHT_GRAY);
            g2D.fillRect(0, 0, getWidth(), getHeight());

            // draw apple
            g2D.setColor(new Color(240, 50, 0));
            Apple apple = sim.getApple();
            point = convToPoint(apple.getPosX(), apple.getPosY());
            g2D.fillRect(point.x, point.y, cellSize, cellSize);

            // draw snake
            g2D.setColor(new Color(40, 180, 40));
            Snake snake = sim.getSnake();
            point = convToPoint(snake.getHead().getPosX(), snake.getHead().getPosY());
            g2D.fillRect(point.x, point.y, cellSize, cellSize);

            g2D.setColor(new Color(50, 220, 50));
            snake.getBodyList().forEach(body -> {
                Point bodyPos = convToPoint(body.getPosX(), body.getPosY());
                g2D.fillRect(bodyPos.x, bodyPos.y, cellSize, cellSize);
            });

            // draw grid
            g2D.setColor(Color.GRAY);
            for (int i = 0; i < sim.getGridWith(); i++) {
                for (int j = 0; j < sim.getGridHeight(); j++) {
                    g2D.drawRect(xOffset + i * cellSize, yOffset + j * cellSize, cellSize, cellSize);
                }
            }

            // draw border
            g2D.setColor(Color.BLACK);
            g2D.drawRect(xOffset, yOffset, sim.getGridWith() * cellSize, sim.getGridHeight() * cellSize);

            // draw score
            g2D.setFont(new Font("Arial", Font.BOLD, 20));
            g2D.drawString("Score: " + sim.getAppleCollected(), 5, 5);
            g2D.drawString("Best: " + sim.getApplesMax(), 5, 25);
            g2D.drawString("Dead: " + sim.getDeadCount(), 5, 45);
            g2D.drawString("All Steps: " + sim.getSnake().getAllSteps(), 5, 65);
            g2D.drawString("Apple Steps: " + sim.getSnake().getAppleSteps(), 5, 85);

            repaint();
        }
    }

    protected Point convToPoint(int posX, int posY)
    {
        return new Point(xOffset + posX * cellSize, yOffset + posY * cellSize);
    }
}
