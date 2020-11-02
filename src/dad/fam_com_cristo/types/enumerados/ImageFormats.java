package dad.fam_com_cristo.types.enumerados;

/**
 * Enumerado que representa as extensões disponíveis para exportar imagens
 * @author dariopereiradp
 *
 */
public enum ImageFormats {
	
	JPG (".jpg"),
	PNG (".png"),
	SVG (".svg"),
	PDF (".pdf");
	
	private String format;
	
	private ImageFormats(String format) {
		this.format = format;
	}
	
	public String getFormat() {
		return format;
	}

}
