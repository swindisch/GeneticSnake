package app;

import simulation.Simulation;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class MainFrame extends JFrame {

	private ArrayList<Simulation> simList;

	public MainFrame(ArrayList<Simulation> simList) {
		this.simList = simList;
	}

	public void createWindow(int width, int height, int xOffset, int yOffset, int cellSize) {

		Runnable r = () -> {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			int maxX = screenSize.width - 50;
			int maxY = screenSize.height - 50;

			setDefaultCloseOperation(EXIT_ON_CLOSE);
			//setLayout(null);
			setTitle("Genetic Snake Game");


			final JPanel gui = new JPanel(new BorderLayout(5, 5));

			gui.setBorder(new TitledBorder("BorderLayout(5,5)"));

			JButton addNew = new JButton("Start");
			addNew.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					validate();
				}
			} );
			gui.add( addNew, BorderLayout.NORTH );

			JButton addNew2 = new JButton("Stop");
			addNew2.addActionListener( new ActionListener() {
				public void actionPerformed(ActionEvent ae) {
					validate();
				}
			} );
			gui.add( addNew2, BorderLayout.NORTH );





			final JPanel labels = new JPanel(new GridLayout(2, 5, 5, 5));
			labels.setBorder(new TitledBorder("GridLayout(0,2,3,3)"));

			simList.forEach(sim -> {
				MainComponent mainComp = new MainComponent(xOffset, yOffset, cellSize, sim);
				labels.add(mainComp);
			});

			KeyboardListener keyListener = new KeyboardListener(simList.get(0));
			labels.addKeyListener(keyListener);

			gui.add(labels, BorderLayout.CENTER);


			//Create a panel and add components to it.
			//		JPanel contentPane = new JPanel(new BorderLayout());

			//		for (int i = 0; i < 3; i++) {
			//			MainComponent mainComp = new MainComponent(xOffset + i * 100, yOffset, cellSize, sim);
			//			contentPane.add(mainComp);
			//		}
			//		setContentPane(contentPane);


			setContentPane(gui);
			setSize(width, height);
			setLocationRelativeTo(null);



			// display the window
			//pack();
			setVisible(true);
		};
		SwingUtilities.invokeLater(r);
	}
}
