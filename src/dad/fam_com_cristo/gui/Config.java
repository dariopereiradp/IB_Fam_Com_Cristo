package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import dad.fam_com_cristo.table.GerirFuncionarios;
import dad.recursos.Log;
import dad.recursos.RegistoLogin;
import dad.recursos.Utils;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.awt.Color;
import javax.swing.JTextField;

/**
 * Classe que representa o diálogo de configurações.
 * @author Dário Pereira
 *
 */
public class Config extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1792634540833333974L;
	private final JPanel contentPanel = new JPanel();
	private JTextField pastor;
	private File conf = Utils.getInstance().getPropertiesFile();
	private String pastorName = "";

	public Config() {
		super(DataGui.getInstance(), ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblConfiguraes = new JLabel("CONFIGURA\u00C7\u00D5ES");
		lblConfiguraes.setBackground(new Color(60, 179, 113));
		lblConfiguraes.setFont(new Font("Dialog", Font.PLAIN, 17));
		lblConfiguraes.setHorizontalAlignment(SwingConstants.CENTER);
		lblConfiguraes.setBounds(0, 0, 444, 25);
		contentPanel.add(lblConfiguraes);

		JLabel lNome = new JLabel("Nome do Pastor Titular: ");
		lNome.setBackground(new Color(255, 255, 255));
		lNome.setFont(new Font("Perpetua", Font.PLAIN, 15));
		lNome.setBounds(25, 72, 145, 30);
		contentPanel.add(lNome);

		JButton bAddFuncionrio = new JButton("ADICIONAR FUNCION\u00C1RIO");
		bAddFuncionrio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistoLogin.getInstance().open(false);
			}
		});
		bAddFuncionrio.setBounds(25, 130, 240, 25);
		contentPanel.add(bAddFuncionrio);

		JLabel lBemVindo = new JLabel("Bem vindo! Est\u00E1 ligado como " + Login.NOME);
		lBemVindo.setBounds(90, 33, 260, 20);
		contentPanel.add(lBemVindo);
		lBemVindo.setHorizontalAlignment(SwingConstants.CENTER);

		JButton bGerirFunc = new JButton("GERIR FUNCION\u00C1RIOS");
		bGerirFunc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPasswordField pass = new JPasswordField();
				int ok = JOptionPane.showConfirmDialog(null, pass, "Introduza a senha do administrador",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
				if (ok == JOptionPane.OK_OPTION) {
					if (String.valueOf(pass.getPassword()).equals(Main.PASS))
						new GerirFuncionarios().open();
					else
						JOptionPane.showMessageDialog(null, "Senha errada!", "SENHA ERRADA", JOptionPane.OK_OPTION,
								new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
				}

			}
		});
		bGerirFunc.setBounds(25, 166, 240, 25);
		contentPanel.add(bGerirFunc);
		if (!Login.NOME.equals("admin")) {
			bGerirFunc.setEnabled(false);
			bGerirFunc.setToolTipText("Apenas o utilizador 'admin' pode gerir os funcionários!");
		}

		JButton bAlterarPass = new JButton("ALTERAR SENHA");
		bAlterarPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChangePassword(Login.NOME, false).open();
			}
		});
		bAlterarPass.setBounds(25, 202, 240, 25);
		if (Login.NOME.equals("admin")) {
			bAlterarPass.setEnabled(false);
			bAlterarPass.setToolTipText("Não é possível alterar a senha do utilizador 'admin'!");
		}
		contentPanel.add(bAlterarPass);
		
		FileInputStream input;
		try {
			input = new FileInputStream(conf);
			Properties prop = new Properties();
			prop.load(input);
			pastorName = prop.getProperty(Main.PASTOR, "");
			input.close();
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		

		pastor = new JTextField();
		pastor.setBounds(183, 77, 240, 25);
		contentPanel.add(pastor);
		pastor.setColumns(10);
		pastor.setText(pastorName);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					
					/**
					 * Se o funcionário alterou o nome do pastor, esse método salva um novo ficheiro 'conf.dad' com o novo nome do pastor.
					 */
					@Override
					public void actionPerformed(ActionEvent e) {
						String nomePastor = pastor.getText();
						if (!nomePastor.equals(pastorName)) {
							int ok = JOptionPane.showOptionDialog(Config.this,
									"Tem certeza que quer alterar o nome do pastor titular?", "ALTERAR NOME DO PASTOR",
									JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
									new ImageIcon(getClass().getResource("/FC_SS.jpg")), Main.OPTIONS, Main.OPTIONS[1]);
							if (ok == JOptionPane.OK_OPTION) {
								JPasswordField pass = new JPasswordField();
								int ok1 = JOptionPane.showConfirmDialog(null, pass,
										"Introduza a senha do administrador", JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE,
										new ImageIcon(getClass().getResource("/FC_SS.jpg")));
								if (ok1 == JOptionPane.OK_OPTION) {
									if (String.valueOf(pass.getPassword()).equals(Main.PASS)) {
										try {
											FileInputStream input = new FileInputStream(conf);
											Properties prop = new Properties();
											prop.load(input);
											input.close();
											FileOutputStream output = new FileOutputStream(conf);
											prop.setProperty(Main.PASTOR, nomePastor);
											prop.store(output, Main.AVISO_INI);
											output.close();
											pastorName = nomePastor;
											Log.getInstance().printLog("Nome do pastor titular alterado com sucesso!");
										} catch (FileNotFoundException e1) {
											Log.getInstance().printLog(
													"Erro ao alterar o nome do pastor: Ficheiro de configuração não encontrado! - "
															+ e1.getMessage());
											e1.printStackTrace();
										} catch (IOException e1) {
											Log.getInstance()
													.printLog("Erro ao alterar o nome do pastor! - " + e1.getMessage());
											e1.printStackTrace();
										}
										dispose();
									} else
										JOptionPane.showMessageDialog(null, "Senha errada!", "SENHA ERRADA",
												JOptionPane.OK_OPTION,
												new ImageIcon(getClass().getResource("/FC_SS.jpg")));
								}
							}
						} else {
							dispose();
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
	 * Torna o diálogo visível.
	 */
	public void open() {
		this.setVisible(true);
	}
}
