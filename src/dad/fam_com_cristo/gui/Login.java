package dad.fam_com_cristo.gui;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import dad.fam_com_cristo.table.conexao.ConexaoLogin;
import dad.fam_com_cristo.table.models.TableModelFuncionario;
import dad.fam_com_cristo.types.Funcionario;
import dad.recursos.CriptografiaAES;
import dad.recursos.IconPasswordField;
import dad.recursos.IconTextField;
import dad.recursos.Log;
import dad.recursos.RoundedBorder;
import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

/**
 * Classe que permite que um funcion�rio possa fazer o login e entrar no
 * programa.
 * 
 * @author D�rio Pereira
 *
 */
public class Login {

	private JFrame frame;
	private IconTextField user;
	private IconPasswordField pass;
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private Funcionario funcionario;
	public static long inicialTime;
	private static Login INSTANCE;

	private Login() {
		INSTANCE = this;
		frame = new JFrame("Igreja Batista Fam�lias com Cristo - Login");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("/FC.jpg"))));
		frame.setBounds(100, 100, 400, 460);
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));

		recreate();
	}

	/**
	 * Usado para controlar o tema (dark/light) para garantir que os bot�es e tudo o resto muda a cor
	 */
	public void recreate() {
		JLabel image = new JLabel("");
		image.setHorizontalAlignment(SwingConstants.CENTER);
		ImageIcon icon = new ImageIcon(new ImageIcon(Login.class.getResource("/FC-T-Big.png")).getImage()
				.getScaledInstance(290, 240, Image.SCALE_SMOOTH));
		image.setIcon(icon);
		image.setBounds(50, 20, 290, 240);
		frame.getContentPane().add(image);

		user = new IconTextField();
		user.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.ACCOUNT_CIRCLE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		user.setHint("Usu�rio");
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
		entrar.setBounds(130, 380, 140, 30);
		entrar.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.CHECK_CIRCLE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(entrar);
		entrar.setBorder(new RoundedBorder(10));
		frame.getContentPane().add(entrar);
		entrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verify();
			}
		});
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				Utils.close("Programa terminou");
			}
		});

		frame.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					verify();
			}
		});

		user.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					verify();
			}
		});

		pass.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					verify();
			}
		});
	}

	/**
	 * Verifica se os campos est�o preenchidos e se o funcion�rio existe na base de
	 * dados.
	 */
	private void verify() {
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
					JOptionPane.showMessageDialog(frame, "O usu�rio n�o existe!", "ERRO", JOptionPane.ERROR_MESSAGE,
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
	 * Verifica se a password est� correta e faz o login do funcion�rio no sistema.
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
				Log.getInstance().printLog("Usu�rio: " + username + " - Conectado com sucesso!");
				funcionario = TableModelFuncionario.getInstance().getFuncionario(username);
				funcionario.registerLogin();
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
	 * verifica se � preciso fazer o registo.
	 * 
	 * @return true - se apenas existe 1 funcion�rio registrado, que � o admin <br>
	 *         false - caso contr�rio
	 */
	private boolean registo() {
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
	 * Usado para controlar a mudan�a de tema (dark/light)
	 * @return
	 */
	public JFrame getFrame() {
		return frame;
	}

	/**
	 * Torna o di�logo vis�vel, verificando antes se � preciso fazer registro antes
	 * ou n�o (caso n�o exista funcion�rio registrado)
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
	 * Torna o di�logo vis�vel.
	 */
	public void openDirect() {
		frame.setVisible(true);
		user.setText("");
		pass.setText("");
	}
	
	public Funcionario getFuncionario() {
		return funcionario;
	}
	
	public static Login getInstance() {
		if (INSTANCE == null) {
			new Login();
		}
		return INSTANCE;
	}
}
