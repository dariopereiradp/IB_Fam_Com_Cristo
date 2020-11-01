package dad.recursos;

import java.math.BigDecimal;

/**
 * Class que representa o dinheiro e permite ordenar e pesquisar
 * @author dariopereiradp
 *
 */
public class Money implements Comparable<Money> {

	private BigDecimal value;
	
	public Money(BigDecimal value) {
		this.value = value;
	}
	
	

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return Utils.getInstance().getNumberFormatCurrency().format(value);
	}



	@Override
	public int compareTo(Money o) {
		return this.getValue().compareTo(o.getValue());
	}

}
