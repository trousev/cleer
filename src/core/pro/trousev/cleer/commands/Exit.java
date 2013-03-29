package pro.trousev.cleer.commands;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Plugin.Interface;

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
		try {
			iface.storage().close();
		} catch (DatabaseError e) {
			// не закрывается  и фиг бы с ней
		}
		System.exit(0);
		return true;
	}
}
