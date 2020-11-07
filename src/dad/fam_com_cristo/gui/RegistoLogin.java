package dad.fam_com_cristo.gui;

import java.awt.Color;
import java.awt.Dialog.ModalityType;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
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
import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

/**
 * Classe para fazer um registo de funcionário no programa.
 * 
 * @author Dário Pereira
 *
 */
public class RegistoLogin {

	private JDialog dialog;
	private IconTextField user;
	private IconPasswordField pass;
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private boolean login;
	private static RegistoLogin INSTANCE;

	private RegistoLogin() {
		INSTANCE = this;
		dialog = new JDialog(null, ModalityType.DOCUMENT_MODAL);
		dialog.setTitle("Igreja Batista Famílias com Cristo - Registro");
		dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("/FC.jpg"))));
		dialog.setBounds(100, 100, 480, 430);
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		dialog.getContentPane().setLayout(null);
		dialog.getRootPane().setBorder(BorderFactory.createSoftBevelBorder(BevelBorder.RAISED));

		JLabel image = new JLabel("");
		image.setHorizontalAlignment(SwingConstants.CENTER);
		ImageIcon icon = new ImageIcon(new ImageIcon(Login.class.getResource("/FC-T-Big.png")).getImage()
				.getScaledInstance(240, 200, Image.SCALE_SMOOTH));
		image.setIcon(icon);
		image.setBounds(115, 20, 240, 200);
		dialog.getContentPane().add(image);
		
		JLabel warning = new JLabel(
				"ATEN\u00C7\u00C3O: N\u00E3o esque\u00E7a da senha! Apenas o administrador pode recuper\u00E1-la!");
		warning.setFont(new Font("Dialog", Font.PLAIN, 12));
		warning.setForeground(Color.RED);
		warning.setHorizontalAlignment(SwingConstants.CENTER);
		warning.setBounds(0, 230, 470, 15);
		dialog.getContentPane().add(warning);

		user = new IconTextField();
		user.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.ACCOUNT_CIRCLE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		user.setFont(new Font("Dialog", Font.PLAIN, 15));
		user.setHint("Criar usuário");
		user.setBounds(115, 255, 250, 30);
		dialog.getContentPane().add(user);

		pass = new IconPasswordField();
		pass.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.LOCK,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		pass.setHint("Criar senha");
		pass.setFont(new Font("Dialog", Font.PLAIN, 15));
		pass.setBounds(115, 295, 250, 30);
		dialog.getContentPane().add(pass);

		JButton registar = new JButton("Registrar");
		registar.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.VERIFIED_USER,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		Utils.personalizarBotao(registar);
		registar.setBounds(180, 350, 120, 30);
		dialog.getContentPane().add(registar);
		registar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				registo();

			}
		});

		JCheckBox showPass = new JCheckBox("Mostrar senha");
		pass.setEchoChar('*');
		showPass.setFont(new Font("Dialog", Font.PLAIN, 10));
		showPass.setBounds(10, 345, 110, 25);
		dialog.getContentPane().add(showPass);
		showPass.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					pass.setEchoChar((char) 0);
				} else {
					pass.setEchoChar('*');
				}
			}
		});

		dialog.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				if (login) {
					Utils.close("Programa terminou");
				} else
					dialog.dispose();
			}
		});

		dialog.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					registo();
			}

		});

		user.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					registo();
			}

		});

		pass.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER)
					registo();
			}

		});
	}

	/**
	 * Verifica se os campos foram preenchidos corretamente e se o usuário ainda não
	 * existe e depois faz o registo
	 */
	private void registo() {
		String username = user.getText();
		String password = String.valueOf(pass.getPassword());
		if (username.trim().equals("") || password.trim().equals("")) {
			JOptionPane.showMessageDialog(dialog, "Preencha os campos de registo", "ERRO", JOptionPane.ERROR_MESSAGE,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
		} else {
			con = ConexaoLogin.getConnection();
			try {
				pst = con.prepareStatement("select * from logins where nome = ?");
				pst.setString(1, username);
				rs = pst.executeQuery();
				if (!rs.next()) {
					inserir(username, password);
				} else
					JOptionPane.showMessageDialog(dialog, "O usuário '" + username + "' já existe!", "ERRO",
							JOptionPane.ERROR_MESSAGE, new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			} catch (SQLException e) {
				e.printStackTrace();
				Log.getInstance().printLog("RegistoLogin - " + e.getMessage());
			} finally {
				try {
					rs.close();
					pst.close();
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
					Log.getInstance().printLog("RegistoLogin - " + e.getMessage());
				}
			}
		}

	}

	/**
	 * Insere o funcionário na base de dados.
	 * 
	 * @param username
	 * @param password
	 */
	private void inserir(String username, String password) {
		try {
			CriptografiaAES.setKey(password);
			CriptografiaAES.encrypt(password);
			password = CriptografiaAES.getEncryptedString();

			con = ConexaoLogin.getConnection();
			pst = con.prepareStatement(
					"insert into logins(Nome,Pass,Num_acessos,Ultimo_Acesso,Data_Criacao) values (?,?,?,?,?)");
			pst.setString(1, username);
			pst.setString(2, password);
			pst.setInt(3, 0);
			pst.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			pst.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
			pst.execute();
			TableModelFuncionario.getInstance()
					.addFuncionario(new Funcionario(username, 0, LocalDateTime.now(), LocalDateTime.now()));
			JOptionPane.showMessageDialog(dialog, "O funcionário '" + username + "' foi criado com sucesso!",
					"FUNCIONÁRIO CRIADO", JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			Log.getInstance().printLog("O funcionário '" + username + "' foi criado com sucesso!");
			if (login)
				Login.getInstance().openDirect();
			dialog.dispose();
		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().printLog("RegistoLogin - " + e.getMessage());
		}

	}

	/**
	 * Torna o diálogo visível.
	 * 
	 * @param login
	 */
	public void open(boolean login) {
		this.login = login;
		dialog.setVisible(true);
		user.setText("");
		pass.setText("");
	}

	public static RegistoLogin getInstance() {
		if (INSTANCE == null) {
			new RegistoLogin();
		}
		return INSTANCE;
	}
}
