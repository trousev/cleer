package pro.trousev.jplay.commands;

import java.io.PrintStream;
import java.util.List;

import pro.trousev.jplay.Player;
import pro.trousev.jplay.Queue;
import pro.trousev.jplay.commands.Command;
import pro.trousev.jplay.Player.Status;
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
	public static class ListPlaylist extends Command
	{
		public String name()
		{
			return "queue";
		}
		public String help(boolean is_short)
		{
			return "Lists current queue";
		}
		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) {
			Queue q = iface.queue();
			int i=0;
			int pi = q.playing_index();
			int songs_prev = 20;
			int songs_next = 20;
			for(pro.trousev.jplay.Track t: q.queue())
			{
				if(i+songs_prev < pi) continue;
				if(i-songs_next > pi) continue;

				if(i == pi)
					System.out.print(" ==> ");
				else 
					System.out.print("     ");
				System.out.println(t.toString());
				i++;
			}
			System.out.println("Number of tracks: "+q.queue().size());
			return true;
		}
	}
	public static class ClearQueue extends Command
	{

		@Override
		public String name() {
			return "clear";
		}

		@Override
		public String help(boolean is_short) {
			return "Clear current queue";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) {
			iface.queue().clear();
			return true;
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
	public static class Jump extends Command
	{

		@Override
		public String name() {
			return "jump";
		}

		@Override
		public String help(boolean is_short) {
			return "Jump to specified #id or string in playlist";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) {
			int s_index = 0;
			if(iface.player().getStatus() == Status.Playing)
				s_index = iface.queue().playing_index();
			int N = iface.queue().size();
			List<pro.trousev.jplay.Track> tracks = iface.queue().queue();
			int found = -1;
			for(int i=s_index+1; i<N; i++)
			{
				if(tracks.get(i).generate_query().contains(args.get(0)))
				{
					found = i; 
					break;
				}
			}
			if(found == -1)
			{
				for(int i=0; i<N; i++)
				{
					if(tracks.get(i).generate_query().contains(args.get(0)))
					{
						found = i; 
						break;
					}
				}
			}
			if(found == -1)
			{
				System.out.println("Can't find anything.");
				return false;
			}
			iface.queue().seek(found - iface.queue().playing_index());
			return true;
		}
	}
}
