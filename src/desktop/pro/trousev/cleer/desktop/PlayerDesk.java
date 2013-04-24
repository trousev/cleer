package pro.trousev.cleer.desktop;

import java.io.IOException;
import java.io.InputStream;

import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Item;

public class PlayerDesk implements Player
{
	interface SubprocessDelegate
	{
		void onCannotLaunchProcess(String message);
		void onEndOfFileReached();
		void onProcessKilled();
		void onReadingError(String message);
		void onStartedProcess();
	};
	class Subprocess extends Thread
	{
		String[] args;
		SubprocessDelegate delegate;
		Process _process;
		Boolean killed = new Boolean(false);
		public Subprocess(String[] args, SubprocessDelegate delegate) 
		{
			this.args = args;
			this.delegate = delegate;
		}
		@Override
		public void run()
		{
			try {
				_process = Runtime.getRuntime().exec(args);
			} catch (IOException e) {
				delegate.onCannotLaunchProcess(e.getMessage());
				return ;
			}
			delegate.onStartedProcess();
			InputStream is = _process.getInputStream();
			//InputStream es = _process.getErrorStream();
			byte[] buffer = new byte[1000];
			while(true)
			{
				int length = 0;
				try {
					length = is.read(buffer);
				} catch (IOException e) {
					delegate.onReadingError(e.getMessage());
					break;
				}
				if(length == -1)
				{
					synchronized (killed) 
					{
						if(killed)
							delegate.onProcessKilled();
						else
							delegate.onEndOfFileReached();
					}
					break;
				}
			}
			try {
				is.close();
				//es.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			_process.destroy();
		}
		void kill()
		{
			synchronized (killed) {
				killed = true;
			}
			_process.destroy();
		}
	};
	Subprocess subprocess;
	PlayerChangeEvent playerChangeEvent = new PlayerChangeEvent();
	Status current_status = Status.Closed;
	Item track;
	@Override
	public void open(Item track) {
		close();
		String[] args = new String[2];
		args[0] = "mplayer";
		if(track == null)
			return ;
		args[1] = track.filename().getAbsolutePath();
		this.track = track;
		subprocess = new Subprocess(args, new SubprocessDelegate() {
			
			@Override
			public void onStartedProcess() {
				
			}
			
			@Override
			public void onReadingError(String message) {
				stop(Reason.EndOfTrack);
			}
			
			@Override
			public void onEndOfFileReached() {
				stop(Reason.EndOfTrack);
			}
			
			@Override
			public void onCannotLaunchProcess(String message) {
				
			}

			@Override
			public void onProcessKilled() {
				//System.out.println("ProcessKilled");
			}
		});
		current_status = Status.Stopped;
	}

	@Override
	public void close() {
		if(current_status == Status.Closed)
			return ;
		stop(Reason.UserBreak);
		track = null;
		subprocess = null;
		current_status = Status.Closed;
	}

	@Override
	public void play() {
		if(subprocess == null)
			return ;
		if(current_status == Status.Playing || current_status == Status.Paused)
			stop(Reason.UserBreak);
		subprocess.start();
		
		playerChangeEvent.error = null;
		playerChangeEvent.reason = null;
		playerChangeEvent.sender = this;
		playerChangeEvent.status = Status.Playing;
		playerChangeEvent.track = track;
		Messaging.fire(playerChangeEvent);
		current_status = Status.Playing;
	}

	@Override
	public void stop(Reason reason) {
		//System.out.println("Stop call, prev.status: "+current_status+", reason: "+reason);
		if(current_status == Status.Stopped || current_status == Status.Closed)
			return ;
		subprocess.kill();
		current_status = Status.Stopped;
		playerChangeEvent.error = null;
		playerChangeEvent.reason = reason;
		playerChangeEvent.sender = this;
		playerChangeEvent.status = Status.Stopped;
		playerChangeEvent.track = track;
		Messaging.fire(playerChangeEvent);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public Item now_playing() {
		return track;
	}

	@Override
	public Status getStatus() {
		return current_status;
	}
}
