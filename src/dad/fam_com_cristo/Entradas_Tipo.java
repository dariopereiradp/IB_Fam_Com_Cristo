package dad.fam_com_cristo;

public enum Entradas_Tipo {

	DIZIMO ("D�zimo"),
	OFERTA ("Oferta"),
	OUTROS ("Outros");
	
	private String descricao;
	
	Entradas_Tipo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}

	@Override
	public String toString() {
		return descricao;
	}
	
	public static Entradas_Tipo getEnum(String descricao){
		for(Entradas_Tipo sexo : values())
            if(sexo.getDescricao().equals(descricao)) return sexo;
        throw new IllegalArgumentException("A descri��o dada n�o corresponde a nenhum enumerado!");
	}
	
}
