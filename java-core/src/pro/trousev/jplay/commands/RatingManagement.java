package pro.trousev.jplay.commands;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.jplay.Plugin.Interface;
import pro.trousev.jplay.Track;

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
			int rating = new Integer(args.get(0));
			Track t = iface.player().now_playing();
			t.set_user_rating(rating);
			iface.library().update(t);
			return true;
		}
		
	}
}
