package app;

import genetic.Individual;
import genetic.Population;

import javax.swing.*;
import java.awt.*;

public class StatisticsComponent extends JComponent {

    private Population population;

    public StatisticsComponent(Population population) {
        this.population = population;
    }

    protected void paintComponent(Graphics g) {

        synchronized (population) {
            super.paintComponent(g);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setFont(new Font("Arial", Font.TRUETYPE_FONT, 14));
            g.drawString("Score: " , 5, 5);
            g.drawString("Best: " , 5, 25);
            g.drawString("Dead: " , 5, 45);
            g.drawString("All Steps: " , 5, 65);
            g.drawString("Apple Steps: " , 5, 85);
        }

        repaint();
    }
}
