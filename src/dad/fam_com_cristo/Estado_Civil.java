package dad.fam_com_cristo;

public enum Estado_Civil {
	

	SOLTEIRO ("Solteiro (a)"),
	CASADO ("Casado (a)"),
	DIVORCIADO ("Divorciado (a)"),
	VIUVO ("Vi�vo (a)"),
	UNIAO ("Uni�o");
	

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
