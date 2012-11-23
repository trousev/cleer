package pro.trousev.jplay;

import java.util.List;

public interface Database {
	public interface DatabaseObject
	{
		String id();
		String contents();
		boolean update(String contents, String search);
		boolean update_contents(String contents);
		boolean update_search(String search);
	}
	/**
	 * Объявляет, что в БД будет раздел с заявленным именем
	 * @param section Имя раздела
	 * @return true, если все хорошо и такая секция может быть. false, в противном случае
	 */
	public boolean declare_section(String section);
	public boolean clear_section(String section);
	public DatabaseObject store(String section, String contents, String keywords);
	public List<DatabaseObject> search(String section, String query);
	public boolean remove(String section, DatabaseObject object);
	void close();
}
