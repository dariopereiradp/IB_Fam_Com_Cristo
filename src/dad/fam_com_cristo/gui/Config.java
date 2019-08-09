package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.MaskFormatter;

import dad.fam_com_cristo.Emprestimo;
import dad.fam_com_cristo.table.TableModelEmprestimo;
import dad.recursos.Log;
import dad.recursos.RegistoLogin;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.awt.Color;
import javax.swing.JFormattedTextField;

public class Config extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1792634540833333974L;
	private final JPanel contentPanel = new JPanel();
	private JFormattedTextField multa;

	public Config() {
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

		JLabel lblValorDaMulta = new JLabel("Valor da Multa (di\u00E1rio): ");
		lblValorDaMulta.setBackground(new Color(255, 255, 255));
		lblValorDaMulta.setFont(new Font("Perpetua", Font.PLAIN, 15));
		lblValorDaMulta.setBounds(25, 72, 145, 30);
		contentPanel.add(lblValorDaMulta);

		MaskFormatter mask;
		try {
			mask = new MaskFormatter("R$ #.##");
			mask.setCommitsOnValidEdit(true);
			multa = new JFormattedTextField(mask);
		} catch (ParseException e1) {
			multa = new JFormattedTextField();
			e1.printStackTrace();
		}
		System.out.println("Multa: " + Emprestimo.MULTA);

		multa.setFont(new Font("Arial", Font.PLAIN, 14));
		multa.setBounds(180, 72, 60, 30);
		multa.setValue("R$ " + String.valueOf(Emprestimo.MULTA));
		contentPanel.add(multa);

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
			bGerirFunc.setToolTipText("Apenas o utilizador 'admin' pode gerir os funcion�rios!");
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
			bAlterarPass.setToolTipText("N�o � poss�vel alterar a senha do utilizador 'admin'!");
		}
		contentPanel.add(bAlterarPass);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						double multaValor = Double.valueOf(multa.getText().trim().replace("R$", ""));
						if (multaValor != Emprestimo.MULTA) {
							int ok = JOptionPane.showConfirmDialog(Config.this,
									"Tem certeza que quer alterar o valor di�rio da multa por atraso?\nAten��o: Essa altera��o ser� v�lida para todos os empr�stimos ativos e futuros!",
									"ALTERAR VALOR DA MULTA", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
									new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
							if (ok == JOptionPane.OK_OPTION) {
								JPasswordField pass = new JPasswordField();
								int ok1 = JOptionPane.showConfirmDialog(null, pass,
										"Introduza a senha do administrador", JOptionPane.OK_CANCEL_OPTION,
										JOptionPane.QUESTION_MESSAGE,
										new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
								if (ok1 == JOptionPane.OK_OPTION) {
									if (String.valueOf(pass.getPassword()).equals(Main.PASS)) {
										Emprestimo.MULTA = multaValor;
										TableModelEmprestimo.getInstance().atualizarMultas();
										File conf = new File(
												System.getenv("APPDATA") + "/BibliotecaDAD/Databases/conf.dad");
										conf.delete();
										try {
											conf.createNewFile();
											PrintWriter pw = new PrintWriter(conf);
											pw.println(multaValor);
											pw.close();
										} catch (FileNotFoundException e1) {
											Log.getInstance().printLog(
													"Erro ao alterar valor da multa: Ficheiro de configura��o n�o encontrado! - "
															+ e1.getMessage());
											e1.printStackTrace();
										} catch (IOException e1) {
											Log.getInstance()
													.printLog("Erro ao alterar valor da multa! - " + e1.getMessage());
											e1.printStackTrace();
										}
										dispose();
									} else
										JOptionPane.showMessageDialog(null, "Senha errada!", "SENHA ERRADA",
												JOptionPane.OK_OPTION,
												new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
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

	public void open() {
		this.setVisible(true);
	}
}
