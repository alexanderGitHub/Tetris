package tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import java.util.*;

public class Tetris extends JFrame {
	
	JPanel stats;
	NewFigureListener newFigPanel;
	ScoreListener scorePanel;
	GameField gameField;
	
	public Tetris() {
		//stats = new JPanel();
		//add(stats, BorderLayout.NORTH);
		
		newFigPanel = new NewFigureListener();
		newFigPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				"Next Figure", 2, 2, new Font("Arial", Font.BOLD, 15), Color.black));
		
		scorePanel = new ScoreListener();
		scorePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black, 1), 
				"Score", 2, 2, new Font("Arial", Font.BOLD, 15), Color.black));
		
		gameField = new GameField();
		add(gameField, BorderLayout.CENTER);
		gameField.setNfListener(newFigPanel);
		gameField.setScoreListener(scorePanel);
		
		JPanel leftPanel = new JPanel();
		leftPanel.setLayout(new FlowLayout());
		leftPanel.setPreferredSize(newFigPanel.getPreferredSize());
		add(leftPanel, BorderLayout.EAST);
		
		leftPanel.add(newFigPanel);
		leftPanel.add(scorePanel);
		
		pack();
	}
	
	/**
	 * Start the game
	 */
	public void start() {
		gameField.start();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Tetris game = new Tetris();
				game.setTitle("Tetris");
				game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				game.setLocationRelativeTo(null);
				game.setResizable(false);
				game.setVisible(true);
				game.start();
			}
		});
	}

}
