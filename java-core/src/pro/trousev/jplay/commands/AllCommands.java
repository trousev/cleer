package pro.trousev.jplay.commands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import pro.trousev.jplay.Console;
import pro.trousev.jplay.Plugin;
import pro.trousev.jplay.Plugin.Command;
import pro.trousev.jplay.Plugin.Interface;



public class AllCommands implements Console 
{
	public AllCommands()
	{
		register( new Exit() );
		register(new Help());
		register (new Library());
		register(new BasicPlayerControl.Next());
		register(new BasicPlayerControl.Previous());
		register(new BasicPlayerControl.Stop());
		register(new BasicPlayerControl.Play());
		register(new BasicLibraryManagement.Search());
	}
	static class CommandNotFoundException extends Console.CommandNotFoundException
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1657397048309882125L;

		public CommandNotFoundException(String message) {
			super(message);
		}
	}
	public List<Command> _commands = new ArrayList<Plugin.Command>();
	public void register(Plugin.Command cmd)
	{
		_commands.add(cmd);
	}
	@Override
	public List<Command> commands() {
		return _commands;
	}

	@Override
	public Command command(String name) {
		for(Command c: commands())
			if(c.name().equals(name))
				return c;
		return null;
	}
	public boolean addCommand(Command cmd)
	{
		_commands.add(cmd);
		return true;
	}
	@Override
	public void invoke(String command, List<String> args, PrintStream writer,
			Interface iface) throws CommandNotFoundException {
		Command cmd = command(command);
		if(cmd == null)
			throw new CommandNotFoundException("No such command: "+command);
		cmd.main(args, writer, iface);
	}
}
