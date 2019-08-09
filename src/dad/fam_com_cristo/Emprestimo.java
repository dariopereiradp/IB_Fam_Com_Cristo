package dad.fam_com_cristo;

import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import com.qoppa.pdfWriter.PDFDocument;

import dad.recursos.PDFGenerator;

/**
 * 
 * @author D�rio Pereira
 *
 */
public class Emprestimo {

	public static double MULTA = 0.5;
	public static int countId = 0;
	private int id;
	private User user;
	private Item item;
	private Date data_emprestimo;
	private Date data_entrega;
	private int num_dias;
	private boolean entregue, pago;
	private String funcionario;

	public Emprestimo(User user, Item item, Date data_emprestimo, Date data_entrega, String funcionario) {
		id = ++countId;
		this.user = user;
		this.item = item;
		this.data_emprestimo = data_emprestimo;
		this.data_entrega = DateUtils.truncate(data_entrega, Calendar.DAY_OF_MONTH);
		this.num_dias = Math.toIntExact(ChronoUnit.DAYS.between(data_emprestimo.toInstant(), data_entrega.toInstant()))
				+ 1;
		entregue = false;
		this.funcionario = funcionario;
		this.pago = false;
	}
	

	public static void setMULTA(double multa) {
		MULTA = multa;
	}

	public void entregar() {
		entregue = true;
		item.dec_exemp_emprestados();
	}
	
	public void pagar (){
		pago = true;
	}
	
	public boolean isPago() {
		return pago;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getData_emprestimo() {
		return data_emprestimo;
	}

	public void setData_emprestimo(Date data_emprestimo) {
		this.data_emprestimo = data_emprestimo;
	}

	public Date getData_entrega() {
		return data_entrega;
	}

	public void setData_entrega(Date data_entrega) {
		this.data_entrega = data_entrega;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getNum_dias() {
		return num_dias;
	}

	public void setNum_dias(int num_dias) {
		this.num_dias = num_dias;
	}

	public boolean isEntregue() {
		return entregue;
	}

	public void setEntregue(boolean entregue) {
		this.entregue = entregue;
	}

	public static int getCountId() {
		return countId;
	}

	public PDFDocument toPdf() {
		return new PDFGenerator(this).generatePDF();
	}

	@Override
	public String toString() {
		int endIndex = Math.min(15, item.getNome().trim().length());
		return id + "-" + item.getNome().trim().substring(0, endIndex) + "-" + item.getId() + "-" + user.getNome().split(" ")[0] + "-"
				+ new SimpleDateFormat("dd_MMM_yyyy").format(data_emprestimo);
	}

	public String getFuncionario() {
		return funcionario;
	}

	public void setFuncionario(String funcionario) {
		this.funcionario = funcionario;
	}

	public double getMulta() {
		Date hoje = new Date();
		long days = ChronoUnit.DAYS.between(data_entrega.toInstant(), hoje.toInstant());
		if (days >= 0)
			return MULTA * days;
		else return 0.0;
	}

}
