package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import dad.fam_com_cristo.Main;
import dad.recursos.Log;
import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

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
 * 
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
	private File conf = Utils.getPropertiesFile();
	private String pastorName = Utils.getInstance().getPastorName();

	public Config() {
		super(DataGui.getInstance(), ModalityType.DOCUMENT_MODAL);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setTitle("Configurações");
		setBounds(100, 100, 480, 350);
		setResizable(false);
		getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lBemVindo = new JLabel("Bem vindo! Est\u00E1 ligado como " + Login.NOME);
		lBemVindo.setFont(new Font("Dialog", Font.PLAIN, 14));
		lBemVindo.setBounds(25, 11, 400, 20);
		contentPanel.add(lBemVindo);
		lBemVindo.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lNome = new JLabel("Nome do Pastor Titular: ");
		lNome.setBackground(new Color(255, 255, 255));
		lNome.setBounds(25, 70, 180, 30);
		contentPanel.add(lNome);

		JButton bAddFuncionrio = new JButton("ADICIONAR FUNCION\u00C1RIO");
		bAddFuncionrio.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PERSON_ADD,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bAddFuncionrio);
		bAddFuncionrio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				RegistoLogin.getInstance().open(false);
			}
		});
		bAddFuncionrio.setBounds(25, 130, 240, 30);
		contentPanel.add(bAddFuncionrio);

		JButton bGerirFunc = new JButton("GERIR FUNCION\u00C1RIOS");
		bGerirFunc.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PERSON,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bGerirFunc);
		bGerirFunc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JPasswordField pass = new JPasswordField();
				int ok = JOptionPane.showConfirmDialog(null, pass, "Introduza a senha do administrador",
						JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
						new ImageIcon(getClass().getResource("/FC_SS.jpg")));
				if (ok == JOptionPane.OK_OPTION) {
					if (String.valueOf(pass.getPassword()).equals(Main.DEFAULT_PASS))
						new GerirFuncionarios().open();
					else
						JOptionPane.showMessageDialog(null, "Senha errada!", "SENHA ERRADA", JOptionPane.OK_OPTION,
								new ImageIcon(getClass().getResource("/FC_SS.jpg")));
				}

			}
		});
		bGerirFunc.setBounds(25, 170, 240, 30);
		contentPanel.add(bGerirFunc);
		if (!Login.NOME.equals("admin")) {
			bGerirFunc.setEnabled(false);
			bGerirFunc.setToolTipText("Apenas o utilizador 'admin' pode gerir os funcionários!");
		}

		JButton bAlterarPass = new JButton("ALTERAR SENHA");
		bAlterarPass.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.LOCK_OPEN,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bAlterarPass);
		bAlterarPass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new ChangePassword(Login.NOME, false).open();
			}
		});
		bAlterarPass.setBounds(25, 210, 240, 30);
		if (Login.NOME.equals("admin")) {
			bAlterarPass.setEnabled(false);
			bAlterarPass.setToolTipText("Não é possível alterar a senha do utilizador 'admin'!");
		}
		contentPanel.add(bAlterarPass);

		pastor = new JTextField();
		pastor.setBounds(215, 70, 225, 30);
		contentPanel.add(pastor);
		pastor.setColumns(10);
		pastor.setText(pastorName);
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

					/**
					 * Se o funcionário alterou o nome do pastor, esse método salva um novo ficheiro
					 * 'conf.dad' com o novo nome do pastor.
					 */
					@Override
					public void actionPerformed(ActionEvent e) {
						String nomePastor = pastor.getText();
						if (!nomePastor.equals(pastorName)) {
							int ok = JOptionPane.showOptionDialog(Config.this,
									"Tem certeza que quer alterar o nome do pastor titular?", "ALTERAR NOME DO PASTOR",
									JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
									new ImageIcon(getClass().getResource("/FC_SS.jpg")), Main.OPTIONS, Main.OPTIONS[0]);
							if (ok == JOptionPane.OK_OPTION) {
								JPasswordField pass = new JPasswordField();
								int ok1 = JOptionPane.showConfirmDialog(null, pass,
										"Introduza a senha do administrador", JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE,
										new ImageIcon(getClass().getResource("/FC_SS.jpg")));
								if (ok1 == JOptionPane.OK_OPTION) {
									if (String.valueOf(pass.getPassword()).equals(Main.DEFAULT_PASS)) {
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
				cancelButton.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.CANCEL,
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
	 * Torna o diálogo visível.
	 */
	public void open() {
		this.setVisible(true);
	}
}
