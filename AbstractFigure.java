package tetris;

import java.awt.*;

/**
 * Abstract class of the figure.
 * @author Alex
 *
 */
public abstract class AbstractFigure implements Figure {
	private Cube[][] v_matrix;
	private Cube[][] h_matrix;
	boolean horizontal = true;
	private int x = 3;
	private int y = 0;
	private int tmpX = 0; // temporary x position 
	
	/**
	 * Initialization of Figure from matrix received from parameter. 
	 * The parameter have to always describe the horizontal figure matrix.
	 * @param mt Matrix of horizontal positioned figure.
	 * @param color Color of boxes the figure made.
	 */
	public AbstractFigure(int[][] mt, Color color) {
		
		h_matrix = new Cube[mt.length][mt[0].length];
		v_matrix = new Cube[mt[0].length][mt.length];
		
		for (int i = 0; i < mt.length; i++)
			for (int j = 0; j < mt[0].length; j++)
				if (mt[i][j] == 1)
					h_matrix[i][j] = new Cube(color);
		
	}
	
	/**
	 * @return matrix of the figure.
	 */
	public Cube[][] getMatrix() {
		if (horizontal)
			return h_matrix;
		else 
			return v_matrix;
	}
	
	/**
	 * Rotate the matrix 90 degree.
	 */
	public void rotate() {
		if (horizontal) {
			for (int i = 0, n = h_matrix.length; i < n; i++)
				for (int j = 0, m = h_matrix[0].length; j < m; j++)
					v_matrix[j][n-i-1] = h_matrix[i][j];
		} else {
			for (int i = 0, n = v_matrix.length; i < n; i++)
				for (int j = 0, m = v_matrix[0].length; j < m; j++)
					h_matrix[j][n-i-1] = v_matrix[i][j];
		}
		horizontal = !horizontal;
		
		// checking if the figure is not out of the screen
		tmpX = x; // memorize current x before shifting
		if (horizontal && x + h_matrix[0].length >= 10) {
			x = 10 - h_matrix[0].length;
		}
	}
		
	/**
	 * Rotate the matrix -90 degree.
	 */
	public void rotateBackward() {
		if (horizontal) {
			for (int i = 0, n = h_matrix.length; i < n; i++)
				for (int j = 0, m = h_matrix[0].length; j < m; j++)
					v_matrix[m-j-1][i] = h_matrix[i][j];
		} else {
			for (int i = 0, n = v_matrix.length; i < n; i++)
				for (int j = 0, m = v_matrix[0].length; j < m; j++)
					h_matrix[m-j-1][i] = v_matrix[i][j];
		}
		horizontal = !horizontal;
		
		// restoring x position
		x = tmpX;
	}
	
	/**
	 * Move left the figure that means to decrease x position of figure.
	 */
	public void moveLeft() {
		if (x - 1 >= 0)
			x--;
	}
	
	/**
	 * Move right the figure that means to increase x position of figure.
	 */
	public void moveRight() {
		if (x + (horizontal ? h_matrix[0].length : v_matrix[0].length) < 10)
			x++;
	}
	
	/**
	 * Move down the figure that means to increase y position of figure.
	 */
	public void moveDown() {
		y++;
	}
	
	/**
	 * @Return y The x position of figure 
	 */
	public int getX() {
		return x;
	}

	/**
	 * @Return y The y position of figure 
	 */
	public int getY() {
		return y;
	}
}
