package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URISyntaxException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.gui.themes.DarkTheme;
import dad.recursos.ImageViewer;
import dad.recursos.Utils;
import mdlaf.utils.MaterialColors;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

/**
 * Diálogo que apresenta as informações sobre o programa.
 * 
 * @author Dário Pereira
 *
 */
public class About extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8083357425196226363L;
	private final JPanel contentPanel = new JPanel();
	/**
	 * Informações a sserem mostradas no diálogo 'Sobre'.
	 */
	private String info = "<div>\u00A9 DPSoft 2020 <br>Feito por D\u00E1rio Pereira\r\n<br>Email de Suporte: <a href=\"#\">"
			+ Main.EMAIL_SUPORTE + "</a>"
			+ "<br><br>Código Fonte (GitHub): <a href=\"https://github.com/dariopereiradp/IB_Fam_Com_Cristo\">IB_Fam_Com_Cristo</a><br><br>"
			+ "Compat\u00EDvel com <a href=\"https://www.java.com/download\">Java 8</a><br><br>"
			+ "<br>Organizado com o <a href=\"https://maven.apache.org/\">Maven</a>"
			+ ", usando o <a href=\"https://www.eclipse.org//\">Eclipse</a><br><br>"
			+ "Bibliotecas usadas:<br><br><a href=\"https://github.com/vincenzopalazzo/material-ui-swing\">Material UI Swing v1.1.2-rc1</a><br>"
			+ "<a href=\"http://ucanaccess.sourceforge.net/site.html\">UCanAccess 5.0.0</a><br>"
			+ "<a href=\"https://github.com/srikanth-lingala/zip4j\">Zip4j 2.6.4</a><br>"
			+ "<a href=\"https://github.com/LGoodDatePicker/LGoodDatePicker\">LGoodDatePicker 11.1.0</a><br>"
			+ "<a href=\"https://www.qoppa.com/pdfwriter\">jPDFWriter v2016R1.04</a><br>"
			+ "<a href=\"https://itextpdf.com/en/products/itext-5-legacy\">iTextPDF 5.5.13.2</a><br>"
			+ "<a href=\"https://knowm.org/open-source/xchart\">xChart 3.6.5</a><br>"
			+ "<a href=\"http://miglayout.com/\">MigLayout-Swing 5.2</a><br>"
			+ "<a href=\"https://commons.apache.org/proper/commons-csv\">Apache Commons CSV 1.8</a><br>"
			+ "<a href=\"https://commons.apache.org/proper/commons-codec\">Apache Commons Codec 1.15</a><br>"
			+ "<a href=\"https://commons.apache.org/proper/commons-io\">Apache Commons IO 2.8.0</a><br>"
			+ "<a href=\"https://commons.apache.org/proper/commons-lang\">Commons Lang 2.6</a><br>"
			+ "<a href=\"https://commons.apache.org/proper/commons-logging\">Apache Commons Logging 1.2</a><br>"
			+ "<br><br><center>Soli Deo Gloria - A Deus toda a glória!<center><br><div>";

	/**
	 * Create the dialog.
	 */
	public About() {
		super(DataGui.getInstance(), ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(350, 100, 480, 400);
		setTitle("Sobre");
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JTextPane sobre = new JTextPane();
			sobre.setEditable(false);
			sobre.setFont(new Font("Tahoma", Font.PLAIN, 12));
			if (Utils.getInstance().getCurrentTheme() instanceof DarkTheme)
				sobre.setBackground(MaterialColors.GRAY_500);

			sobre.setContentType("text/html");
			sobre.setText(info);

			sobre.addHyperlinkListener(new HyperlinkListener() {
				public void hyperlinkUpdate(HyperlinkEvent e) {
					if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
						if (Desktop.isDesktopSupported()) {
							try {
								Desktop.getDesktop().browse(e.getURL().toURI());
							} catch (IOException | URISyntaxException e1) {
								e1.printStackTrace();
							} catch (NullPointerException e2) {

							}
						}
					}
				}
			});
			JScrollPane jsp = new JScrollPane(sobre);
			contentPanel.add(jsp, BorderLayout.CENTER);
		}
		{
			JLabel lTitle = new JLabel(Main.TITLE);
			lTitle.setHorizontalAlignment(SwingConstants.CENTER);
			lTitle.setFont(new Font("Dialog", Font.PLAIN, 15));
			lTitle.setIcon(new ImageIcon(getClass().getResource("/FC_S.jpg")));
			lTitle.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent evt) {
					int count = evt.getClickCount();
					if (count == 2) {
						ImageViewer.show(new ImageIcon(getClass().getResource("/FC.jpg")));
					}
				}

			});
			contentPanel.add(lTitle, BorderLayout.NORTH);
		}
		{
			JLabel lblVerso = new JLabel("Vers\u00E3o " + Main.VERSION + " - " + Main.DATA_PUBLICACAO);
			lblVerso.setFont(new Font("Dialog", Font.PLAIN, 13));
			lblVerso.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblVerso, BorderLayout.SOUTH);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.CHECK,
						Utils.getInstance().getCurrentTheme().getColorIcons()));
				Utils.personalizarBotao(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}

				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

	/**
	 * Torna o diálogo visível.
	 */
	public void open() {
		setVisible(true);
	}

}
