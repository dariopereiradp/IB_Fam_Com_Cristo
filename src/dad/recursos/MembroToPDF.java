package dad.recursos;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;
import com.qoppa.pdfWriter.PDFDocument;
import com.qoppa.pdfWriter.PDFGraphics;
import com.qoppa.pdfWriter.PDFPage;

import dad.fam_com_cristo.Membro;
import dad.fam_com_cristo.gui.Login;
import dad.fam_com_cristo.gui.Main;

/**
 * Library <a href="https://www.qoppa.com/pdfwriter/">jPDFWriter</a>
 * 
 * @author D�rio Pereira
 *
 */
public class MembroToPDF {

	private Membro membro;

	public MembroToPDF(Membro membro) {
		this.membro = membro;
	}

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
		g2d.drawImage(fc_icon.getImage(), (int) (width / 2) - fc_icon.getIconWidth() / 36, 10,
				fc_icon.getIconWidth() / 18, fc_icon.getIconHeight() / 18, null);

		g2d.setFont(PDFGraphics.HELVETICA.deriveFont(20f).deriveFont(Font.BOLD));
		g2d.setColor(Color.BLACK);
		String text = Main.TITLE;
		int sWidth = g2d.getFontMetrics(PDFGraphics.HELVETICA.deriveFont(20f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 2) - sWidth / 2, 200);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(15f).deriveFont(Font.BOLD));
		text = "Empr�stimo n�" + membro.getId();
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(15f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 2) - sWidth / 2, 150);

		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(Color.black);
		g2d.drawRoundRect((int) width / 8, 220, (int) ((width / 8) * 6), 350, 10, 10);

		if (membro.getImg() != null) {

			Image image = membro.getImg().getImage().getScaledInstance(177 * 3, 236 * 3,
					Image.SCALE_AREA_AVERAGING);

			MediaTracker tracker = new MediaTracker(new java.awt.Container());
			tracker.addImage(image, 0);
			try {
				tracker.waitForAll();
			} catch (InterruptedException ex) {
				throw new RuntimeException("Image loading interrupted", ex);
			}

			g2d.drawImage(image, (int) (width / 11) * 8, 220 + 25, 177 / 3, 236 / 3, null);
		} else {
			g2d.setFont(PDFGraphics.COURIER.deriveFont(8f).deriveFont(Font.PLAIN));
			g2d.drawString(" Sem Imagem ", (int) ((width / 11) * 8), 245 + 236 / 6);
		}
		g2d.drawRect((int) (width / 11) * 8, 220 + 25, 177 / 3, 236 / 3);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		text = "DADOS DO ITEM";
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 15);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "ID do item: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 30 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = String.valueOf(membro.getId());
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 30 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "Tipo: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 45 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
//		text = membro.getItem().getTipo();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 45 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "T�tulo: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 45 + 20 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.getNome();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 45 + 20 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "Classifica��o: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 90 + 20 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
//		text = membro.getItem().getClassificacao();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 90 + 20 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		if (membro instanceof Membro) {
			text = "Autor: ";
			sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
			g2d.drawString(text, (int) (width / 8) + 5, 220 + 60 + 20 + 5);

			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
//			text = membro.getItem().getAutor();
			g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 60 + 20 + 5);
			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

			text = "Editora: ";
			sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
			g2d.drawString(text, (int) (width / 8) + 5, 220 + 75 + 20 + 5);

			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
//			text = ((Livro) membro.getItem()).getEditora();
			g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 75 + 20 + 5);
			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		}

		text = "--------------------------------------------------------------------------";
		g2d.drawString(text, (int) (width / 8) + 2, 220 + 105 + 20 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		text = "DADOS DO EMPR�STIMO";
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 115 + 20 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "ID do empr�stimo: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 130 + 20 + 10 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = String.valueOf(membro.getId());
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 130 + 20 + 10 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "Data do empr�stimo: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 145 + 20 + 10 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
//		text = new SimpleDateFormat("dd/MMM/yyyy").format(membro.getData_emprestimo());
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 145 + 20 + 10 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "Data limite para devolu��o: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 160 + 20 + 10 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
//		text = new SimpleDateFormat("EEEE', 'dd 'de' MMMM 'de' yyyy").format(membro.getData_entrega());
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 160 + 20 + 10 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "N�mero de dias: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 175 + 20 + 10 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
//		text = String.valueOf(membro.getNum_dias());
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 175 + 20 + 10 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "--------------------------------------------------------------------------";
		g2d.drawString(text, (int) (width / 8) + 2, 220 + 190 + 20 + 10 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		text = "DADOS DA PESSOA";
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 200 + 20 + 10 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "Nome: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 215 + 20 + 10 + 10 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.getNome();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 215 + 20 + 10 + 10 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "Data de Nascimento: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 230 + 20 + 10 + 10 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = new SimpleDateFormat("dd/MMM/yyyy").format(membro.getData_nascimento());
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 230 + 20 + 10 + 10 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		text = "CPF: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 245 + 20 + 10 + 10 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
//		text = String.valueOf(membro.getUser().getCpf());
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 245 + 20 + 10 + 10 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		text = "Funcion�rio: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 350 - 20);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = Login.NOME;
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 350 - 20);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		g2d.setFont(PDFGraphics.COURIER.deriveFont(9f).deriveFont(Font.PLAIN));
		text = "Aten��o: Ap�s a data limite de entrega ser� cobrado um valor de R$"
				+ " por cada dia de atraso!";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(9f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 2) - sWidth / 2, 590);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));
		text = "__________________________________________";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width / 10), (int) (height - height * 3 / 15));

		text = Login.NOME;
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width * 3.5 / 12) + 3, (int) (height - height * 3 / 15) + 15);

		text = "Naz�ria, " + new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy").format(new Date());
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width / 10), (int) (height - height * 1.5 / 15));

		g2d.setFont(PDFGraphics.COURIER.deriveFont(6f).deriveFont(Font.PLAIN));
		text = "Gerado automaticamente em " + new SimpleDateFormat("dd/MMM/yyyy '�s' HH:mm:ss").format(new Date());
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(6f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width / 10), (int) (height - height / 15));
		
		g2d.drawRoundRect((int) width / 8, 220 + 350 + 60 , 170, 170, 10, 10);
		
		g2d.setFont(PDFGraphics.COURIER.deriveFont(8f).deriveFont(Font.PLAIN));
		text = "Devolvido em: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(8f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 350 + 60 + 20);
		
		text = "_____/_____/_____";
		g2d.drawString(text, (int) (width / 8) + 5 + 3 + sWidth, 220 + 350 + 60 + 20);
		
		text = "Dias de atraso: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(8f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 350 + 60 + 45);
		
		text = "_________";
		g2d.drawString(text, (int) (width / 8) + 5 + 3 + sWidth, 220 + 350 + 60 + 45);
		
		text = "Valor pago: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(8f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 350 + 60 + 70);
		
		text = "R$_________";
		g2d.drawString(text, (int) (width / 8) + 5 + 3 + sWidth, 220 + 350 + 60 + 70);
		
		text = "Funcion�rio: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(8f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 350 + 60 + 95);
		
		text = "__________________";
		g2d.drawString(text, (int) (width / 8) + 5 + 3 + sWidth, 220 + 350 + 60 + 95);
		
		
		text = "________________________________";
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 350 + 60 + 170 - 25);
		
		text = "Assinatura";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(8f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 170/2- sWidth/2, 220 + 350 + 60 + 170 - 15);
		

		return pdfDoc;
	}
}
