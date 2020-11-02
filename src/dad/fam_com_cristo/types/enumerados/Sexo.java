package dad.fam_com_cristo.types.enumerados;

/**
 * Enumenrado que representa os tipos de sexo existentes (masculino ou feminino)
 * @author Dário Pereira
 *
 */
public enum Sexo {

	MASCULINO ("Masculino"),
	FEMININO ("Feminino");
	
	private String descricao;
	
	Sexo(String descricao) {
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
	public static Sexo getEnum(String descricao){
		for(Sexo sexo : values())
            if(sexo.getDescricao().equals(descricao)) return sexo;
        throw new IllegalArgumentException("A descrição dada não corresponde a nenhum enumerado!");
	}
	
}
