package pro.trousev.jplay.commands;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.jplay.Player;
import pro.trousev.jplay.commands.Command;
import pro.trousev.jplay.Plugin.Interface;

public class BasicPlayerControl {
	public static class Next extends Command
	{
		@Override
		public String name() {
			return "next";
		}

		@Override
		public String help(boolean is_short) {
			return "Play next song in queue";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) {
			if(iface.queue().playing_index() >= (iface.queue().size()-1))
			{
				stdout.println("Already at end. No next song.");
				return false;
			}
			return iface.queue().next();
		}
	}
	public static class Previous extends Command
	{

		@Override
		public String name() {
			return "prev";
		}

		@Override
		public String help(boolean is_short) {
			return "Play previous song in queue";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) 
		{
			if(iface.queue().playing_index() <= 0)
			{
				stdout.println("Already at beginning. No previous song.");
				return false;
			}
			return iface.queue().prev();
		}
	}
	public static class Stop extends Command
	{

		@Override
		public String name() {
			return "stop";
		}

		@Override
		public String help(boolean is_short) {
			return "Stops current playback";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) 
		{
			if(iface.player().getStatus() == Player.Status.Closed || iface.player().getStatus() == Player.Status.Stopped)
			{
				stdout.println("Player is already stopped\n");
				return false;
			}
			iface.player().stop(Player.Reason.UserBreak);
			return true;
		}
	}
	public static class Play extends Command
	{

		@Override
		public String name() {
			return "play";
		}

		@Override
		public String help(boolean is_short) {
			return "Starts playback of current song in queue";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) {
			if(iface.queue().playing_track() == null)
			{
				stdout.println("Can't play a song because no song in queue");
				return false;
			}
			return iface.queue().seek(0);
		}
	}
}
