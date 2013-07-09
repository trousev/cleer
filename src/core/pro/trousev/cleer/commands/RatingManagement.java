package pro.trousev.cleer.commands;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.ReadOnlyTagException;
import pro.trousev.cleer.Plugin.Interface;
import pro.trousev.cleer.Item.NoSuchTagException;

public class RatingManagement {
	public static class Tag extends Command
	{

		@Override
		public String name() {
			return "tag";
		}

		@Override
		public String help(boolean is_short) {
			return "Assign selected tag (value) or facet (tag:value) to current (playing) song";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) throws Exception {
			Item t = iface.player().now_playing();
			if(t == null) throw new Exception("Nothing playing now. Can't rate it.");
			String q = args.get(0);
			String key;
			String value;
			if(q.contains(":"))
			{
				String[] arr = q.split(":");
				key = arr[0];
				value = arr[1];
			}
			else
			{
				key = q;
				value = q;
			}
			t.setTagValue(key, value);
			return true;
		}
		
	}
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
			Item t = iface.player().now_playing();
			try {
				t.firstTag("rating").setValue(String.format("%d", rating));
			} catch (NoSuchTagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReadOnlyTagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			iface.library().update(t);
			return true;
		}
		
	}
}
