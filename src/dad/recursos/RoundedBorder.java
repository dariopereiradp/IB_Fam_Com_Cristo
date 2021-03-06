package dad.recursos;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;

import javax.swing.border.Border;

/**
 * Classe que representa uma borda curva
 * @author dariopereiradp
 *
 */
public class RoundedBorder implements Border {

	private int radius;

	public RoundedBorder(int radius) {
		this.radius = radius;
	}

	public Insets getBorderInsets(Component c) {
		return new Insets(this.radius, this.radius, this.radius, this.radius);
	}

	public boolean isBorderOpaque() {
		return true;
	}

	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
	}
}