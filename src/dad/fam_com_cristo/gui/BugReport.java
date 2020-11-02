package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import dad.fam_com_cristo.Main;
import dad.recursos.Log;
import dad.recursos.Utils;
import dad.recursos.ZipCompress;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;
import net.lingala.zip4j.ZipFile;

/**
 * Classe que representa um Di�logo para reportar bugs no programa.
 * 
 * @author D�rio Pereira
 *
 */
public class BugReport extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5804659379003860184L;
	private final JPanel contentPanel = new JPanel();
	private JTextArea text;

	/**
	 * Create the dialog.
	 */
	public BugReport() {
		super(DataGui.getInstance(), ModalityType.DOCUMENT_MODAL);
		setTitle("Bug Report");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 350);
		getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			JLabel lblReportarErro = new JLabel("REPORTAR ERRO");
			lblReportarErro.setIcon(MaterialImageFactory.getInstance().getImage(
	                MaterialIconFont.BUG_REPORT,
	                Utils.getInstance().getCurrentTheme().getColorIcons()));
			lblReportarErro.setFont(new Font("Dialog", Font.PLAIN, 17));
			lblReportarErro.setHorizontalAlignment(SwingConstants.CENTER);
			contentPanel.add(lblReportarErro, BorderLayout.NORTH);
		}
			JPanel panel = new JPanel(new BorderLayout());
			contentPanel.add(panel, BorderLayout.CENTER);
		{
			text = new JTextArea();
			text.setFont(new Font("Dialog", Font.PLAIN, 16));
			text.setLineWrap(true);
			text.setWrapStyleWord(true);
			JScrollPane jsp = new JScrollPane(text);
			panel.add(jsp, BorderLayout.CENTER);
		}
		{
			JLabel lblEscrevaEmDetalhes = new JLabel("Escreva em detalhes o problema e clique em 'OK'");
			lblEscrevaEmDetalhes.setHorizontalAlignment(SwingConstants.CENTER);
			panel.add(lblEscrevaEmDetalhes, BorderLayout.NORTH);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setIcon(MaterialImageFactory.getInstance().getImage(
		                MaterialIconFont.CHECK,
		                Utils.getInstance().getCurrentTheme().getColorIcons()));
				Utils.personalizarBotao(okButton);
				okButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						criarRelatorio();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
				cancelButton.setIcon(MaterialImageFactory.getInstance().getImage(
		                MaterialIconFont.CANCEL,
		                Utils.getInstance().getCurrentTheme().getColorIcons()));
				Utils.personalizarBotao(cancelButton);
				cancelButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();						
					}
				});
				buttonPane.add(cancelButton);
			}
		}
	}
	
	/**
	 * Cria um ficheiro '.zip' com os ficheiros log do m�s atual e com um
	 * ficheiro '.txt' contendo a descri��o do problema.
	 */
	private void criarRelatorio() {
		String data_hora = new SimpleDateFormat("dd/MM/yyyy '�s' HH'h'mm'm'ss").format(new Date());
		String data = new SimpleDateFormat("ddMMMyyyy_HH'h'mm").format(new Date());
		String info = Main.TITLE + " - " + Main.VERSION + "\n" + data_hora + "\n\nRelat�rio criado por: " + Login.NOME + "\n\n" + text.getText();
		String path_temp = Main.DATA_DIR + Main.SIGLA + "_Relatorio_v." + Main.VERSION + "_" + data + ".txt"; 
		File file_text = new File(path_temp);
		String month_year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMyyyy")).toUpperCase();
		String name = Main.SIGLA + "_Relatorio_v." + Main.VERSION + "_" + data + ".zip";
		try (FileWriter fw = new FileWriter(file_text, false);) {
			fw.write(info);
			
			Log.getInstance().close();
			
			ZipCompress.compress(Main.DATA_DIR + "Logs/" + month_year + "/", name, Main.BUG_REPORTS_DIR);
			
			Log.getInstance().open();
			
		} catch (NullPointerException | IOException e) {
			String message = "Erro ao criar o relat�rio de erros! - " + e.getMessage();
			JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			Log.getInstance().printLog(message);
		}
		try{
		ZipFile zip = new ZipFile(Main.BUG_REPORTS_DIR + name);
		zip.addFile(file_text);
		
		String bName = Utils.getInstance().backupDirect();
		
		zip.addFile(new File(bName));
		
		String message = "Relat�rio criado com sucesso em: " + data_hora + "\nEnvie o arquivo de relat�rio '" + name + "' para '" + Main.EMAIL_SUPORTE + "' "
				+ "\npara que possamos resolver o problema o mais r�pido poss�vel! Pedimos desculpa pelos incovenientes causados!";
		JOptionPane.showMessageDialog(null, message, "Sucesso", JOptionPane.INFORMATION_MESSAGE,
				new ImageIcon(getClass().getResource("/FC_SS.jpg")));
		Desktop.getDesktop().open(new File(Main.BUG_REPORTS_DIR));
		Log.getInstance().printLog(message);
		file_text.delete();
		dispose();
		} catch (Exception e) {
			String message = "Erro ao criar o relat�rio de erros! - " + e.getMessage();
			JOptionPane.showMessageDialog(null, message, "Erro", JOptionPane.ERROR_MESSAGE,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			Log.getInstance().printLog(message);
		}
		
	}

	/**
	 * Torna o di�logo vis�vel.
	 */
	public void open(){
		setVisible(true);
	}

}
