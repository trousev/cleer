package pro.trousev.cleer;
import java.io.File;
import java.util.List;

public interface Library {
	// Folders API:
	interface FolderScanCallback
	{
		void started();
		void finished();
		void progress(int current, int maximum);
		void message(String message);
	}
	public List<File> folders();
	public boolean folder_add(File folder);
	public boolean folder_scan(File folder, FolderScanCallback callback);
	public boolean folder_remove(File folder);
	public boolean folder_scan(FolderScanCallback callback);

	// Playlist api
	/**
	 * Возвращает список треков, соответствующих данному запросу. 
	 * Это -- "самый главный метод" в смысле выдачи треков, все остальные
	 * являются оберткой к нему.
	 * @param query Запрос плейлиста
	 * @return Список инстанциированных объектов Track
	 */
	Playlist focus();
	Playlist setFocus(String playlist);
	public Playlist search(String query);
	public Playlist playlist(String query);
	public List<Playlist> playlists();
	public List<String> playlist_names();
	public boolean playlist_remove(String name);
	
	// On-the-fly update API
	public boolean update(Track t);
}
