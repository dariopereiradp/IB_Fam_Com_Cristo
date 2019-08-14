package dad.fam_com_cristo;

public enum Tipo_Membro {
	

	CONGREGADO ("Congregado"),
	MEMBRO_ATIVO ("Membro Ativo"),
	MEMBRO_NOMINAL ("Membro Nominal"),
	LIDERANCA ("Liderança"),
	EX_MEMBRO ("Ex-Membro");
	

	private String descricao;

	Tipo_Membro(String descricao) {
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
