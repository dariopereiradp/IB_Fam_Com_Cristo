package dad.fam_com_cristo.table.command;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.time.LocalDate;

import dad.fam_com_cristo.table.conexao.ConexaoMembro;
import dad.fam_com_cristo.table.models.TableModelMembro;
import dad.fam_com_cristo.types.Membro;
import dad.fam_com_cristo.types.enumerados.Estado_Civil;
import dad.fam_com_cristo.types.enumerados.Sexo;
import dad.fam_com_cristo.types.enumerados.Sim_Nao;
import dad.fam_com_cristo.types.enumerados.Tipo_Membro;
import dad.recursos.Log;

/**
 * Classe que representa um comando para atualizar as informações de um membro,
 * implementando as funções de undo e redo e guardando na base de dados.
 * 
 * @author Dário Pereira
 *
 */
public class AtualizaMembro implements Command {

	private Connection con;
	private PreparedStatement pst;
	private Membro membro;
	private String coluna;
	private Object valor;
	private Object old;
	private TableModelMembro table;
	
	public AtualizaMembro(String coluna, Membro membro, Object valor) {
		this.table = TableModelMembro.getInstance();
		this.coluna = coluna;
		this.membro = membro;
		this.valor = valor;
		con = new ConexaoMembro().getConnection();
		inicializar();
	}

	private void inicializar() {
		switch (coluna) {
		case "Nome":
			old = membro.getNome();
			break;
		case "Data_Nascimento":
			old = membro.getData_nascimento();
			break;
		case "Sexo":
			old = membro.getSexo();
			break;
		case "Estado_Civil":
			old = membro.getEstado_civil();
			break;
		case "Profissao":
			old = membro.getProfissao();
			break;
		case "Endereco":
			old = membro.getEndereco();
			break;
		case "Telefone":
			old = membro.getTelefone();
			break;
		case "Email":
			old = membro.getEmail();
			break;
		case "Igreja_Origem":
			old = membro.getIgreja_origem();
			break;
		case "Tipo_Membro":
			old = membro.getTipo_membro();
			break;
		case "Batizado":
			old = membro.eBatizado();
			break;
		case "Membro_Desde":
			old = membro.getMembro_desde();
			break;
		case "Data_Batismo":
			old = membro.getData_batismo();
			break;
		case "Observacoes":
			old = membro.getObservacoes();
			break;
		default:
			break;
		}

	}

	@Override
	public void execute() {
		try {
			pst = con.prepareStatement("update membros set " + coluna + "=? where ID=" + membro.getId());
			switch (coluna) {
			case "Nome":
				pst.setString(1, (String) valor);
				membro.setNome((String) valor);
				break;
			case "Data_Nascimento":
				LocalDate data_nasc = (LocalDate) valor;
				membro.setData_nascimento(data_nasc);
				pst.setDate(1, Date.valueOf(data_nasc));
				break;
			case "Sexo":
				Sexo sexo = (Sexo) valor;
				pst.setString(1, sexo.getDescricao());
				membro.setSexo(sexo);
				break;
			case "Estado_Civil":
				Estado_Civil estado_Civil = (Estado_Civil) valor;
				pst.setString(1, estado_Civil.getDescricao());
				membro.setEstado_civil(estado_Civil);
				break;
			case "Profissao":
				pst.setString(1, (String) valor);
				membro.setProfissao((String) valor);
				break;
			case "Endereco":
				pst.setString(1, (String) valor);
				membro.setEndereco((String) valor);
				break;
			case "Telefone":
				String telefone = ((String) valor).replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
				if (telefone.length() != 11)
					telefone = "00000000000";
				pst.setString(1, telefone);
				membro.setTelefone(telefone);
				break;
			case "Email":
				pst.setString(1, (String) valor);
				membro.setEmail((String) valor);
				break;
			case "Igreja_Origem":
				String igreja_origem = (String) valor;
				pst.setString(1, igreja_origem);
				membro.setIgreja_origem(igreja_origem);
				break;
			case "Tipo_Membro":
				Tipo_Membro tipo_Membro = (Tipo_Membro) valor;
				pst.setString(1, tipo_Membro.getDescricao());
				membro.setTipo_membro(tipo_Membro);
				break;
			case "Batizado":
				Sim_Nao sim_Nao = (Sim_Nao) valor;
				pst.setString(1, sim_Nao.getDescricao());
				membro.setBatizado(sim_Nao);
				break;
			case "Membro_Desde":
				LocalDate membro_desde = (LocalDate) valor;
				membro.setMembro_desde(membro_desde);
				pst.setDate(1, java.sql.Date.valueOf(membro_desde));
				break;
			case "Data_Batismo":
				LocalDate data_batismo = (LocalDate) valor;
				membro.setData_batismo(data_batismo);
				pst.setDate(1, java.sql.Date.valueOf(data_batismo));
				break;
			case "Observacoes":
				pst.setString(1, (String) valor);
				membro.setObservacoes((String) valor);
				break;
			default:
				break;
			}
			pst.execute();
			table.fireTableDataChanged();
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao atualizar " + coluna);
			e.printStackTrace();
		}

	}

