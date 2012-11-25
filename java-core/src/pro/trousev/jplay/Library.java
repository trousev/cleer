package pro.trousev.jplay;
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
	public List<Track> playlist(String query);
	// List of user-defined playlists
	
	public String user_playlist(String name);
	public List<Track> user_playlist_content(String name);
	public List<String> user_playlist_list();
	public boolean user_playlist_save(String name, String query);
	public boolean user_playlist_remove(String name);
	
}
