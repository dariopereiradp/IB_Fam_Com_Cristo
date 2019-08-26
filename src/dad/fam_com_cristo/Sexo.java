package dad.fam_com_cristo;

/**
 * Enumenrado que representa os tipos de sexo existentes (masculino ou feminino)
 * @author D�rio Pereira
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
	
	public static Sexo getEnum(String descricao){
		for(Sexo sexo : values())
            if(sexo.getDescricao().equals(descricao)) return sexo;
        throw new IllegalArgumentException("A descri��o dada n�o corresponde a nenhum enumerado!");
	}
	
}
