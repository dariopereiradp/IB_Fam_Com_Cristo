package dad.recursos.pdf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;

import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.PieChart;
import org.knowm.xchart.XChartPanel;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.table.models.TableModelFinancas;
import dad.fam_com_cristo.types.enumerados.EstatisticaPeriodos;
import dad.fam_com_cristo.types.enumerados.Tipo_Transacao;
import dad.recursos.GraficosFinancas;
import dad.recursos.Log;
import dad.recursos.Money;
import dad.recursos.Utils;

/**
 * Calsse que gera um PDF contendo o relatório financeiro para o período
 * indicado
 * 
 * @author dariopereiradp
 *
 */
public class TableFinancasToPDF {

	private static TableModelFinancas financas = TableModelFinancas.getInstance();
	private static Utils utils = Utils.getInstance();
	private static String descricaoS;

	/**
	 * 
	 * @param table   				   - tabela base que vai ser convertida para PDF
	 * @param descricao				   - descrição que vai para o título e nome do ficheiro
	 * @param periodo 				   - período do relatório
	 * @param init     				   - data de início
	 * @param end       			   - data de fim
	 * @param anual     			   - true inclui um CategoryChart - false inclui um PieChart
	 * @param incluirSaldoTotal     - true inclui o saldo total - false só inclui o saldo do período
	 * 
	 * @return
	 */
	public static String transacoesToPDF(JTable table, String descricao, EstatisticaPeriodos periodo, LocalDate init,
			LocalDate end, boolean anual, boolean incluirSaldoTotal) {

		descricaoS = descricao;

		String title = Main.SIGLA + "_Relatório_Financeiro_" + descricao + "_"
				+ DateTimeFormatter.ofPattern("ddMMMyyyy").format(LocalDate.now()) + ".pdf";
		try {
			Document doc = new Document(PageSize.A4, 40, 40, 50, 50);

			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(Main.RELATORIOS_DIR + title));
			writer.setPageEvent(new HeaderFooterPageEvent());
			doc.open();

			// Metadata
			doc.addSubject(Main.SIGLA + " - Relatório Financeiro - " + descricao);
			doc.addTitle(title);
			doc.addAuthor(Main.TITLE_SMALL);

			// Logo
			Image logo = Image.getInstance(TableFinancasToPDF.class.getResource("/FC.jpg"));
			logo.scalePercent(3.5f);
			logo.setAlignment(Element.ALIGN_CENTER);
			doc.add(logo);

			// Title
			Font fonteTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
			doc.add(new Paragraph(" "));
			Paragraph titleP = new Paragraph(Main.TITLE_SMALL, fonteTitle);
			titleP.setAlignment(Element.ALIGN_CENTER);
			doc.add(titleP);
			doc.add(new Paragraph(" "));
			Paragraph subTitle = new Paragraph("Relatório Financeiro - " + descricao,
					new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
			subTitle.setAlignment(Element.ALIGN_CENTER);
			doc.add(subTitle);

			// Descrição de movimentos
			doc.add(new Paragraph(" "));

			JPanel panel = null;
			if (anual) {
				panel = new XChartPanel<CategoryChart>(
						GraficosFinancas.getEntrada_Saida_BarChart(init.getYear(), true));

			} else {
				panel = new XChartPanel<PieChart>(GraficosFinancas.getEntrada_Saida_Chart(periodo, init, end, true));
			}

			panel.setSize((int) ((PageSize.A4.getWidth() - 2 * 40) * 2), 400);
			Image grafico = createImage(panel);
			grafico.scaleToFit(PageSize.A4.getWidth() - 2 * 40, 200);
			doc.add(grafico);

			Font zapfdingbats = new Font(FontFamily.ZAPFDINGBATS, 8);
			Chunk bullet = new Chunk(String.valueOf((char) 108), zapfdingbats);

			doc.add(new Paragraph(" "));
			Paragraph descricaoSaldo = new Paragraph("", FontFactory.getFont(FontFactory.COURIER, 12));
			descricaoSaldo.add("Relatório gerado em: " + DateTimeFormatter
					.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("pt")).format(LocalDate.now()) + ".\n");

			if(incluirSaldoTotal) {
				descricaoSaldo.add(bullet);
				descricaoSaldo
						.add(" O saldo atual é: " + utils.getNumberFormatCurrency().format(financas.getTotal()) + ".\n");
				descricaoSaldo.add(bullet);
				descricaoSaldo.add(" O saldo no último dia do período (filtrado) era: "
						.concat(table.getRowCount() == 0 ? "R$0,00.\n" : table.getValueAt(table.getRowCount() - 1, 4) + ".\n"));
			}

			descricaoSaldo.add(bullet);
			descricaoSaldo.add(" O total de entradas no período foi: "
					+ utils.getNumberFormatCurrency().format(financas.getTotalEntradas(init, end)) + ".\n");
			descricaoSaldo.add(bullet);
			descricaoSaldo.add(" O total de saídas no período foi: "
					+ utils.getNumberFormatCurrency().format(financas.getTotalSaidas(init, end)) + ".\n");
			descricaoSaldo.add(bullet);
			descricaoSaldo.add(" Foram registradas " + financas.getNumEntradasPorPeriodo(init, end) + " entrada(s) e "
					+ financas.getNumSaidasPorPeriodo(init, end) + " saída(s) durante o período.\n");
			descricaoSaldo.add(bullet);
			descricaoSaldo.add(" O balanço do período foi (entradas-saídas): "
					+ utils.getNumberFormatCurrency().format(financas.getTotalPeriod(init, end)) + ".");

			descricaoSaldo.setAlignment(Element.ALIGN_LEFT);
			doc.add(descricaoSaldo);
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));

			// Tabela
			((TableModelFinancas) table.getModel()).ordenar();
			PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
			pdfTable.setWidths(new int[] { 150, 150, 150, 400, 150 });
			// adding table headers
			for (int i = 0; i < table.getColumnCount(); i++) {
				Paragraph header;
				header = new Paragraph(table.getColumnName(i).toUpperCase(),
						FontFactory.getFont(FontFactory.COURIER_BOLD, 12));

				PdfPCell cell = new PdfPCell(header);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfTable.addCell(cell);
			}
			
			Money saldoAnterior = new Money();
			
			if(!incluirSaldoTotal && table.getRowCount() > 0) {
				Money subTotal = (Money) table.getModel().getValueAt(table.convertRowIndexToModel(0), 4);
				Money firstOper = (Money) table.getModel().getValueAt(table.convertRowIndexToModel(0), 1);
				
				if(subTotal.compareTo(firstOper) != 0) {
					Tipo_Transacao tipo = (Tipo_Transacao) table.getModel().getValueAt(table.convertRowIndexToModel(0), 2);
					
					saldoAnterior = subTotal.transacaoInversa(tipo, firstOper);
				}
			}
			
			// extracting data from the JTable and inserting it to PdfPTable
			for (int rows = 0; rows < table.getRowCount(); rows++) {
				for (int cols = 0; cols < table.getColumnCount(); cols++) {
					Paragraph p = null;
					String text = "";
					
					if(cols == 4 && !saldoAnterior.eZero()) {
						text = ((Money)table.getModel().getValueAt(table.convertRowIndexToModel(rows), cols)).subtract(saldoAnterior).toString();
					} else {
						text = table.getModel().getValueAt(table.convertRowIndexToModel(rows), cols).toString();
					}
					
					p = new Paragraph(text, FontFactory.getFont(FontFactory.COURIER, 9));
					PdfPCell cell = new PdfPCell(p);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pdfTable.addCell(cell);
				}
			}
			pdfTable.setWidthPercentage(100);
			doc.add(pdfTable);
			doc.add(new Paragraph(" "));

			// Assinatura
			PdfPTable assinatura = new PdfPTable(1);
			assinatura.setWidthPercentage(45f);
			assinatura.setHorizontalAlignment(Element.ALIGN_RIGHT);

			Paragraph assinatura_line = new Paragraph("_________________________________________",
					FontFactory.getFont(FontFactory.COURIER, 9));
			PdfPCell cellOne = new PdfPCell(assinatura_line);
			cellOne.setBorder(Rectangle.NO_BORDER);
			assinatura.addCell(cellOne);

			Paragraph tesoureiro = new Paragraph(("(Tesoureiro)"), FontFactory.getFont(FontFactory.TIMES, 10));
			PdfPCell tesoureiroCell = new PdfPCell(tesoureiro);
			tesoureiroCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			tesoureiroCell.setBorder(Rectangle.NO_BORDER);
			assinatura.addCell(tesoureiroCell);
			
			doc.add(new Paragraph(" "));
			doc.add(assinatura);

