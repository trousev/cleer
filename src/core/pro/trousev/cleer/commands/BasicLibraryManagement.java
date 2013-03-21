package pro.trousev.cleer.commands;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.Queue;
import pro.trousev.cleer.Track;
import pro.trousev.cleer.Plugin.Interface;
import pro.trousev.cleer.sys.Tools;

public class BasicLibraryManagement {
	public static class Search extends Command
	{

		@Override
		public String name() {
			return "search";
		}

		@Override
		public String help(boolean is_short) {
			return "Search media library for playlist and push it into current one";
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
			Playlist ans = iface.library().search(query);
			for(Track t: ans.contents())
			{
				System.out.println(t.toString());
			}
			return true;
		}
	}
	public static class Shuffle extends Command
	{

		@Override
		public String name() {
			return "shuffle";
		}

		@Override
		public String help(boolean is_short) {
			return "Shuffles current queue";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) {
			iface.queue().shuffle();
			return true;
		}
		
	}
	public static class Focus extends Command
	{

		@Override
		public String name() {
			return "focus";
		}

		@Override
		public String help(boolean is_short) {
			return "Change library's focus";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) {

			iface.library().setFocus(args.get(0));
			//iface.library().playlist("#current");
			//iface.queue().enqueue(iface.library().search(query).contents(), Queue.EnqueueMode.ReplaceAll);
			return false;
		}
	}
	public static class Enqueue extends Command
	{
		public String name() { return "enqueue"; }
		public String help(boolean is_short) { return "Enqueue current playlist"; }
		public boolean main(List<String> args, PrintStream stdout, Interface iface)
		{
			iface.queue().enqueue(iface.library().focus().contents(), Queue.EnqueueMode.Immidiaely);
			return true;
		}
	}
}
