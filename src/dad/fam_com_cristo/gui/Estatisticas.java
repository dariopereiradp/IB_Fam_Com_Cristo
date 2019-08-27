package dad.fam_com_cristo.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.style.Styler.ChartTheme;

import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.Entradas_Tipo;
import dad.fam_com_cristo.Estado_Civil;
import dad.fam_com_cristo.Tipo_Transacao;
import dad.fam_com_cristo.Sexo;
import dad.fam_com_cristo.Tipo_Membro;
import dad.fam_com_cristo.table.TableModelMembro;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;

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
	private JTextField total;
	private JTextField n_ativos;
	private JTextField n_congregados;
	private JTextField n_nominais;
	private JTextField n_lideres;
	private JTextField n_exMembros;
	private JTextField n_lid_hom;
	private JTextField n_lid_mul;
	private JTextField batizados;
	private JTextField n_bat_hom;
	private JTextField n_bat_mul;
	private JTextField n_hom;
	private JTextField n_mul;
	private JTextField n_casados;
	private JTextField n_adultos;
	private JTextField n_adol;
	private JTextField n_criancas;
	private JTextField n_adult_hom;
	private JTextField n_adult_mul;
	private JTextField n_adol_hom;
	private JTextField n_adol_mul;
	private JTextField n_crian_hom;
	private JTextField n_crian_mul;
	private JTextField n_solt;
	private JTextField n_divorc;
	private JTextField n_membro_ativo_hom;
	private JTextField n_membro_ativo_mul;

	public Estatisticas() {
		super(DataGui.getInstance(), ModalityType.DOCUMENT_MODAL);
		setBounds(100, 100, 900, 600);
		setResizable(false);
		setTitle("Estatísticas");
		setIconImage(Toolkit.getDefaultToolkit().getImage((getClass().getResource("/FC.jpg"))));
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

		JPanel numeros = new JPanel();
		numeros.setToolTipText("(Exceto Ex-Membros)");
		numeros.setLayout(null);

		JLabel lEstatsticas = new JLabel("ESTAT\u00CDSTICAS");
		lEstatsticas.setBounds(0, 0, 889, 20);
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
		label2.setFont(new Font("Dialog", Font.PLAIN, 14));

		JLabel label3 = new JLabel("Membros Nominais:");
		label3.setBounds(10, 110, 170, 25);
		numeros.add(label3);
		label3.setFont(new Font("Dialog", Font.PLAIN, 14));

		total = new JTextField();
		total.setBounds(190, 40, 62, 25);
		numeros.add(total);
		total.setEditable(false);
		total.setColumns(10);
		total.setText(String.valueOf(TableModelMembro.getInstance().getTotal()));

		n_ativos = new JTextField();
		n_ativos.setBounds(190, 75, 62, 25);
		numeros.add(n_ativos);
		n_ativos.setEditable(false);
		n_ativos.setColumns(10);
		n_ativos.setText(String.valueOf(TableModelMembro.getInstance().getN_Membros_Ativos()));

		JLabel label4 = new JLabel("Congregados: ");
		label4.setBounds(10, 145, 179, 25);
		numeros.add(label4);
		label4.setFont(new Font("Dialog", Font.PLAIN, 14));

		n_congregados = new JTextField();
		n_congregados.setBounds(190, 145, 62, 25);
		numeros.add(n_congregados);
		n_congregados.setEditable(false);
		n_congregados.setColumns(10);
		n_congregados.setText(String.valueOf(TableModelMembro.getInstance().getN_Congregados()));

		n_nominais = new JTextField();
		n_nominais.setBounds(190, 110, 62, 25);
		numeros.add(n_nominais);
		n_nominais.setText(String.valueOf(TableModelMembro.getInstance().getN_Membros_Nominais()));
		n_nominais.setEditable(false);
		n_nominais.setColumns(10);

		JLabel label5 = new JLabel("L\u00EDderes:");
		label5.setBounds(10, 180, 171, 25);
		numeros.add(label5);
		label5.setFont(new Font("Dialog", Font.PLAIN, 14));

		n_lideres = new JTextField();
		n_lideres.setBounds(190, 180, 62, 25);
		numeros.add(n_lideres);
		n_lideres.setText(String.valueOf(TableModelMembro.getInstance().getN_Lideranca()));
		n_lideres.setEditable(false);
		n_lideres.setColumns(10);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
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

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.addTab("Números", numeros);

		JLabel lblExmembros = new JLabel("Ex-Membros:");
		lblExmembros.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblExmembros.setBounds(10, 216, 179, 25);
		numeros.add(lblExmembros);

		n_exMembros = new JTextField();
		n_exMembros.setText("0");
		n_exMembros.setEditable(false);
		n_exMembros.setColumns(10);
		n_exMembros.setBounds(190, 216, 62, 25);
		n_exMembros.setText(String.valueOf(TableModelMembro.getInstance().getN_Ex_Membros()));
		numeros.add(n_exMembros);

		JLabel lblLdereshomens = new JLabel("L\u00EDderes (homens):");
		lblLdereshomens.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblLdereshomens.setBounds(290, 180, 179, 25);
		numeros.add(lblLdereshomens);

		n_lid_hom = new JTextField();
		n_lid_hom.setText(String.valueOf(TableModelMembro.getInstance().getN_Lideres_Homens()));
		n_lid_hom.setEditable(false);
		n_lid_hom.setColumns(10);
		n_lid_hom.setBounds(490, 180, 62, 25);
		numeros.add(n_lid_hom);

		n_lid_mul = new JTextField();
		n_lid_mul.setText(String.valueOf(TableModelMembro.getInstance().getN_Lideres_Mulheres()));
		n_lid_mul.setEditable(false);
		n_lid_mul.setColumns(10);
		n_lid_mul.setBounds(792, 180, 62, 25);
		numeros.add(n_lid_mul);

		JLabel lblLderesmulheres = new JLabel("L\u00EDderes (mulheres):");
		lblLderesmulheres.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblLderesmulheres.setBounds(592, 180, 179, 25);
		numeros.add(lblLderesmulheres);

		JLabel lblBatizados = new JLabel("Batizados: ");
		lblBatizados.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblBatizados.setBounds(10, 265, 171, 25);
		numeros.add(lblBatizados);

		batizados = new JTextField();
		batizados.setText(String.valueOf(TableModelMembro.getInstance().getN_Batizados()));
		batizados.setEditable(false);
		batizados.setColumns(10);
		batizados.setBounds(190, 265, 62, 25);
		numeros.add(batizados);

		JLabel lblBatizadoshomens = new JLabel("Batizados (homens):");
		lblBatizadoshomens.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblBatizadoshomens.setBounds(290, 265, 179, 25);
		numeros.add(lblBatizadoshomens);

		n_bat_hom = new JTextField();
		n_bat_hom.setText(String.valueOf(TableModelMembro.getInstance().getN_Batizados_Homens()));
		n_bat_hom.setEditable(false);
		n_bat_hom.setColumns(10);
		n_bat_hom.setBounds(490, 265, 62, 25);
		numeros.add(n_bat_hom);

		JLabel lblBatizadosmulheres = new JLabel("Batizados (mulheres):");
		lblBatizadosmulheres.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblBatizadosmulheres.setBounds(592, 265, 179, 25);
		numeros.add(lblBatizadosmulheres);

		n_bat_mul = new JTextField();
		n_bat_mul.setText(String.valueOf(TableModelMembro.getInstance().getN_Batizados_Mulheres()));
		n_bat_mul.setEditable(false);
		n_bat_mul.setColumns(10);
		n_bat_mul.setBounds(792, 265, 62, 25);
		numeros.add(n_bat_mul);

		JLabel lblHomens = new JLabel("Homens:");
		lblHomens.setFont(new Font("Dialog", Font.BOLD, 14));
		lblHomens.setBounds(290, 40, 179, 25);
		numeros.add(lblHomens);

		n_hom = new JTextField();
		n_hom.setText(String.valueOf(TableModelMembro.getInstance().getN_Homens()));
		n_hom.setEditable(false);
		n_hom.setColumns(10);
		n_hom.setBounds(490, 40, 62, 25);
		numeros.add(n_hom);

		JLabel lblMulheres = new JLabel("Mulheres:");
		lblMulheres.setFont(new Font("Dialog", Font.BOLD, 14));
		lblMulheres.setBounds(592, 40, 179, 25);
		numeros.add(lblMulheres);

		n_mul = new JTextField();
		n_mul.setText(String.valueOf(TableModelMembro.getInstance().getN_Mulheres()));
		n_mul.setEditable(false);
		n_mul.setColumns(10);
		n_mul.setBounds(792, 40, 62, 25);
		numeros.add(n_mul);

		JLabel lblCasados = new JLabel("Casados: ");
		lblCasados.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblCasados.setBounds(10, 317, 171, 25);
		numeros.add(lblCasados);

		n_casados = new JTextField();
		n_casados.setText(String.valueOf(TableModelMembro.getInstance().getN_Casados()));
		n_casados.setEditable(false);
		n_casados.setColumns(10);
		n_casados.setBounds(190, 317, 62, 25);
		numeros.add(n_casados);

		JLabel lblAdultos = new JLabel("Adultos (+18): ");
		lblAdultos.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAdultos.setBounds(10, 361, 171, 25);
		numeros.add(lblAdultos);

		n_adultos = new JTextField();
		n_adultos.setText(String.valueOf(TableModelMembro.getInstance().getN_Adultos()));
		n_adultos.setEditable(false);
		n_adultos.setColumns(10);
		n_adultos.setBounds(190, 361, 62, 25);
		numeros.add(n_adultos);

		JLabel lblAdolescentes = new JLabel("Adolescentes (13-17):");
		lblAdolescentes.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAdolescentes.setBounds(10, 408, 171, 25);
		numeros.add(lblAdolescentes);

		n_adol = new JTextField();
		n_adol.setText(String.valueOf(TableModelMembro.getInstance().getN_Adolescentes()));
		n_adol.setEditable(false);
		n_adol.setColumns(10);
		n_adol.setBounds(190, 408, 62, 25);
		numeros.add(n_adol);

		JLabel lblCrianas = new JLabel("Crian\u00E7as (-12)");
		lblCrianas.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblCrianas.setBounds(10, 456, 171, 25);
		numeros.add(lblCrianas);

		n_criancas = new JTextField();
		n_criancas.setText(String.valueOf(TableModelMembro.getInstance().getN_Criancas()));
		n_criancas.setEditable(false);
		n_criancas.setColumns(10);
		n_criancas.setBounds(190, 456, 62, 25);
		numeros.add(n_criancas);

		JLabel lblAdultoshomens = new JLabel("Adultos (homens):");
		lblAdultoshomens.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAdultoshomens.setBounds(290, 361, 179, 25);
		numeros.add(lblAdultoshomens);

		n_adult_hom = new JTextField();
		n_adult_hom.setText(String.valueOf(TableModelMembro.getInstance().getN_Adultos_Homens()));
		n_adult_hom.setEditable(false);
		n_adult_hom.setColumns(10);
		n_adult_hom.setBounds(490, 361, 62, 25);
		numeros.add(n_adult_hom);

		JLabel lblAdultosmulheres = new JLabel("Adultos (mulheres):");
		lblAdultosmulheres.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAdultosmulheres.setBounds(592, 361, 179, 25);
		numeros.add(lblAdultosmulheres);

		n_adult_mul = new JTextField();
		n_adult_mul.setText(String.valueOf(TableModelMembro.getInstance().getN_Adultos_Mulheres()));
		n_adult_mul.setEditable(false);
		n_adult_mul.setColumns(10);
		n_adult_mul.setBounds(792, 361, 62, 25);
		numeros.add(n_adult_mul);

		JLabel lblAdolescenteshomens = new JLabel("Adolescentes (homens):");
		lblAdolescenteshomens.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAdolescenteshomens.setBounds(290, 408, 179, 25);
		numeros.add(lblAdolescenteshomens);

		n_adol_hom = new JTextField();
		n_adol_hom.setText(String.valueOf(TableModelMembro.getInstance().getN_Adolescentes_Homens()));
		n_adol_hom.setEditable(false);
		n_adol_hom.setColumns(10);
		n_adol_hom.setBounds(490, 408, 62, 25);
		numeros.add(n_adol_hom);

		JLabel lblAdolescentesmulheres = new JLabel("Adolescentes (mulheres):");
		lblAdolescentesmulheres.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblAdolescentesmulheres.setBounds(592, 408, 179, 25);
		numeros.add(lblAdolescentesmulheres);

		n_adol_mul = new JTextField();
		n_adol_mul.setText(String.valueOf(TableModelMembro.getInstance().getN_Adolescentes_Mulheres()));
		n_adol_mul.setEditable(false);
		n_adol_mul.setColumns(10);
		n_adol_mul.setBounds(792, 408, 62, 25);
		numeros.add(n_adol_mul);

		JLabel lblCrianashomens = new JLabel("Crian\u00E7as (homens):");
		lblCrianashomens.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblCrianashomens.setBounds(290, 456, 179, 25);
		numeros.add(lblCrianashomens);

		n_crian_hom = new JTextField();
		n_crian_hom.setText(String.valueOf(TableModelMembro.getInstance().getN_Criancas_Homens()));
		n_crian_hom.setEditable(false);
		n_crian_hom.setColumns(10);
		n_crian_hom.setBounds(490, 456, 62, 25);
		numeros.add(n_crian_hom);

		JLabel lblCrianasmulheres = new JLabel("Crian\u00E7as (mulheres):");
		lblCrianasmulheres.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblCrianasmulheres.setBounds(592, 456, 179, 25);
		numeros.add(lblCrianasmulheres);

		n_crian_mul = new JTextField();
		n_crian_mul.setText(String.valueOf(TableModelMembro.getInstance().getN_Criancas_Mulheres()));
		n_crian_mul.setEditable(false);
		n_crian_mul.setColumns(10);
		n_crian_mul.setBounds(792, 456, 62, 25);
		numeros.add(n_crian_mul);

		JLabel lblCasadoshomens = new JLabel("Solteiros:");
		lblCasadoshomens.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblCasadoshomens.setBounds(290, 317, 179, 25);
		numeros.add(lblCasadoshomens);

		n_solt = new JTextField();
		n_solt.setText(String.valueOf(TableModelMembro.getInstance().getN_Solteiros()));
		n_solt.setEditable(false);
		n_solt.setColumns(10);
		n_solt.setBounds(490, 317, 62, 25);
		numeros.add(n_solt);

		JLabel lblDivorciados = new JLabel("Divorciados:");
		lblDivorciados.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDivorciados.setBounds(592, 317, 179, 25);
		numeros.add(lblDivorciados);

		n_divorc = new JTextField();
		n_divorc.setText(String.valueOf(TableModelMembro.getInstance().getN_Divorciados()));
		n_divorc.setEditable(false);
		n_divorc.setColumns(10);
		n_divorc.setBounds(792, 317, 62, 25);
		numeros.add(n_divorc);

		JLabel lblMembrosAtivoshomens = new JLabel("Membros Ativos (homens):");
		lblMembrosAtivoshomens.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblMembrosAtivoshomens.setBounds(290, 75, 199, 25);
		numeros.add(lblMembrosAtivoshomens);

		n_membro_ativo_hom = new JTextField();
		n_membro_ativo_hom.setText(String.valueOf(TableModelMembro.getInstance().getN_Membros_Ativos_Homens()));
		n_membro_ativo_hom.setEditable(false);
		n_membro_ativo_hom.setColumns(10);
		n_membro_ativo_hom.setBounds(490, 75, 62, 25);
		numeros.add(n_membro_ativo_hom);

		n_membro_ativo_mul = new JTextField();
		n_membro_ativo_mul.setText(String.valueOf(TableModelMembro.getInstance().getN_Membros_Ativos_Mulheres()));
		n_membro_ativo_mul.setEditable(false);
		n_membro_ativo_mul.setColumns(10);
		n_membro_ativo_mul.setBounds(792, 75, 62, 25);
		numeros.add(n_membro_ativo_mul);

		JLabel lblMembrosAtivosmulheres = new JLabel("Membros Ativos (mulheres):");
		lblMembrosAtivosmulheres.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblMembrosAtivosmulheres.setBounds(592, 75, 199, 25);
		numeros.add(lblMembrosAtivosmulheres);

		JTabbedPane graficosPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.addTab("Gráficos", graficosPane);

		graficosPane.addTab("Tipo de Membro", new XChartPanel<PieChart>(getTipo_Membro_Chart()));
		graficosPane.addTab("Membros Ativos - Sexo", new XChartPanel<PieChart>(getMembro_Ativo_Sexo_Chart()));
		graficosPane.addTab("Membros Batizados", new XChartPanel<PieChart>(getTipo_Membro_Batizado_Chart()));
		graficosPane.addTab("Membros Batizados - Sexo", new XChartPanel<PieChart>(getMembro_Batizado_Sexo_Chart()));
		graficosPane.addTab("Estado Civil", new XChartPanel<PieChart>(getEstado_CivilChart()));
		graficosPane.addTab("Sexo", new XChartPanel<PieChart>(getSexo_Chart()));
		graficosPane.addTab("Idades", new XChartPanel<PieChart>(getIdades_Chart()));
		graficosPane.addTab("Homens - Idade", new XChartPanel<PieChart>(getHomens_Idades_Chart()));
		graficosPane.addTab("Mulheres - Idades", new XChartPanel<PieChart>(getMulheres_Idades_Chart()));
		graficosPane.addTab("Homens - Estado Civil", new XChartPanel<PieChart>(getEstado_Civil_Homens_Chart()));
		graficosPane.addTab("Mulheres - Estado Civil", new XChartPanel<PieChart>(getEstado_Civil_Mulheres_Chart()));
		graficosPane.addTab("Homens - Batizados", new XChartPanel<PieChart>(getHomens_Batizados_Chart()));
		graficosPane.addTab("Mulheres - Batizadas", new XChartPanel<PieChart>(getMulheres_Batizadas_Chart()));
		graficosPane.addTab("Líderes - Sexo", new XChartPanel<PieChart>(getSexo_Lideranca_Chart()));

		JTabbedPane financasPane = new JTabbedPane(JTabbedPane.LEFT);
		tabbedPane.addTab("Finanças", financasPane);

		// Atualizacao TODO
		tabbedPane.setToolTipTextAt(2, "Recurso em desenvolvimento... Brevemente disponível numa atualização futura!");
		tabbedPane.setEnabledAt(2, false);

		financasPane.addTab("Detalhes", new JPanel());
		financasPane.addTab("Entradas / Saídas", new XChartPanel<PieChart>(getEntrada_Saida_Chart()));
		financasPane.addTab("Dízimos / Ofertas", new XChartPanel<PieChart>(getDizimo_Oferta_Chart()));

	}

	/**
	 * 
	 * @return o gráfico dos tipos de membro
	 */
	private PieChart getTipo_Membro_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Tipo de Membro")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries(Tipo_Membro.CONGREGADO.getDescricao(), TableModelMembro.getInstance().getN_Congregados())
				.setToolTip(Tipo_Membro.CONGREGADO.getDescricao() + ": "
						+ String.valueOf(TableModelMembro.getInstance().getN_Congregados()));
		;
		chart.addSeries(Tipo_Membro.MEMBRO_ATIVO.getDescricao() + " (sem líderes)",
				TableModelMembro.getInstance().getN_Membros_Ativos_Sem_Lideranca())
				.setToolTip(Tipo_Membro.MEMBRO_ATIVO.getDescricao() + " (sem líderes): "
						+ String.valueOf(TableModelMembro.getInstance().getN_Membros_Ativos_Sem_Lideranca()));
		;
		chart.addSeries(Tipo_Membro.MEMBRO_NOMINAL.getDescricao(),
				TableModelMembro.getInstance().getN_Membros_Nominais())
				.setToolTip(Tipo_Membro.MEMBRO_NOMINAL.getDescricao() + ": "
						+ String.valueOf(TableModelMembro.getInstance().getN_Membros_Nominais()));
		;
		chart.addSeries(Tipo_Membro.LIDERANCA.getDescricao(), TableModelMembro.getInstance().getN_Lideranca())
				.setToolTip(Tipo_Membro.LIDERANCA.getDescricao() + ": "
						+ String.valueOf(TableModelMembro.getInstance().getN_Lideranca()));
		;
		chart.addSeries(Tipo_Membro.EX_MEMBRO.getDescricao(), TableModelMembro.getInstance().getN_Ex_Membros())
				.setToolTip(Tipo_Membro.EX_MEMBRO.getDescricao() + ": "
						+ String.valueOf(TableModelMembro.getInstance().getN_Ex_Membros()));
		;
		return chart;
	}

	/**
	 * 
	 * @return o gráfico dos membros ativos por sexo
	 */
	private PieChart getMembro_Ativo_Sexo_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500)
				.title(Main.TITLE_SMALL + " - Membros Ativos (por sexo)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries("Membro Ativo (homem)", TableModelMembro.getInstance().getN_Membros_Ativos_Homens()).setToolTip(
				"Membro Ativo (homem): " + String.valueOf(TableModelMembro.getInstance().getN_Membros_Ativos_Homens()));
		chart.addSeries("Membro Ativo (mulher)", TableModelMembro.getInstance().getN_Membros_Ativos_Mulheres())
				.setToolTip("Membro Ativo (mulher): "
						+ String.valueOf(TableModelMembro.getInstance().getN_Membros_Ativos_Mulheres()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico dos membros batizados por sexo
	 */
	private PieChart getMembro_Batizado_Sexo_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500)
				.title(Main.TITLE_SMALL + " - Membros Batizados (por sexo)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries("Membro Batizado (homem)", TableModelMembro.getInstance().getN_Batizados_Homens()).setToolTip(
				"Membro Batizado (homem): " + String.valueOf(TableModelMembro.getInstance().getN_Batizados_Homens()));
		chart.addSeries("Membro Batizado (mulher)", TableModelMembro.getInstance().getN_Batizados_Mulheres())
				.setToolTip("Membro Batizado (mulher): "
						+ String.valueOf(TableModelMembro.getInstance().getN_Batizados_Mulheres()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico dos membros, batizados e não batizados
	 */
	private PieChart getTipo_Membro_Batizado_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Membros Batizados")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries("Batizado", TableModelMembro.getInstance().getN_Batizados())
				.setToolTip("Batizado: " + String.valueOf(TableModelMembro.getInstance().getN_Batizados()));
		chart.addSeries("Não-Batizado", TableModelMembro.getInstance().getN_Nao_Batizados())
				.setToolTip("Não-Batizado: " + String.valueOf(TableModelMembro.getInstance().getN_Nao_Batizados()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico dos homens, indicando se são batizados ou não batizados
	 */
	private PieChart getHomens_Batizados_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Homens Batizados")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries("Homens Batizados", TableModelMembro.getInstance().getN_Batizados_Homens()).setToolTip(
				"Homens Batizado: " + String.valueOf(TableModelMembro.getInstance().getN_Batizados_Homens()));
		chart.addSeries("Homens Não-Batizados", TableModelMembro.getInstance().getN_Nao_Batizados_Homens()).setToolTip(
				"Homens Não-Batizados: " + String.valueOf(TableModelMembro.getInstance().getN_Nao_Batizados_Homens()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico das mulheres, indicando se são batizadas ou não
	 *         batizadas
	 */
	private PieChart getMulheres_Batizadas_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Mulheres Batizadas")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries("Mulheres Batizadas", TableModelMembro.getInstance().getN_Batizados_Mulheres()).setToolTip(
				"Mulheres Batizadas: " + String.valueOf(TableModelMembro.getInstance().getN_Batizados_Mulheres()));
		chart.addSeries("Mulheres Não-Batizadas", TableModelMembro.getInstance().getN_Nao_Batizados_Mulheres())
				.setToolTip("Mulheres Não-Batizadas: "
						+ String.valueOf(TableModelMembro.getInstance().getN_Nao_Batizados_Mulheres()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico dos membros de acordo com o estado civil
	 */
	private PieChart getEstado_CivilChart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Estado Civil")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);

		// Series
		chart.addSeries(Estado_Civil.CASADO.getDescricao(), TableModelMembro.getInstance().getN_Casados())
				.setToolTip(Estado_Civil.CASADO.getDescricao() + ": "
						+ String.valueOf(TableModelMembro.getInstance().getN_Casados()));
		;
		chart.addSeries(Estado_Civil.SOLTEIRO.getDescricao(), TableModelMembro.getInstance().getN_Solteiros())
				.setToolTip(Estado_Civil.SOLTEIRO.getDescricao() + ": "
						+ String.valueOf(TableModelMembro.getInstance().getN_Solteiros()));
		;
		chart.addSeries(Estado_Civil.DIVORCIADO.getDescricao(), TableModelMembro.getInstance().getN_Divorciados())
				.setToolTip(Estado_Civil.DIVORCIADO.getDescricao() + ": "
						+ String.valueOf(TableModelMembro.getInstance().getN_Divorciados()));
		;
		chart.addSeries(Estado_Civil.VIUVO.getDescricao(), TableModelMembro.getInstance().getN_Viuvos())
				.setToolTip(Estado_Civil.VIUVO.getDescricao() + ": "
						+ String.valueOf(TableModelMembro.getInstance().getN_Viuvos()));
		;
		chart.addSeries(Estado_Civil.UNIAO.getDescricao(), TableModelMembro.getInstance().getN_Uniao()).setToolTip(
				Estado_Civil.UNIAO.getDescricao() + ": " + String.valueOf(TableModelMembro.getInstance().getN_Uniao()));
		;
		return chart;
	}

	/**
	 * 
	 * @return o gráfico dos homens, indicando o estado civil
	 */
	private PieChart getEstado_Civil_Homens_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500)
				.title(Main.TITLE_SMALL + " - Homens (Estado Civil)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);

		// Series
		chart.addSeries("Homens - Casados", TableModelMembro.getInstance().getN_Casados_Homens()).setToolTip(
				"Homens - Casados: " + String.valueOf(TableModelMembro.getInstance().getN_Casados_Homens()));
		;
		chart.addSeries("Homens - Solteiros", TableModelMembro.getInstance().getN_Solteiros_Homens()).setToolTip(
				"Homens - Solteiros: " + String.valueOf(TableModelMembro.getInstance().getN_Solteiros_Homens()));
		;
		chart.addSeries("Homens - Divorciados", TableModelMembro.getInstance().getN_Divorciados_Homens()).setToolTip(
				"Homens - Divorciados: " + String.valueOf(TableModelMembro.getInstance().getN_Divorciados_Homens()));
		;
		chart.addSeries("Homens - Viúvos", TableModelMembro.getInstance().getN_Viuvos_Homens())
				.setToolTip("Homens - Viúvos: " + String.valueOf(TableModelMembro.getInstance().getN_Viuvos_Homens()));
		;
		chart.addSeries("Homens - União", TableModelMembro.getInstance().getN_Uniao_Homens())
				.setToolTip("Homens - União: " + String.valueOf(TableModelMembro.getInstance().getN_Uniao_Homens()));
		;
		return chart;
	}

	/**
	 * 
	 * @return o gráfico das mulheres, indicando o estado civil
	 */
	private PieChart getEstado_Civil_Mulheres_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500)
				.title(Main.TITLE_SMALL + " - Mulheres (Estado Civil)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);

		// Series
		chart.addSeries("Mulheres - Casadas", TableModelMembro.getInstance().getN_Casados_Mulheres()).setToolTip(
				"Mulheres - Casadas: " + String.valueOf(TableModelMembro.getInstance().getN_Casados_Mulheres()));
		;
		chart.addSeries("Mulheres - Solteiras", TableModelMembro.getInstance().getN_Solteiros_Mulheres()).setToolTip(
				"Mulheres - Solteiras: " + String.valueOf(TableModelMembro.getInstance().getN_Solteiros_Mulheres()));
		;
		chart.addSeries("Mulheres - Divorciadas", TableModelMembro.getInstance().getN_Divorciados_Mulheres())
				.setToolTip("Mulheres - Divorciadas: "
						+ String.valueOf(TableModelMembro.getInstance().getN_Divorciados_Mulheres()));
		;
		chart.addSeries("Mulheres - Viúvas", TableModelMembro.getInstance().getN_Viuvos_Mulheres()).setToolTip(
				"Mulheres - Viúvas: " + String.valueOf(TableModelMembro.getInstance().getN_Viuvos_Mulheres()));
		;
		chart.addSeries("Mulheres - União", TableModelMembro.getInstance().getN_Uniao_Mulheres()).setToolTip(
				"Mulheres - União: " + String.valueOf(TableModelMembro.getInstance().getN_Uniao_Mulheres()));
		;
		return chart;
	}

	/**
	 * 
	 * @return o gráfico da liderança, dividido pelo sexo
	 */
	private PieChart getSexo_Lideranca_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500)
				.title(Main.TITLE_SMALL + " - Liderança (por sexo)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);

		// Series
		chart.addSeries("Líderes - Homens", TableModelMembro.getInstance().getN_Lideres_Homens()).setToolTip(
				"Líderes - Homens: " + String.valueOf(TableModelMembro.getInstance().getN_Lideres_Homens()));
		;
		chart.addSeries("Líderes - Mulheres", TableModelMembro.getInstance().getN_Lideres_Mulheres()).setToolTip(
				"Líderes - Mulheres: " + String.valueOf(TableModelMembro.getInstance().getN_Lideres_Mulheres()));
		;

		return chart;
	}

	/**
	 * 
	 * @return o gráfico dos membros, divididos por faixa etária.
	 */
	private PieChart getIdades_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Idades")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries("Adultos (+18)", TableModelMembro.getInstance().getN_Adultos())
				.setToolTip("Adultos (+18): " + String.valueOf(TableModelMembro.getInstance().getN_Adultos()));
		chart.addSeries("Adolescentes (13-17)", TableModelMembro.getInstance().getN_Adolescentes()).setToolTip(
				"Adolescentes (13-17) " + String.valueOf(TableModelMembro.getInstance().getN_Adolescentes()));
		chart.addSeries("Crianças (-12)", TableModelMembro.getInstance().getN_Criancas())
				.setToolTip("Crianças (-18): " + String.valueOf(TableModelMembro.getInstance().getN_Criancas()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico dos membros, divididos por sexo
	 */
	private PieChart getSexo_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Sexo")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries(Sexo.MASCULINO.getDescricao(), TableModelMembro.getInstance().getN_Homens()).setToolTip(
				Sexo.MASCULINO.getDescricao() + ": " + String.valueOf(TableModelMembro.getInstance().getN_Homens()));
		chart.addSeries(Sexo.FEMININO.getDescricao(), TableModelMembro.getInstance().getN_Mulheres()).setToolTip(
				Sexo.FEMININO.getDescricao() + ": " + String.valueOf(TableModelMembro.getInstance().getN_Mulheres()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico dos homens, divididos por faixa etária
	 */
	private PieChart getHomens_Idades_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Homens (Idades)")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries("Homens Adultos (+18)", TableModelMembro.getInstance().getN_Adultos_Homens()).setToolTip(
				"Homens Adultos (+18): " + String.valueOf(TableModelMembro.getInstance().getN_Adultos_Homens()));
		chart.addSeries("Homens Adolescentes (13-17)", TableModelMembro.getInstance().getN_Adolescentes_Homens())
				.setToolTip("Homens Adolescentes (13-17) "
						+ String.valueOf(TableModelMembro.getInstance().getN_Adolescentes_Homens()));
		chart.addSeries("Homens Crianças (-12)", TableModelMembro.getInstance().getN_Criancas_Homens()).setToolTip(
				"Homens Crianças (-12): " + String.valueOf(TableModelMembro.getInstance().getN_Criancas_Homens()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico das mulheres, divididas por faixa etária
	 */
	private PieChart getMulheres_Idades_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Mulheres (Idades)")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries("Mulheres Adultas (+18)", TableModelMembro.getInstance().getN_Adultos_Mulheres()).setToolTip(
				"Mulheres Adultas (+18): " + String.valueOf(TableModelMembro.getInstance().getN_Adultos_Mulheres()));
		chart.addSeries("Mulheres Adolescentes (13-17)", TableModelMembro.getInstance().getN_Adolescentes_Mulheres())
				.setToolTip("Mulheres Adolescentes (13-17) "
						+ String.valueOf(TableModelMembro.getInstance().getN_Adolescentes_Mulheres()));
		chart.addSeries("Mulheres Crianças (-12)", TableModelMembro.getInstance().getN_Criancas_Mulheres()).setToolTip(
				"Mulheres Adultos (+18): " + String.valueOf(TableModelMembro.getInstance().getN_Criancas_Mulheres()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico das finanças (entradas/saídas)
	 */
	private PieChart getEntrada_Saida_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Entradas / Saídas")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries(Tipo_Transacao.ENTRADA.getDescricao(), TableModelMembro.getInstance().getN_Batizados());
		chart.addSeries(Tipo_Transacao.SAIDA.getDescricao(), TableModelMembro.getInstance().getN_Nao_Batizados());
		return chart;
	}

	/**
	 * 
	 * @return o gráfico do tipo de entradas (dízimos/ofertas/outros)
	 */
	private PieChart getDizimo_Oferta_Chart() {
		PieChart chart = new PieChartBuilder().width(700).height(500).title(Main.TITLE_SMALL + " - Dízimos / Ofertas")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		// Series
		chart.addSeries(Entradas_Tipo.DIZIMO.getDescricao(), TableModelMembro.getInstance().getN_Batizados());
		chart.addSeries(Entradas_Tipo.OFERTA.getDescricao(), TableModelMembro.getInstance().getN_Nao_Batizados());
		chart.addSeries(Entradas_Tipo.OUTROS.getDescricao(), TableModelMembro.getInstance().getN_Nao_Batizados());
		return chart;
	}

	/**
	 * Torna o diálogo visível.
	 */
	public void open() {
		setVisible(true);
	}
}
