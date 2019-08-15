package dad.fam_com_cristo;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;

import org.apache.commons.lang.WordUtils;

import dad.fam_com_cristo.gui.DataGui;
import dad.recursos.ConexaoMembro;
import dad.recursos.ImageCompression;
import dad.recursos.Log;

public class Membro {

	public static final String imgPath = System.getenv("APPDATA") + "/IB_Fam_Com_Cristo/Databases/Imagens/";
	public static final String key = "dad";
	public static int countID = 0;
	private int id;
	private static Connection con;
	private static PreparedStatement pst;
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
			pst = con.prepareStatement("insert into membros(ID,Nome,Data_Nascimento,Sexo,Estado_Civil,Profissao,Endereco,Telefone,Email,"
					+ "Igreja_Origem,Tipo_Membro,Membro_Desde,Data_Batismo,Observacoes) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pst.setInt(1, getId());
			pst.setString(2, getNome());
			String data = new SimpleDateFormat("yyyy-M-d").format(data_nascimento);
			pst.setDate(3, java.sql.Date.valueOf(data));
			pst.setString(4, getSexo().getDescricao());
			pst.setString(5, getEstado_civil().getDescricao());
			pst.setString(6, getProfissao());
			pst.setString(7, getEndereco());
			pst.setString(8, getTelefone());
			pst.setString(9, getEmail());
			pst.setString(10, getIgreja_origem());
			pst.setString(11, getTipo_membro().getDescricao());
			data = new SimpleDateFormat("yyyy-M-d").format(getMembro_desde());
			pst.setDate(12, java.sql.Date.valueOf(data));
			data = new SimpleDateFormat("yyyy-M-d").format(getData_batismo());
			pst.setDate(13, java.sql.Date.valueOf(data));
			pst.setString(14, getObservacoes());
			pst.execute();
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao adicionar membro na base de dados! - " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void removerBaseDeDados() {
		try {
			pst = con.prepareStatement("delete from usuarios where ID=?");
			pst.setInt(1, getId());
			pst.execute();
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao adicionar membro na base de dados! - " + e.getMessage());
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
