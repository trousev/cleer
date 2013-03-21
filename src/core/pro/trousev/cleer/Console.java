package pro.trousev.cleer;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.cleer.Plugin.Interface;

public interface Console {
	public static abstract class CommandNotFoundException extends Exception
	{
		private static final long serialVersionUID = -5704855769494971397L;
		public CommandNotFoundException(String message) {
			super(message);
		}
	}
	List<Plugin.Command> commands();
	Plugin.Command command(String name);
	void invoke(String command, List<String> args, PrintStream writer, Interface iface) throws CommandNotFoundException;
}
