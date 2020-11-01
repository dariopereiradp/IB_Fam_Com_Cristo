package dad.recursos;

import java.time.LocalDate;

/**
 * Classe que retorna a data num formato String pesquisavel e ao mesmo tempo
 * permite ordenação
 * 
 * @author dariopereiradp
 *
 */
public class DataPesquisavel implements Comparable<DataPesquisavel> {

	private LocalDate data;

	public DataPesquisavel(LocalDate data) {
		this.data = data;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return Utils.getInstance().getDateFormat().format(data);
	}

	@Override
	public int compareTo(DataPesquisavel o) {
		return data.compareTo(o.getData());
	}

}
