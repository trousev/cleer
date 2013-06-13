package pro.trousev.cleer.android.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pro.trousev.cleer.Database;

public class DatabaseAndroidImpl implements Database {

	SQLiteDatabase _db ;
	
	public static class DatabaseAndroidObject implements Database.DatabaseObject
	{
		String _id = null;
		String _contents = null;
		DatabaseAndroidImpl _parent = null;
		String _section= null;
		String _search = null;
		static String[] _cols_both = {"content","search"};
		static String[] _cols_all = {"id","content","search"};

		DatabaseAndroidObject(String section, DatabaseAndroidImpl parent, String id, String contents)
		{
			_parent = parent;
			_id = id;
			_contents = contents;
			_section = section;
		}
		DatabaseAndroidObject(DatabaseAndroidImpl parent, String id)
		{
			_parent = parent;
			_id = id;
		}
		
		
		@Override
		public String id() {
			return _id;
		}
		private void fetch()
		{
			Cursor ans = _parent._db.query(_section, _cols_both, String.format("id='%s'",_id), null, null, null, null);
			assert(ans.getCount() == 1);
			ans.moveToFirst();
			_contents = ans.getString(0);
			_search = ans.getString(1);
		}
		@Override
		public String contents() {
			if(_contents == null)
				fetch();
			return _contents;
		}

		@Override
		public String search() {
			if(_search == null)
				fetch();
			return _search;
		}

		@Override
		public boolean update(String contents, String search)
				throws DatabaseError {
			ContentValues vals = new ContentValues();
			if(contents != null) vals.put("content", contents);
			if(search != null) vals.put("search", search);
			int rows_aff = _parent._db.update(_section, vals, String.format("id='%s'",_id), null);
			if(rows_aff != 1)
				throw new DatabaseError("Update updated not 1 row, but: "+rows_aff);
			if(contents != null) _contents = contents;
			if(search!= null) _search = search;
			return true;
		}

		@Override
		public boolean update_contents(String contents) throws DatabaseError {
			return update(contents,null);
		}

		@Override
		public boolean update_search(String search) throws DatabaseError {
			return update(null, search);
		}
		@Override
		public void delete()
		{
			_parent._db.delete(_section, String.format("id='%s'",_id), null);
		}
		@Override
		public boolean update_tags(Map<String, String> tags)
				throws DatabaseError {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean update_tag(String name, String value)
				throws DatabaseError {
			// TODO Auto-generated method stub
			return false;
		}
		@Override
		public boolean remove_tag(String name) throws DatabaseError {
			// TODO Auto-generated method stub
			return false;
		}
	}
	
	public DatabaseAndroidImpl()
	{
		_db = SQLiteDatabase.openOrCreateDatabase("/sdcard/cleer.sqlite3", null);
	}
	public DatabaseAndroidImpl(String path)
	{
		_db = SQLiteDatabase.openOrCreateDatabase(path, null);
	}
	
	@Override
	public boolean declare_section(String section) throws DatabaseError {
		_db.execSQL(String.format("CREATE TABLE IF NOT EXISTS %s (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, content VARCHAR(10000), search VARCHAR(10000))", section));
		_db.execSQL(String.format("CREATE INDEX IF NOT EXISTS idx_search_%s ON %s (search)", section,section));
		return false;
	}

	@Override
	public boolean clear_section(String section) throws DatabaseError {
		try
		{
			_db.execSQL(String.format("DROP TABLE %s;", section));
		}
		catch (android.database.SQLException e)
		{ 
			// Do nothing. This is CORRECT because any "no such table" events should be silently ignored.
		}
		return false;
	}

	@Override
	public DatabaseObject store(String section, String contents, String keywords)
			throws DatabaseError {
		keywords = keywords.toLowerCase();
		keywords = keywords.replace("'","''");
		contents = contents.replace("'","''");
		ContentValues vals = new ContentValues();
		vals.put("content", contents);
		vals.put("search", keywords);
		long ins_id = _db.insert(section, null, vals);
		if(ins_id == -1)
			throw new DatabaseError("Can't insert element to table "+section);
		return null;
	}

	@Override
	@Deprecated
	public List<DatabaseObject> search(String section, String query)
			throws DatabaseError {
		return search(section, query, SearchLanguage.SearchSqlLike);
	}

