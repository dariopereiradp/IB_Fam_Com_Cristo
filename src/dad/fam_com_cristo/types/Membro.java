package dad.fam_com_cristo.types;

import java.awt.FileDialog;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.swing.ImageIcon;

import org.apache.commons.text.WordUtils;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.table.conexao.ConexaoMembro;
import dad.fam_com_cristo.types.enumerados.Estado_Civil;
import dad.fam_com_cristo.types.enumerados.Sexo;
import dad.fam_com_cristo.types.enumerados.Sim_Nao;
import dad.fam_com_cristo.types.enumerados.Tipo_Membro;
import dad.recursos.DataPesquisavel;
import dad.recursos.ImageCompression;
import dad.recursos.Log;
import dad.recursos.pdf.FichaMembroToPDF;

/**
 * Classe que representa uma pessoa associada de alguma forma à igreja
 * 
 * @author Dário Pereira
 *
 */
public class Membro implements Comparable<Membro> {

	/**
	 * Caminho para a pasta das imagens dos livros.
	 */
	public static final String IMG_PATH = Main.DATABASE_DIR + "Imagens/";
	/**
	 * Variável usada para controle do próximo id a ser atribuído a um novo membro.
	 */
	public static int countID = 0;
	private int id;
	private static Connection con;
	private static PreparedStatement pst;
	private Tipo_Membro tipo_membro;
	private Sexo sexo;
	private Estado_Civil estado_civil;
	private String nome, profissao, endereco, igreja_origem, email, telefone, observacoes;
	private DataPesquisavel data_nascimento;
	private LocalDate data_batismo, membro_desde;
	private Sim_Nao batizado;
	private ImageIcon img;

	public Membro(String nome, LocalDate data_nascimento, Sexo sexo, Estado_Civil estado_civil, String profissao,
			String endereco, String telefone, String email, String igreja_origem, Tipo_Membro tipo_membro,
			Sim_Nao batizado, LocalDate membro_desde, LocalDate data_batismo, String observacoes, ImageIcon img) {

		con = new ConexaoMembro().getConnection();
		setId(++countID);
		this.nome = WordUtils.capitalize(nome);
		this.data_nascimento = new DataPesquisavel(data_nascimento);
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
		this.batizado = batizado;
	}

	/**
	 * Adiciona o membro na base de dados.
	 */
	public void adicionarNaBaseDeDados() {
		try {
			pst = con.prepareStatement(
					"insert into membros(ID,Nome,Data_Nascimento,Sexo,Estado_Civil,Profissao,Endereco,Telefone,Email,"
							+ "Igreja_Origem,Tipo_Membro,Batizado,Membro_Desde,Data_Batismo,Observacoes) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
			pst.setInt(1, getId());
			pst.setString(2, getNome());
			pst.setDate(3, Date.valueOf(getData_nascimento()));
			pst.setString(4, getSexo().getDescricao());
			pst.setString(5, getEstado_civil().getDescricao());
			pst.setString(6, getProfissao());
			pst.setString(7, getEndereco());
			pst.setString(8, getTelefone());
			pst.setString(9, getEmail());
			pst.setString(10, getIgreja_origem());
			pst.setString(11, getTipo_membro().getDescricao());
			pst.setString(12, eBatizado().getDescricao());
			pst.setDate(13, Date.valueOf(getMembro_desde()));
			pst.setDate(14, Date.valueOf(getData_batismo()));
			pst.setString(15, getObservacoes());
			pst.execute();
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao adicionar membro na base de dados! - " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Remove o membro da base de dados.
	 */
	public void removerBaseDeDados() {
		try {
			pst = con.prepareStatement("delete from membros where ID=?");
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

	public LocalDate getData_nascimento() {
		return data_nascimento.getData();
	}
	
	public DataPesquisavel getData_nascimentoPesquisavel() {
		return data_nascimento;
	}

	public void setData_nascimento(LocalDate data_nascimento) {
		this.data_nascimento.setData(data_nascimento);
	}

	/**
	 * Calcula a idade do membro com base na data de nascimento e na data de hoje.
	 * 
	 * @return a idade calculada do membro.
	 */
	public int getIdade() {
		return Math.toIntExact(ChronoUnit.YEARS.between(data_nascimento.getData(), LocalDate.now()));
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

	/**
	 * Abre um FileDialog para escolher uma imagem para o membro
	 */
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

	public LocalDate getData_batismo() {
		return data_batismo;
	}

	public void setData_batismo(LocalDate data_batismo) {
		this.data_batismo = data_batismo;
	}

	public LocalDate getMembro_desde() {
		return membro_desde;
	}

	public void setMembro_desde(LocalDate membro_desde) {
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

	/**
	 * Devolve a descrição do estado civil, dependendo se o membro é homem ou mulher
	 * (ex Solteiro, se for homem, mas Solteira se for mulher)
	 * 
	 * @return a descrição correta do estado civil
	 */
	public String getEstado_Civil_String() {
		if (sexo == Sexo.MASCULINO) {
			switch (estado_civil) {
			case CASADO:
				return "Casado";
			case SOLTEIRO:
				return "Solteiro";
			case DIVORCIADO:
				return "Divorciado";
			case UNIAO:
				return "União";
			case VIUVO:
				return "Viúvo";
			default:
				return estado_civil.getDescricao();
			}
		} else {
			switch (estado_civil) {
			case CASADO:
				return "Casada";
			case SOLTEIRO:
				return "Solteira";
			case DIVORCIADO:
				return "Divorciada";
			case UNIAO:
				return "União";
			case VIUVO:
				return "Viúva";
			default:
				return estado_civil.getDescricao();
			}
		}
	}

	public void setEstado_civil(Estado_Civil estado_civil) {
		this.estado_civil = estado_civil;
	}

	public boolean isBatizado() {
		if (batizado == Sim_Nao.SIM)
			return true;
		return false;
	}

	public Sim_Nao eBatizado() {
		return batizado;
	}


	public void setBatizado(Sim_Nao sim_nao) {
		batizado = sim_nao;
	}

	/**
	 * Converte o número de telefone para o formato (XX) X XXXX-XXXX
	 * @return
	 */
	public String getPhoneString() {
		String phone = getTelefone();
		String formatada = "";
		try {
		formatada =  "(" + phone.substring(0, 2) + ") " + phone.substring(2, 3) + " " + phone.substring(3, 7) + "-"
				+ phone.substring(7);
		} catch (Exception e) {
		}
		
		return formatada;
	}

	/**
	 * 
	 * @return um ficheiro que aponta para a localização da imagem de perfil desse membro
	 */
	public File getImageFile() {
		if (img == null)
			return null;
		else
			return new File(IMG_PATH + id + ".jpg");

	}

	/**
	 * Salva a ficha PDF do membro e pergunta se quer abrir.
	 */
	public void savePdf() {
		FichaMembroToPDF.membroToPdf(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Membro other = (Membro) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id + "." + nome;
	}

	/**
	 * Compara os membros de acordo com o nome
	 */
	@Override
	public int compareTo(Membro o) {
		return this.getNome().compareToIgnoreCase(o.getNome());
	}
}
