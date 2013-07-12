package pro.trousev.cleer;

import java.util.List;

import pro.trousev.cleer.Item;

public interface Queue {
	/**
	 * Тип добавления в очередь.
	 * 
	 * @author doctor
	 * 
	 */
	enum EnqueueMode {
		/**
		 * Песня(и) добавляется после текущей. Текущая песна останавливается,
		 * начинает играть новая
		 */
		Immidiaely,
		/**
		 * Песня(и) добавляется после текущей. Текущее исполнение не
		 * прерывается.
		 */
		AfterCurrent,
		/**
		 * Песня(и) добавляется после всех в данной очереди
		 */
		AfterAll,
		/**
		 * Очередь очищается и заполняется песнями(ей). Исполнение переключается
		 * на новую очередь.
		 */
		ReplaceAll
	}

	/**
	 * @return current playback queue
	 */
	List<Item> queue();

	/**
	 * @return size of current queue
	 */
	int size();

	/**
	 * @return current song's index in queue
	 */
	int playing_index();

	/**
	 * @return now playing track
	 */
	Item playing_track();

	/**
	 * Clears current queue
	 */
	public void clear();

	/**
	 * Adds new songs to current queue
	 * 
	 * @param tracks
	 *            list of tracks
	 * @param mode
	 *            EnqueueMode object
	 */
	public void enqueue(List<Item> tracks, EnqueueMode mode);

	/**
	 * Adds new song to current queue
	 * 
	 * @param track
	 *            track to add
	 * @param mode
	 *            EnqueueMode object
	 */
	public void enqueue(Item track, EnqueueMode mode);

	/**
	 * Adds new song to current queue and start it's playback immidiately
	 * 
	 * @param track
	 *            track to add
	 */
	public void enqueue(Item track);

	/**
	 * Stops current song and plays next one.
	 * 
	 * @return true is all OK, false else
	 */
	boolean next();

	/**
	 * Stops current song and plays previous one
	 * 
	 * @return true is all OK, false else
	 */
	boolean prev();

	/**
	 * Starts playback of current song. If it's plauing not, starts it from
	 * beginning
	 * 
	 * @return
	 */
	boolean play();

	/**
	 * Pauses current playback.
	 * 
	 * @return
	 */
	boolean pause();

	/**
	 * Resumes playback after pause.
	 * 
	 * @return
	 */
	boolean resume();

	/**
	 * Seeks to chosen index in queue from current track.
	 * 
	 * @param index
	 *            song number.
	 * @return true if all OK. false else.
	 */
	boolean seek(int index);

	/**
	 * Перемешивает всю очередь, за исключением текущего индекса
	 */
	void shuffle();
	
	/**
	 * This message is sent when queue is changed.
	 */
	public static class QueueChangedMessage implements Messaging.Message {}
	public static class QueueSongChangedMessage implements Messaging.Message { public int track_number; }
}
