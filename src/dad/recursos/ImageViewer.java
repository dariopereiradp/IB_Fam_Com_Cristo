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
import java.io.InputStream;

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
 * @author D�rio Pereira
 *
 */
public class ImageViewer {

	/**
	 * Visualiza uma imagem, mantendo o aspect ratio
	 * 
	 * @param dialog               - di�logo pai
	 * @param img                  - imagem a ser visualizada
	 * @param optionalSourceFile   - source em formato File (se escolher InputStream
	 *                             pode deixar esse como null)
	 * @param target               - File com localiza��o do destino
	 * @param optionalSourceStream - source em stream (pode ser null se escolher o
	 *                             formato File em optionalSourceFile
	 */
	public static void show(JDialog dialog, ImageIcon img, File optionalSourceFile, File target,
			InputStream optionalSourceStream) {
		JDialog jdialog = new JDialog(dialog, ModalityType.DOCUMENT_MODAL);
		jdialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		jdialog.setTitle(optionalSourceFile != null ? optionalSourceFile.getName() : "FC.jpg");
		jdialog.getContentPane().setLayout(new MigLayout("al center center, wrap, gapy 15"));
		jdialog.setIconImage(Toolkit.getDefaultToolkit().getImage((ImageViewer.class.getResource("/FC.jpg"))));

		Dimension size = new Dimension(600, 600);
		jdialog.setSize(size);
		jdialog.setResizable(false);
		JLabel imageView = new JLabel("");
		imageView.setMinimumSize(size);

		if (optionalSourceFile == null) {
			Dimension scaled = getScaledDimension(new Dimension(img.getIconWidth(), img.getIconHeight()), size);
			imageView.setIcon(new ImageIcon(img.getImage().getScaledInstance((int) scaled.getWidth(),
					(int) scaled.getHeight(), Image.SCALE_AREA_AVERAGING)));
		} else {
			;
			imageView.setIcon(Utils.getScaledImageIcon(size, optionalSourceFile));
		}
		imageView.setBorder(new LineBorder(Color.BLACK, 2));

		jdialog.getContentPane().add(imageView, "center");

		imageView.setComponentPopupMenu(getPopupMenu(optionalSourceFile, target, optionalSourceStream));
		jdialog.setVisible(true);
	}

	/**
	 * Redimensiona a imagem sem perder o aspect ratio
	 * 
	 * @param imageSize - tamanho original da imagem
	 * @param boundary  - tamanho do painel que cont�m a imagem
	 * @return
	 */
	public static Dimension getScaledDimension(Dimension imageSize, Dimension boundary) {

		double widthRatio = boundary.getWidth() / imageSize.getWidth();
		double heightRatio = boundary.getHeight() / imageSize.getHeight();
		double ratio = Math.min(widthRatio, heightRatio);

		return new Dimension((int) (imageSize.width * ratio), (int) (imageSize.height * ratio));
	}

	/**
	 * PopoupMenu com a funcionalidade de salvar a imagem
	 * 
	 * @param optionalSourceFile
	 * @param target
	 * @param optionalSourceStream
	 * @return
	 */
	private static JPopupMenu getPopupMenu(File optionalSourceFile, File target, InputStream optionalSourceStream) {
		JPopupMenu menu = new JPopupMenu();

		JMenuItem save = new JMenuItem("Salvar imagem");
		save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (optionalSourceStream == null)
						Utils.exportImg(new FileInputStream(optionalSourceFile), target);
					else
						Utils.exportImg(optionalSourceStream, target);
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
