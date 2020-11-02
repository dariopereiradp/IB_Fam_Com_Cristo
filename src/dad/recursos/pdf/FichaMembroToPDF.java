package dad.recursos.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPCellEvent;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import dad.fam_com_cristo.Main;
import dad.fam_com_cristo.types.Membro;
import dad.recursos.Log;
import dad.recursos.Utils;

/**
 * Classe para gerar uma ficha de membro em PDF
 * @author dariopereiradp
 *
 */
public class FichaMembroToPDF {

	/**
	 * Gera uma ficha de membro
	 * @param membro - membro para ser gerada a ficha; se for passado null, será gerada uma ficha vazia
	 */
	public static void membroToPdf(Membro membro) {

		DateTimeFormatter dateFormat = Utils.getInstance().getDateFormat();
		String title = membro == null ? Main.SIGLA + "- Ficha de Membro.pdf" : membro.toString() + ".pdf";

		try {

			Document doc = new Document(PageSize.A4);

			PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(Main.MEMBROS_PDF_PATH + title));
			writer.setPageEvent(new HeaderFooterPageEvent());
			doc.open();

			// Metadata
			doc.addSubject(membro == null ? Main.SIGLA + "- Ficha de Membro"
					: Main.SIGLA + " - Ficha de Membro - " + membro.toString());
			doc.addTitle(title);
			doc.addAuthor(Main.TITLE_SMALL);

			// Logo
			Image logo = Image.getInstance(FichaMembroToPDF.class.getResource("/FC.jpg"));
			logo.scalePercent(3.5f);
			logo.setAlignment(Element.ALIGN_CENTER);
			doc.add(logo);

			// Tille
			doc.add(new Paragraph(" "));
			Font fonteTitle = new Font(Font.FontFamily.TIMES_ROMAN, 20, Font.BOLD);
			Paragraph titleP = new Paragraph(Main.TITLE_SMALL, fonteTitle);
			titleP.setAlignment(Element.ALIGN_CENTER);
			doc.add(titleP);
			doc.add(new Paragraph(" "));
			Paragraph subTitle = new Paragraph("Ficha de Membro", new Font(Font.FontFamily.TIMES_ROMAN, 16, Font.BOLD));
			subTitle.setAlignment(Element.ALIGN_CENTER);
			doc.add(subTitle);

			if (membro != null && membro.getImg() != null) {

				Image image;
				image = Image.getInstance(
						membro.getImg().getImage().getScaledInstance(68, 91, java.awt.Image.SCALE_AREA_AVERAGING),
						null);
				image.setAlignment(Element.ALIGN_CENTER);
				image.setAbsolutePosition(480, 505);
				writer.getDirectContent().addImage(image, true);

			} else if (membro != null && membro.getImg() == null) {
				Rectangle rect = new Rectangle(480, 505, 548, 595);
				rect.setBorder(Rectangle.BOX);
				rect.setBorderWidth(1);
				writer.getDirectContent().rectangle(rect);

				ColumnText ct = new ColumnText(writer.getDirectContent());
				ct.setSimpleColumn(rect);
				Font catFont = FontFactory.getFont(FontFactory.COURIER, 8);
				Paragraph p = new Paragraph("Sem imagem", catFont);
				p.setAlignment(Element.ALIGN_CENTER);
				ct.addElement(new Phrase(" "));
				ct.addElement(new Phrase(" "));
				ct.addElement(p);
				ct.go();
			}

			// Table
			PdfPTable table = new PdfPTable(1);
			table.setWidthPercentage(100);
			table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			table.getDefaultCell().setCellEvent(new PdfPCellEvent() {

				@Override
				public void cellLayout(PdfPCell cell, Rectangle position, PdfContentByte[] canvases) {
					PdfContentByte cb = canvases[PdfPTable.BACKGROUNDCANVAS];
					cb.roundRectangle(position.getLeft() + 1.5f, position.getBottom() + 1.5f, position.getWidth(),
							position.getHeight() - 2, 4);
					cb.stroke();
				}
			});

			// Informacoes pessoais
			table.addCell(getCellWithoutBorders(" "));
			table.addCell(getCellWithoutBorders("INFORMAÇÕES PESSOAIS"));

			PdfPTable infoT = new PdfPTable(2);
			infoT.setWidths(new int[] { 140, 415 });
			infoT.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			infoT.addCell(getCellBoldWithSpace("Nome: "));
			infoT.addCell(getCellWithSpace(
					membro == null ? "____________________________________________________" : membro.getNome()));
			infoT.addCell(getCellBoldWithSpace("Data de nascimento:"));
			infoT.addCell(getCellWithSpace(
					membro == null ? "_____/______/_________" : membro.getData_nascimentoPesquisavel().toString()));
			infoT.addCell(getCellBoldWithSpace("Sexo: "));
			infoT.addCell(getCellWithSpace(
					membro == null ? "(   ) Masculino      (   ) Feminino" : membro.getSexo().getDescricao()));
			infoT.addCell(getCellBoldWithSpace("Estado Civil: "));
			infoT.addCell(getCellWithSpace(membro == null ? "____________________________________________________"
					: membro.getEstado_Civil_String()));
			infoT.addCell(getCellBoldWithSpace("Profissão: "));
			infoT.addCell(getCellWithSpace(
					membro == null ? "____________________________________________________" : membro.getProfissao()));

			table.addCell(infoT);
			table.addCell(getCellWithoutBorders("CONTATOS"));

			PdfPTable contactsT = new PdfPTable(2);
			contactsT.setWidths(new int[] { 140, 415 });
			contactsT.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			contactsT.addCell(getCellBoldWithSpace("Endereço: "));
			contactsT.addCell(getCellWithSpace(
					membro == null ? "_____________________________________________________" : membro.getEndereco()));
			contactsT.addCell(getCellBoldWithSpace("Telefone: "));
			contactsT.addCell(getCellWithSpace(membro == null ? "(_____) _____________________________________________"
					: membro.getPhoneString()));
			contactsT.addCell(getCellBoldWithSpace("Email: "));
			contactsT.addCell(getCellWithSpace(
					membro == null ? "_____________________________________________________" : membro.getEmail()));

			table.addCell(contactsT);
			table.addCell(getCellWithoutBorders("VIDA CRISTÃ"));

			PdfPTable vidaCrista = new PdfPTable(2);
			vidaCrista.setWidths(new int[] { 140, 415 });
			vidaCrista.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
			vidaCrista.addCell(getCellBoldWithSpace("Igreja origem: "));
			vidaCrista.addCell(getCellWithSpace(membro == null ? "_____________________________________________________"
					: membro.getIgreja_origem()));
			vidaCrista.addCell(getCellBoldWithSpace("Tipo de membro: "));
			if (membro == null) {
				vidaCrista.addCell(getCellWithSpace("(    ) Membro ativo      (    ) Membro nominal"));
				vidaCrista.addCell(getCellWithSpace(" "));
				vidaCrista.addCell(getCellWithSpace("(    ) Congregado        (    ) Liderança"));
			} else {
				vidaCrista.addCell(getCellWithSpace(membro.getTipo_membro().getDescricao()));
			}
			vidaCrista.addCell(getCellBoldWithSpace("Membro desde: "));
			vidaCrista.addCell(getCellWithSpace(
					membro == null ? "_____/______/_________" : dateFormat.format(membro.getMembro_desde())));

			vidaCrista.addCell(getCellBoldWithSpace("Batizado? "));
			vidaCrista.addCell(getCellWithSpace(
					membro == null ? "(    ) Sim        (    ) Não" : membro.eBatizado().getDescricao()));

			if (membro != null && membro.isBatizado()) {
				vidaCrista.addCell(getCellBoldWithSpace("Data do batismo: "));
				vidaCrista.addCell(getCellWithSpace(dateFormat.format(membro.getMembro_desde())));
			} else if (membro == null) {
				vidaCrista.addCell(getCellBoldWithSpace("Data do batismo: "));
				vidaCrista.addCell(getCellWithSpace("_____/______/_________"));
			}

			vidaCrista.addCell(getCellBoldWithSpace("Observações: "));
			if (membro == null) {
				vidaCrista.addCell(getCellWithSpace("_______________________________________________________"));
				vidaCrista.addCell(getCellWithSpace("__________________"));
				vidaCrista.addCell(getCellWithSpace("_______________________________________________________"));
				vidaCrista.addCell(getCellWithSpace("__________________"));
				vidaCrista.addCell(getCellWithSpace("_______________________________________________________"));
			} else {
				vidaCrista.addCell(getCellWithSpace(membro.getObservacoes()));
			}
			table.addCell(vidaCrista);

			doc.add(table);
			doc.add(new Paragraph(" "));

			// Data
			PdfPTable dataT = new PdfPTable(1);
			if (membro == null)
				dataT.setTotalWidth(320);
			else
				dataT.setTotalWidth(220);
			Paragraph data_line = new Paragraph(
					membro == null ? "Nazária, " + "______ de " + "_____________________ de" + "_________"
							: "Nazária, "
									+ DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy").format(LocalDate.now()),
					FontFactory.getFont(FontFactory.COURIER, 10));
			PdfPCell cellData = new PdfPCell(data_line);
			cellData.setBorder(Rectangle.NO_BORDER);
			dataT.addCell(cellData);

			// Assinatura
			PdfPTable assinatura = new PdfPTable(1);
			assinatura.setTotalWidth(220);

			Paragraph assinatura_line = new Paragraph("_______________________________________",
					FontFactory.getFont(FontFactory.COURIER, 9));
			PdfPCell cellOne = new PdfPCell(assinatura_line);
			cellOne.setBorder(Rectangle.NO_BORDER);
			assinatura.addCell(cellOne);

			Paragraph pastor = new Paragraph(Utils.getInstance().getPastorName(),
					FontFactory.getFont(FontFactory.TIMES, 10));
			PdfPCell pastorCell = new PdfPCell(pastor);
			pastorCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pastorCell.setBorder(Rectangle.NO_BORDER);
			assinatura.addCell(pastorCell);

			Paragraph pastorF = new Paragraph(("(Pastor)"), FontFactory.getFont(FontFactory.TIMES, 10));
			PdfPCell pastorFCell = new PdfPCell(pastorF);
			pastorFCell.setHorizontalAlignment(Element.ALIGN_CENTER);
			pastorFCell.setBorder(Rectangle.NO_BORDER);
			assinatura.addCell(pastorFCell);

			assinatura.writeSelectedRows(0, -1, 315, assinatura.getTotalHeight() + doc.bottom(doc.bottomMargin()),
					writer.getDirectContent());

			int xDataT = 325;
			if (membro == null)
				xDataT = 255;

			dataT.writeSelectedRows(0, -1, xDataT,
					assinatura.getTotalHeight() + dataT.getTotalHeight() + doc.bottom(doc.bottomMargin() + 25),
					writer.getDirectContent());
			// Save
			doc.close();
			String message = membro == null ? "A ficha de membro vazia" : "A ficha do membro " + membro.getNome();
			message += " foi criada com sucesso!\nFoi salvo um documento PDF (que pode ser impresso) na pasta:\n"
					+ Main.LISTAS_DIR + "\nVocê quer abrir o documento agora?";
			Utils.askMessage(message, Main.MEMBROS_PDF_PATH, Main.MEMBROS_PDF_PATH + title);

		} catch (Exception e) {
			e.printStackTrace();
			Log.getInstance().printLog(e.getMessage());
		}
	}

	private static PdfPCell getCellWithoutBorders(String text) {
		PdfPCell cell = new PdfPCell(new Paragraph(text, FontFactory.getFont(FontFactory.COURIER_BOLD, 10)));
		cell.setBorder(PdfPCell.NO_BORDER);
		return cell;
	}

	private static PdfPCell getCellBoldWithSpace(String text) {
		PdfPCell cell = new PdfPCell(new Paragraph(text, FontFactory.getFont(FontFactory.COURIER_BOLD, 10)));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setExtraParagraphSpace(4);
		return cell;
	}

	private static PdfPCell getCellWithSpace(String text) {
		PdfPCell cell = new PdfPCell(new Paragraph(text, FontFactory.getFont(FontFactory.COURIER_OBLIQUE, 11)));
		cell.setBorder(PdfPCell.NO_BORDER);
		cell.setExtraParagraphSpace(7);
		return cell;
	}

	private static class HeaderFooterPageEvent extends PdfPageEventHelper {

		public void onEndPage(PdfWriter writer, Document document) {
			Image image;
			try {
				image = Image.getInstance(FichaMembroToPDF.class.getResource("/FC.jpg"));
				image.setAlignment(Element.ALIGN_CENTER);
				image.setAbsolutePosition(296, 15);
				image.scalePercent(0.8f);
				writer.getDirectContent().addImage(image, true);
			} catch (IOException | DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Paragraph footerLeftP = new Paragraph(
					"Gerado automaticamente em " + Utils.getInstance().getDateTimeFormat().format(LocalDateTime.now()),
					FontFactory.getFont(FontFactory.TIMES, 8));
			Phrase footerLeft = new Phrase(footerLeftP);
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT, footerLeft, 30, 30, 0);
			Paragraph footerRightP = new Paragraph("pg. " + document.getPageNumber() + "/1",
					FontFactory.getFont(FontFactory.TIMES, 8));
			Phrase footerRight = new Phrase(footerRightP);
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, footerRight, 550, 30, 0);
		}
	}

}
