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

import dad.fam_com_cristo.table.conexao.ConexaoLogin;
import dad.recursos.CriptografiaAES;
import dad.recursos.IconPasswordField;
import dad.recursos.IconTextField;
import dad.recursos.Log;
import dad.recursos.RoundedBorder;
import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Image;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Classe que permite que um funcionário possa fazer o login e entrar no
 * programa.
 * 
 * @author Dário Pereira
 *
 */
public class Login {

	private JFrame frame;
	private IconTextField user;
	private IconPasswordField pass;
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

		recreate();
	}

	/**
	 * 
	 */
	public void recreate() {
		JLabel image = new JLabel("");
		image.setHorizontalAlignment(SwingConstants.CENTER);
		ImageIcon icon = new ImageIcon(new ImageIcon(Login.class.getResource("/FC-T-Big.png")).getImage()
				.getScaledInstance(290, 240, Image.SCALE_SMOOTH));
		image.setIcon(icon);
		image.setBounds(40, 22, 325, 270);
		frame.getContentPane().add(image);

		user = new IconTextField();
		user.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.ACCOUNT_CIRCLE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		user.setHint("Usuário");
		user.setFont(new Font("Dialog", Font.PLAIN, 15));
		user.setBounds(100, 300, 200, 30);
		frame.getContentPane().add(user);

		pass = new IconPasswordField();
		pass.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.LOCK,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		pass.setHint("Senha");
		pass.setFont(new Font("Dialog", Font.PLAIN, 15));
		pass.setBounds(100, 335, 200, 30);
		frame.getContentPane().add(pass);

		JButton entrar = new JButton("ENTRAR");
		entrar.setBounds(140, 380, 130, 30);
		entrar.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.CHECK_CIRCLE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(entrar);
		entrar.setBorder(new RoundedBorder(10));
		frame.getContentPane().add(entrar);
		entrar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				verify();

			}
		});
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

	/**
	 * Verifica se os campos estão preenchidos e se o funcionário existe na base de
	 * dados.
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
	 * 
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
	 * 
	 * @return true - se apenas existe 1 funcionário registrado, que é o admin <br>
	 *         false - caso contrário
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
	 * Torna o diálogo visível, verificando antes se é preciso fazer registro antes
	 * ou não (caso não exista funcionário registrado)
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
	
	public JFrame getFrame() {
		return frame;
	}
	
	public static Login getInstance() {
		if (INSTANCE == null) {
			new Login();
		}
		return INSTANCE;
	}
}
