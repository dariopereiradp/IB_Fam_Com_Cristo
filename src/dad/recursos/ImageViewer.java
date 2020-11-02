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
	 * Visualiza uma imagem 3x4 (177*2 x 263*2)
	 * 
	 * @param img - imagem a ser visualizada
	 */
	public static void show(JDialog dialog, ImageIcon img, File source, File target) {
		JDialog jdialog = new JDialog(dialog, ModalityType.DOCUMENT_MODAL);
		jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jdialog.getContentPane().setLayout(new MigLayout("al center center, wrap, gapy 15"));
		jdialog.setIconImage(Toolkit.getDefaultToolkit().getImage((ImageViewer.class.getResource("/FC.jpg"))));
		jdialog.setMinimumSize(new Dimension(177 * 2, 263 * 2));
		jdialog.setResizable(false);
		JLabel imageView = new JLabel("");
		imageView.setMinimumSize(new Dimension(177 * 2, 263 * 2));
		imageView.setBorder(new LineBorder(Color.BLACK, 2));
		imageView
				.setIcon(new ImageIcon(img.getImage().getScaledInstance(177 * 2, 236 * 2, Image.SCALE_AREA_AVERAGING)));
		jdialog.getContentPane().add(imageView, "center");

		imageView.setComponentPopupMenu(getPopupMenu(source, target));
		jdialog.setVisible(true);
	}

	/**
	 * Visualiza a imagem com o tamanho do logotipo
	 * @param img
	 */
	public static void showLogo(JDialog dialog, ImageIcon img) {
		JDialog jdialog = new JDialog(dialog, ModalityType.DOCUMENT_MODAL);
		jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jdialog.getContentPane().setLayout(new MigLayout("al center center, wrap, gapy 15"));
		jdialog.setIconImage(Toolkit.getDefaultToolkit().getImage((ImageViewer.class.getResource("/FC.jpg"))));
		jdialog.setMinimumSize(new Dimension(2873 / 4, 2420 / 4));
		jdialog.setResizable(false);
		JLabel imageView = new JLabel("");
		imageView.setMinimumSize(new Dimension(2873 / 4, 2362 / 4));
		imageView.setBorder(new LineBorder(Color.BLACK, 2));
		imageView.setIcon(
				new ImageIcon(img.getImage().getScaledInstance(2873 / 4, 2362 / 4, Image.SCALE_AREA_AVERAGING)));
		jdialog.getContentPane().add(imageView, "center");
		jdialog.setVisible(true);

	}

	/**
	 * PopoupMenu com a funcionalidade de salvar a imagem
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
