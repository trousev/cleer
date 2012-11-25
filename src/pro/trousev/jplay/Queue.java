package pro.trousev.jplay;

import java.util.List;
import pro.trousev.jplay.Track;
public interface Queue {
	/**
	 * Тип добавления в очередь.
	 * @author doctor
	 *
	 */
	enum EnqueueMode 
	{ 
		/**
		 * Песня(и) добавляется после текущей. Текущая песна останавливается, начинает играть новая
		 */
		Immidiaely, 
		/**
		 * Песня(и) добавляется после текущей. Текущее исполнение не прерывается.
		 */
		AfterCurrent, 
		/**
		 * Песня(и) добавляется после всех в данной очереди
		 */
		AfterAll, 
		/**
		 * Очередь очищается и заполняется песнями(ей). Исполнение переключается на новую очередь.
		 */
		ReplaceAll 
	}
	List<Track> queue();
	// Now playing handlers
	int playing_index();
	Track playing_track();
	Track playing_count();
	// Management
	public void clear();
	public void enqueue(List<Track> tracks,EnqueueMode mode);
	public void enqueue(Track track,EnqueueMode mode);
	public void enqueue(Track track);
}