	@Override
	public void undo() {
		try {
			pst = con.prepareStatement("update membros set " + coluna + "=? where ID=?");
			switch (coluna) {
			case "Nome":
				pst.setString(1, (String) old);
				membro.setNome((String) old);
				break;
			case "Data_Nascimento":
				pst.setDate(1, java.sql.Date.valueOf((LocalDate) old));
				membro.setData_nascimento((LocalDate) old);
				break;
			case "Sexo":
				Sexo sexo = (Sexo) old;
				pst.setString(1, sexo.getDescricao());
				membro.setSexo(sexo);
				break;
			case "Estado_Civil":
				Estado_Civil estado_Civil = (Estado_Civil) old;
				pst.setString(1, estado_Civil.getDescricao());
				membro.setEstado_civil(estado_Civil);
				break;
			case "Profissao":
				pst.setString(1, (String) old);
				membro.setProfissao((String) old);
				break;
			case "Endereco":
				pst.setString(1, (String) old);
				membro.setEndereco((String) old);
				break;
			case "Telefone":
				pst.setString(1, (String) old);
				membro.setTelefone((String) old);
				break;
			case "Email":
				pst.setString(1, (String) old);
				membro.setEmail((String) old);
				break;
			case "Igreja_Origem":
				String igreja_origem = (String) old;
				pst.setString(1, igreja_origem);
				membro.setIgreja_origem(igreja_origem);
				break;
			case "Tipo_Membro":
				Tipo_Membro tipo_Membro = (Tipo_Membro) old;
				pst.setString(1, tipo_Membro.getDescricao());
				membro.setTipo_membro(tipo_Membro);
				break;
			case "Batizado":
				Sim_Nao sim_Nao = (Sim_Nao) old;
				pst.setString(1, sim_Nao.getDescricao());
				membro.setBatizado(sim_Nao);
				break;
			case "Membro_Desde":
				pst.setDate(1, java.sql.Date.valueOf((LocalDate) old));
				membro.setData_nascimento((LocalDate) old);
				break;
			case "Data_Batismo":
				pst.setDate(1, java.sql.Date.valueOf((LocalDate) old));
				membro.setData_nascimento((LocalDate) old);
				break;
			case "Observacoes":
				pst.setString(1, (String) old);
				membro.setObservacoes((String) old);
				break;
			default:
				break;
			}
			pst.setInt(2, membro.getId());
			pst.execute();
			table.fireTableDataChanged();
		} catch (Exception e) {
			Log.getInstance().printLog("Erro ao anular a ação!\n" + e.getMessage());
			e.printStackTrace();
		}

	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getName() {
		return "Editar " + coluna;
	}
}
