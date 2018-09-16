package tetris;

import java.awt.*;

/**
 * Abstract class of the figure
 * @author Alex
 *
 */
public abstract class AbstractFigure implements Figure {
	private int[][] v_matrix;// = new int[3][2];
	private int[][] h_matrix;// = new int[2][3];
	boolean horizontal = true;
	private int x = 3;
	private int y = 0;
	
	public AbstractFigure() {}
	
	public AbstractFigure(int[][] mt) {
		h_matrix = mt;
		v_matrix = new int[mt[0].length][mt.length];
	}
	
	public int[][] getMatrix() {
		if (horizontal)
			return h_matrix;
		else 
			return v_matrix;
	}
	
	/**
	 * Rotate the matrix 90 degree
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
		if (horizontal && x + v_matrix[0].length >= 10) {
			x = 10 - v_matrix[0].length - 1;
		}
	}
	
	/**
	 * Rotates figure matrix and return copy of it, but don't saves rotation changes in figure matrix.
	 * @return Copy of rotated matrix.
	 */
	/*
	public int[][] createMemento() {
		int[][] tmp;
		if (horizontal) {
			tmp = new int[h_matrix[0].length][h_matrix.length];
			for (int i = 0, n = h_matrix.length; i < n; i++)
				for (int j = 0, m = h_matrix[0].length; j < m; j++)
					tmp[j][n-i-1] = h_matrix[i][j];
		} else {
			tmp = new int[v_matrix[0].length][v_matrix.length];
			for (int i = 0, n = v_matrix.length; i < n; i++)
				for (int j = 0, m = v_matrix[0].length; j < m; j++)
					tmp[j][n-i-1] = v_matrix[i][j];
		}
		horizontal = !horizontal;
		
		// checking if the figure is not out of the screen
		if (horizontal && x + tmp[0].length >= 10) {
			x = 10 - tmp[0].length - 1;
		}
		return 
	}*/
	
	/**
	 * Rotate the matrix -90 degree
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
		
		// checking if the figure is not out of the screen
		if (horizontal && x + v_matrix[0].length >= 10) {
			x = 10 - v_matrix[0].length - 1;
		}
	}
	
	public void moveLeft() {
		if (x - 1 >= 0)
			x--;
		System.out.println(x);
	}
	
	public void moveRight() {
		if (x + (horizontal ? h_matrix[0].length : v_matrix[0].length) < 10)
			x++;
		System.out.println(x);
	}
	
	public void moveDown() {
		y++;
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	private class Memento {
		private int[][] vmt;
		private int[][] hmt;
		private boolean hor;
		private int mx;
		private int my;
		
		public Memento() {
			vmt = v_matrix;
			hmt = h_matrix;
			this.hor = horizontal;
			mx = x;
			my = y;
		}
		
		int[][] getVMatrix() {
			return vmt;
		}
		
		int[][] getHMatrix() {
			return hmt;
		}
		
		boolean getHor() {
			return hor;
		}
		
		int getX() {
			return mx;
		}
		
		int getY() {
			return my;
		}
	}
}
