package dad.fam_com_cristo;

public enum Financas_Tipo {

	ENTRADA ("Entrada"),
	SAIDA ("Sa�da");
	
	private String descricao;
	
	Financas_Tipo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
	
	public static Financas_Tipo getEnum(String descricao){
		for(Financas_Tipo sexo : values())
            if(sexo.getDescricao().equals(descricao)) return sexo;
        throw new IllegalArgumentException("A descri��o dada n�o corresponde a nenhum enumerado!");
	}
	
}
