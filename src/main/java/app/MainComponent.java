package app;

import game.Apple;
import game.Snake;
import genetic.Individual;
import genetic.Population;

import javax.swing.*;
import java.awt.*;

public class MainComponent extends JComponent {

    private int xOffset = 0;
    private int yOffset = 0;
    private int cellSize = 0;
    int cellWidth;
    int cellHeight;

    private Population population;
    private int individualIdx;

    public MainComponent(int xOffset, int yOffset, int cellSize, Population population, int individualIdx) {
        super();

        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.cellSize = cellSize;
        this.population = population;
        this.individualIdx = individualIdx;
    }

    protected void paintComponent(Graphics g) {

        synchronized (population) {
            Point point;

            if (population.getIndividuals().size() <= individualIdx) return;

            Individual individual = population.getIndividuals().get(individualIdx);

            super.paintComponent(g);
            Graphics2D g2D = (Graphics2D) g;
            g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            g2D.setColor(Color.LIGHT_GRAY);
            g2D.fillRect(0, 0, getWidth(), getHeight());

            int compWidth = getWidth();
            int compHeight = getHeight();

            cellWidth = (compWidth - 2 * xOffset) / cellSize;
            cellHeight = (compHeight - 2 * yOffset) / cellSize;

            if (cellHeight < cellWidth) cellWidth = cellHeight;

            if (population.isSimulationActive()) {
                // draw apple
                g2D.setColor(new Color(240, 50, 0));
                Apple apple = individual.getGrid().getApple();

                if (apple != null) {
                    point = convToPoint(apple.getPosition());
                    g2D.fillRect(point.x, point.y, cellWidth, cellWidth);
                }

                // draw snake
                Snake snake = individual.getSnake();
                if (snake.isAlive())
                    g2D.setColor(new Color(50, 220, 50));
                else
                    g2D.setColor(new Color(120, 120, 120));

                snake.getBodyList().forEach(body -> {
                    Point bodyPos = convToPoint(body.getPosition());
                    g2D.fillRect(bodyPos.x, bodyPos.y, cellWidth, cellWidth);
                });
                if (snake.isAlive())
                    g2D.setColor(new Color(40, 180, 40));
                else
                    g2D.setColor(new Color(80, 80, 80));

                point = convToPoint(snake.getHead().getPosition());
                g2D.fillRect(point.x, point.y, cellWidth, cellWidth);
                //g2D.setColor(Color.WHITE);
                //g2D.setFont(new Font("Courier", Font.PLAIN,15));
                //g2D.drawString("" + snake.getId(), point.x + 10, point.y + 15);
            }

            // draw grid
            g2D.setColor(Color.GRAY);
            for (int i = 0; i < individual.getGrid().getWidth(); i++) {
                for (int j = 0; j < individual.getGrid().getHeight(); j++) {
                    g2D.drawRect(xOffset + i * cellWidth, yOffset + j * cellWidth, cellWidth, cellWidth);
                }
            }

            // draw border
            g2D.setColor(Color.BLACK);
            g2D.drawRect(xOffset, yOffset, individual.getGrid().getWidth() * cellWidth, individual.getGrid().getWidth() * cellWidth);
        }
    }

    protected Point convToPoint(Point position)
    {
        return new Point(xOffset + position.x * cellWidth, yOffset + position.y * cellWidth);
    }
}
