package tetris;

import java.awt.*;

public class Cube {
	Color color;
	
	public Cube(Color c) {
		color = c;
	}
	
	public Color getColor() {
		return color;
	}
	
	public void setColor(Color color) {
		this.color = color;
	}
}