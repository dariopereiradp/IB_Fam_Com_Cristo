package dad.recursos.pdf;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
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
import dad.recursos.Log;
import dad.recursos.Utils;

/**
 * Classe que gera um PDF da tabela de membros, com os filtros indicados.
 * 
 * @author D�rio Pereira
 *
 */
public class TableMembrosToPDF {

	private static String descricaoS;

	/**
	 * Gera um PDF da tabela, de acordo com o filtro pretendido
	 * 
	 * @param table     tabela que se pretende converter em PDF
	 * @param descricao s�o v�lidos como descri��o: <br>
	 *                  Batizados<br>
	 *                  Ativos<br>
	 *                  Nominais<br>
	 *                  Congregados<br>
	 *                  L�deres<Todos>
	 * @return o nome do ficheiro PDF criado
	 */
	public static String membrosToPDF(JTable table, String descricao) {
		descricaoS = descricao;
		String title = Main.SIGLA + "_Lista de Membros_" + descricao + "_"
				+ DateTimeFormatter.ofPattern("ddMMMyyyy").format(LocalDate.now()) + ".pdf";
		try {
			Document doc = new Document(PageSize.A4, 10, 10, 50, 50);

			PdfWriter.getInstance(doc, new FileOutputStream(Main.LISTAS_DIR + title))
					.setPageEvent(new HeaderFooterPageEvent());
			doc.open();
			doc.addSubject(Main.SIGLA + " - Lista de Membros - " + descricao);
			doc.addTitle(title);
			doc.addAuthor(Main.TITLE_SMALL);
			Font fonteTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
			Image logo = Image.getInstance(TableMembrosToPDF.class.getResource("/FC.jpg"));
			logo.scalePercent(3.5f);

			logo.setAlignment(Element.ALIGN_CENTER);
			doc.add(logo);
			doc.add(new Paragraph(" "));
			Paragraph titleP = new Paragraph(Main.TITLE_SMALL, fonteTitle);
			titleP.setAlignment(Element.ALIGN_CENTER);
			doc.add(titleP);
			doc.add(new Paragraph(" "));
			Paragraph subTitle = new Paragraph("Lista de Membros - " + descricao,
					new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
			subTitle.setAlignment(Element.ALIGN_CENTER);
			doc.add(subTitle);
			doc.add(new Paragraph(" "));
			doc.add(new Paragraph(" "));

			PdfPTable pdfTable = new PdfPTable(table.getColumnCount());
			pdfTable.setWidths(new int[] { 290, 150, 160, 150 });
			// adding table headers
			for (int i = 0; i < table.getColumnCount(); i++) {
				Paragraph header;
				if (i != 1)
					header = new Paragraph(table.getColumnName(i).toUpperCase(),
							FontFactory.getFont(FontFactory.COURIER_BOLD, 12));
				else
					header = new Paragraph("D/NASC", FontFactory.getFont(FontFactory.COURIER_BOLD, 12));
				PdfPCell cell = new PdfPCell(header);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				pdfTable.addCell(cell);
			}
			// extracting data from the JTable and inserting it to PdfPTable
			for (int rows = 0; rows < table.getRowCount(); rows++) {
				for (int cols = 0; cols < table.getColumnCount(); cols++) {
					if (table.getModel().getValueAt(table.convertRowIndexToModel(rows), cols).toString()
							.equals("(00) 0 0000-0000"))
						pdfTable.addCell("");
					else {
						Paragraph p = new Paragraph(
								table.getModel().getValueAt(table.convertRowIndexToModel(rows), cols).toString(),
								FontFactory.getFont(FontFactory.COURIER, 9));
						PdfPCell cell = new PdfPCell(p);
						cell.setHorizontalAlignment(Element.ALIGN_CENTER);
						pdfTable.addCell(cell);
					}
				}
			}
			doc.add(pdfTable);
			doc.add(new Paragraph(" "));
			
			PdfPTable assin = new PdfPTable(1);
			assin.setWidthPercentage(45f);
			assin.setHorizontalAlignment(Element.ALIGN_RIGHT);
			Paragraph assinatura = new Paragraph("___________________________________________",
					FontFactory.getFont(FontFactory.COURIER, 9));
			PdfPCell cellOne = new PdfPCell(assinatura);
			cellOne.setBorder(Rectangle.NO_BORDER);
			assin.addCell(cellOne);

			Paragraph secretario = new Paragraph(("(Secret�rio)"), FontFactory.getFont(FontFactory.TIMES, 10));
			PdfPCell secretarioCell = new PdfPCell(secretario);
			secretarioCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			secretarioCell.setBorder(Rectangle.NO_BORDER);
			assin.addCell(secretarioCell);
			
			doc.add(new Paragraph(" "));
			doc.add(assin);

			doc.close();

			String message = "A Lista de Membros " + descricao
					+ " foi criada com sucesso!\nFoi salvo um documento PDF (que pode ser impresso) na pasta:\n"
					+ Main.LISTAS_DIR + "\nVoc� quer abrir o documento agora?";
			Utils.askMessage(message, Main.LISTAS_DIR, Main.LISTAS_DIR + title);

		} catch (DocumentException ex) {
			ex.printStackTrace();
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException e) {
			Log.getInstance()
					.printLog("Erro ao salvar PDF da lista de membros - " + descricao + " - " + e.getMessage());
			JOptionPane.showMessageDialog(null,
					"N�o foi poss�vel criar o PDF da Lista de Membros!\n"
							+ "Se tiver uma Lista de Membros aberta, por favor feche e tente novamente!",
					"Criar Lista de Membros - Erro", JOptionPane.OK_OPTION,
					new ImageIcon(TableMembrosToPDF.class.getResource("/FC_SS.jpg")));
			e.printStackTrace();
		}

		return title;
	}

	private static class HeaderFooterPageEvent extends PdfPageEventHelper {

		public void onStartPage(PdfWriter writer, Document document) {
			if (document.getPageNumber() != 1) {
				Paragraph headerLeft = new Paragraph("Lista de Membros - " + descricaoS,
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
				image = Image.getInstance(TableMembrosToPDF.class.getResource("/FC.jpg"));
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
