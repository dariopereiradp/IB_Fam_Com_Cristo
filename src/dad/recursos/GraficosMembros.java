package dad.recursos;

import java.awt.Color;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.Styler.ChartTheme;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.gui.themes.DarkTheme;
import dad.fam_com_cristo.table.TableModelMembro;
import dad.fam_com_cristo.types.enumerados.Estado_Civil;
import dad.fam_com_cristo.types.enumerados.Sexo;
import dad.fam_com_cristo.types.enumerados.Tipo_Membro;
import mdlaf.utils.MaterialColors;

/**
 * Classe para agrupar os diferentes gráficos de membros a serem gerados
 * @author dariopereiradp
 *
 */
public class GraficosMembros {
	/**
	 * 
	 * @return o gráfico dos tipos de membro
	 */
	public static PieChart getTipo_Membro_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500).title(Main.TITLE_SMALL + " - Tipo de Membro")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		customizeDarkMode(chart.getStyler());

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
	public static PieChart getMembro_Ativo_Sexo_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500)
				.title(Main.TITLE_SMALL + " - Membros Ativos (por sexo)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getMembro_Batizado_Sexo_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500)
				.title(Main.TITLE_SMALL + " - Membros Batizados (por sexo)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getTipo_Membro_Batizado_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500).title(Main.TITLE_SMALL + " - Membros Batizados")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getHomens_Batizados_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500).title(Main.TITLE_SMALL + " - Homens Batizados")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);
		
		customizeDarkMode(chart.getStyler());

		// Series
		chart.addSeries("Homens Batizados", TableModelMembro.getInstance().getN_Batizados_Homens()).setToolTip(
				"Homens Batizado: " + String.valueOf(TableModelMembro.getInstance().getN_Batizados_Homens()));
		chart.addSeries("Homens Não-Batizados", TableModelMembro.getInstance().getN_Nao_Batizados_Homens()).setToolTip(
				"Homens Não-Batizados: " + String.valueOf(TableModelMembro.getInstance().getN_Nao_Batizados_Homens()));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico das mulheres, indicando se são batizadas ou não batizadas
	 */
	public static PieChart getMulheres_Batizadas_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500).title(Main.TITLE_SMALL + " - Mulheres Batizadas")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getEstado_CivilChart() {
		PieChart chart = new PieChartBuilder().width(800).height(500).title(Main.TITLE_SMALL + " - Estado Civil")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getEstado_Civil_Homens_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500)
				.title(Main.TITLE_SMALL + " - Homens (Estado Civil)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);

		customizeDarkMode(chart.getStyler());
		
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
	public static PieChart getEstado_Civil_Mulheres_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500)
				.title(Main.TITLE_SMALL + " - Mulheres (Estado Civil)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getSexo_Lideranca_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500)
				.title(Main.TITLE_SMALL + " - Liderança (por sexo)").theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getIdades_Chart() {
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
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getSexo_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500).title(Main.TITLE_SMALL + " - Sexo")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getHomens_Idades_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500).title(Main.TITLE_SMALL + " - Homens (Idades)")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);
		
		customizeDarkMode(chart.getStyler());

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
	public static PieChart getMulheres_Idades_Chart() {
		PieChart chart = new PieChartBuilder().width(800).height(500).title(Main.TITLE_SMALL + " - Mulheres (Idades)")
				.theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);
		
		customizeDarkMode(chart.getStyler());

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
	 * Altera as cores do gráfico para se adaptarem ao DarkMode, se estiver ativo
	 * @param styler
	 */
	public static void customizeDarkMode(Styler styler) {
		// DarkTheme
		if (Utils.getInstance().getCurrentTheme().equals(DarkTheme.getInstance())) {
			styler.setChartBackgroundColor(MaterialColors.GRAY_900).setToolTipBackgroundColor(MaterialColors.BLACK)
					.setLegendBackgroundColor(MaterialColors.GRAY_800).setPlotBackgroundColor(MaterialColors.GRAY_800)
					.setPlotBorderColor(MaterialColors.WHITE).setChartTitleBoxBackgroundColor(MaterialColors.BLACK)
					.setChartFontColor(MaterialColors.WHITE)
					.setAnnotationsFontColor(MaterialColors.WHITE);
		}
	}


}
