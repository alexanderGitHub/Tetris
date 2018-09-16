package tetris;

import java.awt.*;

public interface Figure {
	public Cube[][] getMatrix();
	public void rotate();
	public void rotateBackward();
	public int getX();
	public int getY();
	public void moveLeft();
	public void moveRight();
	public void moveDown();
}
