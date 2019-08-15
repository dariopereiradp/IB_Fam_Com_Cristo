package dad.fam_com_cristo.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import dad.fam_com_cristo.Membro;
import dad.recursos.Command;
import dad.recursos.ConexaoMembro;
import dad.recursos.CriptografiaAES;
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
			CriptografiaAES.setKey(Membro.key);
//			CriptografiaAES.encrypt(user.getCpf());
			pst = con.prepareStatement("update usuarios set " + coluna + "=? where ID=?");
			switch (coluna) {
			case "Nome":
				pst.setString(1, (String) valor);
				membro.setNome((String) valor);
			case "Data_Nascimento":
				SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				Date data_nasc = dateFormat.parse((String) valor);
				membro.setData_nascimento(data_nasc);
				String data = new SimpleDateFormat("yyyy-M-d").format(data_nasc);
				pst.setDate(1, java.sql.Date.valueOf(data));
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
				String telefone = ((String) valor).replace("-", "").replace("(", "").replace(")", "").replace(" ", "");
				if(telefone.length()!=11)
					telefone = "00000000000";
				pst.setString(1, telefone);
				membro.setTelefone(telefone);
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
			CriptografiaAES.setKey(Membro.key);
//			CriptografiaAES.encrypt(user.getCpf());
			pst = con.prepareStatement("update usuarios set " + coluna + "=? where CPF=?");
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
			case "Telefone":
				pst.setString(1, (String) old);
				membro.setTelefone((String) old);
				break;
			default:
				break;
			}
			pst.setString(2, CriptografiaAES.getEncryptedString());
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
