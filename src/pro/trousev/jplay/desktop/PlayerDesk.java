package pro.trousev.jplay.desktop;

import java.io.File;
import java.io.IOException;

import pro.trousev.jplay.Player;
public class PlayerDesk implements Player
{
	abstract class ExecutorDelegate
	{
		abstract void stop(String reason);
	}
	public class Executor extends Thread {
		String _filename;
		Process _process;
		ExecutorDelegate _delegate;
		Executor(String filename, ExecutorDelegate delegate)
		{
			_filename = filename;
			_delegate = delegate;
		}
		Exception _exception = null;
		Exception getException()
		{
			return _exception;
		}
        public void run() {              
			try {
				String[] args = {"mplayer",_filename};
				_process = Runtime.getRuntime().exec(args);
				_delegate.stop(null);
			} catch (IOException e) {
				_exception  = e;
				_delegate.stop(e.getMessage());
			}
        }
        public void kill()
        {
        	_process.destroy();
        }
	}
	
	SongState current_state = null;
	File current_file = null;
	Executor mplayer = null;
	@Override
	public void open(String filename, SongState state) {
		if(current_state != null)
			close();
		current_state = state;
		current_file = new File(filename);
		if(!current_file.exists())
			state.error(Error.FileNotFound, "File not found: "+filename);
	}
	@Override
	public void close() {
		if(current_state == null)
			return ;
		stop();
		current_state.destroyed();
		current_state = null;
		current_file = null;
	}
	@Override
	public void play() {
		stop();
		current_state.started();
		ExecutorDelegate d = new ExecutorDelegate() 
		{
			@Override
			void stop(String reason) {
				// TODO Auto-generated method stub
				current_state.finished(reason);
			}
		};
		mplayer = new Executor(current_file.getAbsolutePath(),d );
		mplayer.start();
	}
	@Override
	public void stop() {
		current_state.finished("User Break");
		if(mplayer == null) return ;
		mplayer.kill();
		mplayer = null;
		
	}
	@Override
	public void pause() {
		System.out.println("Play/Pause not implemented");
	}
	@Override
	public void resume() {
		System.out.println("Play/Pause not implemented");
	}
	@Override
	public Status getStatus() {
		if(mplayer != null)
			return Status.Playing;
		if(current_state != null)
			return Status.Stopped;
		return Status.Closed;
	}
	
}
