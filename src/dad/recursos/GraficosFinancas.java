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

import dad.fam_com_cristo.Tipo_Transacao;
import dad.fam_com_cristo.gui.Main;
import dad.fam_com_cristo.table.TableModelFinancas;
import mdlaf.utils.MaterialColors;

public class GraficosFinancas {
	private static TableModelFinancas financas = TableModelFinancas.getInstance();
	private static Utils utils = Utils.getInstance();

	/**
	 * 
	 * @return o gráfico das finanças (entradas/saídas)
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

//		LocalDate initDate = periodo.equals(EstatisticaPeriodos.PERSONALIZADO) ? estatiscas.getDatas1().getInitDate()
//				: periodo.getInit();
//		LocalDate finalDate = periodo.equals(EstatisticaPeriodos.PERSONALIZADO) ? estatiscas.getDatas1().getFinalDate()
//				: periodo.getEnd();

		chart.addSeries(Tipo_Transacao.ENTRADA.getDescricao(),
				TableModelFinancas.getInstance().getTotalEntradas(initDate, finalDate));
		chart.addSeries(Tipo_Transacao.SAIDA.getDescricao(),
				TableModelFinancas.getInstance().getTotalSaidas(initDate, finalDate));
		return chart;
	}

	/**
	 * 
	 * @return o gráfico das finanças (entradas/saídas)
	 */
	public static CategoryChart getEntrada_Saida_BarChart(int ano, boolean print) {
		CategoryChart chart = new CategoryChartBuilder().width(800).height(500)
				.title(Main.TITLE_SMALL + " - Entradas / Saídas - " + ano).theme(ChartTheme.GGPlot2).build();

		// Customize Chart
		chart.getStyler().setLegendVisible(true).setToolTipsEnabled(true)
				.setToolTipHighlightColor(new Color(175, 25, 125, 50))
				.setSeriesColors(
						new Color[] { MaterialColors.GREEN_500, MaterialColors.RED_500, MaterialColors.YELLOW_500 })
				.setChartPadding(10).setChartTitlePadding(10).setDecimalPattern("R$ ###,###.###");

		if (!print)
			GraficosMembros.customizeDarkMode(chart.getStyler());

		// chart.getStyler().setPlotContentSize(0.7);

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
		// chart.addSeries("Sub-total", eixoX, financas.getSubTotalPorMes(ano));
		return chart;
	}
}
