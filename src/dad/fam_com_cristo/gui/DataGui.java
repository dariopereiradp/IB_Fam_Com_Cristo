package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableRowSorter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.time.DurationFormatUtils;

import com.qoppa.pdfWriter.PDFDocument;

import dad.fam_com_cristo.Tipo_Membro;
import dad.fam_com_cristo.gui.themes.DarkTheme;
import dad.fam_com_cristo.gui.themes.LiteTheme;
import dad.fam_com_cristo.table.FinancasPanel;
import dad.fam_com_cristo.table.MembroPanel;
import dad.fam_com_cristo.table.TableModelMembro;
import dad.recursos.FichaMembro_Vazia;
import dad.recursos.Log;
import dad.recursos.SairAction;
import dad.recursos.TableToPDF;
import dad.recursos.Utils;
import dad.recursos.ZipCompress;

/**
 * Classe que torna visível a 'data' das várias bases de dados.
 * 
 * @author Dário Pereira
 *
 */
public class DataGui extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5748160687318648477L;
	/**
	 * Tempo de espera para a janela de informações fechar.
	 */
	private static final int DELAY = 2000;
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
	private JMenu mnExportar;
	private JMenuItem mListaBatizados;
	private JMenuItem mListaAtivos;
	private JMenuItem mListaTotal;
	private JMenuItem mListaNom;
	private JMenuItem mListaCong;
	private JMenuItem mntmListaDeLderes;
	private JMenuItem mntmFichaDeMembro;
	private JMenu mnConf;
	private JMenuItem mnLight;
	private JMenuItem mnDark;
	private JMenu mnTema;
	
	private DataGui() {
		INSTANCE = this;
		setTitle(Main.TITLE_SMALL);
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

		tabbedPane.addTab("Finanças", FinancasPanel.getInstance());
		tabbedPane.setToolTipTextAt(0,
				"Pessoas que vão à IBFC com alguma regularidade: liderança, membros ativos, membros nominais e congregados ou alguém que já foi membro");
		
		//Atualizcao TODO
//		tabbedPane.setToolTipTextAt(1, "Registrar entradas e saídas financeiras da igreja");
		tabbedPane.setToolTipTextAt(1, "Recurso em desenvolvimento... Brevemente disponível numa atualização futura!");
		tabbedPane.setEnabledAt(1, false);

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
		
		mnConf = new JMenu("Configura\u00E7\u00F5es");
		mnArquivo.add(mnConf);
				
				mnTema = new JMenu("Tema");
				mnConf.add(mnTema);
				
				mnLight = new JMenuItem("Claro");
				mnTema.add(mnLight);
				
				mnLight.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Utils.getInstance().changeTheme(new LiteTheme());
						
					}
				});
				
				mnDark = new JMenuItem("Escuro");
				mnTema.add(mnDark);

				mnDark.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						Utils.getInstance().changeTheme(DarkTheme.getInstance());
					}
				});
		
				menuConfig = new JMenuItem("Outra configura\u00E7\u00F5es");
				mnConf.add(menuConfig);
				menuConfig.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						new Config().open();
					}
				});

		menuAtualizar = new JMenuItem("Atualizar Tabelas");
		mnArquivo.add(menuAtualizar);

		mnLimpar = new JMenuItem("Limpar espa\u00E7o");
		mnLimpar.setToolTipText("Apaga os arquivos de logs antigos, que são desnecessários");
		mnLimpar.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				int ok = JOptionPane.showOptionDialog(null,
						"Os arquivos de logs antigos serão apagados. Isso não influencia o funcionamento do programa.\n"
								+ "Tem a certeza que quer limpar?",
						"Limpar espaço", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(getClass().getResource("/FC_SS.jpg")), Main.OPTIONS, Main.OPTIONS[1]);
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

		menuOrdenar = new JMenuItem("Ordenar membros (A-Z)");
		mnEditar.add(menuOrdenar);

		mnExportar = new JMenu("Exportar");
		menuBar.add(mnExportar);

		mListaBatizados = new JMenuItem("Lista de Batizados (membros ativos e nominais)");
		mListaBatizados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TableToPDF.toPDF(MembroPanel.getInstance().newTable("Batizados"), "Batizados");
			}
		});

		mntmFichaDeMembro = new JMenuItem("Ficha de Membro (para preencher)");
		mntmFichaDeMembro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PDFDocument pdf = new FichaMembro_Vazia().generatePDF();
				try {
					pdf.saveDocument(Main.MEMBROS_PDF_PATH + "Ficha de Membro" + ".pdf");
					String message = "A ficha de membro"
							+ " foi criada com sucesso!\nFoi salvo um documento PDF (que pode ser impresso) na pasta:\n"
							+ Main.MEMBROS_PDF_PATH + "\nVocê quer abrir o documento agora?";
					int ok = JOptionPane.showOptionDialog(DataGui.getInstance(), message, "Criado com sucesso",
							JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
							new ImageIcon(getClass().getResource("/FC_SS.jpg")), Main.OPTIONS, Main.OPTIONS[1]);
					Log.getInstance().printLog(message);
					if (ok == JOptionPane.YES_OPTION) {
						Desktop.getDesktop().open(new File(Main.MEMBROS_PDF_PATH));
						Desktop.getDesktop().open(new File(Main.MEMBROS_PDF_PATH + "Ficha de Membro" + ".pdf"));
					}
				} catch (IOException e) {
					Log.getInstance().printLog("Erro ao salvar PDF de ficha de membro - " + e.getMessage());
					JOptionPane.showMessageDialog(null,
							"Não foi possível criar o PDF da Ficha de Membro!\n"
									+ "Se tiver uma ficha de membro aberta, por favor feche e tente novamente!",
							"Criar ficha de membro - Erro", JOptionPane.OK_OPTION,
							new ImageIcon(getClass().getResource("/FC_SS.jpg")));
					e.printStackTrace();
				}
			}
		});
		mnExportar.add(mntmFichaDeMembro);
		mnExportar.add(mListaBatizados);

		mListaAtivos = new JMenuItem("Lista de Membros Ativos");
		mListaAtivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableToPDF.toPDF(MembroPanel.getInstance().newTable("Ativos"), "Ativos");
			}
		});
		mnExportar.add(mListaAtivos);

		mListaNom = new JMenuItem("Lista de Membros Nominais");
		mListaNom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableToPDF.toPDF(MembroPanel.getInstance().newTable("Nominais"), "Nominais");
			}
		});
		mnExportar.add(mListaNom);

		mListaCong = new JMenuItem("Lista de Congregados");
		mListaCong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableToPDF.toPDF(MembroPanel.getInstance().newTable("Congregados"), "Congregados");
			}
		});
		mnExportar.add(mListaCong);

		mntmListaDeLderes = new JMenuItem("Lista de L\u00EDderes");
		mntmListaDeLderes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableToPDF.toPDF(MembroPanel.getInstance().newTable("Líderes"), "Líderes");
			}
		});
		mnExportar.add(mntmListaDeLderes);

		mListaTotal = new JMenuItem("Lista Total (todos, exceto ex-membros)");
		mnExportar.add(mListaTotal);
		mListaTotal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				TableToPDF.toPDF(MembroPanel.getInstance().newTable("Todos"), "Todos");

			}
		});

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
						+ "Igreja Batista Famílias com Cristo/" + "Manual_Instrucoes_IBFC_v" + Main.VERSION + ".pdf";
				try {
					Desktop.getDesktop().open(new File(path));
				} catch (Exception e1) {
					JOptionPane.showMessageDialog(null,
							"Ocorreu um erro ao abrir o manual! Pode ter sido apagado...\n"
									+ "Consulte o manual que veio com o programa ou entre em contato com '"
									+ Main.EMAIL_SUPORTE + "'",
							"Manual de Instruções - Erro", JOptionPane.OK_OPTION,
							new ImageIcon(getClass().getResource("/FC_SS.jpg")));
					e1.printStackTrace();
				}

			}
		});
		menuManual.setEnabled(false);
		menuManual.setToolTipText("Manual ainda não disponível!");

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

	/**
	 * Abre a DataGui e um diálogo de boas vindas
	 */
	public void open() {
		setVisible(true);
		JOptionPane pane = new JOptionPane("Bem vindo " + Login.NOME + "!", JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION, new ImageIcon(getClass().getResource("/FC_SS.jpg")), new Object[] {}, null);
		final JDialog dialog = pane.createDialog("Boas vindas");
		dialog.setModal(true);
		dialog.setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("/FC.jpg"))));
		Timer timer = new Timer(DELAY, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dialog.dispose();
			}
		});
		timer.setRepeats(false);
		timer.start();
		dialog.setVisible(true);
	}

	/**
	 * Apaga os logs dos meses anteriores, para limpar espaço no sistema.
	 */
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
						new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			} catch (IOException e1) {
				Log.getInstance().printLog("Erro ao limpar o espaço! - " + e.getMessage());
				JOptionPane.showMessageDialog(null, "Erro ao fazer a limpeza! - " + e.getMessage(),
						"Limpar espaço - Erro", JOptionPane.OK_OPTION,
						new ImageIcon(getClass().getResource("/FC_SS.jpg")));
				e1.printStackTrace();
			}
		}

	}

	/**
	 * Cria um backup das bases de dados e imagens.
	 */
	public void backup() {
		String message = "Deseja criar uma cópia de segurança de todas as bases de dados do programa?"
				+ "\nObs: A cópia irá incluir as configurações, membros, finanças e imagens, que serão salvos em um único ficheiro.\n"
				+ "Não modifique esse ficheiro!\n"
				+ "Você deve copiá-lo para um lugar seguro (por exemplo, uma pen-drive) para mais tarde ser possível restaurar,\n"
				+ "caso o computador seja formatado ou você pretenda usar o programa em outro computador.";
		int ok = JOptionPane.showOptionDialog(null, message, "Cópia de Segurança", JOptionPane.YES_NO_OPTION,
				JOptionPane.INFORMATION_MESSAGE, new ImageIcon(getClass().getResource("/FC_SS.jpg")), Main.OPTIONS,
				Main.OPTIONS[0]);
		if (ok == JOptionPane.OK_OPTION) {
			backupDirect();
			JOptionPane.showMessageDialog(null, "Cópia de segurança salva com sucesso na pasta:\n" + Main.BACKUP_DIR,
					"Cópia de Segurança - Sucesso", JOptionPane.OK_OPTION,
					new ImageIcon(getClass().getResource("/FC_SS.jpg")));
			try {
				Desktop.getDesktop().open(new File(Main.BACKUP_DIR));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Cria um backup da base de dados diretamente.
	 * 
	 * @return o nome do ficheiro de backup criado.
	 */
	public String backupDirect() {
		String name = "IB_Fam_com_Cristo-Backup-" + new SimpleDateFormat("ddMMMyyyy-HH'h'mm").format(new Date())
				+ ".fccb";
		ZipCompress.compress(Main.DATABASE_DIR, name, Main.BACKUP_DIR);
		return Main.BACKUP_DIR + name;
	}

	/**
	 * Atualiza os menus anular e refazer, de acordo com a tabela que estiver
	 * ativa no momento.
	 */
	public void updateItems() {
		if (tabbedPane.getSelectedIndex() == 0)
			TableModelMembro.getInstance().updateItems();
		else if (tabbedPane.getSelectedIndex() == 1)
			// TODO
			return;
	}

	/**
	 * Responsável por captar as teclas CTRL+Z e CTRL+Y, para anular e refazer,
	 * respetivamente.
	 * 
	 * @author Dário Pereira
	 *
	 */
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

	/**
	 * Chama o método undo da tabela que estiver ativa.
	 */
	public void anular() {
		if (tabbedPane.getSelectedIndex() == 0)
			TableModelMembro.getInstance().getUndoManager().undo();
		else if (tabbedPane.getSelectedIndex() == 1)
			// TODO
			return;
	}

	/**
	 * Chama o método redo da tabela que estiver ativa.
	 */
	public void refazer() {
		if (tabbedPane.getSelectedIndex() == 0)
			TableModelMembro.getInstance().getUndoManager().redo();
		else if (tabbedPane.getSelectedIndex() == 1)
			// TODO
			return;
	}

	/**
	 * Ordena a tabela que estiver ativa, caso esteja disponível essa função.
	 */
	public void ordenar() {
		if (tabbedPane.getSelectedIndex() == 0)
			TableModelMembro.getInstance().ordenar();
	}

	/**
	 * Realiza o filtro de pesquisa na tabela que estiver ativa e de acordo com
	 * as check box marcadas.
	 * 
	 * @param filtro
	 *            - expressão que se pretende pesquisar
	 */
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

	public static DataGui getInstance() {
		if (INSTANCE == null)
			INSTANCE = new DataGui();
		return INSTANCE;
	}

	public JMenuItem getMenuAnular() {
		return menuAnular;
	}

	public JMenuItem getMenuRefazer() {
		return menuRefazer;
	}

	public JTextField getPesquisa() {
		return pesquisa;
	}

}
