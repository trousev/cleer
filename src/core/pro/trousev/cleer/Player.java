package pro.trousev.cleer;


public interface Player  {
	
	enum Error
	{
		NoError,FileNotFound
	}
	enum Status
	{
		Closed, Stopped, Playing, Paused, Processing, Error
	}
	enum Reason
	{
		UserBreak, EndOfTrack, PlayerError, SystemBreak
	}
	public static class PlayerChangeEvent implements Messaging.Message
	{
		public Player sender;
		public Item track;
		public Reason reason;
		public Status status;
		public Error error;
	}
	
	public void open(Item track) throws PlayerException;
	public void close();

	public void play();
	public void stop(Reason reason);
	public void pause();
	public void resume();
	
	public Item now_playing();
	Status getStatus();
	
	public int getCurrentPosition();
	public int getDuration();
	public void setCurrentPosition(int msec);
	
	
	public static class PlayerException extends Exception {
		private static final long serialVersionUID = -23891433149501L; //user defined
		
		public PlayerException (String reason) {
			super(String.format("Player exception: ", reason));
		}
	}
}
