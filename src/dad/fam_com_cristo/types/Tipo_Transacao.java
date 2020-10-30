package dad.fam_com_cristo.types;

/**
 * Enumerado que representa os tipos de transação existentes: entrada ou saída.
 * @author Dário Pereira
 *
 */
public enum Tipo_Transacao {

	ENTRADA ("Entrada"),
	SAIDA ("Saída");
	
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
	
	public static Tipo_Transacao getEnum(String descricao){
		for(Tipo_Transacao tipo : values())
            if(tipo.getDescricao().equals(descricao)) return tipo;
        throw new IllegalArgumentException("A descrição dada não corresponde a nenhum enumerado!");
	}
	
}
