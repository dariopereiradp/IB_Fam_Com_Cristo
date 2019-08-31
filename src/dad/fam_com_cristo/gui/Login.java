package dad.fam_com_cristo.gui;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;

import org.apache.commons.lang.time.DurationFormatUtils;

import dad.recursos.ConexaoLogin;
import dad.recursos.CriptografiaAES;
import dad.recursos.Log;
import dad.recursos.RegistoLogin;
import mdlaf.animation.MaterialUIMovement;
import mdlaf.utils.MaterialColors;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

/**
 * Classe que permite que um funcionário possa fazer o login e entrar no programa.
 * @author Dário Pereira
 *
 */
public class Login {

	private JFrame frame;
	private JTextField user;
	private JPasswordField pass;
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	public static String NOME;
	public static long inicialTime;
	private static Login INSTANCE;

	private Login() {
		INSTANCE = this;
		frame = new JFrame("Igreja Batista Famílias com Cristo - Login");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("/FC.jpg"))));
		frame.setBounds(100, 100, 400, 450);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);

		JLabel txUser = new JLabel("USUÁRIO:");
		txUser.setFont(new Font("Roboto", Font.PLAIN, 13));
		txUser.setBounds(5, 320, 70, 15);
		frame.getContentPane().add(txUser);

		JLabel txSenha = new JLabel("SENHA:");
		txSenha.setFont(new Font("Roboto", Font.PLAIN, 13));
		txSenha.setBounds(5, 351, 70, 15);
		frame.getContentPane().add(txSenha);

		JLabel titulo = new JLabel(Main.TITLE);
		titulo.setFont(new Font("Roboto Black", Font.PLAIN, 18));
		titulo.setHorizontalAlignment(SwingConstants.CENTER);
		titulo.setBounds(5, 229, 380, 39);
		frame.getContentPane().add(titulo);

		JLabel image = new JLabel("");
		image.setHorizontalAlignment(SwingConstants.CENTER);
		image.setIcon(new ImageIcon(Login.class.getResource("/FC_T.png")));
		image.setBounds(95, 81, 200, 137);
		frame.getContentPane().add(image);

		user = new JTextField();
		user.setFont(new Font("Roboto", Font.PLAIN, 15));
		user.setBounds(75, 315, 304, 20);
		user.setBorder(new LineBorder(Color.WHITE, 1));
		user.setMargin(new Insets(0, 0, 20, 0));
		frame.getContentPane().add(user);
		user.setColumns(10);

		pass = new JPasswordField();
		pass.setFont(new Font("Roboto", Font.PLAIN, 14));
		pass.setBounds(75, 346, 304, 20);
		frame.getContentPane().add(pass);

		JButton entrar = new JButton("ENTRAR");
		entrar.setFont(new Font("Roboto", Font.BOLD, 12));
		entrar.setBounds(150, 387, 90, 23);
		entrar.setBackground(MaterialColors.LIGHT_BLUE_600);
		MaterialUIMovement.add(entrar, MaterialColors.GRAY_300, 5, 1000 / 30);
		frame.getContentPane().add(entrar);
		entrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				verify();

			}
		});

		JLabel texto = new JLabel(
				"Escreva o seu nome de utilizador e senha e clique no bot\u00E3o abaixo para entrar no programa");
		texto.setHorizontalAlignment(SwingConstants.CENTER);
		texto.setFont(new Font("Roboto", Font.PLAIN, 9));
		texto.setBounds(5, 270, 390, 20);
		frame.getContentPane().add(texto);
		
		JLabel lblLogin = new JLabel("LOGIN");
		lblLogin.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogin.setFont(new Font("Roboto Black", Font.PLAIN, 18));
		lblLogin.setBounds(5, 31, 380, 39);
		frame.getContentPane().add(lblLogin);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				long time = System.currentTimeMillis() - Main.inicialTime;
				Log.getInstance().printLog("Tempo de Uso: " + DurationFormatUtils.formatDuration(time, "HH'h'mm'm'ss's")
						+ "\nPrograma Terminou");
				System.exit(0);
			}
		});

		frame.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					verify();
			}

		});

		user.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					verify();
			}

		});

		pass.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					verify();
			}

		});
	}

	public static Login getInstance() {
		if (INSTANCE == null) {
			new Login();
		}
		return INSTANCE;
	}

	/**
	 * Verifica se os campos estão preenchidos e se o funcionário existe na base de dados.
	 */
	public void verify() {
		String username = user.getText();
		String password = String.valueOf(pass.getPassword());
		if (username.trim().equals("") || password.trim().equals("")) {
			JOptionPane.showMessageDialog(frame, "Preencha os campos de login!", "ERRO", JOptionPane.ERROR_MESSAGE,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
		} else {
			con = ConexaoLogin.getConnection();
			try {
				pst = con.prepareStatement("select * from logins where nome = ?");
				pst.setString(1, username);
				rs = pst.executeQuery();
				if (!rs.next()) {
					JOptionPane.showMessageDialog(frame, "O usuário não existe!", "ERRO", JOptionPane.ERROR_MESSAGE,
							new ImageIcon(getClass().getResource("/FC_SS.jpg")));
				} else
					login(username, password);
			} catch (SQLException e) {
				e.printStackTrace();
				Log.getInstance().printLog("Login - " + e.getMessage());
			} finally {
				try {
					rs.close();
					pst.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					Log.getInstance().printLog("Login - " + e.getMessage());
				}
			}
		}

	}

	/**
	 * Verifica se a password está correta e faz o login do funcionário no sistema.
	 * @param username
	 * @param password
	 */
	private void login(String username, String password) {
		try {
			CriptografiaAES.setKey(password);
			CriptografiaAES.encrypt(password);
			password = CriptografiaAES.getEncryptedString();

			con = ConexaoLogin.getConnection();
			pst = con.prepareStatement("select * from logins where nome = ? and pass = ?");
			pst.setString(1, username);
			pst.setString(2, password);
			rs = pst.executeQuery();
			if (!rs.next()) {
				JOptionPane.showMessageDialog(frame, "Senha errada!", "ERRO", JOptionPane.ERROR_MESSAGE,
						new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			} else {
				Log.getInstance().printLog("Usuário: " + username + " - Conectado com sucesso!");
				pst = con.prepareStatement(
						"update logins set Num_acessos = Num_acessos + 1,Ultimo_Acesso=? where nome = ?");
				pst.setDate(1, new Date(System.currentTimeMillis()));
				pst.setString(2, username);
				pst.execute();
				NOME = username;
				inicialTime = System.currentTimeMillis();
				frame.setVisible(false);
				DataGui.getInstance().open();
				DataGui.getInstance().newFilter("");
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().printLog("Login - " + e.getMessage());
		} finally {
			try {
				rs.close();
				pst.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Log.getInstance().printLog("Login - " + e.getMessage());
			}
		}

	}

	/**
	 * verifica se é preciso fazer o registo.
	 * @return true - se apenas existe 1 funcionário registrado, que é o admin
	 * 		   <br>false - caso contrário
	 */
	public boolean registo() {
		con = ConexaoLogin.getConnection();
		try {
			pst = con.prepareStatement("select count(*) from logins");
			rs = pst.executeQuery();
			rs.next();
			if (rs.getInt(1) == 1) {
				return true;
			} else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
			Log.getInstance().printLog("Login - " + e.getMessage());
			return false;
		} finally {
			try {
				rs.close();
				pst.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
				Log.getInstance().printLog("Login - " + e.getMessage());
			}
		}
	}

	/**
	 * Torna o diálogo visível, verificando antes se é preciso fazer registro antes ou não (caso não exista funcionário registrado)
	 */
	public void open() {
		if (registo())
			RegistoLogin.getInstance().open(true);
		else {
			frame.setVisible(true);
			user.setText("");
			pass.setText("");
		}
	}

	/**
	 * Torna o diálogo visível.
	 */
	public void openDirect() {
		frame.setVisible(true);
		user.setText("");
		pass.setText("");
	}
}
