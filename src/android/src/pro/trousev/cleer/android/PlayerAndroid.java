package pro.trousev.cleer.android;

import android.media.AudioManager;
import android.media.MediaPlayer;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Player;

//TODO think about callback
public class PlayerAndroid implements Player, MediaPlayer.OnPreparedListener {
	private MediaPlayer mediaPlayer = null;
	private Item currentTrack = null;
	private Status currentStatus = Status.Closed;
	private Boolean prepared = false;
	SongState state = null;
	
	public static class PlayerException extends Exception {
		private static final long serialVersionUID = -23891433149501L; //user defined
		
		public PlayerException (String reason) {
			super(String.format("Player exception: ", reason));
		}
		
	}
	
	@Override
	public void open(Item track, SongState state) throws PlayerException{
		currentTrack = track;
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		String t = currentTrack.filename().toString();
		try {
			mediaPlayer.setDataSource(t);
			currentStatus = Status.Stopped;
		} catch (Exception e) {
			mediaPlayer.release();
			currentStatus = Status.Closed;
			currentTrack = null;
			mediaPlayer = null;
			throw new PlayerException(e.getMessage());
		} finally {
			prepared = false;
		}
	}

	@Override
	public void close() {
		mediaPlayer.release();
		prepared = false;
		currentStatus = Status.Closed;
		mediaPlayer = null;
	}

	@Override
	public void play() {
		if (prepared) {
			mediaPlayer.start();
			currentStatus = Status.Playing;
		} else {
			currentStatus = Status.Processing;
			mediaPlayer.prepareAsync();
		}
	}

	@Override
	public void stop(Reason reason) {
		mediaPlayer.stop();
		prepared = false;
		currentStatus = Status.Stopped;
	}

	@Override
	public void pause() {
		mediaPlayer.pause();
		currentStatus = Status.Paused;
	}

	@Override
	public void resume() {
		this.play();
	}

	@Override
	public Item now_playing() {
		return currentTrack;
	}

	@Override
	public Status getStatus() {
		return currentStatus;
	}

	@Override
	public void onPrepared(MediaPlayer mp) {
		prepared = true;
		mediaPlayer.start();
		currentStatus = Status.Playing;
	}

}
