package pro.trousev.cleer.android.service;

import java.util.List;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Database.DatabaseError;

public class DatabaseImpl implements Database{
	
	public class DatabaseObject implements Database.DatabaseObject {
		private String id = null;
		private String contents = null;
		private DatabaseImpl parent = null;
		private String section= null;
		private String search = null;
		
		public DatabaseObject (String section, DatabaseImpl parent, String id, String contents)
		{
			this.parent = parent;
			this.id = id;
			this.contents = contents;
			this.section = section;
		}
		
		public DatabaseObject (DatabaseImpl parent, String id)
		{
			this.parent = parent;
			this.id = id;
		}
		
		/**
        		Идентификатор
		 **/
		public String id() {
			return this.id;
		}
		/**
				Содержимое объекта
		 **/
		public String contents() {
			
			if (this.contents == null)
			{
				// TODO try-catch of something ? 
			}
			return null;
			
		}
		/**
				Поисковой контекст.
		 **/
		public String search() {
			return null;
			
		}
		/**
				Метод, обновляющий объект и заменяющий и содержимое, и поисковой контекст. 

				Если contents или search выставлены в null, то соответствующий объект изменяться не должен.
		 * @throws DatabaseError 
		 **/
		public boolean update(String contents, String search) throws DatabaseError {
			return false;
			
		}
		/**
				Обновляет только содержимое
		 **/
		public boolean update_contents(String contents) throws DatabaseError {
			return false;
			
		}
		/**
				Обновляет только поисковой контекст.
		 **/
		public boolean update_search(String search) throws DatabaseError {
			return false;
			
		}
	}

	@Override
	public boolean declare_section(String section) throws DatabaseError {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean clear_section(String section) throws DatabaseError {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public pro.trousev.cleer.Database.DatabaseObject store(String section,
			String contents, String keywords) throws DatabaseError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<pro.trousev.cleer.Database.DatabaseObject> search(
			String section, String query) throws DatabaseError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<pro.trousev.cleer.Database.DatabaseObject> search(
			String section, String query, SearchLanguage language)
			throws DatabaseError {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(String section,
			pro.trousev.cleer.Database.DatabaseObject object)
			throws DatabaseError {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void close() throws DatabaseError {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean begin() throws DatabaseError {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean commit() throws DatabaseError {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean rollback() throws DatabaseError {
		// TODO Auto-generated method stub
		return false;
	}
}
