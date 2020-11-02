package dad.recursos;

import java.awt.Color;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.style.Styler.ChartTheme;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.table.TableModelFinancas;
import dad.fam_com_cristo.types.enumerados.EstatisticaPeriodos;
import dad.fam_com_cristo.types.enumerados.Tipo_Transacao;
import mdlaf.utils.MaterialColors;

/**
 * Classe que agrupa os gráficos das finanças
 * @author dariopereiradp
 *
 */
public class GraficosFinancas {
	private static TableModelFinancas financas = TableModelFinancas.getInstance();
	private static Utils utils = Utils.getInstance();

	/**
	 * Gera um gráfico PieChart de Entradas/Saídas para o período indicado
	 * @param periodo
	 * @param initDate - pode ser null (sem data de inicio)
	 * @param finalDate - pode ser null (sem data de fim)
	 * @param print - indica se o gráfico gerado vai ser impresso ou não (usada para controlar as cores)
	 * @return
	 */
	public static PieChart getEntrada_Saida_Chart(EstatisticaPeriodos periodo, LocalDate initDate, LocalDate finalDate, boolean print) {
		PieChart chart = new PieChartBuilder().width(800).height(500)
				.title(Main.TITLE_SMALL + " - Entradas / Saídas - " + periodo.toString()).theme(ChartTheme.GGPlot2)
				.build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true);
		chart.getStyler().setToolTipsEnabled(true);
		chart.getStyler().setToolTipHighlightColor(new Color(175, 25, 125, 50));
		chart.getStyler().setAnnotationDistance(1.15);
		chart.getStyler().setPlotContentSize(0.7);
		chart.getStyler().setStartAngleInDegrees(90);
		chart.getStyler().setDrawAllAnnotations(true);

		if (!print)
			GraficosMembros.customizeDarkMode(chart.getStyler());
		else {
			chart.getStyler().setSeriesColors(
					new Color[] { MaterialColors.BLUE_900, MaterialColors.BLUE_300 });
		}

		chart.addSeries(Tipo_Transacao.ENTRADA.getDescricao(),
				TableModelFinancas.getInstance().getTotalEntradas(initDate, finalDate));
		chart.addSeries(Tipo_Transacao.SAIDA.getDescricao(),
				TableModelFinancas.getInstance().getTotalSaidas(initDate, finalDate));
		return chart;
	}

	/**
	 * Gera um gráfico de barras para o ano indicado
	 * @param ano
	 * @param print - indica se o gráfico gerado vai ser impresso ou não (usada para controlar as cores)
	 * @return
	 */
	public static CategoryChart getEntrada_Saida_BarChart(int ano, boolean print) {
		CategoryChart chart = new CategoryChartBuilder().width(800).height(500)
				.title(Main.TITLE_SMALL + " - Entradas / Saídas - " + ano).theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true).setToolTipsEnabled(true)
				.setToolTipHighlightColor(new Color(175, 25, 125, 50))
				.setSeriesColors(
						new Color[] { MaterialColors.GREEN_500, MaterialColors.RED_500 })
				.setChartPadding(10).setChartTitlePadding(10).setDecimalPattern("R$ ###,###.###");

		if (!print)
			GraficosMembros.customizeDarkMode(chart.getStyler());
		else {
			chart.getStyler().setSeriesColors(
					new Color[] { MaterialColors.BLUE_900, MaterialColors.BLUE_300 });
		}

		List<String> eixoX = new ArrayList<>();
		String[] tooltips = new String[12];
		for (Month m : Month.values()) {
			String month = StringUtils.capitalize(m.getDisplayName(TextStyle.SHORT, new Locale("pt")));
			eixoX.add(month);
			String tooltip = month + " - Entradas: "
					+ utils.getNumberFormatCurrency().format(financas.getTotalEntradasPorMes(ano).get(m.getValue() - 1))
					+ " | Saídas: "
					+ utils.getNumberFormatCurrency().format(financas.getTotalSaidasPorMes(ano).get(m.getValue() - 1))
					+ " | Sub-total: "
					+ utils.getNumberFormatCurrency().format(financas.getSubTotalPorMes(ano).get(m.getValue() - 1));
			tooltips[m.getValue() - 1] = tooltip;
		}

		chart.addSeries(Tipo_Transacao.ENTRADA.getDescricao(), eixoX, financas.getTotalEntradasPorMes(ano))
				.setCustomToolTips(true).setToolTips(tooltips);
		chart.addSeries(Tipo_Transacao.SAIDA.getDescricao(), eixoX, financas.getTotalSaidasPorMes(ano))
				.setCustomToolTips(true).setToolTips(tooltips);
		return chart;
	}
}
