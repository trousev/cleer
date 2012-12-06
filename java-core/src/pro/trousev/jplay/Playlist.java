package pro.trousev.jplay;

import java.util.List;

public interface Playlist {
	enum Type
	{
		SmartPlaylist,
		PredefinedPlaylist
	}
	/**
	 * Возвращает название плейлиста
	 * @return
	 */
	String title();
	/**
	 * Запрос, которым был сгенерирован плейлист
	 * @return
	 */
	String query();
	
	/**
	 * Содержимое плейлиста. Может быть ленивым.
	 * @return
	 */
	List<Track> contents();
	
	/**
	 *  Возвращает тип плейлиста
	 */
	Type type();
	
	/**
	 * Трансформирует плейлист в плейлист другого типа и возвращает трансформированный плейлист
	 */
	Playlist setType(Type new_type);
	

	/**
	 * Сохраняет плейлист, что бы это ни значило
	 * Скорее всего, имеется в виду сохранение плейлиста в MediaLibrary
	 */
	boolean save(String name);
}
