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
					Sexo sexo = Sexo.valueOf(rs.getString(4));
					Estado_Civil estado_civil = Estado_Civil.valueOf(rs.getString(5));
					String profissao = rs.getString(6);
					String endereco = rs.getString(7);
					String telefone = rs.getString(8);
					String email = rs.getString(9);
					String igreja_origem = rs.getString(10);
					Tipo_Membro tipo_membro = Tipo_Membro.valueOf(rs.getString(11));
					Date membro_desde = rs.getDate(12);
					Date data_batismo = rs.getDate(13);
					String observacoes = rs.getString(14);
					ImageIcon img = null;
					File f = new File(Membro.imgPath + id + ".jpg");
					if (f.exists())
						img = new ImageIcon(f.getPath());
					Membro membro = new Membro(nome, data_nascimento, sexo, estado_civil, profissao, endereco, telefone,
							email, igreja_origem, tipo_membro, membro_desde, data_batismo, observacoes, img);
					membro.setId(id);
					if (id > maior)
						maior = id;
					membros.add(membro);
				} while (rs.next());
			}
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

	public void addUser(Membro user) {
		undoManager.execute(new AddUser(user));
		fireTableDataChanged();
	}

	public Membro getUser(int rowIndex) {
		return membros.get(rowIndex);
	}

	public int getRow(Membro user) {
		for (int i = 0; i < membros.size(); i++) {
			if (membros.get(i).getId() == user.getId())
				return i;
		}
		return -1;
	}

	public void removeUser(int[] rows) {
		undoManager.execute(new RemoverUser(rows));
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return membros.get(rowIndex).getNome();
		case 1:
			return new SimpleDateFormat("dd/MM/yyyy").format(membros.get(rowIndex).getData_nascimento());
		case 3:
			String phone = membros.get(rowIndex).getTelefone();
			return "(" + phone.substring(0, 2) + ") " + phone.substring(2, 3) + " " + phone.substring(3, 7) + "-"
					+ phone.substring(7);
		case 4:
			return new SimpleDateFormat("dd/MM/yyyy").format(membros.get(rowIndex).getData_batismo());
		default:
			return membros.get(rowIndex);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Class getColumnClass(int column) {
		switch (column) {
		case 0:
			return Long.class;
		case 4:
			return Integer.class;
		default:
			return String.class;
		}
	}

	@Override
	public void setValueAt(Object valor, int rowIndex, int columnIndex) {
		try {
			if (!(columnIndex == 1 && (String.valueOf(valor)).trim().equals(""))) {
				if ((String.valueOf(valor).trim().equals("")))
					valor = "-";
				Membro user = membros.get(rowIndex);
				switch (columnIndex) {
				case 1:
					if (!((String) valor).equals(user.getNome())) {
						undoManager.execute(new AtualizaMembro(this, "Nome", user, valor));
					}
					break;
				case 2:
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
					Date data_nasc = dateFormat.parse((String) valor);
					if (!dateFormat.format(user.getData_nascimento()).equals(dateFormat.format(data_nasc)))
						undoManager.execute(new AtualizaMembro(this, "Data_Nascimento", user, valor));
					break;
				case 3:
					String telefone = ((String) valor).replace("-", "").replace("(", "").replace(")", "").replace(" ",
							"");
					if (!user.getTelefone().equals(telefone))
						if (telefone.length() == 11)
							undoManager.execute(new AtualizaMembro(this, "Telefone", user, valor));
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

	private class RemoverUser implements Command {

		private int[] rows;
		private ArrayList<Membro> remover = new ArrayList<>();

		public RemoverUser(int[] rows) {
			this.rows = rows;
		}

		@Override
		public void execute() {
			try {
				con = ConexaoMembro.getConnection();
				for (int i = 0; i < rows.length; i++) {
					membros.get(rows[i]).removerBaseDeDados();
					remover.add(membros.get(rows[i]));
				}
				membros.removeAll(remover);
				fireTableDataChanged();
				MembroPanel.getInstance().getJtfTotal().setText(String.valueOf(membros.size()));
				Log.getInstance().printLog("Usuários apagados com sucesso!");
			} catch (Exception e) {
				Log.getInstance().printLog("Erro ao apagar o(s) usuário(s)\n" + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void undo() {
			for (int i = 0; i < rows.length; i++) {
				insertUser(remover.get(i), rows[i]);
			}
			fireTableDataChanged();
		}

		@Override
		public void redo() {
			execute();
		}

		@Override
		public String getName() {
			return "Remover Usuário";
		}
	}

	private class AddUser implements Command {

		private Membro user;

		public AddUser(Membro user) {
			this.user = user;
		}

		@Override
		public void execute() {
			try {
				user.adicionarNaBaseDeDados();
				membros.add(user);
				MembroPanel.getInstance().getJtfTotal().setText(String.valueOf(membros.size()));
			} catch (Exception e) {
				Log.getInstance().printLog("Erro ao criar cliente! " + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void undo() {
			user.removerBaseDeDados();
			membros.remove(user);
			MembroPanel.getInstance().getJtfTotal().setText(String.valueOf(membros.size()));
			fireTableDataChanged();
		}

		@Override
		public void redo() {
			execute();
		}

		@Override
		public String getName() {
			return "Adicionar Cliente";
		}
	}

}
