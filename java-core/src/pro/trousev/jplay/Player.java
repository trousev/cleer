package pro.trousev.jplay;

public interface Player  {
	
	enum Error
	{
		FileNotFound
	}
	enum Status
	{
		Closed,Stopped,Playing,Paused
	}
	enum Reason
	{
		UserBreak, EndOfTrack, PlayerError, SystemBreak
	}
	public interface SongState
	{
		/**
		 * Вызывается после того, как песня начала играть.
		 */
		void started(Player sender, Track track);
		/**
		 * Вызывается после того, как песна закончила играть
		 * @param reason Строковый комментарий, посвященный ошибке, если таковая была. Может быть null
		 */
		void finished(Player sender, Track track, Reason reason);
		/**
		 * Вызывается, если произошла какая-то ошибка
		 * @param errorMessage
		 */
		void error(Player sender, Error errorCode, String errorMessage);
		/**
		 * Вызывается, если песню приостановили
		 */
		void paused(Player sender, Track track);
		/**
		 * Вызывается, если песню продолжили играть
		 */
		void resumed(Player sender, Track track);
		/**
		 * Вызывается, если песня уничтожилась и плеер ее больше не играет
		 */
		void destroyed(Player sender);
	}
	
	public void open(Track track, SongState state);
	public void close();

	public void play();
	public void stop(Reason reason);
	public void pause();
	public void resume();
	
	public Track now_playing();
	Status getStatus();
	
}
