package pro.trousev.cleer.sys;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pro.trousev.cleer.Player;
import pro.trousev.cleer.Queue;
import pro.trousev.cleer.Track;
import pro.trousev.cleer.Player.Error;
import pro.trousev.cleer.Player.SongState;
import pro.trousev.cleer.Player.Status;

public class QueueImpl implements Queue {

	private Player player;
	private List<Track> queue = null;
	private int current = 0;
	private static class Reactor implements Player.SongState
	{
		//Player player;
		QueueImpl queue;
		Reactor(Player pl, QueueImpl q)
		{
			//player = pl;
			queue = q;
		}
		@Override
		public void started(Player sender, Track track) {
			System.out.println("\nNow playing: "+track);
		}

		@Override
		public void finished(Player sender, Track track,  Player.Reason reason) {
			// System.out.println("Finished playing, reason: "+reason);
			if(reason == Player.Reason.EndOfTrack)
			{
				//System.out.println("Playing next song...");
				queue.next();
			}
		}

		@Override
		public void error(Player sender, Error errorCode, String errorMessage) {
			
			
		}

		@Override
		public void paused(Player sender, Track track) {
			
		}

		@Override
		public void resumed(Player sender, Track track) {
			
		}

		@Override
		public void destroyed(Player sender) {
			
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
		return true;
	}
	@Override
	public void shuffle() {
		Random r = new Random();
		int nti = -1;
		if(player.getStatus() == Status.Playing)
			nti = playing_index();
		if(queue == null)
			return ;
		int N = queue.size();
		for(int i=0; i<N*5; i++)
		{
			
			int first = r.nextInt(N);
			int second = r.nextInt(N);
			if(first == second) continue;
			if (first == nti || second == nti) continue;
			Track F = queue.get(first);
			Track S = queue.get(second);
			queue.set(first, S);
			queue.set(second, F);
		}
	}
}
