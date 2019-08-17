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
import dad.fam_com_cristo.Tipo_Membro;
import dad.fam_com_cristo.gui.Main;

/**
 * Library <a href="https://www.qoppa.com/pdfwriter/">jPDFWriter</a>
 * 
 * @author Dário Pereira
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
		g2d.drawImage(fc_icon.getImage(), (int) (width / 2) - fc_icon.getIconWidth() / 40, 10,
				fc_icon.getIconWidth() / 20, fc_icon.getIconHeight() / 20, null);

		g2d.setFont(new Font("Matura MT Script Capitals", Font.PLAIN, 20));
		g2d.setColor(Color.BLACK);
		String text = "Igreja Batista Famílias com Cristo";
		int sWidth = g2d.getFontMetrics(new Font("Matura MT Script Capitals", Font.PLAIN, 20)).stringWidth(text);
		g2d.drawString(text, (int) (width / 2) - sWidth / 2, 160);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(15f).deriveFont(Font.BOLD));
		text = "FICHA DE MEMBRESIA";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(15f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 2) - sWidth / 2, 200);

		g2d.setStroke(new BasicStroke(1));
		g2d.setColor(Color.black);
		g2d.drawRoundRect((int) width / 8, 220, (int) ((width / 8) * 6), 350, 10, 10);

		if (membro.getImg() != null) {

			Image image = membro.getImg().getImage().getScaledInstance(177 * 3, 236 * 3, Image.SCALE_AREA_AVERAGING);

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
		text = "INFORMAÇÕES PESSOAIS";
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 15);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Nome: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 30 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.getNome();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 30 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Data de Nascimento: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 45 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = new SimpleDateFormat("dd/MM/yyyy").format(membro.getData_nascimento());
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 45 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Sexo: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 45 + 15 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.getSexo().getDescricao();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 45 + 15 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Estado Civil: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 60 + 15 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.getEstado_Civil_String();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 60 + 15 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Profissão: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 75 + 15 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.getProfissao();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 75 + 15 + 5);
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
		text = membro.getEndereco();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 115 + 15 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Telefone: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 130 + 15 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		String phone = membro.getTelefone();
		text = "(" + phone.substring(0, 2) + ") " + phone.substring(2, 3) + " " + phone.substring(3, 7) + "-"
				+ phone.substring(7);
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 130 + 15 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Email: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 145 + 15 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.getEmail();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 145 + 15 + 5 + 5);
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
		text = membro.getIgreja_origem();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 185 + 15 + 10 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Tipo de membro: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 200 + 15 + 10 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.getTipo_membro().getDescricao();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 200 + 15 + 10 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		text = "Membro desde: ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 215 + 15 + 10 + 5 + 5);

		if (membro.getTipo_membro() != Tipo_Membro.CONGREGADO) {
			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
			text = new SimpleDateFormat("dd/MM/yyyy").format(membro.getMembro_desde());
			g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 215 + 15 + 10 + 5 + 5);
			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		} else {
			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
			text = "-";
			g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 215 + 15 + 10 + 5 + 5);
			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		}

		text = "Batizado? ";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 230 + 15 + 10 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.eBatizado().getDescricao();
		g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 230 + 15 + 10 + 5 + 5);
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));

		if (membro.isBatizado()) {
			text = "Data do batismo: ";
			sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
			g2d.drawString(text, (int) (width / 8) + 5 + 250, 220 + 230 + 15 + 10 + 5 + 5);

			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
			text = new SimpleDateFormat("dd/MM/yyyy").format(membro.getData_batismo());
			;
			g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3 + 250, 220 + 230 + 15 + 10 + 5 + 5);
			g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		}

		text = "Observações: ";
		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD));
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.BOLD)).stringWidth(text);
		g2d.drawString(text, (int) (width / 8) + 5, 220 + 245 + 15 + 10 + 5 + 5);

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC));
		text = membro.getObservacoes();
		int sWidth2 = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC)).stringWidth(text);
		System.out.println(sWidth2);
		System.out.println(g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC))
				.stringWidth("Obedientes à chamada, temos servido ao Senhor, no interior "));
		if (text.length() < 60)
			g2d.drawString(text, (int) (width / 8) + 5 + sWidth + 3, 220 + 245 + 15 + 10 + 5 + 5);
		else {
			g2d.drawString(text.substring(0, 59), (int) (width / 8) + 5 + sWidth + 3, 220 + 245 + 15 + 10 + 5 + 5);
			if (text.length() < 130) {
				g2d.drawString(text.substring(59), (int) (width / 8) + 5, 220 + 260 + 15 + 10 + 5 + 5);
			} else {
				g2d.drawString(text.substring(59, 129), (int) (width / 8) + 5, 220 + 260 + 15 + 10 + 5 + 5);
				if (text.length() < 200) {
					g2d.drawString(text.substring(129), (int) (width / 8) + 5, 220 + 275 + 15 + 10 + 5 + 5);
				} else {
					g2d.drawString(text.substring(129, 199), (int) (width / 8) + 5, 220 + 275 + 15 + 10 + 5 + 5);
					if (text.length() < 270) {
						g2d.drawString(text.substring(199), (int) (width / 8) + 5, 220 + 290 + 15 + 10 + 5 + 5);
					} else {
						g2d.drawString(text.substring(199, 269), (int) (width / 8) + 5, 220 + 290 + 15 + 10 + 5 + 5);
						if (text.length() < 340) {
							g2d.drawString(text.substring(269), (int) (width / 8) + 5, 220 + 305 + 15 + 10 + 5 + 5);
						} else {
							g2d.drawString(text.substring(269, 339), (int) (width / 8) + 5,
									220 + 305 + 15 + 10 + 5 + 5);
							if (text.length() >= 340) {
								int sWidth3 = g2d
										.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.ITALIC))
										.stringWidth(text.substring(269, 339));
								g2d.drawString("...", (int) (width / 8) + 5 + sWidth3, 220 + 305 + 15 + 10 + 5 + 5);
							}
						}
					}
				}
			}
		}

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));

		g2d.setFont(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN));
		text = "____________________________________________________";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width / 10), (int) (height - height * 3 / 15));

		text = Main.PASTOR + " (pastor titular)";
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width * 3.5 / 20) + 3, (int) (height - height * 3 / 15) + 15);

		text = "Nazária, " + new SimpleDateFormat("dd 'de' MMMMM 'de' yyyy").format(new Date());
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(10f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width / 10), (int) (height - height * 1.5 / 15));

		g2d.setFont(PDFGraphics.COURIER.deriveFont(6f).deriveFont(Font.PLAIN));
		text = "Gerado automaticamente em " + new SimpleDateFormat("dd/MMM/yyyy 'às' HH:mm:ss").format(new Date());
		sWidth = g2d.getFontMetrics(PDFGraphics.COURIER.deriveFont(6f).deriveFont(Font.PLAIN)).stringWidth(text);
		g2d.drawString(text, (int) (width - sWidth - width / 10), (int) (height - height / 15));

		return pdfDoc;
	}
}
