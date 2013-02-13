package pro.trousev.jplay.desktop;

import java.io.IOException;
import java.io.InputStream;

import pro.trousev.jplay.Player;
import pro.trousev.jplay.Track;

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
				// TODO Auto-generated catch block
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
			/*try {
				this.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
	};
	Subprocess subprocess;
	SongState current_state;
	Status current_status = Status.Closed;
	Track track;
	@Override
	public void open(Track track, SongState state) {
		close();
		String[] args = new String[2];
		args[0] = "mplayer";
		if(track == null)
			return ;
		args[1] = track.filename().getAbsolutePath();
		current_state = state;
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
		current_state = null;
		current_status = Status.Closed;
	}

	@Override
	public void play() {
		if(subprocess == null || current_state == null)
			return ;
		if(current_status == Status.Playing || current_status == Status.Paused)
			stop(Reason.UserBreak);
		subprocess.start();
		current_state.started(track);
		current_status = Status.Playing;
	}

	@Override
	public void stop(Reason reason) {
		//System.out.println("Stop call, prev.status: "+current_status+", reason: "+reason);
		if(current_status == Status.Stopped || current_status == Status.Closed)
			return ;
		subprocess.kill();
		current_state.finished(reason);
		current_status = Status.Stopped;
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Track now_playing() {
		return track;
	}

	@Override
	public Status getStatus() {
		return current_status;
	}
}
