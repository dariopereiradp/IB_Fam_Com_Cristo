package dad.recursos.pdf;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import com.qoppa.pdfWriter.PDFDocument;
import com.qoppa.pdfWriter.PDFGraphics;
import com.qoppa.pdfWriter.PDFPage;

import dad.fam_com_cristo.Main;
import dad.recursos.Utils;

/**
 * Classe para gerar um PDF da ficha de membro vazia.<br>
 * Library <a href="https://www.qoppa.com/pdfwriter/">jPDFWriter</a>
 * 
 * @author Dário Pereira
 *
 */
public class FichaMembro_VaziaToPDF {

	public PDFDocument generatePDF() {
		PDFDocument pdfDoc = new PDFDocument();

		double width = 8.3 * 72;
		double height = 11.7 * 72;

		Paper p = new Paper();
		p.setSize(width, height);
		p.setImageableArea(0, 0, 8.3 * 72, 11.7 * 72);
		PageFormat pf = new PageFormat();
		pf.setPaper(p);

		PDFPage page = pdfDoc.createPage(pf);
		pdfDoc.addPage(page);

		PDFGraphics g2d = (PDFGraphics) page.createGraphics();

		ImageIcon fc_icon;
		fc_icon = new ImageIcon(getClass().getResource("/FC.jpg"));
		g2d.drawImage(fc_icon.getImage(), (int) (width / 2) - fc_icon.getIconWidth() / 54, 40,
				fc_icon.getIconWidth() / 27, fc_icon.getIconHeight() / 27, null);

		g2d.setFont(new Font("Matura MT Script Capitals", Font.PLAIN, 20));
		g2d.setColor(Color.BLACK);
		String text = Main.TITLE_SMALL;
		int sWidth = g2d.getFontMetrics(new Font("Matura MT Script Capitals", Font.PLAIN, 20)).stringWidth(text);
		g2d.drawString(text, (int) (width / 2) - sWidth / 2, 160);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(15f).deriveFont(Font.BOLD));
		text = "FICHA DE MEMBRESIA";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(15f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 2) - sWidth / 2, 200);

		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(Color.black);
		g2d.drawRoundRect((int) width / 8, 220, (int) ((width / 8) * 6), 420, 10, 10);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		text = "INFORMAÇÕES PESSOAIS";
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 15);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Nome: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 30 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "__________________________________________________________________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 30 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Data de Nascimento: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 45 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "_____/_____/________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 45 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Sexo: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 45 + 15 + 5 + 6);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "____________________________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 45 + 15 + 5 + 6);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Estado Civil: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 60 + 15 + 5 + 7);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "____________________________";
		;
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 60 + 15 + 5 + 7);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Profissão: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 75 + 15 + 5 + 9);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "_____________________________________________________________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 75 + 15 + 5 + 9);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "--------------------------------------------------------------------------";
		g2d.drawString(text, (int) (width / 8) + 2, 220 + 90 + 15 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		text = "CONTATOS";
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 100 + 15 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Endereço: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 115 + 15 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "______________________________________________________________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 115 + 15 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Telefone: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 130 + 15 + 5 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "(_____) ____________________________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 130 + 15 + 5 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Email: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 145 + 15 + 5 + 5 + 10 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "_____________________________________________________________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 145 + 15 + 5 + 5 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "--------------------------------------------------------------------------";
		g2d.drawString(text, (int) (width / 8) + 2, 220 + 160 + 15 + 10 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		text = "VIDA CRISTÃ";
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 170 + 15 + 10 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Igreja de Origem: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 185 + 15 + 10 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "______________________________________________________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 185 + 15 + 10 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Tipo de membro: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 200 + 15 + 10 + 5 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "(    ) Membro Ativo     (    ) Membro Nominal";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 200 + 15 + 10 + 5 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "(    ) Congregado       (    ) Liderança";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 215 + 15 + 10 + 5 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Membro desde: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 230 + 15 + 10 + 5 + 5 + 10);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "_____/_____/________";
		;
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 230 + 15 + 10 + 5 + 5 + 10);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Batizado? ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 245 + 15 + 10 + 5 + 5 + 15);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "(    ) Sim       (    ) Não";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 245 + 15 + 10 + 5 + 5 + 15);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Data do batismo: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 260 + 15 + 10 + 5 + 5 + 20);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "_____/_____/________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 260 + 15 + 10 + 5 + 5 + 20);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Observações: ";
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 275 + 15 + 10 + 5 + 5 + 25);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = "____________________________________________________________";
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 275 + 15 + 10 + 5 + 5 + 25);

		String text1 = "_________________________________________________________________________";
		g2d.drawString(text1, (int) (width / 8) + 5, 220 + 290 + 15 + 10 + 5 + 5 + 30);

		g2d.drawString(text1, (int) (width / 8) + 5, 220 + 305 + 15 + 10 + 5 + 5 + 35);

		g2d.drawString(text1, (int) (width / 8) + 5, 220 + 320 + 15 + 10 + 5 + 5 + 40);

		g2d.drawString(text1, (int) (width / 8) + 5, 220 + 335 + 15 + 10 + 5 + 5 + 45);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "Nazária, " + "_______ de " + "______________________ de" + "___________";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width / 10), (int) (height - height * 2.5 / 15));
		
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));
		text = "_____________________________________________";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width / 10), (int) (height - height * 1.5 / 15) - 5);

		text = Utils.getInstance().getPastorName();
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width * 3.5 / 16) + 3, (int) (height - height * 1.5 / 15) + 15 - 5);

		text = "(Pastor Titular)";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width * 3.4 / 14) + 3, (int) (height - height * 1.5 / 15) + 15 + 15 - 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(6f).deriveFont(Font.PLAIN));
		text = "Gerado automaticamente em " + new SimpleDateFormat("dd/MMM/yyyy 'às' HH:mm:ss").format(new Date());
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(6f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width / 10), (int) (height - height / 15) + 10);

		return pdfDoc;
	}
}
