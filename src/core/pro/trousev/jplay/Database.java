package pro.trousev.jplay;

import java.util.List;


/**
  Database -- Интерфейс, предоставляющий функционал произвольного хранилища.

  Хранилище представляет собой ограниченный пополняемый набор _секций_

  Каждая секция представляет собой произвольно сортированный список объектов 
  и их поисковых контекстов. У каждого объекта должен быть уникальный идентификатор.
  Дублирование поисковых контекстов допускается.

  Поисковой контекст -- это строка-список ключевых слов, разделенных пробелом.

**/
public interface Database {
	/**
	    DatabaseObject -- интерфейс, представляющий один объект Database.
	**/
	public interface DatabaseObject
	{
		/**
                  Идентификатор
		**/
		String id();
		/**
		  Содержимое объекта
		**/
		String contents();
		/**
		  Поисковой контекст
		**/
		String search();
		/**
		  Метод, обновляющий объект и заменяющий и содержимое, и поисковой контекст. 
		  
		  Если contents или search выставлены в null, то соответствующий объект изменяться не должен.
		**/
		boolean update(String contents, String search);
		/**
		  Обновляет только содержимое
		**/
		boolean update_contents(String contents);
                /**
                  Обновляет только поисковой контекст.
                **/
		boolean update_search(String search);
	}
	/**
	 * Объявляет, что в БД будет раздел с заявленным именем
	 * @param section Имя раздела
	 * @return true, если все хорошо и такая секция может быть. false, в противном случае
	 */
	public boolean declare_section(String section);
	/**
	  Уничтожает секцию в БД. Все объекты в секции тоже должны быть уничтожены.
	**/
	public boolean clear_section(String section);
	/**
	  Создает и возвращает новый объект в соответствующую секцию в БД.
	**/
	public DatabaseObject store(String section, String contents, String keywords);
	/**
	  Производит поиск всех объектов в секции, соответствующих заданному запросу. 
	  Алгоритм поиска не формализован, но должен быть умным, эвристичным и т.п.
	  Налагаются дополнительные требования на алгоритм поиска, за подробностями на trousev@yandex.ru
	**/
	public List<DatabaseObject> search(String section, String query);
	/**
	  Уничтожает выбранный DatabaseObject
	**/
	public boolean remove(String section, DatabaseObject object);
	/**
	  Закрывает БД. Все последующие соединения могут возвращать что угодно и бросать exception-ы.
	**/
	void close();
}
