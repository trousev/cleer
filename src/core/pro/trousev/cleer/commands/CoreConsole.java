package pro.trousev.cleer.commands;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import pro.trousev.cleer.Console;
import pro.trousev.cleer.Plugin;
import pro.trousev.cleer.Plugin.Command;
import pro.trousev.cleer.Plugin.Interface;



public class CoreConsole implements Console 
{
	public CoreConsole()
	{
		register( new Exit() );
		register(new Help());
		register (new Library());
		register(new BasicPlayerControl.Next());
		register(new BasicPlayerControl.Previous());
		register(new BasicPlayerControl.Stop());
		register(new BasicPlayerControl.Play());
		register(new BasicPlayerControl.ListPlaylist());
		register(new BasicPlayerControl.ClearQueue());
		register(new BasicPlayerControl.Jump());
		register(new BasicLibraryManagement.Search());
		register(new BasicLibraryManagement.Shuffle());
		register(new BasicLibraryManagement.Focus());
		register(new BasicLibraryManagement.Enqueue());
		register(new SystemCommands.GarbageCollection());
		register(new SystemCommands.PerformTests());
		register(new RatingManagement.Rate());
	}
	static class CommandNotFoundException extends Console.CommandNotFoundException
	{
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
