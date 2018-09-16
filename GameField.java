package tetris;

import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class GameField extends JPanel {
	
	public static final int BOX_WIDTH = 40;
	private static final int WIDTH = 10 * BOX_WIDTH + 1;
	private static final int HEIGHT = 20 * BOX_WIDTH + 1;
	private static enum Direction {LEFT, RIGHT};
	
	Timer timer;
	JLabel helpLabel;
	private Figure curFig, nextFig = null;
	private Cube[][] field = new Cube[20][10];
	
	Random random = new Random();
	NewFigureListener nfListener;
	ScoreListener sListener;
	
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
				else if (e.getKeyCode() == KeyEvent.VK_F1) {
					timer.stop();
					if (helpLabel == null) {
						helpLabel = new JLabel("<html>Thanks for playing this game.<br>Scores table:<br>"+ 
								"1 line: 100 points;<br> lines: 300 points; 3 lines: 700 points;"+
								"4 lines: 1500 points.<br>The speed of figures flow will be increased"+
								"after 40 lines and then after each 10 lines more.<br>Have a nice time!</html>");
					}
					JOptionPane.showMessageDialog(GameField.this, helpLabel);
					timer.start();
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
			sListener.reset();
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
		sListener.addLines(numFullLines);
	}
	
	/**
	 * Fabric method that creates a random Figure.
	 * Sending nextFigure figure to nfListener so it's can be showed in the window of next figure.
	 * After creating it checks if it is possible to move down. If not - game over
	 */
	private void createFigure() {
		curFig = nextFig;
		
		int tmp = random.nextInt(7);
		switch(tmp) {
			case 0: nextFig = new TFigure(); break;
			case 1: nextFig = new LFigure(); break;
			case 2: nextFig = new BLFigure(); break;
			case 3: nextFig = new BFigure(); break;
			case 4: nextFig = new ZFigure(); break;
			case 5: nextFig = new BZFigure(); break;
			case 6: nextFig = new IFigure(); break;
		}
		
		// if it is first figure, then next figure was null
		if (curFig == null) {
			createFigure();
			return;
		}
		
		// Tell NewFigListener about new next figure
		if (nfListener != null)
			nfListener.setFigure(nextFig);
		
		if (!canMoveDown())
			gameOver();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		// paint vertical right border
		g.setColor(Color.black);
		g.drawLine(this.getWidth()-1, 0, this.getWidth()-1, this.getHeight());
		
		// paint Figure
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
	
	/**
	 * Setting speed of the flow.
	 * @param speed Speed that affect on the flow.
	 */
	public void setSpeed(int speed) {
		timer.setDelay(1000 - 100*speed);
	}
	
	/**
	 * Setting a NewFigureListener object, so it's possible to show
	 * the next figure on the right top corner.
	 * @param nfListener
	 */
	public void setNfListener(NewFigureListener nfListener) {
		this.nfListener = nfListener;
		if (nextFig != null)
			nfListener.setFigure(nextFig);
	}
	
	/**
	 * Setting ScoreListener, so it's possible to show statistics.
	 * Also, current class sends this, so that is possible to encrease
	 * the speed after some amount of full lines, that is controlled
	 * by ScoreListener object. 
	 * @param sListener
	 */
	public void setScoreListener(ScoreListener sListener) {
		this.sListener = sListener;
		sListener.setGameField(this);
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(WIDTH, HEIGHT);
	}
}
