package pro.trousev.jplay.commands;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.jplay.Plugin.Interface;
import pro.trousev.jplay.Queue;
import pro.trousev.jplay.sys.Tools;

public class BasicLibraryManagement {
	public static class Search extends Command
	{

		@Override
		public String name() {
			return "search";
		}

		@Override
		public String help(boolean is_short) {
			return "Search media library for playlist and make in 'current' playlist in queue";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) {
			
			String query;
			try
			{
				query = Tools.StringJoin(args," ");
			}
			catch (Exception e)
			{
				stdout.println("Wrong query or no query at all");
				return false;
			}
			iface.queue().enqueue(iface.library().search(query).contents(), Queue.EnqueueMode.ReplaceAll);
			return false;
		}
		
	}
}
