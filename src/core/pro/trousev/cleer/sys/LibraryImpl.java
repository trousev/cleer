package pro.trousev.cleer.sys;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Database.SearchLanguage;
import pro.trousev.cleer.Library;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Database.DatabaseObject;

public class LibraryImpl implements Library {

	final Database _db;
	String _focus = "default";
	Item.Factory _item_factory;
	private LibrarySmartPlaylist playlistFromDatabase(DatabaseObject dbo)
	{
		LibrarySmartPlaylist p = Tools.Deserialize(dbo.contents());
		p.setDatabase(_db);
		return p;
	}
	
	public LibraryImpl(Database db, Item.Factory item_factory) throws DatabaseError
	{
		_item_factory = item_factory;
		_db = db;
		_db.declare_section("folders");
		_db.declare_section("songs");
		_db.declare_section("playlists");
	}
	@Override
	public List<File> folders() 
	{
		List<File> ans = new ArrayList<File>();
		try {
			for(DatabaseObject obj: _db.search("folders", "folder:",SearchLanguage.SearchSqlLike))
			{
				ans.add(new File(obj.contents()));
			}
		} catch (DatabaseError e) {
			// TODO Make some re-throws here too.
			e.printStackTrace();
			return null;
		}
		return ans;
	}

	@Override
	public boolean folder_add(File folder) {
		try
		{
			_db.search("folders", "fh"+Hash.hash(folder.getAbsolutePath()),SearchLanguage.SearchSqlLike).get(0);
			return false;
		}
		catch(Exception e)
		{
			try {
				_db.store("folders", folder.getAbsolutePath(), "folder: "+folder.getAbsolutePath()+" fh"+Hash.hash(folder.getAbsolutePath()));
			} catch (DatabaseError e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			} 
			return true;
		}
	}

	@Override
	public boolean folder_scan(File folder, FolderScanCallback callback) {
		folder_add(folder);
		callback.started();
		class Scan
		{
			public void addTree(File file, Collection<File> all, FolderScanCallback cb) 
			{
			    File[] children = file.listFiles();
			    if (children != null) {
			    	cb.message("Scanning folder: "+file.getAbsolutePath());
			        for (File child : children) {
			            
			            addTree(child, all,cb);
			        }
			    }
			    else 
			    	all.add(file);
			}
		};
		Scan scan = new Scan();
		List<File> all_files = new ArrayList<File>();
		scan.addTree(folder, all_files, callback);
		int n = all_files.size();
		int i=0;
		String fh = Hash.hash(folder.getAbsolutePath());
		try {
			_db.begin();
			for(DatabaseObject dbo: _db.search("songs", "fh"+fh,SearchLanguage.SearchSqlLike))
				_db.remove("songs", dbo);
			for(File f: all_files)
			{
				callback.progress(i++, n);
				try {
					Item t= _item_factory.createTrack(f);
					_db.store("songs", t.serialize(), t.getSearchQuery() + " fh"+fh);
				} 
				catch (Exception e) {
					e.printStackTrace();
					callback.message("Skipping "+f.getAbsolutePath()+": "+e.getMessage());
				}
			}
			_db.commit();
		} catch (DatabaseError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return false;
		}
		callback.finished();
		return false;
	}

	@Override
	public boolean folder_remove(File folder) {
		try
		{
			String fh = Hash.hash(folder.getAbsolutePath());
			_db.begin();
			for(DatabaseObject dbo: _db.search("songs", "fh"+fh,SearchLanguage.SearchSqlLike))
				_db.remove("songs", dbo);
			DatabaseObject obj = _db.search("folders", "fh"+Hash.hash(folder.getAbsolutePath()),SearchLanguage.SearchSqlLike).get(0);
			_db.remove("folders", obj);
			_db.commit();
		}
		catch(Exception e)
		{
			//TODO: re-throw exception, don't block it!
			//System.out.println(e.getMessage());
			return false;
		}
		return false;
	}

	@Override
	public boolean folder_scan( FolderScanCallback callback) {
		try {
			for(DatabaseObject dbo: _db.search("folders", "folder:",SearchLanguage.SearchSqlLike))
				folder_scan(new File(dbo.contents()), callback);
			return true;
		} catch (DatabaseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	@Override
	public Playlist search(String query) {
		Playlist p = new LibrarySmartPlaylist(_db,_focus,query);
		p.save(_focus);
		return p;
	}

	@Override
	public Playlist playlist(String name) 
	{
		try
		{
			DatabaseObject dbo = _db.search("playlists", Tools.playlist_hash(name),SearchLanguage.SearchSqlLike).get(0);
			return playlistFromDatabase(dbo);
		}
		catch(IndexOutOfBoundsException e)
		{
			return null;
		} catch (DatabaseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public List<Playlist> playlists()
	{
		List<Playlist> ans = new ArrayList<Playlist>();
		try {
			for(DatabaseObject dbo: _db.search("playlists", Tools.playlist_hash("pl"), SearchLanguage.SearchSqlLike))
				ans.add(playlistFromDatabase(dbo));
		} catch (DatabaseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return ans;
	}
	public List<String> playlist_names()
	{
		List<String> ans = new ArrayList<String>();
		try {
			for(DatabaseObject dbo: _db.search("playlists", Tools.playlist_hash("pl"), SearchLanguage.SearchSqlLike))
				ans.add(playlistFromDatabase(dbo).title());
		} catch (DatabaseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return ans;
	}
	
	@Override
	public boolean playlist_remove(String name) {
		try {
			for(DatabaseObject dbo: _db.search("playlists", Tools.playlist_hash(name), SearchLanguage.SearchSqlLike))
				_db.remove("playlists", dbo);
		} catch (DatabaseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	@Override
	public Playlist focus() {
		return playlist(_focus);
	}

	@Override
	public Playlist setFocus(String playlist) {
		_focus = playlist;
		return focus();
	}

	@Override
	public boolean update(Item t) {
		DatabaseObject dbo = t.linkedObject();
		//FIXME: This is a BUG! Folder Information is lost!
		
		String fh = dbo.search().replaceAll(".*fh", "");
		try {
			dbo.update_search(t.getSearchQuery()+" fh"+fh);
		} catch (DatabaseError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
