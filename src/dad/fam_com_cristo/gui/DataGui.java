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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
import javax.swing.table.TableRowSorter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import dad.fam_com_cristo.table.TableModelMembro;
import dad.fam_com_cristo.Tipo_Membro;
import dad.fam_com_cristo.table.MembroPanel;
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
	private JCheckBox checkMembroAtivo, checkMembroNominal, checkCongregados, checkLideranca, check_ex_membros;
	private JMenuItem mntmRelatarErro;
	private JMenuItem mnLimpar;
	private JMenuItem menuManual;

	private DataGui() {
		INSTANCE = this;
		setTitle(Main.TITLE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("/FC.jpg"))));
		setMinimumSize(new Dimension(1350, 600));
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
		checkMembroAtivo.setToolTipText("Pessoas batizadas que participam ativamente na igreja");

		checkMembroNominal = new JCheckBox("Membro Nominal");
		checkMembroNominal.setSelected(true);
		filtrosPanel.add(checkMembroNominal);
		checkMembroNominal.setToolTipText("Pessoas que já foram batizadas mas não são muito envolvidas na igreja");

		checkCongregados = new JCheckBox("Congregados");
		checkCongregados.setSelected(true);
		filtrosPanel.add(checkCongregados);
		checkCongregados.setToolTipText("Pessoas que vão à igreja com regularidade mas não são batizadas");

		checkLideranca = new JCheckBox("Liderança");
		checkLideranca.setSelected(true);
		filtrosPanel.add(checkLideranca);
		checkLideranca.setToolTipText("Líderes da igreja e dos ministérios da igreja. São também membros ativos");

		check_ex_membros = new JCheckBox("Ex-Membros");
		check_ex_membros.setSelected(false);
		filtrosPanel.add(check_ex_membros);
		check_ex_membros.setToolTipText("Pessoas que já foram membros mas saíram por transferência ou abandono");

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("Pessoas associadas", MembroPanel.getInstance());

		tabbedPane.setToolTipTextAt(0,
				"Pessoas que vão à IBFC com alguma regularidade: liderança, membros ativos, membros nominais e congregados ou alguém que já foi membro");

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

		menuBackup = new JMenuItem("Cópia de segurança");
		menuBackup.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				backup();
			}
		});
		mnArquivo.add(menuBackup);

		menuImportar = new JMenuItem("Restaurar Cópia de Segurança");
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
		mnLimpar.setToolTipText("Apaga os arquivos de logs antigos, que são desnecessários");
		mnLimpar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ok = JOptionPane.showConfirmDialog(null,
						"Os arquivos de logs antigos serão apagados. Isso não influencia o funcionamento do programa.\n"
								+ "Tem a certeza que quer limpar?",
						"Limpar espaço", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
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
						+ "Biblioteca Dádiva de Deus/" + "Manual_Instrucoes_Biblioteca_DAD_v" + Main.VERSION + ".pdf";
				try {
					Desktop.getDesktop().open(new File(path));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							"Ocorreu um erro ao abrir o manual! Pode ter sido apagado...\n"
									+ "Consulte o manual que veio com o programa ou entre em contato com '"
									+ Main.EMAIL_SUPORTE + "'",
							"Manual de Instruções - Erro", JOptionPane.OK_OPTION,
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

		getRootPane().setDefaultButton(MembroPanel.getInstance().getbAdd());

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
				TableModelMembro.getInstance().fireTableDataChanged();
			}
		});

		pesquisa.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				newFilter(pesquisa.getText().toLowerCase());
			}

		});

		checkMembroAtivo.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});
		checkMembroNominal.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});
		checkCongregados.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});

		checkLideranca.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});

		check_ex_membros.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});

		TableModelMembro.getInstance().addListeners();

		tabbedPane.addChangeListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				newFilter("");
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
				JOptionPane.showMessageDialog(null, "Limpeza feita!", "Limpar espaço - Sucesso", JOptionPane.OK_OPTION,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
			} catch (IOException e1) {
				Log.getInstance().printLog("Erro ao limpar o espaço! - " + e.getMessage());
				JOptionPane.showMessageDialog(null, "Erro ao fazer a limpeza! - " + e.getMessage(),
						"Limpar espaço - Erro", JOptionPane.OK_OPTION,
						new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
				e1.printStackTrace();
			}
		}

	}

	public void backup() {
		String message = "Deseja criar uma cópia de segurança de todas as bases de dados do programa?"
				+ "\nObs: A cópia irá incluir as configurações, livros, empréstimos, clientes, funcionários e imagens, que serão salvos em um único ficheiro.\n"
				+ "Não modifique esse ficheiro!\n"
				+ "Você deve copiá-lo para um lugar seguro (por exemplo, uma pen-drive) para mais tarde ser possível restaurar,\n"
				+ "caso o computador seja formatado ou você pretenda usar o programa em outro computador.";
		int ok = JOptionPane.showConfirmDialog(null, message, "Cópia de Segurança", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
		if (ok == JOptionPane.OK_OPTION) {
			String name = "BibliotecaDAD-Backup-" + new SimpleDateFormat("ddMMMyyyy-HH'h'mm").format(new Date())
					+ ".dadb";
			ZipCompress.compress(Main.DATABASE_DIR, name, Main.BACKUP_DIR);
			JOptionPane.showMessageDialog(null, "Cópia de segurança salva com sucesso na pasta:\n" + Main.BACKUP_DIR,
					"Cópia de Segurança - Sucesso", JOptionPane.OK_OPTION,
					new ImageIcon(getClass().getResource("/DAD_SS.jpg")));
			try {
				Desktop.getDesktop().open(new File(Main.BACKUP_DIR));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void updateItems() {
		TableModelMembro.getInstance().updateItems();
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

	public void anular() {

		TableModelMembro.getInstance().getUndoManager().undo();
	}

	public void refazer() {

		TableModelMembro.getInstance().getUndoManager().redo();
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

	public void ordenar() {
		// if (tabbedPane.getSelectedIndex() == 0)
		// TableModelUser.getInstance().ordenar();
	}

//	private int num_checkboxEnabled() {
//		int count = 0;
//		if (tabbedPane.getSelectedIndex() == 0) {
//			if (checkMembroAtivo.isSelected())
//				count++;
//			if (checkMembroNominal.isSelected())
//				count++;
//			if (checkCongregados.isSelected())
//				count++;
//			if (checkLideranca.isSelected())
//				count++;
//			if (check_ex_membros.isSelected())
//				count++;
//		}
//		return count;
//	}
//
//	public int[] checkBoxEnabled() {
//		int count = 0;
//		int[] check = new int[num_checkboxEnabled()];
//		if (checkMembroAtivo.isSelected())
//			check[count++] = 0;
//		if (checkMembroNominal.isSelected())
//			check[count++] = 1;
//		if (checkCongregados.isSelected())
//			check[count++] = 2;
//		if (checkLideranca.isSelected())
//			check[count++] = 3;
//		if (check_ex_membros.isSelected())
//			check[count++] = 4;
//		return check;
//	}

	public static DataGui getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DataGui();
		return INSTANCE;
	}

	// public void filter(String filtro) {
	// if (tabbedPane.getSelectedIndex() == 0) {
	// TableRowSorter<TableModelMembro> sorter = new
	// TableRowSorter<TableModelMembro>(
	// TableModelMembro.getInstance());
	// MembroPanel.getInstance().getMembros().setRowSorter(sorter);
	// RowFilter<TableModelMembro, Object> filter;
	// if (filtro.trim().equals("")) {
	// sorter.setRowFilter(null);
	// } else {
	// filter = RowFilter.regexFilter((Pattern.compile("(?i)" + filtro,
	// Pattern.CASE_INSENSITIVE).toString()));
	// MembroPanel.getInstance().getMembros().setDefaultRenderer(Object.class,
	// new CellRenderer());
	// sorter.setRowFilter(filter);
	// }
	// }
	// }

	public void newFilter(String filtro) {
		RowFilter<TableModelMembro, Object> rf = null;
		RowFilter<TableModelMembro, Object> rowFilter = null;
		TableRowSorter<TableModelMembro> sorter = new TableRowSorter<TableModelMembro>(TableModelMembro.getInstance());
		List<RowFilter<TableModelMembro, Object>> filters = new ArrayList<RowFilter<TableModelMembro, Object>>(5);
		List<RowFilter<TableModelMembro, Object>> andFilters = new ArrayList<RowFilter<TableModelMembro, Object>>(1);
		MembroPanel.getInstance().getMembros().setRowSorter(sorter);
		andFilters.add(RowFilter.regexFilter((Pattern.compile("(?i)" + filtro, Pattern.CASE_INSENSITIVE).toString())));
		if (checkMembroAtivo.isSelected()) {
			filters.add(RowFilter.regexFilter(Tipo_Membro.MEMBRO_ATIVO.getDescricao(), 3));
		}
		if (checkMembroNominal.isSelected()) {
			filters.add(RowFilter.regexFilter(Tipo_Membro.MEMBRO_NOMINAL.getDescricao(), 3));
		}
		if (check_ex_membros.isSelected()) {
			filters.add(RowFilter.regexFilter(Tipo_Membro.EX_MEMBRO.getDescricao(), 3));
		}
		if (checkCongregados.isSelected()) {
			filters.add(RowFilter.regexFilter(Tipo_Membro.CONGREGADO.getDescricao(), 3));
		}
		if (checkLideranca.isSelected()) {
			filters.add(RowFilter.regexFilter(Tipo_Membro.LIDERANCA.getDescricao(), 3));
		}

		try {
			rf = RowFilter.orFilter(filters);
			andFilters.add(rf);
			rowFilter = RowFilter.andFilter(andFilters);
		} catch (java.util.regex.PatternSyntaxException e) {
			return;
		}
		sorter.setRowFilter(rowFilter);
	}

	public JTextField getPesquisa() {
		return pesquisa;
	}

}
