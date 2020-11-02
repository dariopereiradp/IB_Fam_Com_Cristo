package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridLayout;
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
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableRowSorter;

import org.apache.commons.lang.time.DurationFormatUtils;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.gui.themes.DarkTheme;
import dad.fam_com_cristo.gui.themes.LiteTheme;
import dad.fam_com_cristo.table.FinancasPanel;
import dad.fam_com_cristo.table.MembroPanel;
import dad.fam_com_cristo.table.TableModelFinancas;
import dad.fam_com_cristo.table.TableModelMembro;
import dad.fam_com_cristo.table.conexao.ConexaoFinancas;
import dad.fam_com_cristo.table.conexao.ConexaoMembro;
import dad.fam_com_cristo.types.enumerados.EstatisticaPeriodos;
import dad.fam_com_cristo.types.enumerados.ImageFormats;
import dad.fam_com_cristo.types.enumerados.Tipo_Membro;
import dad.fam_com_cristo.types.enumerados.Tipo_Transacao;
import dad.recursos.CSVExport;
import dad.recursos.DataPesquisavel;
import dad.recursos.Log;
import dad.recursos.MultiDatePicker;
import dad.recursos.SairAction;
import dad.recursos.Utils;
import dad.recursos.pdf.FichaMembroToPDF;
import dad.recursos.pdf.TableFinancasToPDF;
import dad.recursos.pdf.TableMembrosToPDF;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

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
	private static final int DELAY = 1500;
	private static DataGui INSTANCE;
	private JTabbedPane tabbedPane;
	private JTextField pesquisa;
	private JPanel filtrosPanel;
	private JCheckBox checkMembroAtivo, checkMembroNominal, checkCongregados, checkLideranca, check_ex_membros,
			checkEntradas, checkSaidas;
	private MultiDatePicker datas;
	private JMenuBar menuBar;
	private JMenu mnArquivo, mnAjuda, mnEditar;
	private JMenuItem menuSobre, menuEstatisticas, menuSair, menuAnular, menuRefazer, menuImportar, menuBackup,
			menuOrdenar, menuAtualizar, menuConfig;
	private JMenuItem mntmRelatarErro, mnLimpar, menuManual;
	private JMenu mnGerar;
	private JMenuItem mListaBatizados, mListaAtivos, mListaTotal, mListaNom, mListaCong, mListaDeLideres,
			mFichaDeMembro;
	private JMenu mnConf, mnTema, mnMembros, mnFinancas;
	private JRadioButtonMenuItem mnLight, mnDark;
	private JMenuItem mnRelatorioAnual, mnMesAnterior, mnRelatorioTotal;
	private JMenu mnExportar, mnExportLogo;
	private JMenuItem mnExportMembros, mnExportTransacoes;
	private JMenuItem mnExportLogoPng, mnExportJpg, mnExportLogoSvg, mnExportarLogoPdf, mnModeloOficio;

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

		recreate();
	}

	/**
	 * Cria os componentes
	 */
	public void recreate() {
		JPanel pesquisaPanel = new JPanel(new BorderLayout());
		pesquisa = new JTextField();
		JLabel pesquisaLabel = new JLabel("Pesquisa: ");
		pesquisaPanel.add(pesquisaLabel, BorderLayout.WEST);

		JPanel pesquisaPanel1 = new JPanel(new GridLayout(3, 1));
		pesquisaPanel1.add(new JLabel(""));
		pesquisaPanel1.add(pesquisa);
		pesquisaPanel1.add(new JLabel(""));

		pesquisaPanel.add(pesquisaPanel1, BorderLayout.CENTER);

		getContentPane().add(pesquisaPanel, BorderLayout.NORTH);

		filtrosPanel = new JPanel();
		pesquisaPanel.add(filtrosPanel, BorderLayout.EAST);

		createCheckBoxes();

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("Pessoas associadas", MembroPanel.getInstance());

		tabbedPane.addTab("Finanças", FinancasPanel.getInstance());
		tabbedPane.setToolTipTextAt(0,
				"Pessoas que vão à IBFC com alguma regularidade: liderança, membros ativos, membros nominais e congregados ou alguém que já foi membro");

		tabbedPane.setToolTipTextAt(1, "Registrar entradas e saídas financeiras da igreja");

		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				visibleBoxes();
				newFilter("");
				updateItems();
				if (tabbedPane.getSelectedIndex() == 1)
					getRootPane().setDefaultButton(FinancasPanel.getInstance().getBtnAdd());
				else
					getRootPane().setDefaultButton(null);
			}
		});

		datas = new MultiDatePicker();
		filtrosPanel.add(datas);
		datas.setVisible(false);

		createMenus();

		KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		manager.addKeyEventDispatcher(new MyDispatcher());

		pesquisa.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});

		TableModelMembro.getInstance().addListeners();
		TableModelFinancas.getInstance().addListeners();
	}

	/**
	 * 
	 */
	public void createMenus() {
		menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		mnArquivo = new JMenu("Arquivo");
		menuBar.add(mnArquivo);

		menuEstatisticas = new JMenuItem("Estat\u00EDsticas");
		menuEstatisticas.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.INSERT_CHART,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		menuEstatisticas.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Estatisticas().open();
			}
		});
		mnArquivo.add(menuEstatisticas);

		menuBackup = new JMenuItem("Cópia de segurança");
		menuBackup.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.BACKUP,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		menuBackup.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.getInstance().backup();
			}
		});
		mnArquivo.add(menuBackup);

		menuImportar = new JMenuItem("Restaurar Cópia de Segurança");
		menuImportar.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.RESTORE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnArquivo.add(menuImportar);
		menuImportar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Restauro().open();
			}
		});

		mnConf = new JMenu("Configura\u00E7\u00F5es");
		mnConf.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.SETTINGS_APPLICATIONS,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnArquivo.add(mnConf);

		mnTema = new JMenu("Tema");
		mnTema.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.SETTINGS_BRIGHTNESS,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnConf.add(mnTema);

		mnLight = new JRadioButtonMenuItem("Claro");
		mnLight.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.WB_SUNNY,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnTema.add(mnLight);

		mnLight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.getInstance().changeTheme(new LiteTheme());
			}
		});

		mnDark = new JRadioButtonMenuItem("Escuro");
		mnDark.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.WB_CLOUDY,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnTema.add(mnDark);

		mnDark.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.getInstance().changeTheme(DarkTheme.getInstance());
			}
		});

		if (Utils.getInstance().getCurrentTheme() instanceof DarkTheme)
			mnDark.setSelected(true);
		else
			mnLight.setSelected(true);

		menuConfig = new JMenuItem("Outra configura\u00E7\u00F5es");
		menuConfig.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.SETTINGS,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnConf.add(menuConfig);
		menuConfig.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new Config().open();
			}
		});

		menuAtualizar = new JMenuItem("Atualizar Tabelas");
		menuAtualizar.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.UPDATE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnArquivo.add(menuAtualizar);

		mnLimpar = new JMenuItem("Limpar espa\u00E7o");
		mnLimpar.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.CLEAR_ALL,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnLimpar.setToolTipText("Apaga os arquivos de logs antigos, que são desnecessários");
		mnLimpar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int ok = JOptionPane.showOptionDialog(null,
						"Os arquivos de logs antigos serão apagados. Isso não influencia o funcionamento do programa.\n"
								+ "Tem a certeza que quer limpar?",
						"Limpar espaço", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
						new ImageIcon(getClass().getResource("/FC_SS.jpg")), Main.OPTIONS, Main.OPTIONS[1]);
				if (ok == JOptionPane.YES_OPTION)
					Log.limpar();
			}
		});
		mnArquivo.add(mnLimpar);

		menuSair = new JMenuItem("Sair");
		menuSair.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.EXIT_TO_APP,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		menuSair.addActionListener(new SairAction());
		mnArquivo.add(menuSair);

		mnEditar = new JMenu("Editar");
		menuBar.add(mnEditar);

		menuAnular = new JMenuItem("Anular (Ctrl+Z) - ()");
		menuAnular.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.UNDO,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		menuAnular.setDisabledIcon(menuAnular.getIcon());
		menuAnular.setEnabled(false);
		mnEditar.add(menuAnular);

		menuRefazer = new JMenuItem("Refazer (Ctrl+Y) - ()");
		menuRefazer.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.REDO,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		menuRefazer.setDisabledIcon(menuRefazer.getIcon());
		menuRefazer.setEnabled(false);
		mnEditar.add(menuRefazer);

		menuOrdenar = new JMenuItem("Ordenar");
		menuOrdenar.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.REORDER,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnEditar.add(menuOrdenar);

		mnGerar = new JMenu("Gerar");
		menuBar.add(mnGerar);

		mnMembros = new JMenu("Membros");
		mnMembros.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PEOPLE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnGerar.add(mnMembros);

		mFichaDeMembro = new JMenuItem("Ficha de Membro (para preencher)");
		mFichaDeMembro.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnMembros.add(mFichaDeMembro);

		mListaBatizados = new JMenuItem("Lista de Batizados (membros ativos e nominais)");
		mListaBatizados.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnMembros.add(mListaBatizados);

		mListaAtivos = new JMenuItem("Lista de Membros Ativos");
		mListaAtivos.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnMembros.add(mListaAtivos);

		mListaNom = new JMenuItem("Lista de Membros Nominais");
		mListaNom.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnMembros.add(mListaNom);

		mListaCong = new JMenuItem("Lista de Congregados");
		mListaCong.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnMembros.add(mListaCong);

		mListaDeLideres = new JMenuItem("Lista de L\u00EDderes");
		mListaDeLideres.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnMembros.add(mListaDeLideres);

		mListaTotal = new JMenuItem("Lista Total (todos, exceto ex-membros)");
		mListaTotal.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnMembros.add(mListaTotal);

		mnFinancas = new JMenu("Finan\u00E7as");
		mnFinancas.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.MONETIZATION_ON,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnGerar.add(mnFinancas);

		mnRelatorioAnual = new JMenuItem("Relat\u00F3rio do ano anterior");
		mnRelatorioAnual.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnFinancas.add(mnRelatorioAnual);

		mnMesAnterior = new JMenuItem("Relat\u00F3rio do m\u00EAs anterior");
		mnMesAnterior.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnFinancas.add(mnMesAnterior);

		mnRelatorioTotal = new JMenuItem("Relat\u00F3rio completo");
		mnRelatorioTotal.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnFinancas.add(mnRelatorioTotal);
		mListaTotal.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				TableMembrosToPDF.membrosToPDF(MembroPanel.getInstance().newTable("Todos"), "Todos");

			}
		});
		mListaDeLideres.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableMembrosToPDF.membrosToPDF(MembroPanel.getInstance().newTable("Líderes"), "Líderes");
			}
		});
		mListaCong.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableMembrosToPDF.membrosToPDF(MembroPanel.getInstance().newTable("Congregados"), "Congregados");
			}
		});
		mListaNom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableMembrosToPDF.membrosToPDF(MembroPanel.getInstance().newTable("Nominais"), "Nominais");
			}
		});
		mListaAtivos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableMembrosToPDF.membrosToPDF(MembroPanel.getInstance().newTable("Ativos"), "Ativos");
			}
		});
		mListaBatizados.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TableMembrosToPDF.membrosToPDF(MembroPanel.getInstance().newTable("Batizados"), "Batizados");
			}
		});
		mFichaDeMembro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				FichaMembroToPDF.membroToPdf(null);
			}
		});

		mnRelatorioAnual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalDate init = EstatisticaPeriodos.ANO_ANTERIOR.getInit();
				LocalDate fim = EstatisticaPeriodos.ANO_ANTERIOR.getEnd();
				TableFinancasToPDF.transacoesToPDF(FinancasPanel.getInstance().newTable("Todos", init, fim),
						String.valueOf(init.getYear()), EstatisticaPeriodos.ANO_ANTERIOR, init, fim, true);
			}
		});

		mnMesAnterior.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LocalDate init = EstatisticaPeriodos.MES_ANTERIOR.getInit();
				LocalDate fim = EstatisticaPeriodos.MES_ANTERIOR.getEnd();
				TableFinancasToPDF.transacoesToPDF(FinancasPanel.getInstance().newTable("Todos", init, fim),
						init.getMonth().getDisplayName(TextStyle.FULL, new Locale("pt")) + " " + init.getYear(),
						EstatisticaPeriodos.MES_ANTERIOR, init, fim, false);
			}
		});

		mnRelatorioTotal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableFinancasToPDF.transacoesToPDF(FinancasPanel.getInstance().newTable("Todos", null, null),
						"Completo", EstatisticaPeriodos.DESDE_SEMPRE, null, null, false);
			}
		});

		mnExportar = new JMenu("Exportar");
		menuBar.add(mnExportar);

		mnExportMembros = new JMenuItem("Membros (CSV)");
		mnExportMembros.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PEOPLE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnExportar.add(mnExportMembros);

		mnExportMembros.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CSVExport.exportToCsv(new ConexaoMembro());
			}
		});

		mnExportTransacoes = new JMenuItem("Transa\u00E7\u00F5es (CSV)");
		mnExportTransacoes.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.MONETIZATION_ON,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnExportar.add(mnExportTransacoes);

		mnModeloOficio = new JMenuItem("Modelo de of\u00EDcio (.docx)");
		mnModeloOficio.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.exportModelo();
			}
		});
		mnModeloOficio.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.EVENT_NOTE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnExportar.add(mnModeloOficio);

		mnExportLogo = new JMenu("Logotipo");
		mnExportLogo.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.IMAGE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnExportar.add(mnExportLogo);

		mnExportLogoPng = new JMenuItem("PNG (fundo transparente)");
		mnExportLogoPng.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.IMAGE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnExportLogoPng.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.exportLogo(ImageFormats.PNG);
			}
		});
		mnExportLogo.add(mnExportLogoPng);

		mnExportJpg = new JMenuItem("JPG (fundo branco)");
		mnExportJpg.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.IMAGE,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnExportJpg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.exportLogo(ImageFormats.JPG);
			}
		});
		mnExportLogo.add(mnExportJpg);

		mnExportLogoSvg = new JMenuItem("SVG (para imprimir)");
		mnExportLogoSvg.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.IMAGE_ASPECT_RATIO,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnExportLogoSvg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.exportLogo(ImageFormats.SVG);
			}
		});
		mnExportLogo.add(mnExportLogoSvg);

		mnExportarLogoPdf = new JMenuItem("PDF");
		mnExportarLogoPdf.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.PICTURE_AS_PDF,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mnExportarLogoPdf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Utils.exportLogo(ImageFormats.PDF);
			}
		});
		mnExportLogo.add(mnExportarLogoPdf);

		mnExportTransacoes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CSVExport.exportToCsv(new ConexaoFinancas());
			}
		});

		mnAjuda = new JMenu("Ajuda");
		menuBar.add(mnAjuda);

		mntmRelatarErro = new JMenuItem("Relatar erro");
		mntmRelatarErro.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.BUG_REPORT,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		mntmRelatarErro.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new BugReport().open();
			}
		});

		menuManual = new JMenuItem("Manual de Instru\u00E7\u00F5es");
		menuManual.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.BOOK,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		menuManual.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = System.getenv("ProgramFiles(X86)") + System.getProperty("file.separator")
						+ "Igreja Batista Famílias com Cristo/" + "Manual_Instrucoes_" + Main.SIGLA + "_v"
						+ Main.VERSION + ".pdf";
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
		menuManual.setToolTipText("Manual ainda não disponível!");

		mnAjuda.add(menuManual);
		mnAjuda.add(mntmRelatarErro);

		menuSobre = new JMenuItem("Sobre");
		menuSobre.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.HELP,
				Utils.getInstance().getCurrentTheme().getColorIcons()));
		menuSobre.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new About().open();
			}
		});
		mnAjuda.add(menuSobre);

		menuAnular.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				anular();
			}
		});

		menuRefazer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				refazer();
			}
		});

		menuOrdenar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ordenar();
			}
		});

		menuAtualizar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TableModelMembro.getInstance().fireTableDataChanged();
			}
		});
	}

	/**
	 * Cria as checkboxes
	 */
	public void createCheckBoxes() {
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

		checkEntradas = new JCheckBox("Entradas");
		checkEntradas.setSelected(true);
		filtrosPanel.add(checkEntradas);
		checkEntradas.setVisible(false);

		checkSaidas = new JCheckBox("Saídas");
		checkSaidas.setSelected(true);
		filtrosPanel.add(checkSaidas);
		checkSaidas.setVisible(false);

		addCheckBoxListeners();

	}

	/**
	 * Adiciona os changeListeners às checkboxes
	 */
	public void addCheckBoxListeners() {
		checkMembroAtivo.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});
		checkMembroNominal.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});
		checkCongregados.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});

		checkLideranca.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});

		check_ex_membros.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});

		checkEntradas.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});

		checkSaidas.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				newFilter(pesquisa.getText().toLowerCase());
			}
		});
	}

	/**
	 * Atualiza quais check-box estarão visíveis, dependendo da tabela que estiver
	 * ativa.
	 */
	public void visibleBoxes() {
		if (tabbedPane.getSelectedIndex() == 0) {
			checkMembroAtivo.setVisible(true);
			checkMembroNominal.setVisible(true);
			checkCongregados.setVisible(true);
			checkLideranca.setVisible(true);
			check_ex_membros.setVisible(true);
			checkEntradas.setVisible(false);
			checkSaidas.setVisible(false);
			datas.setVisible(false);
		} else if (tabbedPane.getSelectedIndex() == 1) {
			checkMembroAtivo.setVisible(false);
			checkMembroNominal.setVisible(false);
			checkCongregados.setVisible(false);
			checkLideranca.setVisible(false);
			check_ex_membros.setVisible(false);
			checkEntradas.setVisible(true);
			checkSaidas.setVisible(true);
			datas.setVisible(true);
		}
	}

	/**
	 * Atualiza os menus anular e refazer, de acordo com a tabela que estiver ativa
	 * no momento.
	 */
	public void updateItems() {
		if (tabbedPane.getSelectedIndex() == 0)
			TableModelMembro.getInstance().updateItems();
		else if (tabbedPane.getSelectedIndex() == 1)
			TableModelFinancas.getInstance().updateItems();
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
			TableModelFinancas.getInstance().getUndoManager().undo();
	}

	/**
	 * Chama o método redo da tabela que estiver ativa.
	 */
	public void refazer() {
		if (tabbedPane.getSelectedIndex() == 0)
			TableModelMembro.getInstance().getUndoManager().redo();
		else if (tabbedPane.getSelectedIndex() == 1)
			TableModelFinancas.getInstance().getUndoManager().redo();
	}

	/**
	 * Ordena a tabela que estiver ativa, caso esteja disponível essa função.
	 */
	public void ordenar() {
		if (tabbedPane.getSelectedIndex() == 0)
			TableModelMembro.getInstance().ordenar();
		else if (tabbedPane.getSelectedIndex() == 1)
			TableModelFinancas.getInstance().ordenar();
	}

	/**
	 * Realiza o filtro de pesquisa na tabela que estiver ativa e de acordo com as
	 * check box marcadas.
	 * 
	 * @param filtro - expressão que se pretende pesquisar
	 */
	public void newFilter(String filtro) {
		if (tabbedPane.getSelectedIndex() == 0) {
			RowFilter<TableModelMembro, Object> rf = null;
			RowFilter<TableModelMembro, Object> rowFilter = null;
			TableRowSorter<TableModelMembro> sorter = new TableRowSorter<TableModelMembro>(
					TableModelMembro.getInstance());
			List<RowFilter<TableModelMembro, Object>> filters = new ArrayList<RowFilter<TableModelMembro, Object>>(5);
			List<RowFilter<TableModelMembro, Object>> andFilters = new ArrayList<RowFilter<TableModelMembro, Object>>(
					1);
			MembroPanel.getInstance().getMembros().setRowSorter(sorter);
			andFilters.add(
					RowFilter.regexFilter((Pattern.compile("(?i)" + filtro, Pattern.CASE_INSENSITIVE).toString())));
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
		} else if (tabbedPane.getSelectedIndex() == 1) {
			RowFilter<TableModelFinancas, Object> rf = null;
			RowFilter<TableModelFinancas, Object> rowFilter = null;
			TableRowSorter<TableModelFinancas> sorter = new TableRowSorter<TableModelFinancas>(
					TableModelFinancas.getInstance());
			List<RowFilter<TableModelFinancas, Object>> filters = new ArrayList<RowFilter<TableModelFinancas, Object>>();
			List<RowFilter<TableModelFinancas, Object>> andFilters = new ArrayList<RowFilter<TableModelFinancas, Object>>();
			FinancasPanel.getInstance().getTransacoes().setRowSorter(sorter);
			andFilters.add(
					RowFilter.regexFilter((Pattern.compile("(?i)" + filtro, Pattern.CASE_INSENSITIVE).toString())));
			if (checkEntradas.isSelected()) {
				filters.add(RowFilter.regexFilter(Tipo_Transacao.ENTRADA.getDescricao(), 3));
			}
			if (checkSaidas.isSelected()) {
				filters.add(RowFilter.regexFilter(Tipo_Transacao.SAIDA.getDescricao(), 3));
			}
			if (datas.getInitDate() != null) {
				andFilters.add(new RowFilter<TableModelFinancas, Object>() {

					@Override
					public boolean include(Entry<? extends TableModelFinancas, ? extends Object> entry) {
						LocalDate data = ((DataPesquisavel) entry.getModel().getValueAt((Integer) entry.getIdentifier(),
								1)).getData();
						if (data.isAfter(datas.getInitDate()) || data.isEqual(datas.getInitDate()))
							return true;
						else
							return false;
					}
				});
			}
			if (datas.getFinalDate() != null)
				andFilters.add(new RowFilter<TableModelFinancas, Object>() {

					@Override
					public boolean include(Entry<? extends TableModelFinancas, ? extends Object> entry) {
						LocalDate data = ((DataPesquisavel) entry.getModel().getValueAt((Integer) entry.getIdentifier(),
								1)).getData();
						if (data.isBefore(datas.getFinalDate()) || data.isEqual(datas.getFinalDate()))
							return true;
						else
							return false;
					}
				});
			try {
				rf = RowFilter.orFilter(filters);
				andFilters.add(rf);
				rowFilter = RowFilter.andFilter(andFilters);
			} catch (java.util.regex.PatternSyntaxException e) {
				return;
			}
			sorter.setRowFilter(rowFilter);
		}
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

	public MultiDatePicker getDatas() {
		return datas;
	}

	public JCheckBox getCheckEntradas() {
		return checkEntradas;
	}

	public JCheckBox getCheckSaidas() {
		return checkSaidas;
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

	public static DataGui getInstance() {
		if (INSTANCE == null)
			new DataGui();
		return INSTANCE;
	}
}
