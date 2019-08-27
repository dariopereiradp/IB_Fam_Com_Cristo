package dad.recursos;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import dad.fam_com_cristo.gui.DataGui;
import net.miginfocom.swing.MigLayout;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * Classe para visualizar uma imagem.
 * 
 * @author D�rio Pereira
 *
 */
public class ImageViewer {

	/**
	 * Visualiza uma imagem 3x4 (177*2 x 263*2)
	 * 
	 * @param img
	 *            - imagem a ser visualizada
	 */
	public static void show(ImageIcon img) {
		JFrame frame = new JFrame();
		frame.getContentPane().setLayout(new MigLayout("al center center, wrap, gapy 15"));
		frame.setIconImage(
				Toolkit.getDefaultToolkit().getImage((DataGui.getInstance().getClass().getResource("/FC.jpg"))));
		frame.setMinimumSize(new Dimension(177 * 2, 263 * 2));
		frame.setResizable(false);
		JLabel imageView = new JLabel("");
		imageView.setMinimumSize(new Dimension(177 * 2, 263 * 2));
		imageView.setBorder(new LineBorder(Color.BLACK, 2));
		imageView
				.setIcon(new ImageIcon(img.getImage().getScaledInstance(177 * 2, 236 * 2, Image.SCALE_AREA_AVERAGING)));
		frame.getContentPane().add(imageView, "center");
		frame.setVisible(true);
	}
}
