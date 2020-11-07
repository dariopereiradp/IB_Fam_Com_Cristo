package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.table.conexao.ConexaoFinancas;
import dad.fam_com_cristo.table.conexao.ConexaoLogin;
import dad.fam_com_cristo.table.conexao.ConexaoMembro;
import dad.recursos.IconPasswordField;
import dad.recursos.Log;
import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

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
		setBounds(100, 100, 700, 380);
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
				IconPasswordField pass = Utils.getPasswordField();
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
		contentPanel.add(bAlterarPass);

		pastor = new JTextField();
		pastor.setBounds(215, 70, 410, 30);
		contentPanel.add(pastor);
		pastor.setColumns(10);
		pastor.setText(pastorName);

		JButton bResetMembros = new JButton("APAGAR TODOS OS MEMBROS");
		bResetMembros.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.DELETE_FOREVER,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bResetMembros);
		bResetMembros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (resetMembros()) {
					Utils.close("Programa terminou após fazer reset à database de membros");
				}
			}
		});
		bResetMembros.setBounds(325, 130, 340, 30);
		contentPanel.add(bResetMembros);

		JButton bResetFinancas = new JButton("APAGAR TODAS AS TRANSA\u00C7\u00D5ES");
		bResetFinancas.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.DELETE_FOREVER,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bResetFinancas);
		bResetFinancas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (resetFinancas()) {
					Utils.close("Programa terminou após fazer reset à database de finanças");
				}
			}
		});
		bResetFinancas.setBounds(325, 170, 340, 30);
		contentPanel.add(bResetFinancas);

		JButton bResetFuncionarios = new JButton("APAGAR TODOS OS FUNCION\u00C1RIOS");
		bResetFuncionarios.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.DELETE_FOREVER,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bResetFuncionarios);
		bResetFuncionarios.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (resetFuncionarios()) {
					Utils.close("Programa terminou após fazer reset à database de finanças");
				}
			}
		});
		bResetFuncionarios.setBounds(325, 210, 340, 30);
		contentPanel.add(bResetFuncionarios);

		JButton bDeleteAll = new JButton("APAGAR TUDO");
		bDeleteAll.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.DELETE_FOREVER,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(bDeleteAll);
		bDeleteAll.setBounds(325, 251, 340, 30);
		bDeleteAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (resetAll()) {
					Utils.close("Programa terminou após fazer reset total");
				}
			}
		});
		contentPanel.add(bDeleteAll);
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
							if (Utils.askMessage("ALTERAR NOME DO PASTOR",
									"Tem certeza que quer alterar o nome do pastor titular?")) {
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

		if (!Login.NOME.equals(Main.DEFAULT_USER)) {
			bResetFinancas.setEnabled(false);
			bResetFinancas.setToolTipText("Apenas o utilizador 'admin' pode fazer reset");
			bResetFuncionarios.setEnabled(false);
			bResetFuncionarios.setToolTipText("Apenas o utilizador 'admin' pode fazer reset");
			bResetMembros.setEnabled(false);
			bResetMembros.setToolTipText("Apenas o utilizador 'admin' pode fazer reset");
			bDeleteAll.setEnabled(false);
			bDeleteAll.setToolTipText("Apenas o utilizador 'admin' pode fazer reset");
			bGerirFunc.setEnabled(false);
			bGerirFunc.setToolTipText("Apenas o utilizador 'admin' pode gerir os funcionários!");
		} else {
			bAlterarPass.setEnabled(false);
			bAlterarPass.setToolTipText("Não é possível alterar a senha do administrador");
		}

	}

	/**
	 * Recria a base de dados dos membros
	 * 
	 * @return true se chegou ao fim da etapa e false se o utilizador cancelou,
	 *         errou a senha ou aconteceu uma exceção
	 */
	private boolean resetMembros() {
		try {
			if (Utils.askMessage("Resetar base de dados: membros",
					"Tem a certeza que quer apagar a base de dados de membros?\n"
							+ "ATENÇÃO: ESSA AÇÃO VAI APAGAR TODOS OS MEMBROS E NÃO É POSSÍVEL "
							+ "REVERTER, A NÃO SER RESTAURANDO UMA CÓPIA DE SEGURANÇA!\n"
							+ "(por precaução, vai ser feita uma cópia de segurança da base "
							+ "de dados, antes de ser apagada e o programa vai ser fechado. "
							+ "Assim que abrir o programa outra vez, o processo é concluído!)")) {
				Utils.getInstance().backupDirect();
				new ConexaoMembro().getConnection().close();
				ConexaoMembro.createTable(true);
				return true;
			} else {
				return false;
			}
		} catch (SQLException | IOException e1) {
			Log.getInstance().printLog("Erro ao resetar database de membros: " + e1.getMessage());
			e1.printStackTrace();
			return false;
		}
	}

	/**
	 * Recria a base de dados das financas
	 * 
	 * @return true se chegou ao fim da etapa e false se o utilizador cancelou,
	 *         errou a senha ou aconteceu uma exceção
	 */
	private boolean resetFinancas() {
		try {
			if (Utils.askMessage("Resetar base de dados: finanças",
					"Tem a certeza que quer apagar a base de dados de finanças?\n"
							+ "ATENÇÃO: ESSA AÇÃO VAI APAGAR TODOS AS TRANSAÇÕES E NÃO É POSSÍVEL "
							+ "REVERTER, A NÃO SER RESTAURANDO UMA CÓPIA DE SEGURANÇA!\n"
							+ "(por precaução, vai ser feita uma cópia de segurança da base "
							+ "de dados, antes de ser apagada e o programa vai ser fechado. "
							+ "Assim que abrir o programa outra vez, o processo é concluído!)")) {
				Utils.getInstance().backupDirect();
				new ConexaoFinancas().getConnection().close();
				ConexaoFinancas.createTable(true);
				return true;
			} else {
				return false;
			}
		} catch (SQLException | IOException e1) {
			Log.getInstance().printLog("Erro ao resetar database de financas: " + e1.getMessage());
			e1.printStackTrace();
			return false;
		}
	}

	/**
	 * Recria a base de dados dos funcionarios
	 * 
	 * @return true se chegou ao fim da etapa e false se o utilizador cancelou,
	 *         errou a senha ou aconteceu uma exceção
	 */
	private boolean resetFuncionarios() {
		try {
			if (Utils.askMessage("Resetar base de dados: funcionários",
					"Tem a certeza que quer apagar a base de dados de funcionários?\n"
							+ "ATENÇÃO: ESSA AÇÃO VAI APAGAR TODOS OS FUNCIONÁRIOS E NÃO É POSSÍVEL "
							+ "REVERTER, A NÃO SER RESTAURANDO UMA CÓPIA DE SEGURANÇA!\n"
							+ "(por precaução, vai ser feita uma cópia de segurança da base "
							+ "de dados, antes de ser apagada e o programa vai ser fechado. "
							+ "Assim que abrir o programa outra vez, o processo é concluído!)")) {
				Utils.getInstance().backupDirect();
				new ConexaoMembro().getConnection().close();
				ConexaoMembro.createTable(true);
				return true;
			} else
				return false;
		} catch (SQLException | IOException e1) {
			Log.getInstance().printLog("Erro ao resetar database de funcionários: " + e1.getMessage());
			e1.printStackTrace();
			return false;
		}
	}

	/**
	 * Restaura as definições de fábrica do programa (recria todas as bases de dados
	 * e recria as configurações)
	 * 
	 * @return true se chegou ao fim da etapa e false se o utilizador cancelou,
	 *         errou a senha ou aconteceu uma exceção
	 */
	private boolean resetAll() {
		try {
			if (Utils.askMessage("Restaurar dados de fábrica",
					"Tem a certeza que quer restaurar o programa ao estado original?\n"
							+ "ATENÇÃO: ESSA AÇÃO VAI APAGAR TODOS AS BASES DE DADOS "
							+ "E RESTAURAR AS CONFIGURAÇÕES AO ESTADO DE FÁBRICA! "
							+ "NÃO É POSSÍVEL REVERTER, A NÃO SER RESTAURANDO UMA CÓPIA DE SEGURANÇA\n"
							+ "(por precaução, vai ser feita uma cópia de segurança da base"
							+ " de dados, antes de ser apagada e o programa vai ser fechado."
							+ " Assim que abrir o programa outra vez, o processo é concluído!)")) {
				Utils.getInstance().backupDirect();
				new ConexaoMembro().getConnection().close();
				ConexaoMembro.createTable(true);
				new ConexaoFinancas().getConnection().close();
				ConexaoFinancas.createTable(true);
				ConexaoLogin.getConnection().close();
				ConexaoLogin.createTable(true);
				Utils.getPropertiesFile().delete();
				return true;
			} else
				return false;
		} catch (Exception e1) {
			Log.getInstance().printLog("Erro ao resetar o programa: " + e1.getMessage());
			e1.printStackTrace();
			return false;
		}
	}

	/**
	 * Torna o diálogo visível.
	 */
	public void open() {
		this.setVisible(true);
	}
}
