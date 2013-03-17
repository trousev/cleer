package pro.trousev.jplay.commands;

import java.io.PrintStream;
import java.util.Collections;
import java.util.List;

import pro.trousev.jplay.Plugin;
import pro.trousev.jplay.Plugin.Interface;

public class Help extends Command{

	@Override
	public String name() {
		return "help";
	}

	@Override
	public String help(boolean is_short) {
		return "Print this help";
	}

	@Override
	public boolean main(List<String> args, PrintStream stdout, Interface iface) 
	{
		String subcommand = null;
		try
		{
			subcommand = args.get(0);
		}
		catch (Throwable e) {}
		
		if(subcommand == null)
		{
			List<Plugin.Command> commands = iface.console().commands();
			Collections.sort(commands);
			for(Plugin.Command cmd: commands)
			{
				stdout.print(cmd.name());
				for(int i=10; i>cmd.name().length(); i--)
					stdout.print(" ");
				String help = cmd.help(true);
				if(help != null)
					stdout.print(help);
				stdout.println();
			}
			stdout.println("Type 'help command' to know more about 'command'");
		}
		else
		{
			Plugin.Command cmd = null;
			try
			{
				cmd = iface.console().command(subcommand);
			} catch (Throwable e) {}
			if(cmd == null)
			{
				stdout.println("No such command: "+subcommand);
			}
			else
			{
				String help = cmd.help(false);
				if(help == null)
					stdout.println("No help aviable.");
				else
					stdout.println(help);
			}
		}
		return true;
	}
}
