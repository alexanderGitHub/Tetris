package tetris;

import java.awt.*;
import javax.swing.*;

public class NewFigureListener extends JPanel {
	
	private Figure f;
	
	public void setFigure(Figure f) {
		this.f = f;
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		if (f == null) return;
		
		// calculate offsets due to figure size
		int offsetX = (this.getWidth() - f.getMatrix()[0].length*GameField.BOX_WIDTH) / 2;
		int offsetY = (this.getHeight() - f.getMatrix().length*GameField.BOX_WIDTH) / 2;
		
		for (int i = 0; i < f.getMatrix().length; i++)
			for (int j = 0; j < f.getMatrix()[0].length; j++)
				if (f.getMatrix()[i][j] != null) {
					g.setColor(f.getMatrix()[i][j].getColor());
					g.fillRect(offsetX + j*GameField.BOX_WIDTH, offsetY + i*GameField.BOX_WIDTH, GameField.BOX_WIDTH, GameField.BOX_WIDTH);
					g.setColor(Color.BLACK);
					g.drawRect(offsetX + j*GameField.BOX_WIDTH, offsetY + i*GameField.BOX_WIDTH, GameField.BOX_WIDTH, GameField.BOX_WIDTH);
				}
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(GameField.BOX_WIDTH*4+10, GameField.BOX_WIDTH*3);
	}
}
