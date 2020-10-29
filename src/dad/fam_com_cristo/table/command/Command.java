package dad.fam_com_cristo.table.command;

/**
 * Interface para representar um comando, com as funções de execute, undo e redo.
 * @author Dário Pereira
 *
 */
public interface Command {

	void execute();

	void undo();

	void redo();

	String getName();

}
