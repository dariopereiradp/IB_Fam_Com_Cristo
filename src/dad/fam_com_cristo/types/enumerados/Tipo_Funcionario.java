package dad.fam_com_cristo.types.enumerados;

public enum Tipo_Funcionario {
	
	PASTOR ("Pastor"), TESOUREIRO ("Tesoureiro"), SECRETARIO ("Secretário");
	
	private String descricao;

	Tipo_Funcionario(String descricao) {
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
	 * @param descricao descrição exata do enumerado {@link #getDescricao()}
	 * @return o enumerado correspondente, se houver; caso contrário, lança uma
	 *         IllegalArgumentException
	 */
	public static Tipo_Funcionario getEnum(String descricao){
		for(Tipo_Funcionario tipo : values())
            if(tipo.getDescricao().equals(descricao)) return tipo;
        throw new IllegalArgumentException("A descrição dada não corresponde a nenhum enumerado!");
	}

}