//			assinatura.writeSelectedRows(0, -1, 315, assinatura.getTotalHeight() + doc.bottom(doc.bottomMargin()),
//					writer.getDirectContent());

			// Save
			doc.close();
			String message = "O Relatório Financeiro - " + descricao
					+ " foi criado com sucesso!\nFoi salvo um documento PDF (que pode ser impresso) na pasta:\n"
					+ Main.RELATORIOS_DIR+ "\nVocê quer abrir o documento agora?";
			Utils.askMessage(message, Main.RELATORIOS_DIR, Main.RELATORIOS_DIR + title);

		} catch (DocumentException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			Log.getInstance()
					.printLog("Erro ao salvar PDF do relatório financeiro - " + descricao + " - " + e.getMessage());
			JOptionPane.showMessageDialog(null,
					"Não foi possível criar o PDF do Relatório Financeiro!\n"
							+ "Se tiver um relatório aberto, por favor feche e tente novamente!",
					"Criar Relatório Financeiro - Erro", JOptionPane.OK_OPTION,
					new ImageIcon(TableFinancasToPDF.class.getResource("/FC_SS.jpg")));
			e.printStackTrace();
		}

		return title;
	}

	private static Image createImage(JComponent component) throws BadElementException, IOException {

		int w = component.getWidth();
		int h = component.getHeight();
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bi.createGraphics();
		component.paint(g);
		g.dispose();
		return Image.getInstance(bi.getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH), null);
	}

	private static class HeaderFooterPageEvent extends PdfPageEventHelper {

		public void onStartPage(PdfWriter writer, Document document) {
			if (document.getPageNumber() != 1) {
				Paragraph headerLeft = new Paragraph("Relatório Financeiro - " + descricaoS,
						FontFactory.getFont(FontFactory.TIMES, 8));
				ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, new Phrase(headerLeft), 30,
						800, 0);
				Paragraph headerRight = new Paragraph(Main.TITLE_SMALL, FontFactory.getFont(FontFactory.TIMES, 8));
				ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase(headerRight), 550,
						800, 0);
			}
		}

		public void onEndPage(PdfWriter writer, Document document) {
			Image image;
			try {
				image = Image.getInstance(TableFinancasToPDF.class.getResource("/FC.jpg"));
				image.setAlignment(Element.ALIGN_CENTER);
				image.setAbsolutePosition(296, 15);
				image.scalePercent(0.8f);
				writer.getDirectContent().addImage(image, true);
			} catch (IOException | DocumentException e) {
				Log.getInstance().printLog(e.getMessage());
				e.printStackTrace();
			}
			Paragraph footerLeftP = new Paragraph(
					DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy").format(LocalDate.now()),
					FontFactory.getFont(FontFactory.TIMES, 8));
			Phrase footerLeft = new Phrase(footerLeftP);
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, footerLeft, 30, 30, 0);
			Paragraph footerRightP = new Paragraph("pg. " + document.getPageNumber(),
					FontFactory.getFont(FontFactory.TIMES, 8));
			Phrase footerRight = new Phrase(footerRightP);
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, footerRight, 550, 30, 0);
		}
	}

}