	@Override
	public List<DatabaseObject> search(String section, String query,
			SearchLanguage language) throws DatabaseError {
		query = query.toLowerCase();
		String where = null;
		if(language == SearchLanguage.SearchDirectMatch)
			where = _language_match(query);
		if(language == SearchLanguage.SearchSqlLike)
			where = _language_like(query);
		if(language == SearchLanguage.SearchPyplay)
			where = _language_pyplay(query);
		try {
			return _p_search(section, where);
		} catch (SQLException e) {
			throw new DatabaseError(e); 
		}
	}

	@Override
	public boolean remove(String section, DatabaseObject object)
			throws DatabaseError {
		object.delete();
		return true;
	}

	@Override
	public void close() throws DatabaseError {
		_db.close();
	}

	@Override
	public boolean begin() throws DatabaseError {
		_db.beginTransactionNonExclusive();
		return true;
	}

	@Override
	public boolean commit() throws DatabaseError {
		_db.setTransactionSuccessful();
		_db.endTransaction();
		return true;
	}

	@Override
	public boolean rollback() throws DatabaseError {
		_db.endTransaction();
		return true;
	}


	
	private List<DatabaseObject> _p_search(String section, String where)
			throws DatabaseError, SQLException 
	{
		List<DatabaseObject> ans = new ArrayList<Database.DatabaseObject>();
		Cursor cur;
		cur = _db.query(section, DatabaseAndroidObject._cols_all, where, null, null, null, null);
		
		if(!cur.moveToFirst()) return ans;
		while(!cur.isAfterLast())
		{
			ans.add(new DatabaseAndroidObject(section, this, cur.getString(0), cur.getString(1)));
			cur.moveToNext();
		}
		return ans;
	}	
	//FIXME: move languages to separate interface and implement it in core.
	private String _language_match(String query)
	{
		return String.format("search LIKE '%%%s%%'", query);
	}
	private String _language_like(String query)
	{
		String[] keywords = query.split(" ");
		String where = null;
		for(String word : keywords)
		{
			word = word.replace("'", "''");
			word = word.toLowerCase();
			String clause = String.format("search LIKE '%%%s%%'",word);
			if(where == null)
				where = clause;
			else where += " AND "+clause;
		}
		return where;
	}
	private String _language_pyplay(String query)
	{
		query = query.replaceAll("\\@", "artist:");
		query = query.replaceAll("\\$", "album:");
		query = query.replaceAll("\\!", "title:");
		query = query.replaceAll("\\^", "genre:");
		query = query.replaceAll("N\\/R", "rating:0");
		query = query.replaceAll("\\{0\\}", "rating:0");
		query = query.replaceAll("\\{1\\}", "rating:1");
		query = query.replaceAll("\\{2\\}", "rating:2");
		query = query.replaceAll("\\{3\\}", "rating:3");
		query = query.replaceAll("\\{4\\}", "rating:4");
		query = query.replaceAll("\\{5\\}", "rating:5");
		String where = "";
		for(String or_list: query.split(" "))
		{
			or_list = or_list.trim();
			if(or_list.isEmpty()) continue;
			String or_clause = "";
			for(String item: or_list.split(","))
			{
				if(!or_clause.isEmpty())
					or_clause += " OR ";
				or_clause += String.format("search LIKE '%%%s%%'",item);
			}
			if(or_clause.isEmpty()) continue;
			if(!where.isEmpty())
				where += " AND ";
			where += String.format(" ( %s ) ", or_clause);
		}
		return where;
	}
	@Override
	public boolean declare_tag(String section, String name) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public DatabaseObject store(String section, String contents,
			String keywords, Map<String, String> tags) throws DatabaseError {
		// Since android database have no OLAP extensions, no need for this method
		//FIXME: BUT: tags are nesessary, and they're should be implemented anyway.
		return store(section, contents, keywords);
	}
	@Override
	public List<DatabaseObject> search(String section, String query,
			SearchLanguage language, Map<String, String> filter)
			throws DatabaseError {
		// Since android database have no OLAP extensions, no need for this method
		// FIXME: BUT: tags are nesessary, and they're should be implemented anyway.
		return search(section, query, language);
	}
	@Override
	public List<String> search_tag(String section, String tag,
			Map<String, String> filter) {
		// TODO Auto-generated method stub
		// Since android database have no OLAP extensions, no need for this method
		// FIXME: BUT: tags are nesessary, and they're should be implemented anyway.
		return null;
	}
	
}
