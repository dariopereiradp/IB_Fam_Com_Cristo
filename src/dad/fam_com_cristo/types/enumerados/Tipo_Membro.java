package dad.fam_com_cristo.types.enumerados;

/**
 * Enumerado que representa o tipo de membro que uma pessoa pode ser.
 * @author D�rio Pereira
 *
 */
public enum Tipo_Membro {
	

	CONGREGADO ("Congregado"),
	MEMBRO_ATIVO ("Membro Ativo"),
	MEMBRO_NOMINAL ("Membro Nominal"),
	LIDERANCA ("Lideran�a"),
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
	 * @param descricao descri��o exata do enumerado {@link #getDescricao()}
	 * @return o enumerado correspondente, se houver; caso contr�rio, lan�a uma
	 *         IllegalArgumentException
	 */
	public static Tipo_Membro getEnum(String descricao){
		for(Tipo_Membro tipo_membro : values())
            if(tipo_membro.getDescricao().equals(descricao)) return tipo_membro;
        throw new IllegalArgumentException("A descri��o dada n�o corresponde a nenhum enumerado!");
	}
}
