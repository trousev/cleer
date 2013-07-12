package pro.trousev.cleer.sys;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pro.trousev.cleer.Database;

public class DatabaseSqlite implements Database {

	Connection link = null;
	Map<String, List<String> > _tags_stored;
	public DatabaseSqlite(String path) throws SQLException, ClassNotFoundException
	{
		//Class.forName("org.hsqldb.jdbcDriver");
		Class.forName("org.sqlite.JDBC");
		//link = DriverManager.getConnection(String.format("jdbc:hsqldb:file:%s;hsqldb.default_table_type=cached;hsqldb.script_format=3",path),"sa","");

		//link = DriverManager.getConnection(String.format("jdbc:hsqldb:file:%s",path),"sa","");
		link = DriverManager.getConnection(String.format("jdbc:sqlite:%s",path));

	}
	@Override
	public void close() 
	{
		/*try {
			link.prepareStatement("SHUTDOWN").execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}*/
		try {
			link.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void finalize() throws Throwable
	{
		close();
		super.finalize();
	}
	public class DatabaseObjectHsql implements Database.DatabaseObject
	{
		String _id = null;
		String _contents = null;
		DatabaseSqlite _parent = null;
		String _section= null;
		String _search = null;
		DatabaseObjectHsql(String section, DatabaseSqlite parent, String id, String contents)
		{
			_parent = parent;
			_id = id;
			_contents = contents;
			_section = section;
		}
		DatabaseObjectHsql(DatabaseSqlite parent, String id)
		{
			_parent = parent;
			_id = id;
		}
		@Override
		public String id() {
			return _id;
		}

		@Override
		public String contents() {
			if(_contents == null)
			{
				try {
					ResultSet set = _parent.link.prepareStatement(String.format("select value from %s where id='%s';",_section,_id)).executeQuery();
					set.next();
					_contents = set.getString(1);
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
			return _contents;
		}
		@Override
		public String toString()
		{
			return id()+": "+contents();
		}
		@Override
		public boolean update(String contents, String search) throws DatabaseError {
			if(contents != null) contents = contents.replace("'", "''");
			if(search != null) search = search.replace("'", "''");
			if(search != null) search = search.toLowerCase();
			String query = null;
			if(contents == null)
				query=String.format("update %s set search='%s' where id='%s'", _section, search,_id);
			else if (search == null)
				query=String.format("update %s set value='%s'  where id='%s'",_section,contents,_id);
			else 
				query=String.format("update %s set value='%s' , search='%s' where id='%s'",_section,contents,  search,_id);
			try {
				PreparedStatement st = _parent.link.prepareStatement(query);
				st.execute();
				if(st.getUpdateCount() == 1)
				{
					if(contents != null)
						_contents = null;
					if(search!= null)
						_search = null;
					return true;
				}
				else throw new DatabaseError("Statement returned not one updated row. Query: "+query+" UpdatedRows: "+st.getUpdateCount());
			} catch (SQLException e) {
				throw new DatabaseError(e);
			}
			
		}
		@Override
		public boolean update_contents(String contents) throws DatabaseError {
			return update(contents, null);
		}
		@Override
		public boolean update_search(String search) throws DatabaseError {
			return update(null,search);
		}
		@Override
		public String search() {
			if(_search == null)
			{
				try {
					ResultSet set = _parent.link.prepareStatement(String.format("select search from %s where id='%s';",_section,_id)).executeQuery();
					set.next();
					_search = set.getString(1);
					set.close();
				} catch (SQLException e) {
					e.printStackTrace();
					return null;
				}
			}
			return _search;

		}
		@Override
		public void delete() {
			_parent.remove(_section, this);
		}
		@Override
		public boolean update_tags(Map<String, String> tags) throws DatabaseError {
			if(tags != null) ensureTagEnlisted(_section, tags.keySet());
			String update = "";
			for(String key: tags.keySet())
			{
				if(update.length() != 0)
					update += ", ";
				update += String.format(" tag_%s='%s' ", key, tags.get(key));
			}
			update = String.format("UPDATE %s SET %s where id=%s; ", _section,update,_id);
			try {
				PreparedStatement st = _parent.link.prepareStatement(update);
				st.execute();
				if(st.getUpdateCount() == 1)
				{
					return true;
				}
				else throw new DatabaseError("Statement returned not one updated row. Query: "+update+" UpdatedRows: "+st.getUpdateCount());
			} catch (SQLException e) {
				throw new DatabaseError(e);
			}
		}
		@Override
		public boolean update_tag(String name, String value) throws DatabaseError {
			Map<String, String> m = new HashMap<String, String>();
			m.put(name, value);
			return update_tags(m);
		}
		@Override
		public boolean remove_tag(String name) throws DatabaseError {
			return update_tag(name, "");
		}
	}
	private void ensureTagEnlisted(String section, Collection<String> tagNames)
	{
		if(_tags_stored == null) _tags_stored = new HashMap<String, List<String>>();
		if(_tags_stored.get(section) == null) _tags_stored.put(section, new ArrayList<String>());
		for(String tagName: tagNames)
		{
			if(_tags_stored.get(section).contains(tagName))
				continue;
			try
			{
				declare_tag(section, tagName);
			} catch(Throwable t) 
			{ 
				t.printStackTrace();
			}
			_tags_stored.get(section).add(tagName);
		}
		
	}
	@Override
	public DatabaseObject store(String section, String contents,
			String keywords, Map<String, String> tags) throws DatabaseError {
		if(tags != null) ensureTagEnlisted(section, tags.keySet());
		keywords = keywords.toLowerCase();
		keywords = keywords.replace("'","''");
		contents = contents.replace("'","''");
		String query = "No QUery";
		try {
			String names = "";
			String values = "";
			names="value";
			names += ", search";
			values = "'"+contents+"'";
			values += ",'"+keywords+"'";
			if(tags != null) for(String name: tags.keySet())
			{
				String value = tags.get(name);
				names += ", tag_" + name;
				value = value.replace("'", "''");
				values+= ",'"+value+"'";
			}
			query = String.format("INSERT INTO %s(%s) VALUES(%s);",section, names, values);
			link.prepareStatement(query).execute();
		} catch (SQLException e) {
			throw new DatabaseError(e+ "/q:"+query);
		}
		return null;
	}
	@Override
	public DatabaseObject store(String section, String contents, String keywords) throws DatabaseError {
		return store(section,contents,keywords,null);
	}

	@Override
	public List<DatabaseObject> search(String section, String query) throws DatabaseError {
		return search(section, query, SearchLanguage.SearchSqlLike);
	}

	@Override
	public boolean remove(String section, DatabaseObject object) {
		try {
			return link.prepareStatement(String.format("DELETE FROM %s WHERE id='%s';", section,object.id())).execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean declare_section(String section) 
	{
	    try
	    {
	    	link.prepareStatement(String.format("CREATE TABLE %s (id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, value VARCHAR(10000), search VARCHAR(10000))", section)).execute();
	    	link.prepareStatement(String.format("CREATE INDEX IF NOT EXISTS idx_search_%s ON %s (search)", section,section)).execute();
	    	return true;
	    }
	    catch (Exception e)
	    {
	    	return false;
	    }
	}
	@Override
	public boolean declare_tag(String section, String name) 
	{
	    try
	    {
	    	link.prepareStatement(String.format("ALTER TABLE %s ADD COLUMN tag_%s TEXT; ", section,name)).execute();
	    	link.prepareStatement(String.format("CREATE INDEX IF NOT EXISTS idx_tag_%s_%s ON %s (%s)",name, section,section,name)).execute();
	    	return true;
	    }
	    catch (Exception e)
	    {
	    	return false;
	    }
	}
	@Override
	public boolean clear_section(String section) {
	    try
	    {
	    	return  link.prepareStatement(
	    		String.format("DELETE FROM %s ;", section)
                )
                .execute();
	    }
	    catch (Exception e)
	    {
	    	return false;
	    }
	}
	@Override
	public boolean begin() {
		try {
			return link.prepareStatement("BEGIN TRANSACTION").execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean commit() {
		try {
			return link.prepareStatement("COMMIT TRANSACTION").execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public boolean rollback() {
		try {
			return link.prepareStatement("ROLLBACK TRANSACTION").execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}
	private List<DatabaseObject> _p_search(String section, String where)
			throws DatabaseError, SQLException 
	{
		List<DatabaseObject> ans = new ArrayList<Database.DatabaseObject>();
		String qq; 
		if(where == null)
			qq = String.format("SELECT id, value FROM %s;",section);
		else
			qq = String.format("SELECT id, value FROM %s WHERE %s;",section,where);
		ResultSet set = link.prepareStatement(qq).executeQuery();
		while(set.next())
		{
			ans.add(new DatabaseObjectHsql(section,this, set.getString(1),set.getString(2)));
		}
		set.close();
		return ans;

	}
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
			if(or_list.length() == 0) continue;
			String or_clause = "";
			for(String item: or_list.split(","))
			{
				if(or_clause.length() != 0)
					or_clause += " OR ";
				or_clause += String.format("search LIKE '%%%s%%'",item);
			}
			if(or_clause.length() == 0) continue;
			if(where.length() != 0)
				where += " AND ";
			where += String.format(" ( %s ) ", or_clause);
		}
		return where;
	}
	@Override
	public List<DatabaseObject> search(String section, String query, SearchLanguage language) 
			throws DatabaseError 
	{
		return search(section, query, language, null);
	}
	@Override
	public List<DatabaseObject> search(String section, String query,
			SearchLanguage language, Map<String, String> filter)
			throws DatabaseError 
	{
		query = query.toLowerCase();
		String where = "";
		if(language == SearchLanguage.SearchDirectMatch)
			where = _language_match(query);
		if(language == SearchLanguage.SearchSqlLike)
			where = _language_like(query);
		if(language == SearchLanguage.SearchPyplay)
			where = _language_pyplay(query);
		if(filter != null)
		{
			for(String key: filter.keySet())
			{
				String value = filter.get(key);
				if(where.length() != 0)
					where += " AND ";
				where += String.format("%s = '%s'", key, value);
			}
		}
		try {
			return _p_search(section, where);
		} catch (SQLException e) {
			throw new DatabaseError(e); 
		}
		//return null;
	}
	
	@Override
	public List<String> search_tag(String section, String tag,
			Map<String, String> filter) throws DatabaseError {
		String qq;
		String where = "";
		for(String key: filter.keySet())
		{
			String value = filter.get(key);
			if(where.length() != 0)
				where += " AND ";
			where += String.format(" %s = '%s' ", key, value); 
		}
		if(where.length() == 0)
			qq = String.format("SELECT DISTINCT(tag_%s) FROM %s;",tag,section);
		else
			qq = String.format("SELECT DISTINCT(tag_%s) FROM %s WHERE %s;",tag,section,where);
		try {
			List<String> ans = new ArrayList<String>();
			ResultSet set = link.prepareStatement(qq).executeQuery();
			while(set.next())
			{
				ans.add(set.getString(1));
			}
			set.close();
			return ans;
		} catch (SQLException e) {
			throw new DatabaseError(e);
		}
	}
	
}
