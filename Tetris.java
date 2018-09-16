package tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;

import java.util.*;

public class Tetris extends JFrame {
	
	JPanel stats, leftPanel;
	GameField gf;
	
	public Tetris() {
		//stats = new JPanel();
		//add(stats, BorderLayout.NORTH);
		
		
		gf = new GameField();
		add(gf, BorderLayout.CENTER);
		
		//leftPanel = new JPanel();
		//add(leftPanel, BorderLayout.EAST);
		
		//setSize(gameField.getWidth()+100, gameField.getHeight() + 100);
		pack();
		
	}
	
	/**
	 * Start the game
	 */
	public void start() {
		gf.start();
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Tetris game = new Tetris();
				game.setTitle("Tetris");
				game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				game.setLocationRelativeTo(null);
				game.setVisible(true);
				game.start();
			}
		});
	}

}
