package app;

import genetic.Population;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame implements Updatable {

//	private ArrayList<Simulation> simList;
	private Population population;
	private JPanel gui;


	public MainFrame(Population population) {
		this.population = population;
	}

	public void createWindow(int width, int height, int xOffset, int yOffset, int cellSize) {

		Runnable r = () -> {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int maxX = screenSize.width - 200;
			int maxY = screenSize.height - 200;

			setDefaultCloseOperation(EXIT_ON_CLOSE);
			//setLayout(null);
			setTitle("Genetic Snake Game");


			gui = new JPanel(new BorderLayout(5, 5));
			gui.setBorder(new TitledBorder("Population"));

			JPanel statsPanel = new JPanel(new GridLayout(1, 5, 5, 5));
			statsPanel.setBorder(new TitledBorder("Statistics") );


			JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 5, 5));

			JButton loadStartButton = new JButton("Load Simulation");
			loadStartButton.addActionListener(ae -> {
				population.loadAndStartPopulation();
				repaint();
			});
			loadStartButton.setPreferredSize(new Dimension(50, 20));
			buttonPanel.add(loadStartButton);

			JButton startButton = new JButton("Start Simulation");
			startButton.addActionListener(ae -> {
				population.startSimulation();
				repaint();
			});
			startButton.setPreferredSize(new Dimension(50, 20));
			buttonPanel.add(startButton);

			JButton stopButton = new JButton("Stop Simulation");
			stopButton.addActionListener(ae -> {
				population.stopSimulation();
				repaint();
			});
			stopButton.setPreferredSize(new Dimension(50, 20));
			buttonPanel.add(stopButton);

			JButton switchButton = new JButton("Switch Speed");
			switchButton.addActionListener(ae -> {
				population.switchSpeed();
				repaint();
			});
			switchButton.setPreferredSize(new Dimension(50, 20));
			buttonPanel.add(switchButton);

			buttonPanel.setSize(50, 50);
			buttonPanel.setPreferredSize(new Dimension(50, 50));

			statsPanel.add(buttonPanel, BorderLayout.WEST);

			PopulationLabel pop = new PopulationLabel(population);
			pop.setPreferredSize(new Dimension(800,150));
			//pop.setSize(500, 100);
			statsPanel.add(pop, BorderLayout.CENTER);

			gui.add(statsPanel, BorderLayout.NORTH);

			final JPanel labels = new JPanel(new GridLayout(2, 5, 5, 5));
			labels.setBorder(new TitledBorder("Individuals"));

			for (int i = 0; i < Math.min(population.getPopulationSize(), 10); i++) {
				MainComponent mainComp = new MainComponent(xOffset, yOffset, cellSize, population, i);
				labels.add(mainComp);
			}

/*			KeyboardListener keyListener = new KeyboardListener(simList.get(0));
			labels.addKeyListener(keyListener);
*/
			gui.add(labels, BorderLayout.CENTER);

			setContentPane(gui);
			setSize(maxX, maxY);
			setLocationRelativeTo(null);
			setVisible(true);
			//repaint();
		};
		SwingUtilities.invokeLater(r);
	}


	@Override
	public void update() {
		gui.repaint();
	}
}
