/**
 * 
 */
package dad.fam_com_cristo.types;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

import dad.fam_com_cristo.table.TableModelFinancas;

/**
 * @author dariopereiradp
 *
 */
public enum EstatisticaPeriodos {

	DESDE_SEMPRE("Desde sempre", null, null),
	MES_ATUAL("Mês atual", LocalDate.now().withDayOfMonth(1), LocalDate.now()),
	MES_ANTERIOR("Mês anterior",
			LocalDate.now().withMonth(LocalDate.now().getMonth().minus(1).getValue()).withDayOfMonth(1),
			LocalDate.now().withMonth(LocalDate.now().getMonth().minus(1).getValue())
					.with(TemporalAdjusters.lastDayOfMonth())),
	ANO_ATUAL("Ano atual", LocalDate.now().withMonth(1).withDayOfMonth(1), LocalDate.now()),
	ANO_ANTERIOR("Ano anterior", LocalDate.now().withYear(LocalDate.now().getYear() - 1).withMonth(1).withDayOfMonth(1),
			LocalDate.now().withYear(LocalDate.now().getYear() - 1).withMonth(12).withDayOfMonth(31)),
	PERSONALIZADO("Período personalizado", null, null);

	private String descricao;
	private LocalDate init, end;

	EstatisticaPeriodos(String descricao, LocalDate init, LocalDate end) {
		this.descricao = descricao;
		this.init = init;
		this.end = end;
	}

	public LocalDate getInit() {
		return init;
	}

	public LocalDate getEnd() {
		return end;
	}

	public BigDecimal getTotalEntradas() {
		return TableModelFinancas.getInstance().getTotalEntradas(init, end);
	}

	public BigDecimal getTotalSaidas() {
		return TableModelFinancas.getInstance().getTotalSaidas(init, end);
	}

	public BigDecimal getTotal() {
		return TableModelFinancas.getInstance().getTotalPeriod(init, end);
	}

	@Override
	public String toString() {
		return descricao;
	}

}
