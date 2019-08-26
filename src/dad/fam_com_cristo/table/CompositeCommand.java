package dad.fam_com_cristo.table;

import dad.recursos.Command;

/**
 * Classe que representa um comando composto por v�rios outros comandos.
 * @author D�rio Pereira
 *
 */
public class CompositeCommand implements Command {

	private Command[] commands;
	private String name;

	public CompositeCommand(String name, Command... commands) {
		this.name = name;
		this.commands = commands;
	}

	@Override
	public void execute() {
		for (Command command : commands)
			command.execute();
	}

	@Override
	public void undo() {
		for (int i = commands.length - 1; i >= 0; i--) {
			Command command = commands[i];
			command.undo();
		}

	}

	@Override
	public void redo() {
		execute();
	}

	@Override
	public String getName() {
		return name;
	}

}