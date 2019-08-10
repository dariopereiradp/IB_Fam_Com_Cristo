package dad.fam_com_cristo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;

import org.apache.commons.lang.WordUtils;

import dad.recursos.ConexaoUser;
import dad.recursos.CriptografiaAES;

public class User {

	public static final String key = "dad";
	private static Connection con;
	private static PreparedStatement pst;
	private static ResultSet rs;
	private String nome, profissao, endereco, igreja_origem, telefone, observacoes, motivo_saida;
	private Date data_nascimento, data_batismo, data_termino;
	private boolean casado;
	private ImageIcon img;

	public User(String nome, Date data_nascimento, String telefone, int n_emprestimos, boolean adicionar) {
		con = ConexaoUser.getConnection();
		nome = WordUtils.capitalize(nome);
		this.setNome(nome);
		this.setData_nascimento(data_nascimento);;
		if (telefone.length() == 11)
			this.setTelefone(telefone);
		else
			this.setTelefone("00000000000");
		if (adicionar) {
			adicionarNaBaseDeDados();
		}
	}

	public void adicionarNaBaseDeDados() {
		try {
			CriptografiaAES.setKey(key);
//			CriptografiaAES.encrypt(cpf);
			pst = con.prepareStatement("insert into usuarios(CPF,Nome,Data_Nascimento) values (?,?,?)");
			pst.setString(1, CriptografiaAES.getEncryptedString());
			pst.setString(2, getNome());
			String data = new SimpleDateFormat("yyyy-M-d").format(data_nascimento);
			pst.setDate(3, java.sql.Date.valueOf(data));
			pst.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void removerBaseDeDados() {
		try {
			CriptografiaAES.setKey(key);
//			CriptografiaAES.encrypt(cpf);
			pst = con.prepareStatement("delete from usuarios where CPF=?");
			pst.setString(1, CriptografiaAES.getEncryptedString());
			pst.execute();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getData_nascimento() {
		return data_nascimento;
	}

	public void setData_nascimento(Date data_nascimento) {
		this.data_nascimento = data_nascimento;
	}


	public void atualizarDados() {
		try {
//			String cpf = this.cpf;
			pst = con.prepareStatement("update usuarios set nome=?,Data_Nascimento=? where cpf=?");
			CriptografiaAES.setKey(key);
//			CriptografiaAES.encrypt(cpf);
//			cpf = CriptografiaAES.getEncryptedString();
			pst.setString(1, getNome());
			String data = new SimpleDateFormat("yyyy-M-d").format(data_nascimento);
			pst.setDate(2, java.sql.Date.valueOf(data));
			pst.setString(3, CriptografiaAES.getEncryptedString());
			pst.execute();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static boolean existe(String cpf) {
		try {
			CriptografiaAES.setKey(key);
			CriptografiaAES.encrypt(cpf);
			cpf = CriptografiaAES.getEncryptedString();
			con = ConexaoUser.getConnection();
			pst = con.prepareStatement("select * from usuarios where cpf = ?");
			pst.setString(1, cpf);
			rs = pst.executeQuery();
			if (rs.next())
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public static User getUser(String cpf) {
		String nome = "";
		Date data_nascimento = new Date();
		String telefone = "";
		int n_emprestimos = 0;
		try {
			CriptografiaAES.setKey(key);
			CriptografiaAES.encrypt(cpf);
			con = ConexaoUser.getConnection();
			pst = con.prepareStatement("select * from usuarios where cpf=?");
			pst.setString(1, CriptografiaAES.getEncryptedString());
			rs = pst.executeQuery();
			rs.next();
			nome = rs.getString(2);
			data_nascimento = rs.getDate(3);
			n_emprestimos = rs.getInt(4);
			telefone = rs.getString(5);
			if (telefone == null)
				telefone = "-";
			// data_nascimento =
			// DateFormat.getDateInstance().parse(rs.getString(3));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new User(nome, data_nascimento, telefone, n_emprestimos, false);
	}

	public static User newUser(String nome, Date data_nascimento, String cpf, String telefone, int n_emprestimos) {
		if (existe(cpf))
			return getUser(cpf);
		else
			return new User(nome, data_nascimento, telefone, n_emprestimos, true);
	}

	@Override
	public String toString() {
		return nome;
	}

	public String toText() {
		return nome + " | " + new SimpleDateFormat("dd/MM/yyyy").format(data_nascimento);
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

}
