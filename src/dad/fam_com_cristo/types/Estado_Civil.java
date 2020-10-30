package dad.fam_com_cristo.types;

/**
 * Enumerado que representa os tipos de estado civil existentes.
 * @author Dário Pereira
 *
 */
public enum Estado_Civil {
	

	SOLTEIRO ("Solteiro (a)"),
	CASADO ("Casado (a)"),
	DIVORCIADO ("Divorciado (a)"),
	VIUVO ("Viúvo (a)"),
	UNIAO ("União");
	

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
	
	public static Estado_Civil getEnum(String descricao){
		for(Estado_Civil estado_civil : values())
            if(estado_civil.getDescricao().equals(descricao)) return estado_civil;
        throw new IllegalArgumentException("A descrição dada não corresponde a nenhum enumerado!");
	}
}
