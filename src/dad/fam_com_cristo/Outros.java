package dad.fam_com_cristo;

public class Outros extends Item {

	private String outrasInf;

	public Outros(String nome, String tipo) {
		super(nome, tipo);
		this.setOutrasInf("-");
	}

	public Outros(String nome, String autor, String tipo, String classificacao, String outrasInf, String local) {
		super(nome, autor, classificacao, local, null, tipo);
		if (!outrasInf.trim().equals(""))
			this.setOutrasInf(outrasInf);
		else
			this.setOutrasInf("-");

	}

	public String getOutrasInf() {
		return outrasInf;
	}

	public void setOutrasInf(String outrasInf) {
		this.outrasInf = outrasInf;
	}

}
