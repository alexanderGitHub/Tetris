package tetris;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.*;

public class ScoreListener extends JPanel {
	
	private GameField gameField;
	private Font font = new Font("Arial", Font.BOLD, 14);
	
	int recordScore, curScore = 0;
	int numOfLines = 0;
	int speed = 0;
	
	/**
	 * Receives count of full lines, so game should add earned
	 *  points and increase total count of full lines.
	 * @param n Count of full lines.
	 */
	public void addLines(int n) {
		switch (n) {
			case 1: curScore += 100; break;
			case 2: curScore += 300; break;
			case 3: curScore += 700; break;
			case 4: curScore += 1500; break;
		}
		
		if (curScore > recordScore) recordScore = curScore;
		
		numOfLines += n;
		
		// after 40 full lines speed increases and after each 10 lines 
		int tmp = numOfLines - 30 / 10;
		if (tmp > speed) {
			speed = tmp;
			gameField.setSpeed(speed);
		}
		repaint();
	}
	
	/**
	 * Resets all the stats excluding recordScore.
	 */
	public void reset() {
		curScore = numOfLines = speed = 0;
		
	}
	
	/**
	 * Setting a GameField object for reason to encrease 
	 * the figure flow speed after encreasing the speed.
	 * @param gf An GameField object. 
	 */
	public void setGameField(GameField gf) {
		gameField = gf;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setFont(font);
		g.drawString("Record: " + recordScore, 15, 35);
		g.drawString("Current: " + curScore, 15, 55);
		g.drawString("Speed: " + speed, 15, 75);
		g.drawString("Lines: " + numOfLines, 15, 95);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(GameField.BOX_WIDTH*4+10, GameField.BOX_WIDTH*3);
	}
}
