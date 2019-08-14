package dad.fam_com_cristo;

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
}
