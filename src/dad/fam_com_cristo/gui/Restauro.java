package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import dad.recursos.Log;
import dad.recursos.ZipCompress;
import net.lingala.zip4j.ZipFile;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.JCheckBox;

public class Restauro extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7863423688679744400L;
	private final JPanel contentPanel = new JPanel();
	private File backupFile;
	private JCheckBox conf, func, livros, emprestimos, clientes;

	/**
	 * Create the dialog.
	 */
	public Restauro() {
		setBounds(100, 100, 350, 300);
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
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
		conf.setBounds(6, 56, 142, 25);
		contentPanel.add(conf);

		func = new JCheckBox("Funcion\u00E1rios");
		func.setSelected(true);
		func.setFont(new Font("Dialog", Font.PLAIN, 14));
		func.setBounds(6, 91, 142, 25);
		contentPanel.add(func);

		livros = new JCheckBox("Livros");
		livros.setSelected(true);
		livros.setFont(new Font("Dialog", Font.PLAIN, 14));
		livros.setBounds(6, 126, 142, 25);
		contentPanel.add(livros);

		emprestimos = new JCheckBox("Empr\u00E9stimos");
		emprestimos.setSelected(true);
		emprestimos.setFont(new Font("Dialog", Font.PLAIN, 14));
		emprestimos.setBounds(6, 161, 142, 25);
		contentPanel.add(emprestimos);

		clientes = new JCheckBox("Clientes");
		clientes.setSelected(true);
		clientes.setFont(new Font("Dialog", Font.PLAIN, 14));
		clientes.setBounds(6, 196, 142, 25);
		contentPanel.add(clientes);

		JLabel lInfo = new JLabel("Selecione quais itens deseja restaurar");
		lInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lInfo.setBounds(0, 35, 334, 14);
		contentPanel.add(lInfo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						int ok = JOptionPane.showConfirmDialog(null,
								"Tem certeza que quer restaurar a c�pia de seguran�a selecionada?\n"
										+ "Tenha aten��o que os dados selecionados ser�o perdidos e substitu�dos pelos dados da c�pia!\n"
										+ "Se clicar em 'Yes', o programa vai ser fechado. Quando voc� abrir outra vez os dados da c�pia estar�o restaurados.\n"
										+ "Obs: Por seguran�a, vai ser realizada uma c�pia de seguran�a dos dados atuais. Essa c�pia pode ser restaurada mais tarde.",
								"Restaurar C�pia de Seguran�a", JOptionPane.YES_NO_OPTION,
								JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
						if (ok == JOptionPane.YES_OPTION) {
							String name = "BibliotecaDAD-Backup-"
									+ new SimpleDateFormat("ddMMMyyyy-HH'h'mm").format(new Date());
							ZipCompress.compress(Main.DATABASE_DIR, name, Main.BACKUP_DIR);
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

	public void restaurar() {
		String tempDir = Main.DATA_DIR + "temp/";
		File temp = new File(tempDir);
		temp.mkdirs();
		try {
			new ZipFile(backupFile).extractAll(temp.getPath());
			if (!conf.isSelected()) {
				File confFile = new File(tempDir + "conf.dad");
				confFile.delete();
			}
			if (!func.isSelected()) {
				File funcFile = new File(tempDir + "logins.mdb");
				funcFile.delete();
			}
			if (!livros.isSelected()) {
				File livrosFile = new File(tempDir + "livros.mdb");
				livrosFile.delete();
				File images = new File(tempDir + "Imagens/");
				FileUtils.deleteDirectory(images);
			}
			if (!emprestimos.isSelected()) {
				File empFile = new File(tempDir + "emprestimos.mdb");
				empFile.delete();
			}
			if (!clientes.isSelected()) {
				File userFile = new File(tempDir + "users.mdb");
				userFile.delete();
			}
			long time = System.currentTimeMillis() - Main.inicialTime;
			Log.getInstance().printLog("Tempo de Uso: " + DurationFormatUtils.formatDuration(time, "HH'h'mm'm'ss's")
					+ "\nO programa terminou, para restaurar a c�pia de seguran�a!");
			System.exit(0);
		} catch (Exception e) {
			Log.getInstance()
					.printLog("Ocorreram alguns erros ao restaurar a c�pia de seguran�a... - " + e.getMessage());
			e.printStackTrace();
		}

	}

	public void open() {
		JFileChooser jfc = new JFileChooser(Main.BACKUP_DIR);
		jfc.setDialogTitle("Selecione o arquivo da c�pia de seguran�a que deseja restaurar");
		jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		jfc.setFileFilter(new FileNameExtensionFilter("Arquivo de backup DAD (*.dadb)", "dadb"));
		jfc.setAcceptAllFileFilterUsed(false);
		if (jfc.showOpenDialog(DataGui.getInstance()) == JFileChooser.APPROVE_OPTION) {
			backupFile = jfc.getSelectedFile();
			setVisible(true);
		} else {
			dispose();
		}
	}
}
