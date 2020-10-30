package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;

import com.github.lgooddatepicker.optionalusertools.DateChangeListener;
import com.github.lgooddatepicker.zinternaltools.DateChangeEvent;

import dad.fam_com_cristo.table.TableModelFinancas;
import dad.fam_com_cristo.table.TableModelMembro;
import dad.fam_com_cristo.types.EstatisticaPeriodos;
import dad.recursos.GraficosFinancas;
import dad.recursos.GraficosMembros;
import dad.recursos.MultiDatePicker;
import dad.recursos.Utils;
import mdlaf.utils.MaterialImageFactory;
import mdlaf.utils.icons.MaterialIconFont;

/**
 * Classe que representa e mostra as estatísticas e gráficos das bases de dados
 * da igreja.
 * 
 * @author Dário Pereira
 *
 */
public class Estatisticas extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3843370061976082815L;
	private JTextField total, n_ativos, n_congregados, n_nominais, n_lideres, n_exMembros, n_lid_hom, n_lid_mul,
			batizados, n_bat_hom, n_bat_mul, n_hom, n_mul, n_casados, n_adultos, n_adol, n_criancas, n_adult_hom,
			n_adult_mul, n_adol_hom, n_adol_mul, n_crian_hom, n_crian_mul, n_solt, n_divorc, n_membro_ativo_hom,
			n_membro_ativo_mul;
	private JFormattedTextField totalEntradasPers, totalSaidasPers, subTotalPers;
	private MultiDatePicker datas, datas1;
	private JTabbedPane tabbedPane, graficosPane;
	private Utils utils = Utils.getInstance();
	private TableModelFinancas financas = TableModelFinancas.getInstance();
	private TableModelMembro membros = TableModelMembro.getInstance();
	private JPanel panelGraficosFinancas;

	public Estatisticas() {
		super(DataGui.getInstance(), ModalityType.DOCUMENT_MODAL);
		setBounds(100, 100, 1200, 600);
		setResizable(false);
		setTitle("Estatísticas");
		setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("/FC.jpg"))));
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setIcon(MaterialImageFactory.getInstance().getImage(MaterialIconFont.CHECK,
						utils.getCurrentTheme().getColorIcons()));
				Utils.personalizarBotao(okButton);
				okButton.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();

					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		graficosPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.addTab("Gráficos", graficosPane);

		graficosPane.addTab("Tipo de Membro", new XChartPanel<PieChart>(GraficosMembros.getTipo_Membro_Chart()));
		graficosPane.addTab("Membros Ativos - Sexo",
				new XChartPanel<PieChart>(GraficosMembros.getMembro_Ativo_Sexo_Chart()));
		graficosPane.addTab("Membros Batizados",
				new XChartPanel<PieChart>(GraficosMembros.getTipo_Membro_Batizado_Chart()));
		graficosPane.addTab("Membros Batizados - Sexo",
				new XChartPanel<PieChart>(GraficosMembros.getMembro_Batizado_Sexo_Chart()));
		graficosPane.addTab("Estado Civil", new XChartPanel<PieChart>(GraficosMembros.getEstado_CivilChart()));
		graficosPane.addTab("Sexo", new XChartPanel<PieChart>(GraficosMembros.getSexo_Chart()));
		graficosPane.addTab("Idades", new XChartPanel<PieChart>(GraficosMembros.getIdades_Chart()));
		graficosPane.addTab("Homens - Idade", new XChartPanel<PieChart>(GraficosMembros.getHomens_Idades_Chart()));
		graficosPane.addTab("Mulheres - Idades", new XChartPanel<PieChart>(GraficosMembros.getMulheres_Idades_Chart()));
		graficosPane.addTab("Homens - Estado Civil",
				new XChartPanel<PieChart>(GraficosMembros.getEstado_Civil_Homens_Chart()));
		graficosPane.addTab("Mulheres - Estado Civil",
				new XChartPanel<PieChart>(GraficosMembros.getEstado_Civil_Mulheres_Chart()));
		graficosPane.addTab("Homens - Batizados",
				new XChartPanel<PieChart>(GraficosMembros.getHomens_Batizados_Chart()));
		graficosPane.addTab("Mulheres - Batizadas",
				new XChartPanel<PieChart>(GraficosMembros.getMulheres_Batizadas_Chart()));
		graficosPane.addTab("Líderes - Sexo", new XChartPanel<PieChart>(GraficosMembros.getSexo_Lideranca_Chart()));

		JPanel panel = new JPanel(null);

		// Painel do mes atual
		LocalDate init = EstatisticaPeriodos.MES_ATUAL.getInit();
		LocalDate end = EstatisticaPeriodos.MES_ATUAL.getEnd();

		JPanel panelMesAtual = new JPanel(null);
		panelMesAtual.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelMesAtual.setBounds(350, 35, 300, 190);
		panel.add(panelMesAtual);

		JLabel lblMesAtual = new JLabel("M\u00EAs atual");
		lblMesAtual.setFont(new Font("Dialog", Font.BOLD, 15));
		lblMesAtual.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesAtual.setBounds(90, 10, 120, 30);
		panelMesAtual.add(lblMesAtual);

		JLabel lblTotalEntradaMes = new JLabel("Total de Entradas: ");
		lblTotalEntradaMes.setBounds(20, 75, 140, 25);
		panelMesAtual.add(lblTotalEntradaMes);

		JFormattedTextField totalEntradasMesAtual = utils.getNewCurrencyTextField();
		totalEntradasMesAtual.setBounds(160, 75, 120, 25);
		totalEntradasMesAtual.setValue(financas.getTotalEntradas(init, end));
		panelMesAtual.add(totalEntradasMesAtual);

		JLabel lblTotalSaidasMes = new JLabel("Total de Saídas: ");
		lblTotalSaidasMes.setBounds(20, 110, 140, 25);
		panelMesAtual.add(lblTotalSaidasMes);

		JFormattedTextField totalSaidasMesAtual = utils.getNewCurrencyTextField();
		totalSaidasMesAtual.setBounds(160, 110, 120, 25);
		totalSaidasMesAtual.setValue(financas.getTotalSaidas(init, end));
		panelMesAtual.add(totalSaidasMesAtual);

		JLabel lblSubTotalMes = new JLabel("Movimento:");
		lblSubTotalMes.setBounds(20, 145, 140, 25);
		panelMesAtual.add(lblSubTotalMes);

		JFormattedTextField subTotalMesAtual = utils.getNewCurrencyTextField();
		subTotalMesAtual.setBounds(160, 145, 120, 25);
		subTotalMesAtual.setValue(financas.getTotalPeriod(init, end));
		panelMesAtual.add(subTotalMesAtual);

		JLabel lblMesAtualPeriod = new JLabel(utils.getMesAtualString());
		lblMesAtualPeriod.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesAtualPeriod.setBounds(50, 40, 200, 25);
		panelMesAtual.add(lblMesAtualPeriod);

		// Painel do mes anterior
		init = EstatisticaPeriodos.MES_ANTERIOR.getInit();
		end = EstatisticaPeriodos.MES_ANTERIOR.getEnd();

		JPanel panelMesAnterior = new JPanel((LayoutManager) null);
		panelMesAnterior.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelMesAnterior.setBounds(350, 280, 300, 190);
		panel.add(panelMesAnterior);

		JLabel lblMesAnterior = new JLabel("M\u00EAs anterior");
		lblMesAnterior.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesAnterior.setFont(new Font("Dialog", Font.BOLD, 15));
		lblMesAnterior.setBounds(90, 10, 120, 30);
		panelMesAnterior.add(lblMesAnterior);

		JLabel lblTotalEntradaMesAnterior = new JLabel("Total de Entradas: ");
		lblTotalEntradaMesAnterior.setBounds(20, 75, 140, 25);
		panelMesAnterior.add(lblTotalEntradaMesAnterior);

		JFormattedTextField totalEntradasMesAnterior = utils.getNewCurrencyTextField();
		totalEntradasMesAnterior.setBounds(160, 75, 120, 25);
		totalEntradasMesAnterior.setValue(financas.getTotalEntradas(init, end));
		panelMesAnterior.add(totalEntradasMesAnterior);

		JLabel lblTotalSaidasMesAnterior = new JLabel("Total de Sa\u00EDdas: ");
		lblTotalSaidasMesAnterior.setBounds(20, 110, 140, 25);
		panelMesAnterior.add(lblTotalSaidasMesAnterior);

		JFormattedTextField totalSaidasMesAnterior = utils.getNewCurrencyTextField();
		totalSaidasMesAnterior.setBounds(160, 110, 120, 25);
		totalSaidasMesAnterior.setValue(financas.getTotalSaidas(init, end));
		panelMesAnterior.add(totalSaidasMesAnterior);

		JLabel lblSubTotalMesAnterior = new JLabel("Movimento: ");
		lblSubTotalMesAnterior.setBounds(20, 145, 140, 25);
		panelMesAnterior.add(lblSubTotalMesAnterior);

		JFormattedTextField subTotalMesAnterior = utils.getNewCurrencyTextField();
		subTotalMesAnterior.setBounds(160, 145, 120, 25);
		subTotalMesAnterior.setValue(financas.getTotalPeriod(init, end));
		panelMesAnterior.add(subTotalMesAnterior);

		JLabel lblMesAnteriorPeriod = new JLabel(utils.getMesAnteriorString());
		lblMesAnteriorPeriod.setHorizontalAlignment(SwingConstants.CENTER);
		lblMesAnteriorPeriod.setBounds(50, 40, 200, 25);
		panelMesAnterior.add(lblMesAnteriorPeriod);

		// Painel do ano atual
		init = EstatisticaPeriodos.ANO_ATUAL.getInit();
		end = EstatisticaPeriodos.ANO_ATUAL.getEnd();

		JPanel panelAnoAtual = new JPanel(null);
		panelAnoAtual.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelAnoAtual.setBounds(690, 35, 300, 190);
		panel.add(panelAnoAtual);

		JLabel lblAnoAtual = new JLabel("Ano atual");
		lblAnoAtual.setFont(new Font("Dialog", Font.BOLD, 15));
		lblAnoAtual.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnoAtual.setBounds(90, 10, 120, 30);
		panelAnoAtual.add(lblAnoAtual);

		JLabel lblTotalEntradaAnoAtual = new JLabel("Total de Entradas: ");
		lblTotalEntradaAnoAtual.setBounds(20, 75, 140, 25);
		panelAnoAtual.add(lblTotalEntradaAnoAtual);

		JFormattedTextField totalEntradasAnoAtual = utils.getNewCurrencyTextField();
		totalEntradasAnoAtual.setBounds(160, 75, 120, 25);
		totalEntradasAnoAtual.setValue(financas.getTotalEntradas(init, end));
		panelAnoAtual.add(totalEntradasAnoAtual);

		JLabel lblTotalSaidasAnoAtual = new JLabel("Total de Saídas: ");
		lblTotalSaidasAnoAtual.setBounds(20, 110, 140, 25);
		panelAnoAtual.add(lblTotalSaidasAnoAtual);

		JFormattedTextField totalSaidasAnoAtual = utils.getNewCurrencyTextField();
		totalSaidasAnoAtual.setBounds(160, 110, 120, 25);
		totalSaidasAnoAtual.setValue(financas.getTotalSaidas(init, end));
		panelAnoAtual.add(totalSaidasAnoAtual);

		JLabel lblSubTotalAnoAtual = new JLabel("Movimento:");
		lblSubTotalAnoAtual.setBounds(20, 145, 140, 25);
		panelAnoAtual.add(lblSubTotalAnoAtual);

		JFormattedTextField subTotalAnoAtual = utils.getNewCurrencyTextField();
		subTotalAnoAtual.setBounds(160, 145, 120, 25);
		subTotalAnoAtual.setValue(financas.getTotalPeriod(init, end));
		panelAnoAtual.add(subTotalAnoAtual);

		JLabel lblAnoAtualPeriod = new JLabel(utils.getAnoAtualString());
		lblAnoAtualPeriod.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnoAtualPeriod.setBounds(50, 40, 200, 25);
		panelAnoAtual.add(lblAnoAtualPeriod);

		// Painel do ano anterior
		init = EstatisticaPeriodos.ANO_ANTERIOR.getInit();
		end = EstatisticaPeriodos.ANO_ANTERIOR.getEnd();

		JPanel panelAnoAnterior = new JPanel((LayoutManager) null);
		panelAnoAnterior.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelAnoAnterior.setBounds(690, 280, 300, 190);
		panel.add(panelAnoAnterior);

		JLabel lblAnoAnterior = new JLabel("Ano anterior");
		lblAnoAnterior.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnoAnterior.setFont(new Font("Dialog", Font.BOLD, 15));
		lblAnoAnterior.setBounds(90, 10, 120, 30);
		panelAnoAnterior.add(lblAnoAnterior);

		JLabel lblTotalEntradaAnoAnterior = new JLabel("Total de Entradas: ");
		lblTotalEntradaAnoAnterior.setBounds(20, 75, 140, 25);
		panelAnoAnterior.add(lblTotalEntradaAnoAnterior);

		JFormattedTextField totalEntradasAnoAnterior = utils.getNewCurrencyTextField();
		totalEntradasAnoAnterior.setBounds(160, 75, 120, 25);
		totalEntradasAnoAnterior.setValue(financas.getTotalEntradas(init, end));
		panelAnoAnterior.add(totalEntradasAnoAnterior);

		JLabel lblTotalSaidasAnoAnterior = new JLabel("Total de Sa\u00EDdas: ");
		lblTotalSaidasAnoAnterior.setBounds(20, 110, 140, 25);
		panelAnoAnterior.add(lblTotalSaidasAnoAnterior);

		JFormattedTextField totalSaidasAnoAnterior = utils.getNewCurrencyTextField();
		totalSaidasAnoAnterior.setBounds(160, 110, 120, 25);
		totalSaidasAnoAnterior.setValue(financas.getTotalSaidas(init, end));
		panelAnoAnterior.add(totalSaidasAnoAnterior);

		JLabel lblSubTotalAnoAnterior = new JLabel("Movimento: ");
		lblSubTotalAnoAnterior.setBounds(20, 145, 140, 25);
		panelAnoAnterior.add(lblSubTotalAnoAnterior);

		JFormattedTextField subTotalAnoAnterior = utils.getNewCurrencyTextField();
		subTotalAnoAnterior.setBounds(160, 145, 120, 25);
		subTotalAnoAnterior.setValue(financas.getTotalPeriod(init, end));
		panelAnoAnterior.add(subTotalAnoAnterior);

		JLabel lblAnoAnteriorPeriodo = new JLabel(utils.getAnoAnteriorString());
		lblAnoAnteriorPeriodo.setHorizontalAlignment(SwingConstants.CENTER);
		lblAnoAnteriorPeriodo.setBounds(50, 40, 200, 25);
		panelAnoAnterior.add(lblAnoAnteriorPeriodo);

		// Painel desde sempre
		JPanel panelDesdeSempre = new JPanel(null);
		panelDesdeSempre.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelDesdeSempre.setBounds(10, 35, 300, 190);
		panel.add(panelDesdeSempre);

		JLabel lblDesdeSempre = new JLabel("Desde sempre");
		lblDesdeSempre.setFont(new Font("Dialog", Font.BOLD, 15));
		lblDesdeSempre.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesdeSempre.setBounds(90, 10, 120, 30);
		panelDesdeSempre.add(lblDesdeSempre);

		JLabel lblTotalEntradas = new JLabel("Total de Entradas: ");
		lblTotalEntradas.setBounds(20, 75, 140, 25);
		panelDesdeSempre.add(lblTotalEntradas);

		JFormattedTextField totalEntradas = utils.getNewCurrencyTextField();
		totalEntradas.setBounds(160, 75, 120, 25);
		totalEntradas.setValue(financas.getTotal_Entradas());
		totalEntradas.setEditable(false);
		panelDesdeSempre.add(totalEntradas);

		JLabel lblTotalSaidas = new JLabel("Total de Saídas: ");
		lblTotalSaidas.setBounds(20, 110, 140, 25);
		panelDesdeSempre.add(lblTotalSaidas);

		JFormattedTextField totalSaidas = utils.getNewCurrencyTextField();
		totalSaidas.setBounds(160, 110, 120, 25);
		totalSaidas.setValue(financas.getTotal_Saidas());
		totalSaidas.setEditable(false);
		panelDesdeSempre.add(totalSaidas);

		JLabel lblTotal = new JLabel("Saldo atual: ");
		lblTotal.setBounds(20, 145, 140, 25);
		panelDesdeSempre.add(lblTotal);

		JFormattedTextField totalFinancas = utils.getNewCurrencyTextField();
		totalFinancas.setBounds(160, 145, 120, 25);
		totalFinancas.setValue(financas.getTotal());
		totalFinancas.setEditable(false);
		panelDesdeSempre.add(totalFinancas);

		JLabel lblDesdeSemprePeriod = new JLabel(utils.getDesdeSempreString());
		lblDesdeSemprePeriod.setHorizontalAlignment(SwingConstants.CENTER);
		lblDesdeSemprePeriod.setBounds(50, 40, 200, 25);
		panelDesdeSempre.add(lblDesdeSemprePeriod);

		// Painel personalizado
		JPanel panelPersonal = new JPanel(null);
		panelPersonal.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		panelPersonal.setBounds(10, 250, 300, 220);
		panel.add(panelPersonal);

		JLabel lblPersoanlizado = new JLabel("Período personalizado");
		lblPersoanlizado.setFont(new Font("Dialog", Font.BOLD, 15));
		lblPersoanlizado.setHorizontalAlignment(SwingConstants.CENTER);
		lblPersoanlizado.setBounds(50, 10, 200, 30);
		panelPersonal.add(lblPersoanlizado);

		datas = new MultiDatePicker();
		datas.getInitDateChooser().addDateChangeListener(new Listener());
		datas.getFinalDateChooser().addDateChangeListener(new Listener());
		datas.setBounds(5, 50, 290, 50);
		panelPersonal.add(datas);

		JLabel lblTotalEntradasPers = new JLabel("Total de Entradas: ");
		lblTotalEntradasPers.setBounds(20, 110, 140, 25);
		panelPersonal.add(lblTotalEntradasPers);

		totalEntradasPers = utils.getNewCurrencyTextField();
		totalEntradasPers.setBounds(160, 110, 120, 25);
		totalEntradasPers.setValue(0);
		panelPersonal.add(totalEntradasPers);

		JLabel lblTotalSaidasPers = new JLabel("Total de Saídas: ");
		lblTotalSaidasPers.setBounds(20, 145, 140, 25);
		panelPersonal.add(lblTotalSaidasPers);

		totalSaidasPers = utils.getNewCurrencyTextField();
		totalSaidasPers.setBounds(160, 145, 120, 25);
		totalSaidasPers.setValue(0);
		panelPersonal.add(totalSaidasPers);

		JLabel lblTotalPers = new JLabel("Movimento: ");
		lblTotalPers.setBounds(20, 180, 140, 25);
		panelPersonal.add(lblTotalPers);

		subTotalPers = utils.getNewCurrencyTextField();
		subTotalPers.setBounds(160, 180, 120, 25);
		subTotalPers.setValue(0);
		panelPersonal.add(subTotalPers);

		// Graficos
		JTabbedPane financasPane = new JTabbedPane(JTabbedPane.LEFT);
		financasPane.addTab("Detalhes", panel);

		panelGraficosFinancas = new JPanel(new BorderLayout());
		financasPane.addTab("Entradas / Saídas", panelGraficosFinancas);

		JPanel graficosFinancasChooser = new JPanel(new GridLayout(1, 3));
		panelGraficosFinancas.add(graficosFinancasChooser, BorderLayout.NORTH);

		panelGraficosFinancas.add(
				new XChartPanel<PieChart>(GraficosFinancas.getEntrada_Saida_Chart(EstatisticaPeriodos.DESDE_SEMPRE,
						EstatisticaPeriodos.DESDE_SEMPRE.getInit(), EstatisticaPeriodos.DESDE_SEMPRE.getEnd(), false)),
				BorderLayout.CENTER);

		graficosFinancasChooser.add(new JLabel("Escolha o período: "));

		datas1 = new MultiDatePicker();
		datas1.getInitDateChooser().addDateChangeListener(new Listener1());
		datas1.getFinalDateChooser().addDateChangeListener(new Listener1());
		datas1.setEnabled(false);

		JComboBox<EstatisticaPeriodos> opcoes = new JComboBox<>();
		opcoes.setModel(new DefaultComboBoxModel<>(EstatisticaPeriodos.values()));
		opcoes.setSelectedIndex(0);

		opcoes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (opcoes.getSelectedIndex() == 5) {
					datas1.setEnabled(true);
				} else {
					datas1.setEnabled(false);
					panelGraficosFinancas.remove(1);
					EstatisticaPeriodos periodo = (EstatisticaPeriodos) opcoes.getSelectedItem();
					panelGraficosFinancas.add(new XChartPanel<PieChart>(GraficosFinancas.getEntrada_Saida_Chart(periodo,
							periodo.getInit(), periodo.getEnd(), false)), BorderLayout.CENTER);
					datas1.getInitDateChooser().setDate(periodo.getInit());
					datas1.getFinalDateChooser().setDate(periodo.getEnd());
				}
				panelGraficosFinancas.repaint();

			}
		});

		graficosFinancasChooser.add(opcoes);

		graficosFinancasChooser.add(datas1);

		JPanel painelGraficosPorMes = new JPanel(new BorderLayout());

		JPanel painelGraficosPorMesChooser = new JPanel(new GridLayout(1, 6));
		painelGraficosPorMesChooser.add(new JLabel(""));
		painelGraficosPorMesChooser.add(new JLabel(""));
		painelGraficosPorMesChooser.add(new JLabel("Escolha o ano: "));

		Integer[] anosList = new Integer[250];
		for (int i = 2000; i < 2250; i++)
			anosList[i - 2000] = i;

		JComboBox<Integer> anos = new JComboBox<Integer>();
		anos.setModel(new DefaultComboBoxModel<Integer>(anosList));
		anos.setSelectedItem(LocalDate.now().getYear());

		anos.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				painelGraficosPorMes.remove(1);
				painelGraficosPorMes.add(
						new XChartPanel<CategoryChart>(
								GraficosFinancas.getEntrada_Saida_BarChart((int) anos.getSelectedItem(), false)),
						BorderLayout.CENTER);
				painelGraficosPorMes.repaint();
			}
		});
		painelGraficosPorMesChooser.add(anos);
		painelGraficosPorMesChooser.add(new JLabel(""));
		painelGraficosPorMesChooser.add(new JLabel(""));
		painelGraficosPorMes.add(painelGraficosPorMesChooser, BorderLayout.NORTH);

		painelGraficosPorMes.add(
				new XChartPanel<CategoryChart>(
						GraficosFinancas.getEntrada_Saida_BarChart(LocalDate.now().getYear(), false)),
				BorderLayout.CENTER);

		financasPane.addTab("Por meses", painelGraficosPorMes);

		setUpMembrosNumerosPanel();

		tabbedPane.addTab("Finanças", financasPane);
	}

	/**
	 * 
	 */
	public void setUpMembrosNumerosPanel() {
		JPanel numeros = new JPanel();
		numeros.setToolTipText("(Exceto Ex-Membros)");
		numeros.setLayout(null);

		tabbedPane.addTab("Membros", numeros);

		JLabel lEstatsticas = new JLabel("ESTAT\u00CDSTICAS");
		lEstatsticas.setBounds(0, 0, 1200, 20);
		numeros.add(lEstatsticas);
		lEstatsticas.setBackground(new Color(60, 179, 113));
		lEstatsticas.setFont(new Font("Dialog", Font.PLAIN, 17));
		lEstatsticas.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel lblNmeroTotal = new JLabel("N\u00FAmero Total:");
		lblNmeroTotal.setBounds(10, 40, 179, 25);
		numeros.add(lblNmeroTotal);
		lblNmeroTotal.setFont(new Font("Dialog", Font.BOLD, 14));

		JLabel label2 = new JLabel("Membros Ativos: ");
		label2.setBounds(10, 75, 170, 25);
		numeros.add(label2);

		JLabel label3 = new JLabel("Membros Nominais:");
		label3.setBounds(10, 110, 170, 25);
		numeros.add(label3);

		total = new JTextField();
		total.setBounds(190, 40, 62, 25);
		numeros.add(total);
		total.setEditable(false);
		total.setColumns(10);
		total.setText(String.valueOf(membros.getTotal()));

		n_ativos = new JTextField();
		n_ativos.setBounds(190, 75, 62, 25);
		numeros.add(n_ativos);
		n_ativos.setEditable(false);
		n_ativos.setColumns(10);
		n_ativos.setText(String.valueOf(membros.getN_Membros_Ativos()));

		JLabel label4 = new JLabel("Congregados: ");
		label4.setBounds(10, 145, 179, 25);
		numeros.add(label4);

		n_congregados = new JTextField();
		n_congregados.setBounds(190, 145, 62, 25);
		numeros.add(n_congregados);
		n_congregados.setEditable(false);
		n_congregados.setColumns(10);
		n_congregados.setText(String.valueOf(membros.getN_Congregados()));

		n_nominais = new JTextField();
		n_nominais.setBounds(190, 110, 62, 25);
		numeros.add(n_nominais);
		n_nominais.setText(String.valueOf(membros.getN_Membros_Nominais()));
		n_nominais.setEditable(false);
		n_nominais.setColumns(10);

		JLabel label5 = new JLabel("L\u00EDderes:");
		label5.setBounds(10, 180, 171, 25);
		numeros.add(label5);

		n_lideres = new JTextField();
		n_lideres.setBounds(190, 180, 62, 25);
		numeros.add(n_lideres);
		n_lideres.setText(String.valueOf(membros.getN_Lideranca()));
		n_lideres.setEditable(false);
		n_lideres.setColumns(10);

		JLabel lblExmembros = new JLabel("Ex-Membros:");
		lblExmembros.setBounds(10, 216, 179, 25);
		numeros.add(lblExmembros);

		n_exMembros = new JTextField();
		n_exMembros.setText("0");
		n_exMembros.setEditable(false);
		n_exMembros.setColumns(10);
		n_exMembros.setBounds(190, 216, 62, 25);
		n_exMembros.setText(String.valueOf(membros.getN_Ex_Membros()));
		numeros.add(n_exMembros);

		JLabel lblLdereshomens = new JLabel("L\u00EDderes (homens):");
		lblLdereshomens.setBounds(430, 180, 179, 25);
		numeros.add(lblLdereshomens);

		n_lid_hom = new JTextField();
		n_lid_hom.setText(String.valueOf(membros.getN_Lideres_Homens()));
		n_lid_hom.setEditable(false);
		n_lid_hom.setColumns(10);
		n_lid_hom.setBounds(630, 180, 62, 25);
		numeros.add(n_lid_hom);

		n_lid_mul = new JTextField();
		n_lid_mul.setText(String.valueOf(membros.getN_Lideres_Mulheres()));
		n_lid_mul.setEditable(false);
		n_lid_mul.setColumns(10);
		n_lid_mul.setBounds(1080, 180, 62, 25);
		numeros.add(n_lid_mul);

		JLabel lblLderesmulheres = new JLabel("L\u00EDderes (mulheres):");
		lblLderesmulheres.setBounds(880, 180, 179, 25);
		numeros.add(lblLderesmulheres);

		JLabel lblBatizados = new JLabel("Batizados: ");
		lblBatizados.setBounds(10, 265, 171, 25);
		numeros.add(lblBatizados);

		batizados = new JTextField();
		batizados.setText(String.valueOf(membros.getN_Batizados()));
		batizados.setEditable(false);
		batizados.setColumns(10);
		batizados.setBounds(190, 265, 62, 25);
		numeros.add(batizados);

		JLabel lblBatizadoshomens = new JLabel("Batizados (homens):");
		lblBatizadoshomens.setBounds(430, 265, 179, 25);
		numeros.add(lblBatizadoshomens);

		n_bat_hom = new JTextField();
		n_bat_hom.setText(String.valueOf(membros.getN_Batizados_Homens()));
		n_bat_hom.setEditable(false);
		n_bat_hom.setColumns(10);
		n_bat_hom.setBounds(630, 265, 62, 25);
		numeros.add(n_bat_hom);

		JLabel lblBatizadosmulheres = new JLabel("Batizados (mulheres):");
		lblBatizadosmulheres.setBounds(880, 265, 179, 25);
		numeros.add(lblBatizadosmulheres);

		n_bat_mul = new JTextField();
		n_bat_mul.setText(String.valueOf(membros.getN_Batizados_Mulheres()));
		n_bat_mul.setEditable(false);
		n_bat_mul.setColumns(10);
		n_bat_mul.setBounds(1080, 265, 62, 25);
		numeros.add(n_bat_mul);

		JLabel lblHomens = new JLabel("Homens:");
		lblHomens.setFont(new Font("Dialog", Font.BOLD, 14));
		lblHomens.setBounds(430, 40, 179, 25);
		numeros.add(lblHomens);

		n_hom = new JTextField();
		n_hom.setText(String.valueOf(membros.getN_Homens()));
		n_hom.setEditable(false);
		n_hom.setColumns(10);
		n_hom.setBounds(630, 40, 62, 25);
		numeros.add(n_hom);

		JLabel lblMulheres = new JLabel("Mulheres:");
		lblMulheres.setFont(new Font("Dialog", Font.BOLD, 14));
		lblMulheres.setBounds(880, 40, 179, 25);
		numeros.add(lblMulheres);

		n_mul = new JTextField();
		n_mul.setText(String.valueOf(membros.getN_Mulheres()));
		n_mul.setEditable(false);
		n_mul.setColumns(10);
		n_mul.setBounds(1080, 40, 62, 25);
		numeros.add(n_mul);

		JLabel lblCasados = new JLabel("Casados: ");
		lblCasados.setBounds(10, 317, 171, 25);
		numeros.add(lblCasados);

		n_casados = new JTextField();
		n_casados.setText(String.valueOf(membros.getN_Casados()));
		n_casados.setEditable(false);
		n_casados.setColumns(10);
		n_casados.setBounds(190, 317, 62, 25);
		numeros.add(n_casados);

		JLabel lblAdultos = new JLabel("Adultos (+18): ");
		lblAdultos.setBounds(10, 361, 171, 25);
		numeros.add(lblAdultos);

		n_adultos = new JTextField();
		n_adultos.setText(String.valueOf(membros.getN_Adultos()));
		n_adultos.setEditable(false);
		n_adultos.setColumns(10);
		n_adultos.setBounds(190, 361, 62, 25);
		numeros.add(n_adultos);

		JLabel lblAdolescentes = new JLabel("Adolescentes (13-17):");
		lblAdolescentes.setBounds(10, 408, 171, 25);
		numeros.add(lblAdolescentes);

		n_adol = new JTextField();
		n_adol.setText(String.valueOf(membros.getN_Adolescentes()));
		n_adol.setEditable(false);
		n_adol.setColumns(10);
		n_adol.setBounds(190, 408, 62, 25);
		numeros.add(n_adol);

		JLabel lblCrianas = new JLabel("Crian\u00E7as (-12)");
		lblCrianas.setBounds(10, 456, 171, 25);
		numeros.add(lblCrianas);

		n_criancas = new JTextField();
		n_criancas.setText(String.valueOf(membros.getN_Criancas()));
		n_criancas.setEditable(false);
		n_criancas.setColumns(10);
		n_criancas.setBounds(190, 456, 62, 25);
		numeros.add(n_criancas);

		JLabel lblAdultoshomens = new JLabel("Adultos (homens):");
		lblAdultoshomens.setBounds(430, 361, 179, 25);
		numeros.add(lblAdultoshomens);

		n_adult_hom = new JTextField();
		n_adult_hom.setText(String.valueOf(membros.getN_Adultos_Homens()));
		n_adult_hom.setEditable(false);
		n_adult_hom.setColumns(10);
		n_adult_hom.setBounds(630, 361, 62, 25);
		numeros.add(n_adult_hom);

		JLabel lblAdultosmulheres = new JLabel("Adultos (mulheres):");
		lblAdultosmulheres.setBounds(880, 361, 179, 25);
		numeros.add(lblAdultosmulheres);

		n_adult_mul = new JTextField();
		n_adult_mul.setText(String.valueOf(membros.getN_Adultos_Mulheres()));
		n_adult_mul.setEditable(false);
		n_adult_mul.setColumns(10);
		n_adult_mul.setBounds(1080, 361, 62, 25);
		numeros.add(n_adult_mul);

		JLabel lblAdolescenteshomens = new JLabel("Adolescentes (homens):");
		lblAdolescenteshomens.setBounds(430, 408, 179, 25);
		numeros.add(lblAdolescenteshomens);

		n_adol_hom = new JTextField();
		n_adol_hom.setText(String.valueOf(membros.getN_Adolescentes_Homens()));
		n_adol_hom.setEditable(false);
		n_adol_hom.setColumns(10);
		n_adol_hom.setBounds(630, 408, 62, 25);
		numeros.add(n_adol_hom);

		JLabel lblAdolescentesmulheres = new JLabel("Adolescentes (mulheres):");
		lblAdolescentesmulheres.setBounds(880, 408, 179, 25);
		numeros.add(lblAdolescentesmulheres);

		n_adol_mul = new JTextField();
		n_adol_mul.setText(String.valueOf(membros.getN_Adolescentes_Mulheres()));
		n_adol_mul.setEditable(false);
		n_adol_mul.setColumns(10);
		n_adol_mul.setBounds(1080, 408, 62, 25);
		numeros.add(n_adol_mul);

		JLabel lblCrianashomens = new JLabel("Crian\u00E7as (homens):");
		lblCrianashomens.setBounds(430, 456, 179, 25);
		numeros.add(lblCrianashomens);

		n_crian_hom = new JTextField();
		n_crian_hom.setText(String.valueOf(membros.getN_Criancas_Homens()));
		n_crian_hom.setEditable(false);
		n_crian_hom.setColumns(10);
		n_crian_hom.setBounds(630, 456, 62, 25);
		numeros.add(n_crian_hom);

		JLabel lblCrianasmulheres = new JLabel("Crian\u00E7as (mulheres):");
		lblCrianasmulheres.setBounds(880, 456, 179, 25);
		numeros.add(lblCrianasmulheres);

		n_crian_mul = new JTextField();
		n_crian_mul.setText(String.valueOf(membros.getN_Criancas_Mulheres()));
		n_crian_mul.setEditable(false);
		n_crian_mul.setColumns(10);
		n_crian_mul.setBounds(1080, 456, 62, 25);
		numeros.add(n_crian_mul);

		JLabel lblCasadoshomens = new JLabel("Solteiros:");
		lblCasadoshomens.setBounds(430, 317, 179, 25);
		numeros.add(lblCasadoshomens);

		n_solt = new JTextField();
		n_solt.setText(String.valueOf(membros.getN_Solteiros()));
		n_solt.setEditable(false);
		n_solt.setColumns(10);
		n_solt.setBounds(630, 317, 62, 25);
		numeros.add(n_solt);

		JLabel lblDivorciados = new JLabel("Divorciados:");
		lblDivorciados.setBounds(880, 317, 179, 25);
		numeros.add(lblDivorciados);

		n_divorc = new JTextField();
		n_divorc.setText(String.valueOf(membros.getN_Divorciados()));
		n_divorc.setEditable(false);
		n_divorc.setColumns(10);
		n_divorc.setBounds(1080, 317, 62, 25);
		numeros.add(n_divorc);

		JLabel lblMembrosAtivoshomens = new JLabel("Membros Ativos (homens):");
		lblMembrosAtivoshomens.setBounds(430, 75, 199, 25);
		numeros.add(lblMembrosAtivoshomens);

		n_membro_ativo_hom = new JTextField();
		n_membro_ativo_hom.setText(String.valueOf(membros.getN_Membros_Ativos_Homens()));
		n_membro_ativo_hom.setEditable(false);
		n_membro_ativo_hom.setColumns(10);
		n_membro_ativo_hom.setBounds(630, 75, 62, 25);
		numeros.add(n_membro_ativo_hom);

		n_membro_ativo_mul = new JTextField();
		n_membro_ativo_mul.setText(String.valueOf(membros.getN_Membros_Ativos_Mulheres()));
		n_membro_ativo_mul.setEditable(false);
		n_membro_ativo_mul.setColumns(10);
		n_membro_ativo_mul.setBounds(1080, 75, 62, 25);
		numeros.add(n_membro_ativo_mul);

		JLabel lblMembrosAtivosmulheres = new JLabel("Membros Ativos (mulheres):");
		lblMembrosAtivosmulheres.setBounds(880, 75, 199, 25);
		numeros.add(lblMembrosAtivosmulheres);
	}

	public MultiDatePicker getDatas1() {
		return datas1;
	}

	/**
	 * Torna o diálogo visível.
	 */
	public void open() {
		setVisible(true);
	}

	private class Listener implements DateChangeListener {

		@Override
		public void dateChanged(DateChangeEvent event) {
			LocalDate init = datas.getInitDate();
			LocalDate end = datas.getFinalDate();
			if (init == null && end == null) {
				totalEntradasPers.setValue(0);
				totalSaidasPers.setValue(0);
				subTotalPers.setValue(0);
			} else {
				totalEntradasPers.setValue(financas.getTotalEntradas(init, end));
				totalSaidasPers.setValue(financas.getTotalSaidas(init, end));
				subTotalPers.setValue(financas.getTotalPeriod(init, end));
			}
		}
	}

	private class Listener1 implements DateChangeListener {

		@Override
		public void dateChanged(DateChangeEvent event) {
			if (event.getSource().isEnabled()) {
				panelGraficosFinancas.remove(1);
				panelGraficosFinancas.add(new XChartPanel<PieChart>(GraficosFinancas.getEntrada_Saida_Chart(
						EstatisticaPeriodos.PERSONALIZADO, datas1.getInitDate(), datas1.getFinalDate(), false)));
			}
		}

	}
}
