package dad.fam_com_cristo.gui;

import java.awt.Dimension;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.Timer;

import java.util.Date;
import java.util.regex.Pattern;

import javax.swing.JFrame;
import javax.swing.JLabel;

import java.awt.BorderLayout;
import java.awt.Desktop;

import javax.swing.JMenuBar;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import dad.fam_com_cristo.table.TableModelUser;
import dad.fam_com_cristo.table.UserPanel;
import dad.recursos.CellRenderer;
import dad.recursos.CellRendererNoImage;
import dad.recursos.DefaultCellRenderer;
import dad.recursos.Log;
import dad.recursos.SairAction;
import dad.recursos.ZipCompress;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;

public class DataGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5748160687318648477L;
	private static final int DELAY = 2200;
	private static DataGui INSTANCE;
	private JTabbedPane tabbedPane;
	private JMenu mnArquivo, mnAjuda, mnEditar;
	private JMenuItem menuSobre, menuEstatisticas, menuSair, menuAnular, menuRefazer, menuImportar, menuBackup,
			menuOrdenar, menuAtualizar, menuConfig;
	private JTextField pesquisa;
	private JPanel filtrosPanel;
	private JCheckBox checkMembroAtivo, checkMembroNominal, checkCongregados, checkLideranca;
	private JMenuItem mntmRelatarErro;
	private JMenuItem mnLimpar;
	private JMenuItem menuManual;

	private DataGui() {
		INSTANCE = this;
		setTitle(Main.TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("/DAD.jpg"))));
		setMinimumSize(new Dimension(800, 600));
		setExtendedState(MAXIMIZED_BOTH);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				long time = System.currentTimeMillis() - Main.inicialTime;
				Log.getInstance().printLog("Tempo de Uso: " + DurationFormatUtils.formatDuration(time, "HH'h'mm'm'ss's")
						+ "\nPrograma Terminou");
				System.exit(0);
			}
		});
		getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel pesquisaPanel = new JPanel(new BorderLayout());
		pesquisa = new JTextField();
		JLabel pesquisaLabel = new JLabel("Pesquisa: ");
		pesquisaPanel.add(pesquisaLabel, BorderLayout.WEST);
		pesquisaPanel.add(pesquisa, BorderLayout.CENTER);

		getContentPane().add(pesquisaPanel, BorderLayout.NORTH);

		filtrosPanel = new JPanel();
		pesquisaPanel.add(filtrosPanel, BorderLayout.EAST);

		checkMembroAtivo = new JCheckBox("Membro Ativo");
		checkMembroAtivo.setSelected(true);
		filtrosPanel.add(checkMembroAtivo);

		checkMembroNominal = new JCheckBox("Membro Nominal");
		checkMembroNominal.setSelected(true);
		filtrosPanel.add(checkMembroNominal);

		checkCongregados = new JCheckBox("Congregados");
		checkCongregados.setSelected(true);
		filtrosPanel.add(checkCongregados);

		checkLideranca = new JCheckBox("Lideran�a");
		checkLideranca.setSelected(true);
		filtrosPanel.add(checkLideranca);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("Pessoas associadas", UserPanel.getInstance());

		tabbedPane.setToolTipTextAt(1,
				"Pessoas que v�o � IBFC com alguma regularidade: membros ativos, membros nominais e congregados");

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnArquivo = new JMenu("Arquivo");
		menuBar.add(mnArquivo);

		menuEstatisticas = new JMenuItem("Estat\u00EDsticas");
		menuEstatisticas.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Estatisticas().open();
			}
		});
		mnArquivo.add(menuEstatisticas);

		menuBackup = new JMenuItem("C�pia de seguran�a");
		menuBackup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				backup();
			}
		});
		mnArquivo.add(menuBackup);

		menuImportar = new JMenuItem("Restaurar C�pia de Seguran�a");
		mnArquivo.add(menuImportar);
		menuImportar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Restauro().open();

			}
		});

		menuConfig = new JMenuItem("Configura\u00E7\u00F5es");
		menuConfig.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new Config().open();
			}
		});
		mnArquivo.add(menuConfig);

		menuAtualizar = new JMenuItem("Atualizar Tabelas");
		mnArquivo.add(menuAtualizar);

		mnLimpar = new JMenuItem("Limpar espa\u00E7o");
		mnLimpar.setToolTipText("Apaga os arquivos de logs antigos, que s�o desnecess�rios");
		mnLimpar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ok = JOptionPane.showConfirmDialog(null,
						"Os arquivos de logs antigos ser�o apagados. Isso n�o influencia o funcionamento do programa.\n"
								+ "Tem a certeza que quer limpar?",
						"Limpar espa�o", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
				if (ok == JOptionPane.YES_OPTION)
					limpar();
			}
		});
		mnArquivo.add(mnLimpar);

		menuSair = new JMenuItem("Sair");
		menuSair.addActionListener(new SairAction());
		mnArquivo.add(menuSair);

		mnEditar = new JMenu("Editar");
		menuBar.add(mnEditar);

		menuAnular = new JMenuItem("Anular (Ctrl+Z) - ()");
		menuAnular.setEnabled(false);
		mnEditar.add(menuAnular);

		menuRefazer = new JMenuItem("Refazer (Ctrl+Y) - ()");
		menuRefazer.setEnabled(false);
		mnEditar.add(menuRefazer);

		menuOrdenar = new JMenuItem("Ordenar livros (A-Z)");
		mnEditar.add(menuOrdenar);

		mnAjuda = new JMenu("Ajuda");
		menuBar.add(mnAjuda);

		mntmRelatarErro = new JMenuItem("Relatar erro");
		mntmRelatarErro.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new BugReport().open();
			}
		});

		menuManual = new JMenuItem("Manual de Instru\u00E7\u00F5es");
		menuManual.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String path = System.getenv("ProgramFiles(X86)") + System.getProperty("file.separator")
						+ "Biblioteca D�diva de Deus/" + "Manual_Instrucoes_Biblioteca_DAD_v" + Main.VERSION + ".pdf";
				try {
					Desktop.getDesktop().open(new File(path));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							"Ocorreu um erro ao abrir o manual! Pode ter sido apagado...\n"
									+ "Consulte o manual que veio com o programa ou entre em contato com '"
									+ Main.EMAIL_SUPORTE + "'",
							"Manual de Instru��es - Erro", JOptionPane.OK_OPTION,
							new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
					e1.printStackTrace();
				}

			}
		});
		mnAjuda.add(menuManual);
		mnAjuda.add(mntmRelatarErro);

		menuSobre = new JMenuItem("Sobre");
		menuSobre.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				new About().open();

			}
		});
		mnAjuda.add(menuSobre);

		getRootPane().setDefaultButton(UserPanel.getInstance().getbAdd());

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());

		menuAnular.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				anular();
			}
		});

		menuRefazer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				refazer();
			}
		});

		menuOrdenar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				ordenar();
			}
		});

		menuAtualizar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				TableModelUser.getInstance().fireTableDataChanged();
			}
		});

		pesquisa.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				filter(pesquisa.getText().toLowerCase());
			}

		});

		TableModelUser.getInstance().addListeners();

		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				filter("");
				updateItems();
			}
		});

	}

	public void limpar() {
		String logPath = Main.DATA_DIR + "Logs/";
		String month_year = LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMyyyy")).toUpperCase();
		File logs = new File(logPath);
		File logMonth = new File(logPath + month_year + "/");
		File logMonthTmp = new File(Main.DATA_DIR + month_year + "/");

		try {
			FileUtils.copyDirectory(logMonth, logMonthTmp);
			FileUtils.deleteDirectory(logs);
		} catch (IOException e) {
			try {
				FileUtils.copyDirectory(logMonthTmp, logMonth);
				FileUtils.deleteDirectory(logMonthTmp);
				JOptionPane.showMessageDialog(null, "Limpeza feita!", "Limpar espa�o - Sucesso", JOptionPane.OK_OPTION,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
			} catch (IOException e1) {
				Log.getInstance().printLog("Erro ao limpar o espa�o! - " + e.getMessage());
				JOptionPane.showMessageDialog(null, "Erro ao fazer a limpeza! - " + e.getMessage(),
						"Limpar espa�o - Erro", JOptionPane.OK_OPTION,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
				e1.printStackTrace();
			}
		}

	}

	public void backup() {
		String message = "Deseja criar uma c�pia de seguran�a de todas as bases de dados do programa?"
				+ "\nObs: A c�pia ir� incluir as configura��es, livros, empr�stimos, clientes, funcion�rios e imagens, que ser�o salvos em um �nico ficheiro.\n"
				+ "N�o modifique esse ficheiro!\n"
				+ "Voc� deve copi�-lo para um lugar seguro (por exemplo, uma pen-drive) para mais tarde ser poss�vel restaurar,\n"
				+ "caso o computador seja formatado ou voc� pretenda usar o programa em outro computador.";
		int ok = JOptionPane.showConfirmDialog(null, message, "C�pia de Seguran�a", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
		if (ok == JOptionPane.OK_OPTION) {
			String name = "BibliotecaDAD-Backup-" + new SimpleDateFormat("ddMMMyyyy-HH'h'mm").format(new Date())
					+ ".dadb";
			ZipCompress.compress(Main.DATABASE_DIR, name, Main.BACKUP_DIR);
			JOptionPane.showMessageDialog(null, "C�pia de seguran�a salva com sucesso na pasta:\n" + Main.BACKUP_DIR,
					"C�pia de Seguran�a - Sucesso", JOptionPane.OK_OPTION,
					new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
			try {
				Desktop.getDesktop().open(new File(Main.BACKUP_DIR));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateItems() {
		TableModelUser.getInstance().updateItems();
	}

	private class MyDispatcher implements KeyEventDispatcher {
		@Override
		public boolean dispatchKeyEvent(KeyEvent e) {
			if (e.getID() == KeyEvent.KEY_PRESSED) {
				if ((e.getKeyCode() == KeyEvent.VK_Z) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					anular();
				} else if ((e.getKeyCode() == KeyEvent.VK_Y) && ((e.getModifiers() & KeyEvent.CTRL_MASK) != 0)) {
					refazer();
				}

			}
			return false;
		}
	}

	public JMenuItem getMenuAnular() {
		return menuAnular;
	}

	public JMenuItem getMenuRefazer() {
		return menuRefazer;
	}

	public void open() {
		setVisible(true);
		JOptionPane pane = new JOptionPane("Bem vindo " + Login.NOME + "!", JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, new ImageIcon(getClass().getResource("/DAD_SS.jpg")), new Object[] {},
				null);
		final JDialog dialog = pane.createDialog("Boas vindas");
		dialog.setModal(true);
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("/DAD.jpg"))));
		Timer timer = new Timer(DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}

	public void anular() {

		TableModelUser.getInstance().getUndoManager().undo();
	}

	public void refazer() {

		TableModelUser.getInstance().getUndoManager().redo();
	}

	public void ordenar() {
//		if (tabbedPane.getSelectedIndex() == 0)
//			TableModelUser.getInstance().ordenar();
	}

	private int num_checkboxEnabled() {
		int count = 0;
		if (tabbedPane.getSelectedIndex() == 0) {
			if (checkMembroAtivo.isSelected())
				count++;
			if (checkMembroNominal.isSelected())
				count++;
			if (checkCongregados.isSelected())
				count++;
			if (checkLideranca.isSelected())
				count++;
		}
		return count;
	}

	public int[] checkBoxEnabled() {
		int count = 0;
		int[] columns = new int[num_checkboxEnabled() + 3];
		if (tabbedPane.getSelectedIndex() == 0) {
			if (checkMembroAtivo.isSelected())
				columns[count++] = 0;
			if (checkMembroNominal.isSelected())
				columns[count++] = 1;
			if (checkCongregados.isSelected())
				columns[count++] = 2;
			if (checkLideranca.isSelected())
				columns[count++] = 3;
			columns[count++] = 5;
			columns[count++] = 6;
			columns[count++] = 7;
		}
		return columns;
	}

	public static DataGui getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DataGui();
		return INSTANCE;
	}

	public void filter(String filtro) {
		if (tabbedPane.getSelectedIndex() == 0) {
			TableRowSorter<TableModelUser> sorter = new TableRowSorter<TableModelUser>(TableModelUser.getInstance());
			UserPanel.getInstance().getUsers().setRowSorter(sorter);
			RowFilter<TableModelUser, Object> filter;
			if (filtro.trim().equals("")) {
				sorter.setRowFilter(null);
			} else {
				if (num_checkboxEnabled() == 6 || num_checkboxEnabled() == 0) {
					filter = RowFilter
							.regexFilter((Pattern.compile("(?i)" + filtro, Pattern.CASE_INSENSITIVE).toString()));
					UserPanel.getInstance().getUsers().setDefaultRenderer(Object.class, new CellRenderer());
				} else
					filter = RowFilter.regexFilter(
							(Pattern.compile("(?i)" + filtro, Pattern.CASE_INSENSITIVE).toString()), checkBoxEnabled());
				sorter.setRowFilter(filter);
				setRenderers();
			}
		}
	}

	public void setRenderers() {
		if (tabbedPane.getSelectedIndex() == 0) {
			TableColumnModel tcl = UserPanel.getInstance().getUsers().getColumnModel();
			if (checkMembroAtivo.isSelected())
				tcl.getColumn(0).setCellRenderer(new CellRendererNoImage());
			else
				tcl.getColumn(0).setCellRenderer(new DefaultCellRenderer());
			if (checkMembroNominal.isSelected())
				tcl.getColumn(1).setCellRenderer(new CellRenderer());
			else
				tcl.getColumn(1).setCellRenderer(new DefaultCellRenderer());
			if (checkCongregados.isSelected())
				tcl.getColumn(2).setCellRenderer(new CellRenderer());
			else
				tcl.getColumn(2).setCellRenderer(new DefaultCellRenderer());
			if (checkLideranca.isSelected())
				tcl.getColumn(3).setCellRenderer(new CellRenderer());
			else
				tcl.getColumn(3).setCellRenderer(new DefaultCellRenderer());
		}
	}

	public JTextField getPesquisa() {
		return pesquisa;
	}

}
