package dad.recursos;

import java.math.BigDecimal;

import dad.fam_com_cristo.types.enumerados.Tipo_Transacao;

/**
 * Class que representa o dinheiro (usando internament um BigDecimal) e permite ordenar e pesquisar
 * @author dariopereiradp
 *
 */
public class Money implements Comparable<Money> {

	private BigDecimal value;
	
	public Money() {
		this.value = new BigDecimal(0);
	}
	
	public Money(BigDecimal value) {
		this.value = value;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	/**
	 * 
	 * @param money
	 * @return new money with result
	 */
	public Money plus(Money money) {
		return new Money(value.add(money.value));
	}
	
	/**
	 * 
	 * @param money
	 * @return new money with result
	 */
	public Money subtract (Money money) {
		return new Money(value.subtract(money.value));
	}
	
	public Money transacao (Tipo_Transacao tipo, Money money) {
		if(tipo == Tipo_Transacao.ENTRADA)
			return this.plus(money);
		else
			return this.subtract(money);
	}
	
	public Money transacaoInversa (Tipo_Transacao tipo, Money money) {
		if(tipo == Tipo_Transacao.SAIDA)
			return this.plus(money);
		else
			return this.subtract(money);
	}
	
	public boolean eZero () {
		return value.compareTo(new BigDecimal("0")) == 0;
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
