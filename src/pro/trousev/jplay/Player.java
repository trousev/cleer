package pro.trousev.jplay;

public interface Player {
	
	enum Error
	{
		FileNotFound
	}
	enum Status
	{
		Closed,Stopped,Playing,Paused
	}
	public interface SongState
	{
		/**
		 * Вызывается после того, как песня начала играть.
		 */
		void started();
		/**
		 * Вызывается после того, как песна закончила играть
		 * @param reason Строковый комментарий, посвященный ошибке, если таковая была. Может быть null
		 */
		void finished(String reason);
		/**
		 * Вызывается, если произошла какая-то ошибка
		 * @param errorMessage
		 */
		void error(Error errorCode, String errorMessage);
		/**
		 * Вызывается, если песню приостановили
		 */
		void paused();
		/**
		 * Вызывается, если песню продолжили играть
		 */
		void resumed();
		/**
		 * Вызывается, если песня уничтожилась и плеер ее больше не играет
		 */
		void destroyed();
	}
	
	public void open(String filename, SongState state);
	public void close();

	public void play();
	public void stop();
	public void pause();
	public void resume();
	Status getStatus();
	
}
