package dad.fam_com_cristo.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import dad.fam_com_cristo.Estado_Civil;
import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.Sexo;
import dad.fam_com_cristo.Tipo_Membro;
import dad.recursos.Command;
import dad.recursos.ConexaoMembro;
import dad.recursos.Log;

public class AtualizaMembro implements Command {

	private Connection con;
	private PreparedStatement pst;
	private Membro membro;
	private String coluna;
	private Object valor;
	private Object old;
	private AbstractTableModel table;

	public AtualizaMembro(AbstractTableModel table, String coluna, Membro membro, Object valor) {
		this.table = table;
		this.coluna = coluna;
		this.membro = membro;
		this.valor = valor;
		con = ConexaoMembro.getConnection();
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
			pst = con.prepareStatement("update membros set " + coluna + "=? where ID=?");
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			switch (coluna) {
			case "Nome":
				pst.setString(1, (String) valor);
				membro.setNome((String) valor);
				break;
			case "Data_Nascimento":
				Date data_nasc = dateFormat.parse((String) valor);
				membro.setData_nascimento(data_nasc);
				String data = new SimpleDateFormat("yyyy-M-d").format(data_nasc);
				pst.setDate(1, java.sql.Date.valueOf(data));
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
				String telefone = ((String) valor);
				if (telefone.length() != 11)
					telefone = "00000000000";
				pst.setString(1, telefone);
				membro.setTelefone(telefone);
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
			case "Membro_Desde":
				Date membro_desde = dateFormat.parse((String) valor);
				membro.setMembro_desde(membro_desde);
				String data_membro = new SimpleDateFormat("yyyy-M-d").format(membro_desde);
				pst.setDate(1, java.sql.Date.valueOf(data_membro));
				break;
			case "Data_Batismo":
				Date data_batismo = dateFormat.parse((String) valor);
				membro.setData_batismo(data_batismo);
				String batismo_data = new SimpleDateFormat("yyyy-M-d").format(data_batismo);
				pst.setDate(1, java.sql.Date.valueOf(batismo_data));
				break;
			case "Observacoes":
				pst.setString(1, (String) valor);
				membro.setObservacoes((String) valor);
				break;
			default:
				break;
			}
			pst.setInt(2, membro.getId());
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
				String data = new SimpleDateFormat("yyyy-M-d").format((Date) old);
				pst.setDate(1, java.sql.Date.valueOf(data));
				membro.setData_nascimento((Date) old);
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
			case "Membro_Desde":
				String membro_desde = new SimpleDateFormat("yyyy-M-d").format((Date) old);
				pst.setDate(1, java.sql.Date.valueOf(membro_desde));
				membro.setData_nascimento((Date) old);
				break;
			case "Data_Batismo":
				String data_batismo = new SimpleDateFormat("yyyy-M-d").format((Date) old);
				pst.setDate(1, java.sql.Date.valueOf(data_batismo));
				membro.setData_nascimento((Date) old);
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
			Log.getInstance().printLog("Erro ao anular a a��o!\n" + e.getMessage());
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
