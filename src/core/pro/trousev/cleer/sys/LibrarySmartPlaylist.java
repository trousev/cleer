package pro.trousev.cleer.sys;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Database.SearchLanguage;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.Track;
import pro.trousev.cleer.Database.DatabaseObject;

public class LibrarySmartPlaylist implements Playlist, Serializable{

	private static final long serialVersionUID = 1698233509444499865L;
	
	String _title = null;
	String _query;
	transient List<Track> _contents = null;
	transient Database _db;
	LibrarySmartPlaylist(Database storage, String title, String query)
	{
		_db = storage;
		_title = title;
		_query = query;
	}
	public void setDatabase(Database db)
	{
		_db = db;
	}
	@Override
	public String query() {
		return _query;
	}

	@Override
	public List<Track> contents() {
		if(_contents != null)
			return _contents;
		try
		{
			_contents = new ArrayList<Track>();
			for(DatabaseObject dbo: _db.search("songs", _query, SearchLanguage.SearchPyplay))
				_contents.add(new TrackImpl(dbo));
			return _contents;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Type type() {
		return Type.SmartPlaylist;
	}

	@Override
	public pro.trousev.cleer.Playlist setType(Type new_type) {
		if(new_type == Type.SmartPlaylist)
			return this;
		return null;
	}

	@Override
	public boolean save(String name) {
		_contents = null;
		_title = name;
		try {
			for(DatabaseObject dbo: _db.search("playlists", Tools.playlist_hash(name), SearchLanguage.SearchSqlLike))
			{
				_db.remove("playlists", dbo);
			}
			_db.store("playlists",Tools.Serialize(this), Tools.playlist_hash(name));
		} catch (DatabaseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public String title() {
		return _title;
	}

}
