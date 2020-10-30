package dad.recursos.pdf;

import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
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
import dad.fam_com_cristo.gui.DataGui;
import dad.fam_com_cristo.table.TableModelFinancas;
import dad.fam_com_cristo.types.EstatisticaPeriodos;
import dad.recursos.GraficosFinancas;
import dad.recursos.Log;
import dad.recursos.Utils;

public class TableFinancasToPDF {

	private static TableModelFinancas financas = TableModelFinancas.getInstance();
	private static Utils utils = Utils.getInstance();
	private static String descricaoS;

	public static String transacoesToPDF(JTable table, String descricao, EstatisticaPeriodos periodo, LocalDate init,
			LocalDate end, boolean anual) {

		descricaoS = descricao;
		
		String title = "IBFC_Relat�rio_Financeiro_" + descricao + "_"
				+ new SimpleDateFormat("ddMMMyyyy").format(new Date()) + ".pdf";
		try {
			Document doc = new Document(PageSize.A4, 40, 40, 50, 50);

			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(Main.LISTAS_DIR + title));
			writer.setPageEvent(new HeaderFooterPageEvent());
			doc.open();

			// Metadata
			doc.addSubject("IBFC - Relat�rio Financeiro - " + descricao);
			doc.addTitle(title);
			doc.addAuthor(Main.TITLE_SMALL);

			Font fonteTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
			Image logo = Image.getInstance(DataGui.getInstance().getClass().getResource("/FC.jpg"));
			logo.scalePercent(3.5f);

			logo.setAlignment(Element.ALIGN_CENTER);
			doc.add(logo);
			doc.add(new Paragraph(" "));
			Paragraph titleP = new Paragraph(Main.TITLE_SMALL, fonteTitle);
			titleP.setAlignment(Element.ALIGN_CENTER);
			doc.add(titleP);
			doc.add(new Paragraph(" "));
			Paragraph subTitle = new Paragraph("Relat�rio Financeiro - " + descricao,
					new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
			subTitle.setAlignment(Element.ALIGN_CENTER);
			doc.add(subTitle);

			// Descri��o de movimentos
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
			descricaoSaldo.add("Relat�rio gerado em: " + DateTimeFormatter
					.ofPattern("EEEE, d 'de' MMMM 'de' yyyy", new Locale("pt")).format(LocalDate.now()) + ".\n");
			descricaoSaldo.add(bullet);
			descricaoSaldo
					.add(" O saldo atual �: " + utils.getNumberFormatCurrency().format(financas.getTotal()) + ".\n");
			descricaoSaldo.add(bullet);
			descricaoSaldo.add(" O saldo total no �ltimo dia do per�odo era: "
					+ table.getValueAt(table.getRowCount() - 1, 5) + ".\n");
			descricaoSaldo.add(bullet);
			descricaoSaldo.add(" O total de entradas no per�odo foi: "
					+ utils.getNumberFormatCurrency().format(financas.getTotalEntradas(init, end)) + ".\n");
			descricaoSaldo.add(bullet);
			descricaoSaldo.add(" O total de sa�das no per�odo foi: "
					+ utils.getNumberFormatCurrency().format(financas.getTotalSaidas(init, end)) + ".\n");
			descricaoSaldo.add(bullet);
			descricaoSaldo.add(" Foram registadas " + financas.getNumEntradasPorPeriodo(init, end) + " entradas e "
					+ financas.getNumSaidasPorPeriodo(init, end) + " sa�das durante o per�odo.\n");
			descricaoSaldo.add(bullet);
			descricaoSaldo.add(" O balan�o do per�odo foi (entradas-sa�das): "
					+ utils.getNumberFormatCurrency().format(financas.getTotalPeriod(init, end)) + ".");

			descricaoSaldo.setAlignment(Element.ALIGN_LEFT);
			doc.add(descricaoSaldo);
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));

