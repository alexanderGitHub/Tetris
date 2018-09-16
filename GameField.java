package tetris;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class GameField extends JPanel {
	
	private static final int BOX_WIDTH = 40;
	private static final int WIDTH = 10 * BOX_WIDTH + 1;
	private static final int HEIGHT = 20 * BOX_WIDTH + 1;
	private Figure curFig = null;
	private Cube[][] field = new Cube[20][10];
	Timer timer;
	private static enum Direction {LEFT, RIGHT};
	
	/**
	 * Constructor. Createing new Figure and applying key listener
	 */
	public GameField() {
		
		this.setFocusable(true);
		this.requestFocusInWindow();
		
		createFigure();
		
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tick();
			}
		});
		
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					if (ckeckRotationCollision()) {
						repaint();
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					if (checkHorizontalCollision(Direction.LEFT)) {
						curFig.moveLeft();
						repaint();
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					if (checkHorizontalCollision(Direction.RIGHT)) {
						curFig.moveRight();
						repaint();
					}
				}
				else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
					tick();
				}
			}
		});
	}
	
	/**
	 * Starts the timer
	 */
	public void start() {
		if (!timer.isRunning())
			timer.start();
	}
		
	/**
	 * func for each tick of the timer or pressing KEY_DOWN
	 */
	public void tick() {
		if (canMoveDown()) {
			curFig.moveDown();
		}
		else {
			addFigureToField();
			createFigure();
		}
		repaint();
	}
	
	/**
	 * Game over method
	 */
	private void gameOver() {
		timer.stop();
		//int r = ;
		if (JOptionPane.showConfirmDialog(this, "Do you want to play again?", "GAME OVER", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
			field = new Cube[20][10];
			createFigure();
			start();
		} else 
			System.exit(0);
	}

	/**
	 * If it is possible to move the figure one step down
	 */
	public boolean canMoveDown() {
		for (int i = 0; i < curFig.getMatrix().length; i++) {
			for (int j = 0; j < curFig.getMatrix()[0].length; j++) {
				if (curFig.getY()+i+1 >= 20 || curFig.getMatrix()[i][j] != null && field[curFig.getY()+i+1][curFig.getX()+j] != null) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Check if player can move the figure left or rigth. 
	 * @param d Directon of the move
	 * @return true if it can be moved and false if not.
	 */
	private boolean checkHorizontalCollision(Direction d) {
		int shift = d == Direction.LEFT ? -1 : 1;
		
		for (int i = 0; i < curFig.getMatrix().length; i++) {
			for (int j = 0; j < curFig.getMatrix()[0].length; j++) {
				if (curFig.getX()+j+shift < 0 || curFig.getX()+j+shift >= 10 || 
						curFig.getMatrix()[i][j] != null && field[curFig.getY()+i][curFig.getX()+j+shift] != null) {
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Checks if it possible to rotate the Figure and not to collide with existing field.
	 * it also rotate the figure if rotation can be done.
	 * @return true if figure can be rotated and false - if not.
	 */
	private boolean ckeckRotationCollision() {
		curFig.rotate();
		for (int i = 0; i < curFig.getMatrix().length; i++) {
			for (int j = 0; j < curFig.getMatrix()[0].length; j++) {
				if (curFig.getMatrix()[i][j] != null && field[curFig.getY()+i][curFig.getX()+j] != null) {
					curFig.rotateBackward();
					return false;
				}
			}
		}
		return true;
	}
	
	/**
	 * Export matrix of Figure to the field matrix.
	 * And then check for full lines.
	 */
	private void addFigureToField() {
		for (int i = 0; i < curFig.getMatrix().length; i++) {
			for (int j = 0; j < curFig.getMatrix()[0].length; j++) {
				if (curFig.getMatrix()[i][j] != null) {
					field[curFig.getY()+i][curFig.getX()+j] = curFig.getMatrix()[i][j];
				}
			}
		}
		
		// Removing full lines
		int numFullLines = 0;
		for (int i = curFig.getY(); i < curFig.getY()+curFig.getMatrix().length; i++) {
			boolean fullLine = true;
			for (int j = 0; j < 10; j++) {
				if (field[i][j] == null) {
					fullLine = false;
					break;
				}
			}
			
			if (fullLine) {
				// clear ful line
				for (int j = 0; j < 10; j++)
					field[i][j] = null;
				// shift upper blocks down
				for (int z = i; z > 0; z--) {
					for (int j = 0; j < 10; j++)
						field[z][j] = field[z-1][j];
				}
				numFullLines++;
			}
		}
	}
	
	/**
	 * Fabric method that creates a random Figure.
	 * After creating it checks if it is possible to move down. If not - game over
	 */
	private void createFigure() {
		Random random = new Random();
		int tmp = random.nextInt(7);
		switch(tmp) {
			case 0: curFig = new TFigure(); break;
			case 1: curFig = new LFigure(); break;
			case 2: curFig = new BLFigure(); break;
			case 3: curFig = new BFigure(); break;
			case 4: curFig = new ZFigure(); break;
			case 5: curFig = new BZFigure(); break;
			case 6: curFig = new IFigure(); break;
		}
		if (!canMoveDown())
			gameOver();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// paint Figure
		g.setColor(Color.BLACK);
		for (int i = 0; i < curFig.getMatrix().length; i++) {
			for (int j = 0; j < curFig.getMatrix()[0].length; j++) {
				if (curFig.getMatrix()[i][j] != null) {
					g.setColor(curFig.getMatrix()[i][j].getColor());
					g.fillRect(curFig.getX()*BOX_WIDTH + j*BOX_WIDTH, curFig.getY()*BOX_WIDTH + i*BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);
					g.setColor(Color.BLACK);
					g.drawRect(curFig.getX()*BOX_WIDTH + j*BOX_WIDTH, curFig.getY()*BOX_WIDTH + i*BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);
				}
			}
		}
		
		// paint existing field
		for (int i = 0; i < field.length; i++) {
			for (int j = 0; j < field[0].length; j++) {
				if (field[i][j] != null) {
					g.setColor(field[i][j].getColor());
					g.fillRect(j*BOX_WIDTH, i*BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);
					g.setColor(Color.BLACK);
					g.drawRect(j*BOX_WIDTH, i*BOX_WIDTH, BOX_WIDTH, BOX_WIDTH);
				}
			}
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
}
