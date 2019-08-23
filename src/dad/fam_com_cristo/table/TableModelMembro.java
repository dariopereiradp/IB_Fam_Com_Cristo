package dad.fam_com_cristo.table;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.table.AbstractTableModel;

import dad.fam_com_cristo.Estado_Civil;
import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.Sexo;
import dad.fam_com_cristo.Sim_Nao;
import dad.fam_com_cristo.Tipo_Membro;
import dad.fam_com_cristo.gui.DataGui;
import dad.recursos.Command;
import dad.recursos.ConexaoMembro;
import dad.recursos.Log;
import dad.recursos.UndoManager;

public class TableModelMembro extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3247984074345998765L;
	private static TableModelMembro INSTANCE;
	private ArrayList<Membro> membros;
	private String[] colunas = { "Nome", "Data de Nascimento", "Telefone", "Tipo" };
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private UndoManager undoManager;

	private TableModelMembro() {
		INSTANCE = this;
		undoManager = new UndoManager();
	}

	public void uploadDataBase() {
		membros = new ArrayList<>();
		int maior = 0;
		try {
			con = ConexaoMembro.getConnection();
			pst = con.prepareStatement("select * from membros order by Nome");
			rs = pst.executeQuery();
			if (rs.next()) {
				do {
					int id = rs.getInt(1);
					String nome = rs.getString(2);
					Date data_nascimento = rs.getDate(3);
					Sexo sexo = Sexo.getEnum(rs.getString(4));
					Estado_Civil estado_civil = Estado_Civil.getEnum(rs.getString(5));
					String profissao = rs.getString(6);
					String endereco = rs.getString(7);
					String telefone = rs.getString(8);
					String email = rs.getString(9);
					String igreja_origem = rs.getString(10);
					Tipo_Membro tipo_membro = Tipo_Membro.getEnum(rs.getString(11));
					Sim_Nao batizado = Sim_Nao.getEnum(rs.getString(12));
					Date membro_desde = rs.getDate(13);
					Date data_batismo = rs.getDate(14);
					String observacoes = rs.getString(15);
					ImageIcon img = null;
					File f = new File(Membro.imgPath + id + ".jpg");
					if (f.exists())
						img = new ImageIcon(f.getPath());
					Membro membro = new Membro(nome, data_nascimento, sexo, estado_civil, profissao, endereco, telefone,
							email, igreja_origem, tipo_membro, batizado, membro_desde, data_batismo, observacoes, img);
					membro.setId(id);
					if (id > maior)
						maior = id;
					membros.add(membro);
				} while (rs.next());
			}
			Membro.countID = maior;
			fireTableDataChanged();
			Log.getInstance().printLog("Base de dados membros carregada com sucesso!");
		} catch (Exception e) {
			Log.getInstance()
					.printLog("Erro ao carregar a base de dados dos Membros: " + e.getMessage() + "\n" + getClass());
			e.printStackTrace();
		}
	}

	public static TableModelMembro getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new TableModelMembro();
		}
		return INSTANCE;
	}

	public UndoManager getUndoManager() {
		return undoManager;
	}

	public void addListeners() {
		undoManager.addPropertyChangeListener(e -> updateItems());
		updateItems();
	}

	public void updateItems() {
		DataGui.getInstance().getMenuAnular().setEnabled(undoManager.isUndoAvailable());
		DataGui.getInstance().getMenuAnular().setText("Anular (Ctrl+Z) - (" + undoManager.getUndoName() + ")");
		DataGui.getInstance().getMenuRefazer().setEnabled(undoManager.isRedoAvailable());
		DataGui.getInstance().getMenuRefazer().setText("Refazer (Ctrl+Y) - (" + undoManager.getRedoName() + ")");
	}

	@Override
	public int getRowCount() {
		return membros.size();
	}

	@Override
	public int getColumnCount() {
		return colunas.length;
	}

	@Override
	public String getColumnName(int columnIndex) {
		return colunas[columnIndex];
	}

	public ArrayList<Membro> getUsers() {
		return membros;
	}

	public void addMembro(Membro user) {
		undoManager.execute(new AddMembro(user));
		fireTableDataChanged();
	}

	public Membro getMembro(int rowIndex) {
		return membros.get(rowIndex);
	}

	public int getRow(Membro membro) {
		for (int i = 0; i < membros.size(); i++) {
			if (membros.get(i).getId() == membro.getId())
				return i;
		}
		return -1;
	}

	public void removerMembro(int[] rows) {
		undoManager.execute(new RemoverMembro(rows));
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return membros.get(rowIndex).getNome();
		case 1:
			return new SimpleDateFormat("dd/MM/yyyy").format(membros.get(rowIndex).getData_nascimento());
		case 2:
			String phone = membros.get(rowIndex).getTelefone();
			if (phone.length() == 11)
				return "(" + phone.substring(0, 2) + ") " + phone.substring(2, 3) + " " + phone.substring(3, 7) + "-"
						+ phone.substring(7);
			else
				return phone;
		case 3:
			return membros.get(rowIndex).getTipo_membro();
		default:
			return membros.get(rowIndex);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int column) {
		switch (column) {
		case 3:
			return Tipo_Membro.class;
		default:
			return String.class;
		}
	}

	@Override
	public void setValueAt(Object valor, int rowIndex, int columnIndex) {
		try {
			if (!(columnIndex == 0 && (String.valueOf(valor)).trim().equals(""))) {
				if ((String.valueOf(valor).trim().equals("")))
					valor = "-";
				Membro membro = membros.get(rowIndex);
				switch (columnIndex) {
				case 0:
					if (!((String) valor).equals(membro.getNome())) {
						undoManager.execute(new AtualizaMembro(this, "Nome", membro, valor));
					}
					break;
				case 1:
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date data_nasc = dateFormat.parse((String) valor);
					if (!dateFormat.format(membro.getData_nascimento()).equals(dateFormat.format(data_nasc)))
						undoManager.execute(new AtualizaMembro(this, "Data_Nascimento", membro, valor, true));
					break;
				case 2:
					String telefone = ((String) valor).replace("-", "").replace("(", "").replace(")", "").replace(" ",
							"");
					if (!membro.getTelefone().equals(telefone))
						if (telefone.length() == 11)
							undoManager.execute(new AtualizaMembro(this, "Telefone", membro, valor));
					break;
				case 3:
					if (membro.getTipo_membro() != (Tipo_Membro) valor)
						undoManager.execute(new CompositeCommand("Tipo de Membro",
								new AtualizaMembro(this, "Tipo_Membro", membro, valor),
								new AtualizaMembro(this, "Batizado", membro, valor)));

					break;
				default:
					membros.get(rowIndex);
					break;
				}
				fireTableDataChanged();
			}
		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().printLog("Erro no setValue()\n" + e.getMessage() + "\n" + getClass());
		}
	}

	public void insertUser(Membro user, int pos) {
		user.adicionarNaBaseDeDados();
		membros.add(pos, user);

	}

	private class RemoverMembro implements Command {

		private int[] rows;
		private ArrayList<Membro> remover = new ArrayList<>();

		public RemoverMembro(int[] rows) {
			this.rows = rows;
		}

		@Override
		public void execute() {
			try {
				for (int i = 0; i < rows.length; i++) {
					membros.get(rows[i]).removerBaseDeDados();
					remover.add(membros.get(rows[i]));
				}
				membros.removeAll(remover);
				fireTableDataChanged();
				atualizarTextFieldsNumeros();
				Log.getInstance().printLog("Membro(s) apagados com sucesso!");
			} catch (Exception e) {
				Log.getInstance().printLog("Erro ao apagar o(s) membro(s)\n" + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void undo() {
			for (int i = 0; i < rows.length; i++) {
				insertUser(remover.get(i), rows[i]);
			}
			atualizarTextFieldsNumeros();
			fireTableDataChanged();
		}

		@Override
		public void redo() {
			execute();
		}

		@Override
		public String getName() {
			return "Remover Membro";
		}
	}

	private class AddMembro implements Command {

		private Membro membro;

		public AddMembro(Membro membro) {
			this.membro = membro;
		}

		@Override
		public void execute() {
			try {
				membro.adicionarNaBaseDeDados();
				membros.add(membro);
				atualizarTextFieldsNumeros();
			} catch (Exception e) {
				Log.getInstance().printLog("Erro ao criar membro! " + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void undo() {
			membro.removerBaseDeDados();
			membros.remove(membro);
			atualizarTextFieldsNumeros();
			fireTableDataChanged();
		}

		@Override
		public void redo() {
			execute();
			fireTableDataChanged();
		}

		@Override
		public String getName() {
			return "Adicionar Membro";
		}
	}

	public void atualizarTextFieldsNumeros() {
		MembroPanel.getInstance().getJtfTotal().setText(String.valueOf(getTotal()));
		MembroPanel.getInstance().getJft_congregados().setText(String.valueOf(getN_Congregados()));
		MembroPanel.getInstance().getJft_ex_membros().setText(String.valueOf(getN_Ex_Membros()));
		MembroPanel.getInstance().getJft_lideranca().setText(String.valueOf(getN_Lideranca()));
		MembroPanel.getInstance().getJft_membros_ativos().setText(String.valueOf(getN_Membros_Ativos()));
		MembroPanel.getInstance().getJft_membros_nominais().setText(String.valueOf(getN_Membros_Nominais()));
		MembroPanel.getInstance().getJft_criancas().setText(String.valueOf(getN_Criancas()));
		MembroPanel.getInstance().getJft_adolescentes().setText(String.valueOf(getN_Adolescentes()));
		MembroPanel.getInstance().getJft_adultos().setText(String.valueOf(getN_Adultos()));
		MembroPanel.getInstance().getJftCasados().setText(String.valueOf(getN_Casados()));
		MembroPanel.getInstance().getJft_Homens().setText(String.valueOf(getN_Homens()));
		MembroPanel.getInstance().getJft_Mulheres().setText(String.valueOf(getN_Mulheres()));
		

	}

	public int getTotal() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() != Tipo_Membro.EX_MEMBRO)
				n++;
		}
		return n;
	}

	public int getN_Ex_Membros() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() == Tipo_Membro.EX_MEMBRO)
				n++;
		}
		return n;
	}

	public int getN_Membros_Nominais() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() == Tipo_Membro.MEMBRO_NOMINAL)
				n++;
		}
		return n;
	}

	public int getN_Membros_Ativos() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() == Tipo_Membro.MEMBRO_ATIVO || m.getTipo_membro() == Tipo_Membro.LIDERANCA)
				n++;
		}
		return n;
	}

	public int getN_Lideranca() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() == Tipo_Membro.LIDERANCA)
				n++;
		}
		return n;
	}

	public int getN_Congregados() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() == Tipo_Membro.CONGREGADO)
				n++;
		}
		return n;
	}

	public int getN_Homens() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() != Tipo_Membro.EX_MEMBRO && m.getSexo() == Sexo.MASCULINO)
				n++;
		}
		return n;
	}

	public int getN_Mulheres() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() != Tipo_Membro.EX_MEMBRO && m.getSexo() == Sexo.FEMININO)
				n++;
		}
		return n;
	}

	public int getN_Casados() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() != Tipo_Membro.EX_MEMBRO && m.getEstado_civil() == Estado_Civil.CASADO)
				n++;
		}
		return n;
	}

	public int getN_Adolescentes() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() != Tipo_Membro.EX_MEMBRO && m.getIdade() >= 13 && m.getIdade() <= 17)
				n++;
		}
		return n;
	}

	public int getN_Adultos() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() != Tipo_Membro.EX_MEMBRO && m.getIdade() >= 18)
				n++;
		}
		return n;
	}

	public int getN_Criancas() {
		int n = 0;
		for (Membro m : membros) {
			if (m.getTipo_membro() != Tipo_Membro.EX_MEMBRO && m.getIdade() <= 12)
				n++;
		}
		return n;
	}

}
