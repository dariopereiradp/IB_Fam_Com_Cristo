package dad.fam_com_cristo.types.enumerados;

/**
 * Enumerado que representa os tipos de transa��o existentes: entrada ou sa�da.
 * @author D�rio Pereira
 *
 */
public enum Tipo_Transacao {

	ENTRADA ("Entrada"),
	SAIDA ("Sa�da");
	
	private String descricao;
	
	Tipo_Transacao(String descricao) {
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
	public static Tipo_Transacao getEnum(String descricao){
		for(Tipo_Transacao tipo : values())
            if(tipo.getDescricao().equals(descricao)) return tipo;
        throw new IllegalArgumentException("A descri��o dada n�o corresponde a nenhum enumerado!");
	}
	
}
