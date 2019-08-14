package dad.fam_com_cristo;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;

import org.apache.commons.lang.WordUtils;

import dad.fam_com_cristo.gui.DataGui;
import dad.recursos.ConexaoUser;
import dad.recursos.CriptografiaAES;
import dad.recursos.ImageCompression;
import dad.recursos.Log;

public class Membro {

	public static final String imgPath = System.getenv("APPDATA") + "/IB_Fam_Com_Cristo/Databases/Imagens/";
	public static final String key = "dad";
	public static int countID = 0;
	private int id;
	private static Connection con;
	private static PreparedStatement pst;
	private static ResultSet rs;
	private String nome, profissao, endereco, igreja_origem, telefone, observacoes, motivo_saida;
	private Date data_nascimento, data_batismo, data_termino;
	private boolean casado;
	private ImageIcon img;

	public Membro(String nome, Date data_nascimento, String telefone, int n_emprestimos, boolean adicionar) {
		con = ConexaoUser.getConnection();
		setId(++countID);
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

	public static Membro getUser(String cpf) {
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
		return new Membro(nome, data_nascimento, telefone, n_emprestimos, false);
	}

	public static Membro newUser(String nome, Date data_nascimento, String cpf, String telefone, int n_emprestimos) {
		if (existe(cpf))
			return getUser(cpf);
		else
			return new Membro(nome, data_nascimento, telefone, n_emprestimos, true);
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
	
	public ImageIcon getImg() {
		return img;
	}

	public void setImg(ImageIcon img) {
		this.img = img;
	}
	
	public void addImg() {
		FileDialog fd = new FileDialog(DataGui.getInstance(), "Escolher uma imagem", FileDialog.LOAD);
		fd.setDirectory(System.getProperty("user.home") + System.getProperty("file.separator") + "Pictures");
		fd.setFile("*.jpg");
		fd.setVisible(true);
		String filename = fd.getFile();
		if (filename != null)
			try {
				ImageCompression.compress(new File(fd.getDirectory() + filename), this);
			} catch (IOException e) {
				e.printStackTrace();
				Log.getInstance().printLog("Item - addImg: Erro ao copiar a imagem!");
			}
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProfissao() {
		return profissao;
	}

	public void setProfissao(String profissao) {
		this.profissao = profissao;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getObservacoes() {
		return observacoes;
	}

	public void setObservacoes(String observacoes) {
		this.observacoes = observacoes;
	}

	public String getIgreja_origem() {
		return igreja_origem;
	}

	public void setIgreja_origem(String igreja_origem) {
		this.igreja_origem = igreja_origem;
	}

	public String getMotivo_saida() {
		return motivo_saida;
	}

	public void setMotivo_saida(String motivo_saida) {
		this.motivo_saida = motivo_saida;
	}

	public Date getData_batismo() {
		return data_batismo;
	}

	public void setData_batismo(Date data_batismo) {
		this.data_batismo = data_batismo;
	}

	public Date getData_termino() {
		return data_termino;
	}

	public void setData_termino(Date data_termino) {
		this.data_termino = data_termino;
	}

	public boolean isCasado() {
		return casado;
	}

	public void setCasado(boolean casado) {
		this.casado = casado;
	}

}
