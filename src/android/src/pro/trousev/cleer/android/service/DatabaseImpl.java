package pro.trousev.cleer.android.service;

import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.database.DatabaseErrorHandler;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Database.DatabaseError;

public class DatabaseImpl implements Database{
	SQLiteDatabase db;
	
	public DatabaseImpl (String path) {
		DatabaseErrorHandler errorHandler = null;
		String str = null;
		SQLiteDatabase.CursorFactory factory = null;
		
		this.db = SQLiteDatabase.openOrCreateDatabase (str, factory, errorHandler);
		
		
	}
	
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
		public String contents()  {
			
			if(this.contents == null)
			{
				try {
					// переменные для query
				    String[] columns = {"value"};
				    String selection = "id = " + this.id;
				    // курсор
				    Cursor c = db.query("sections", columns, selection, null, null, null, null);
				    this.contents = c.getColumnName(0);
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
			return this.contents;
			
		}
		/**
				Поисковой контекст.
		 **/
		public String search() {
			return this.search;
			
		}
		/**
				Метод, обновляющий объект и заменяющий и содержимое, и поисковой контекст. 

				Если contents или search выставлены в null, то соответствующий объект изменяться не должен.
		 * @throws DatabaseError 
		 **/
		public boolean update(String contents, String search) throws DatabaseError {
			ContentValues cv = new ContentValues();
		    cv.put("value", contents);
		    cv.put("search", search);
		    		    
			if (contents == null || search == null) {
				return false;
			} 
				try {
					int updCount = db.update("sections", cv, "id = " + this.id, new String[] { id });
				}
				//TODO поменять название ошибки на соответствующее
				catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				
			return true;
			
		}
		/**
				Обновляет только содержимое
		 **/
		public boolean update_contents(String contents) throws DatabaseError {
			ContentValues cv = new ContentValues();
		    cv.put("value", contents);
		    		    
			if (contents == null) {
				return false;
			} 
				try {
					int updCount = db.update("sections", cv, "id = " + this.id, new String[] { id });
				}
				//TODO поменять название ошибки на соответствующее
				catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				
			return true;
			
		}
		/**
				Обновляет только поисковой контекст.
		 **/
		public boolean update_search(String search) throws DatabaseError {
			ContentValues cv = new ContentValues();
		    cv.put("search", search);
		    		    
			if (search == null) {
				return true;
			} 
				try {
					int updCount = db.update("sections", cv, "id = " + this.id, new String[] { id });
				}
				//TODO поменять название ошибки на соответствующее
				catch (SQLException e) {
					e.printStackTrace();
					return false;
				}
				
			return true;
			
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
