package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;

import dad.fam_com_cristo.Main;
import dad.recursos.Log;
import dad.recursos.Utils;
import net.lingala.zip4j.ZipFile;

/**
 * Classe que permite escolher o que se deseja restaurar de uma cópia de
 * segurança.
 * 
 * @author Dário Pereira
 *
 */
public class Restauro extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7863423688679744400L;
	private final JPanel contentPanel = new JPanel();
	private File backupFile;
	private JCheckBox conf, membros, financas, funcionarios;

	/**
	 * Create the dialog.
	 */
	public Restauro() {
		super(DataGui.getInstance(), ModalityType.DOCUMENT_MODAL);
		setBounds(100, 100, 350, 300);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel title = new JLabel("RESTAURAR C\u00D3PIA DE SEGURAN\u00C7A");
			title.setBounds(0, 10, 334, 23);
			title.setHorizontalAlignment(SwingConstants.CENTER);
			title.setFont(new Font("Dialog", Font.PLAIN, 17));
			title.setBackground(new Color(60, 179, 113));
			contentPanel.add(title);
		}

		conf = new JCheckBox("Configura\u00E7\u00F5es");
		conf.setSelected(true);
		conf.setFont(new Font("Dialog", Font.PLAIN, 14));
		conf.setBounds(6, 55, 142, 25);
		contentPanel.add(conf);

		membros = new JCheckBox("Membros");
		membros.setSelected(true);
		membros.setFont(new Font("Dialog", Font.PLAIN, 14));
		membros.setBounds(6, 90, 142, 25);
		contentPanel.add(membros);

		financas = new JCheckBox("Finan\u00E7as");
		financas.setSelected(true);
		financas.setFont(new Font("Dialog", Font.PLAIN, 14));
		financas.setBounds(6, 125, 142, 25);
		contentPanel.add(financas);

		JLabel lInfo = new JLabel("Selecione quais itens deseja restaurar");
		lInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lInfo.setBounds(0, 35, 334, 14);
		contentPanel.add(lInfo);

		funcionarios = new JCheckBox("Funcion\u00E1rios");
		funcionarios.setSelected(true);
		funcionarios.setFont(new Font("Dialog", Font.PLAIN, 14));
		funcionarios.setBounds(6, 160, 142, 25);
		contentPanel.add(funcionarios);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						String message = "Tem certeza que quer restaurar a cópia de segurança selecionada?\n"
								+ "Tenha atenção que os dados selecionados serão perdidos e substituídos pelos dados da cópia!\n"
								+ "Se clicar em 'Sim', o programa vai ser fechado. Quando você abrir outra vez os dados da cópia estarão restaurados.\n"
								+ "Obs: Por segurança, vai ser realizada uma cópia de segurança dos dados atuais. Essa cópia pode ser restaurada mais tarde.";
						if(Utils.askMessage("Restaurar Cópia de Segurança", message)) {
							Utils.getInstance().backupDirect();
							restaurar();
						}
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancelar");
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
	 * Extrai os arquivos da cópia de segurança para uma pasta temporária,
	 * mantendo apenas aqueles que foram selecionados nas check-box e depois
	 * encerra o programa.
	 */
	private void restaurar() {
		String tempDir = Main.DATA_DIR + "temp/";
		File temp = new File(tempDir);
		temp.mkdirs();
		try {
			ZipFile zip = new ZipFile(backupFile);
			zip.extractAll(temp.getPath());
			if (!conf.isSelected()) {
				File confFile = new File(tempDir + "conf.dad");
				confFile.delete();
			}
			if (!membros.isSelected()) {
				File membrosFile = new File(tempDir + "membros.accdb");
				membrosFile.delete();
				File images = new File(tempDir + "Imagens/");
				FileUtils.deleteDirectory(images);
			}
			if (!financas.isSelected()) {
				File financasFile = new File(tempDir + "financas.accdb");
				financasFile.delete();
			}
			if (funcionarios.isSelected()) {
				File funcFile = new File(tempDir + "logins.accdb");
				funcFile.delete();
			}
			zip.close();
			Utils.close("O programa terminou, para restaurar a cópia de segurança!");
		} catch (Exception e) {
			Log.getInstance()
					.printLog("Ocorreram alguns erros ao restaurar a cópia de segurança... - " + e.getMessage());
			e.printStackTrace();
		}

	}

	/**
	 * Abre um JFileChooser para escolher a base de dados que se deseja
	 * restaurar, depois abre os diálogos.
	 */
	public void open() {
		JFileChooser jfc = new JFileChooser(Main.BACKUP_DIR);
		jfc.setDialogTitle("Selecione o arquivo da cópia de segurança que deseja restaurar");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de backup (*.fccb)", "fccb"));
		jfc.setAcceptAllFileFilterUsed(false);
		if (jfc.showOpenDialog(DataGui.getInstance()) == JFileChooser.APPROVE_OPTION) {
			backupFile = jfc.getSelectedFile();
			setVisible(true);
		} else {
			dispose();
		}
	}
}
