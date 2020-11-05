package dad.recursos;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;

/**
 * Classe para visualizar uma imagem.
 * 
 * @author Dário Pereira
 *
 */
public class ImageViewer {

	/**
	 * Visualiza uma imagem, mantendo o aspect ratio
	 * 
	 * @param img - imagem a ser visualizada
	 */
	public static void show(JDialog dialog, ImageIcon img, File source, File target) {
		JDialog jdialog = new JDialog(dialog, ModalityType.DOCUMENT_MODAL);
		jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jdialog.setTitle(source.getName());
		jdialog.getContentPane().setLayout(new MigLayout("al center center, wrap, gapy 15"));
		jdialog.setIconImage(Toolkit.getDefaultToolkit().getImage((ImageViewer.class.getResource("/FC.jpg"))));

		Dimension size = new Dimension(600, 600);
		jdialog.setSize(size);
		jdialog.setResizable(false);
		JLabel imageView = new JLabel("");
		imageView.setMinimumSize(size);

		Dimension scaled = getScaledDimension(new Dimension(img.getIconWidth(), img.getIconHeight()), size);
		imageView.setBorder(new LineBorder(Color.BLACK, 2));
		imageView.setIcon(new ImageIcon(img.getImage().getScaledInstance((int) scaled.getWidth(),
				(int) scaled.getHeight(), Image.SCALE_AREA_AVERAGING)));
		jdialog.getContentPane().add(imageView, "center");

		imageView.setComponentPopupMenu(getPopupMenu(source, target));
		jdialog.setVisible(true);
	}

	/**
	 * Redimensiona a imagem sem perder o aspect ratio
	 * @param imageSize - tamanho original da imagem
	 * @param boundary - tamanho do painel que contém a imagem
	 * @return
	 */
	public static Dimension getScaledDimension(Dimension imageSize, Dimension boundary) {

	    double widthRatio = boundary.getWidth() / imageSize.getWidth();
	    double heightRatio = boundary.getHeight() / imageSize.getHeight();
	    double ratio = Math.min(widthRatio, heightRatio);

	    return new Dimension((int) (imageSize.width  * ratio),
	                         (int) (imageSize.height * ratio));
	}

	/**
	 * PopoupMenu com a funcionalidade de salvar a imagem
	 * 
	 * @param source
	 * @param target
	 * @return
	 */
	private static JPopupMenu getPopupMenu(File source, File target) {
		JPopupMenu menu = new JPopupMenu();

		JMenuItem save = new JMenuItem("Salvar imagem");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Utils.exportImg(new FileInputStream(source), target);
				} catch (Exception e1) {
					Log.getInstance().printLog(e1.getMessage());
					e1.printStackTrace();
				}
			}
		});
		menu.add(save);
		return menu;
	}
}
