package pro.trousev.cleer.commands;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.cleer.Track;
import pro.trousev.cleer.Plugin.Interface;

public class RatingManagement {
	public static class Rate extends Command
	{

		@Override
		public String name() {
			return "rate";
		}

		@Override
		public String help(boolean is_short) {
			return "Rate current song";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) {
			int rating ;
			String rated = args.get(0);
			if(rated.equals("*****")) rating = 5;
			else if(rated.equals("****")) rating = 4;
			else if(rated.equals("***")) rating = 3;
			else if(rated.equals("**")) rating = 2;
			else if(rated.equals("*")) rating = 1;
			else if(rated.equals("none")) rating = 0;
			else rating = new Integer(rated);
			Track t = iface.player().now_playing();
			t.set_user_rating(rating);
			iface.library().update(t);
			return true;
		}
		
	}
}
