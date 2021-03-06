package dad.fam_com_cristo.types.enumerados;

/**
 * Enumerado que representa os tipos de estado civil existentes.
 * 
 * @author D�rio Pereira
 *
 */
public enum Estado_Civil {

	SOLTEIRO("Solteiro (a)"), CASADO("Casado (a)"), DIVORCIADO("Divorciado (a)"), VIUVO("Vi�vo (a)"), UNIAO("Uni�o");

	private String descricao;

	Estado_Civil(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}

	/**
	 * Converte uma String no enumerado correspondente, se huver
	 * 
	 * @param descricao descri��o exata do enumerado {@link #getDescricao()}
	 * @return o enumerado correspondente, se houver; caso contr�rio, lan�a uma
	 *         IllegalArgumentException
	 */
	public static Estado_Civil getEnum(String descricao) {
		for (Estado_Civil estado_civil : values())
			if (estado_civil.getDescricao().equals(descricao))
				return estado_civil;
		throw new IllegalArgumentException("A descri��o dada n�o corresponde a nenhum enumerado!");
	}
}
