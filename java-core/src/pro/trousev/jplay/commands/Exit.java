package pro.trousev.jplay.commands;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.jplay.Plugin.Interface;

public class Exit extends Command {

	@Override
	public String name() {
		return "exit";
	}

	@Override
	public String help(boolean is_short) {
		return "Exit program";
	}

	@Override
	public boolean main(List<String> args, PrintStream stdout, Interface iface) {
		iface.storage().close();
		System.exit(0);
		return true;
	}
}
