package dad.fam_com_cristo.table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.table.AbstractTableModel;

import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.gui.DataGui;
import dad.recursos.Command;
import dad.recursos.ConexaoUser;
import dad.recursos.CriptografiaAES;
import dad.recursos.Log;
import dad.recursos.UndoManager;

public class TableModelMembro extends AbstractTableModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3247984074345998765L;
	private static TableModelMembro INSTANCE;
	private ArrayList<Membro> users;
	private String[] colunas = { "Nome", "Data de Nascimento", "Telefone", "Data de Batismo" };
	private Connection con;
	private PreparedStatement pst;
	private ResultSet rs;
	private UndoManager undoManager;

	private TableModelMembro() {
		INSTANCE = this;
		undoManager = new UndoManager();
	}

	public void uploadDataBase() {
		users = new ArrayList<>();
		try {
			con = ConexaoUser.getConnection();
			pst = con.prepareStatement("select * from usuarios order by CPF");
			rs = pst.executeQuery();
			if (rs.next()) {
				do {
					String cpf = rs.getString(1);
					CriptografiaAES.setKey(Membro.key);
					CriptografiaAES.decrypt(cpf);
					cpf = CriptografiaAES.getDecryptedString();
					Membro user = Membro.getUser(cpf);
					users.add(user);
				} while (rs.next());
			}
			fireTableDataChanged();
			Log.getInstance().printLog("Base de dados users carregada com sucesso!");
		} catch (Exception e) {
			Log.getInstance()
					.printLog("Erro ao carregar a base de dados dos Users: " + e.getMessage() + "\n" + getClass());
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
		return users.size();
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
		return users;
	}

	public void addUser(Membro user) {
		undoManager.execute(new AddUser(user));
		MembroPanel.getInstance().clearTextFields();
		fireTableDataChanged();
	}

	public Membro getUser(int rowIndex) {
		return users.get(rowIndex);
	}
	
	public int getRow(Membro user) {
		for (int i = 0; i < users.size(); i++) {
			if (users.get(i).getId() == user.getId())
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
			return users.get(rowIndex).getNome();
		case 1:
			return new SimpleDateFormat("dd/MM/yyyy").format(users.get(rowIndex).getData_nascimento());
		case 3:
			String phone = users.get(rowIndex).getTelefone();
			return "(" + phone.substring(0, 2) + ") " + phone.substring(2, 3) + " " + phone.substring(3, 7) + "-"
					+ phone.substring(7);
		case 4:
			return new SimpleDateFormat("dd/MM/yyyy").format(users.get(rowIndex).getData_batismo());
		default:
			return users.get(rowIndex);
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
				Membro user = users.get(rowIndex);
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
					users.get(rowIndex);
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
		users.add(pos, user);

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
				con = ConexaoUser.getConnection();
				for (int i = 0; i < rows.length; i++) {
					users.get(rows[i]).removerBaseDeDados();
					remover.add(users.get(rows[i]));
				}
				users.removeAll(remover);
				fireTableDataChanged();
				MembroPanel.getInstance().getJtfTotal().setText(String.valueOf(users.size()));
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
				users.add(user);
				MembroPanel.getInstance().getJtfTotal().setText(String.valueOf(users.size()));
			} catch (Exception e) {
				Log.getInstance().printLog("Erro ao criar cliente! " + e.getMessage());
				e.printStackTrace();
			}
		}

		@Override
		public void undo() {
			user.removerBaseDeDados();
			users.remove(user);
			MembroPanel.getInstance().getJtfTotal().setText(String.valueOf(users.size()));
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
