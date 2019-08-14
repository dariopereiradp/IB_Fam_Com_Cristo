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
import dad.recursos.ConexaoMembro;
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
	private Tipo_Membro tipo_membro;
	private Sexo sexo;
	private Estado_Civil estado_civil;
	private String nome, profissao, endereco, igreja_origem, email, telefone, observacoes;
	private Date data_nascimento, data_batismo, membro_desde;
	private Sim_Nao batizado;
	private ImageIcon img;

	public Membro(String nome, Date data_nascimento, Sexo sexo, Estado_Civil estado_civil, String profissao,
			String endereco, String telefone, String email, String igreja_origem, Tipo_Membro tipo_membro,
			Date membro_desde, Date data_batismo, String observacoes, ImageIcon img) {
		con = ConexaoMembro.getConnection();
		setId(++countID);
		this.nome = WordUtils.capitalize(nome);
		this.data_nascimento = data_nascimento;
		this.setSexo(sexo);
		this.setEstado_civil(estado_civil);
		this.profissao = profissao;
		this.endereco = endereco;
		this.telefone = telefone;
		this.setEmail(email);
		this.igreja_origem = igreja_origem;
		this.setTipo_membro(tipo_membro);
		this.setMembro_desde(membro_desde);
		this.data_batismo = data_batismo;
		this.observacoes = observacoes;
		this.img = img;
		setBatizado();
	}

	public void adicionarNaBaseDeDados() {
		try {
			CriptografiaAES.setKey(key);
			// CriptografiaAES.encrypt(cpf);
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
			// CriptografiaAES.encrypt(cpf);
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
			// String cpf = this.cpf;
			pst = con.prepareStatement("update usuarios set nome=?,Data_Nascimento=? where cpf=?");
			CriptografiaAES.setKey(key);
			// CriptografiaAES.encrypt(cpf);
			// cpf = CriptografiaAES.getEncryptedString();
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
			con = ConexaoMembro.getConnection();
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public Date getData_batismo() {
		return data_batismo;
	}

	public void setData_batismo(Date data_batismo) {
		this.data_batismo = data_batismo;
	}

	public Date getMembro_desde() {
		return membro_desde;
	}

	public void setMembro_desde(Date membro_desde) {
		this.membro_desde = membro_desde;
	}

	public Tipo_Membro getTipo_membro() {
		return tipo_membro;
	}

	public void setTipo_membro(Tipo_Membro tipo_membro) {
		this.tipo_membro = tipo_membro;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Estado_Civil getEstado_civil() {
		return estado_civil;
	}

	public void setEstado_civil(Estado_Civil estado_civil) {
		this.estado_civil = estado_civil;
	}

	public boolean isBatizado() {
		if(batizado==Sim_Nao.SIM)
			return true;
		return false;
	}
	
	public Sim_Nao eBatizado(){
		return batizado;
	}

	public void setBatizado() {
		if (tipo_membro == Tipo_Membro.CONGREGADO) {
			batizado = Sim_Nao.NAO;
		} else if (tipo_membro == Tipo_Membro.LIDERANCA || tipo_membro == Tipo_Membro.MEMBRO_ATIVO
				|| tipo_membro == Tipo_Membro.MEMBRO_NOMINAL)
			batizado = Sim_Nao.SIM;
	}

	public void setBatizado(Sim_Nao sim_nao) {
		batizado = sim_nao;
	}
}
