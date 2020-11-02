package dad.fam_com_cristo.types.enumerados;

/**
 * Enumerado que representa o tipo de membro que uma pessoa pode ser.
 * @author Dário Pereira
 *
 */
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
	
	/**
	 * Converte uma String no enumerado correspondente, se huver
	 * 
	 * @param descricao descrição exata do enumerado {@link #getDescricao()}
	 * @return o enumerado correspondente, se houver; caso contrário, lança uma
	 *         IllegalArgumentException
	 */
	public static Tipo_Membro getEnum(String descricao){
		for(Tipo_Membro tipo_membro : values())
            if(tipo_membro.getDescricao().equals(descricao)) return tipo_membro;
        throw new IllegalArgumentException("A descrição dada não corresponde a nenhum enumerado!");
	}
}