			// Tabela
			((TableModelFinancas) table.getModel()).ordenar();
			PdfPTable pdfTable = new PdfPTable(table.getColumnCount() - 1);
			pdfTable.setWidths(new int[] { 150, 150, 150, 400, 150 });
			// adding table headers
			for (int i = 1; i < table.getColumnCount(); i++) {
				Paragraph header;
				header = new Paragraph(table.getColumnName(i).toUpperCase(),
						FontFactory.getFont(FontFactory.COURIER_BOLD, 12));

				PdfPCell cell = new PdfPCell(header);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfTable.addCell(cell);
			}
			// extracting data from the JTable and inserting it to PdfPTable
			for (int rows = 0; rows < table.getRowCount(); rows++) {
				for (int cols = 1; cols < table.getColumnCount(); cols++) {
					Paragraph p = null;
					if (cols == 1)
						p = new Paragraph(
								utils.getDateFormat()
										.format((LocalDate) table.getModel()
												.getValueAt(table.convertRowIndexToModel(rows), cols)),
								FontFactory.getFont(FontFactory.COURIER, 9));
					else
						p = new Paragraph(
								table.getModel().getValueAt(table.convertRowIndexToModel(rows), cols).toString(),
								FontFactory.getFont(FontFactory.COURIER, 9));
					PdfPCell cell = new PdfPCell(p);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					pdfTable.addCell(cell);
				}
			}
			pdfTable.setWidthPercentage(100);
			doc.add(pdfTable);
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));

			// Assinatura
			PdfPTable assinatura = new PdfPTable(1);
			assinatura.setTotalWidth(240);

			Paragraph assinatura_line = new Paragraph("_________________________________________",
					FontFactory.getFont(FontFactory.COURIER, 9));
			PdfPCell cellOne = new PdfPCell(assinatura_line);
			cellOne.setBorder(Rectangle.NO_BORDER);
			assinatura.addCell(cellOne);
			Paragraph pastor = new Paragraph(utils.getPastorName(), FontFactory.getFont(FontFactory.TIMES, 10));
			PdfPCell pastorCell = new PdfPCell(pastor);
			pastorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pastorCell.setBorder(Rectangle.NO_BORDER);
			assinatura.addCell(pastorCell);

			Paragraph pastorF = new Paragraph(("(Pastor Principal)"), FontFactory.getFont(FontFactory.TIMES, 10));
			PdfPCell pastorFCell = new PdfPCell(pastorF);
			pastorFCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pastorFCell.setBorder(Rectangle.NO_BORDER);
			assinatura.addCell(pastorFCell);

			assinatura.writeSelectedRows(0, -1, 315, assinatura.getTotalHeight() + doc.bottom(doc.bottomMargin()),
					writer.getDirectContent());
			doc.close();
			System.out.println("done");

			String message = "O Relat�rio Financeiro - " + descricao
					+ " foi criado com sucesso!\nFoi salvo um documento PDF (que pode ser impresso) na pasta:\n"
					+ Main.LISTAS_DIR + "\nVoc� quer abrir o documento agora?";
			int ok = JOptionPane.showOptionDialog(DataGui.getInstance(), message, "Criado com sucesso",
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
					new ImageIcon(DataGui.getInstance().getClass().getResource("/FC_SS.jpg")), Main.OPTIONS,
					Main.OPTIONS[1]);
			Log.getInstance().printLog(message);
			if (ok == JOptionPane.YES_OPTION) {
				Desktop.getDesktop().open(new File(Main.LISTAS_DIR));
				Desktop.getDesktop().open(new File(Main.LISTAS_DIR + title));
			}

		} catch (DocumentException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			Log.getInstance()
					.printLog("Erro ao salvar PDF do relat�rio financeiro - " + descricao + " - " + e.getMessage());
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel criar o PDF do Relat�rio Financeiro!\n"
							+ "Se tiver um relat�rio aberto, por favor feche e tente novamente!",
					"Criar Relat�rio Financeiro - Erro", JOptionPane.OK_OPTION,
					new ImageIcon(DataGui.getInstance().getClass().getResource("/DAD_SS.jpg")));
			e.printStackTrace();
		}

		return title;
	}

	public static Image createImage(JComponent component) throws BadElementException, IOException {

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
				Paragraph headerLeft = new Paragraph("Relat�rio Financeiro - " + descricaoS,
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
				image = Image.getInstance(DataGui.getInstance().getClass().getResource("/FC.jpg"));
				image.setAlignment(Element.ALIGN_CENTER);
				image.setAbsolutePosition(296, 15);
				image.scalePercent(0.8f);
				writer.getDirectContent().addImage(image, true);
			} catch (IOException | DocumentException e) {
				// TODO Auto-generated catch block
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
