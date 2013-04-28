package pro.trousev.cleer;

import java.util.List;

/**
 * Database -- Интерфейс, предоставляющий функционал произвольного хранилища.
 * 
 * Хранилище представляет собой ограниченный пополняемый набор _секций_
 * 
 * Каждая секция представляет собой произвольно сортированный список объектов и
 * их поисковых контекстов. У каждого объекта должен быть уникальный
 * идентификатор. Дублирование поисковых контекстов допускается.
 * 
 * Поисковой контекст -- это строка-список ключевых слов, разделенных пробелом.
 **/
public interface Database {
	enum SearchLanguage {
		SearchDirectMatch, SearchSqlLike, SearchPyplay
	}

	/**
	 * DatabaseObject -- интерфейс, представляющий один объект Database.
	 **/
	public interface DatabaseObject {
		/**
		 * Идентификатор
		 **/
		String id();

		/**
		 * Содержимое объекта
		 **/
		String contents();

		/**
		 * Поисковой контекст.
		 **/
		String search();

		/**
		 * Метод, обновляющий объект и заменяющий и содержимое, и поисковой
		 * контекст.
		 * 
		 * Если contents или search выставлены в null, то соответствующий объект
		 * изменяться не должен.
		 * 
		 * @throws DatabaseError
		 **/
		boolean update(String contents, String search) throws DatabaseError;

		/**
		 * Обновляет только содержимое
		 **/
		boolean update_contents(String contents) throws DatabaseError;

		/**
		 * Обновляет только поисковой контекст.
		 **/
		boolean update_search(String search) throws DatabaseError;
	}

	/**
	 * Exception-имплементация для интерфейса. Только ее могут бросать методы
	 * Database.
	 * 
	 * @author doctor
	 * 
	 */
	public class DatabaseError extends Exception {
		private static final long serialVersionUID = -9219827468982827445L;
		Throwable _rethrow = null;

		public DatabaseError(Throwable reason) {
			super("Failure: " + reason.getMessage());
			_rethrow = reason;
		}

		public DatabaseError(String message) {
			super(message);
		}
	}

	/**
	 * Объявляет, что в БД будет раздел с заявленным именем
	 * 
	 * @param section
	 *            Имя раздела
	 * @return true, если все хорошо и такая секция может быть. false, в
	 *         противном случае
	 */
	public boolean declare_section(String section) throws DatabaseError;

	/**
	 * Уничтожает секцию в БД. Все объекты в секции тоже должны быть уничтожены.
	 **/
	public boolean clear_section(String section) throws DatabaseError;

	/**
	 * Создает и возвращает новый объект в соответствующую секцию в БД.
	 **/
	public DatabaseObject store(String section, String contents, String keywords)
			throws DatabaseError;

	/**
	 * DEPRECATED. Используйте search(section, query, language) вместо этого
	 * метода.
	 * 
	 * Производит поиск всех объектов в секции, соответствующих заданному
	 * запросу. Алгоритм поиска не формализован, но должен быть умным,
	 * эвристичным и т.п. Налагаются дополнительные требования на алгоритм
	 * поиска, за подробностями на trousev@yandex.ru
	 **/
	@Deprecated
	public List<DatabaseObject> search(String section, String query)
			throws DatabaseError;

	/**
	 * DEPRECATED. Используйте search(section, query, language) вместо этого
	 * метода.
	 * 
	 * Производит поиск всех объектов в секции, соответствующих заданному
	 * запросу. Алгоритм поиска не формализован, но должен быть умным,
	 * эвристичным и т.п. Налагаются дополнительные требования на алгоритм
	 * поиска, за подробностями на trousev@yandex.ru
	 **/
	public List<DatabaseObject> search(String section, String query,
			SearchLanguage language) throws DatabaseError;

	/**
	 * Уничтожает выбранный DatabaseObject
	 **/
	public boolean remove(String section, DatabaseObject object)
			throws DatabaseError;

	/**
	 * Закрывает БД. Все последующие соединения могут возвращать что угодно и
	 * бросать exception-ы.
	 **/
	void close() throws DatabaseError;

	/**
	 * Старт транзакции
	 * 
	 * @return
	 */
	public boolean begin() throws DatabaseError;

	/**
	 * Коммит транзакции
	 * 
	 * @return
	 */
	public boolean commit() throws DatabaseError;

	/**
	 * Откат транзакции
	 * 
	 * @return
	 */
	public boolean rollback() throws DatabaseError;
}
