package pro.trousev.jplay.sys;

import java.util.ArrayList;
import java.util.List;

import pro.trousev.jplay.Player;
import pro.trousev.jplay.Player.Error;
import pro.trousev.jplay.Player.SongState;
import pro.trousev.jplay.Queue;
import pro.trousev.jplay.Track;

public class QueueImpl implements Queue {

	private Player player;
	private List<Track> queue = null;
	private int current = 0;
	private static class Reactor implements Player.SongState
	{
		Player player;
		QueueImpl queue;
		Reactor(Player pl, QueueImpl q)
		{
			player = pl;
			queue = q;
		}
		@Override
		public void started() {
			System.out.println("Started song.");
		}

		@Override
		public void finished(Player.Reason reason) {
			System.out.println("Stopped: "+reason);
			if(reason == Player.Reason.EndOfTrack)
			{
				System.out.println("Next");
				queue.next();
			}
		}

		@Override
		public void error(Error errorCode, String errorMessage) {
			
			
		}

		@Override
		public void paused() {
			
		}

		@Override
		public void resumed() {
			
		}

		@Override
		public void destroyed() {
			
		}
	}
	Reactor reactor;
	
	
	
	public QueueImpl(Player player)
	{
		this.player = player;
		queue = new ArrayList<Track>();
		reactor = new Reactor(this.player, this);
	}
	@Override
	public List<Track> queue() {
		return queue;
	}

	@Override
	public int size() {
		return queue.size();
	}

	@Override
	public int playing_index() {
		return current;
	}

	@Override
	public Track playing_track() {
		try
		{
			return queue.get(current);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	@Override
	public void clear() {
		player.stop(Player.Reason.UserBreak);
		player.close();
		queue.clear();
		current = 0;
	}

	@Override
	public void enqueue(List<Track> tracks, EnqueueMode mode) {
		if(mode == EnqueueMode.AfterAll)
		{
			queue.addAll(tracks);
		}
		if(mode == EnqueueMode.AfterCurrent)
		{
			int s = current+1;
			if(s>=size())
				s = size();
			queue.addAll(s, tracks);
		}
		if(mode == EnqueueMode.Immidiaely)
		{
			if(size()!=0)
				queue.addAll(current, tracks);
			else
				queue.addAll(tracks);
			player.stop(Player.Reason.UserBreak);
			player.open(playing_track(), reactor);
			player.play();
		}
		if(mode == EnqueueMode.ReplaceAll)
		{
			clear();
			enqueue(tracks, EnqueueMode.Immidiaely);
		}
	}

	@Override
	public void enqueue(Track track, EnqueueMode mode) {
		List<Track> l = new ArrayList<Track>();
		l.add(track);
		enqueue(l, mode);
	}

	@Override
	public void enqueue(Track track) {
		enqueue(track, EnqueueMode.Immidiaely);
	}
	@Override
	public SongState queue_song_state() {
		return reactor;
	}
	
	@Override 
	public boolean next()
	{
		return seek(+1);
	}
	@Override
	public boolean prev() {
		return seek(-1);
	}
	@Override
	public boolean play() {
		return seek(0);
	}
	@Override
	public boolean pause() {
		player.pause();
		return true;
	}
	@Override
	public boolean resume() {
		player.resume();
		return true;
	}
	@Override
	public boolean seek(int index) {
		current += index;
		if(current >= size()) return false;
		if(current <0 ) return false;
		player.stop(Player.Reason.UserBreak);
		player.open(playing_track(), reactor);
		player.play();
		System.out.println("Now playing: "+playing_track());
		return true;
	}
}
