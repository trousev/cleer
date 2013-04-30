package pro.trousev.cleer.android.service;

import android.media.AudioManager;
import android.media.MediaPlayer;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Player;

//TODO think about callback
//TODO make Service for it (where?)
//TODO make state errors in PlayerException
//TODO think about headset hot removal not in this file
//TODO think how to implement volume up/down buttons using this interface
//TODO make non-silent exit on asynchronous error
//TODO: Implement more status-change messages via Messaging.fire(...).
//TODO If you encounter problem with slow preparing you should make prepare in open.

public class PlayerAndroid implements Player, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {
	private static MediaPlayer mediaPlayer = null;
	private static Item currentTrack = null;
	private static Status currentStatus = Status.Closed;
	private static Boolean prepared = false;
	private static PlayerChangeEvent changeEvent = new PlayerChangeEvent(); 
	
	@Override
	public void open(Item track) throws PlayerException {
		currentTrack = track;
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		String t = currentTrack.filename().toString();
		try {
			mediaPlayer.setDataSource(t);
			currentStatus = Status.Stopped;
			mediaPlayer.setOnPreparedListener(this);
		} catch (Exception e) {
			close();
			throw new PlayerException(e.getMessage());
		} finally {
			prepared = false;
		}
	}

	@Override
	public void close() {
		
		mediaPlayer.release();
		mediaPlayer = null;
		currentTrack = null;
		currentStatus = Status.Closed;
		prepared = false;
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
		changeEvent.error = null;
		changeEvent.reason = null;
		changeEvent.sender = this;
		changeEvent.status = currentStatus;
		changeEvent.track = currentTrack;
		Messaging.fire(changeEvent);
	}

	@Override
	public void stop(Reason reason) {
		mediaPlayer.stop();
		prepared = false;
		currentStatus = Status.Stopped;
		changeEvent.error = null;
		changeEvent.reason = null;	// FILL THIS PLEASE!
		changeEvent.sender = this;
		changeEvent.status = currentStatus;
		changeEvent.track = currentTrack;
		Messaging.fire(changeEvent);
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
	
	@Override
	public boolean onError(MediaPlayer mp, int what, int extra) {
		close();
		currentStatus = Status.Error;
		return false;
	}

}
