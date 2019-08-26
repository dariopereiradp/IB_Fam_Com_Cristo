package dad.recursos;

/**
 * Interface para representar um comando, com as fun��es de execute, undo e redo.
 * @author D�rio Pereira
 *
 */
public interface Command {

	void execute();

	void undo();

	void redo();

	String getName();

}
