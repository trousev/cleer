package pro.trousev.jplay.sys;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import pro.trousev.jplay.Database;
import pro.trousev.jplay.Library;
import pro.trousev.jplay.Database.DatabaseObject;
import pro.trousev.jplay.Track;

public class LibraryImpl implements Library {

	Database _db;
	public LibraryImpl(Database db)
	{
		_db = db;
		_db.declare_section("folders");
		_db.declare_section("songs");
		_db.declare_section("playlists");
	}
	@Override
	public List<File> folders() 
	{
		List<File> ans = new ArrayList<File>();
		for(DatabaseObject obj: _db.search("folders", "folder:"))
		{
			ans.add(new File(obj.contents()));
		}
		return ans;
	}

	@Override
	public boolean folder_add(File folder) {
		try
		{
			_db.search("folders", "fh"+Hash.hash(folder.getAbsolutePath())).get(0);
			return false;
		}
		catch(Exception e)
		{
			_db.store("folders", folder.getAbsolutePath(), "folder: "+folder.getAbsolutePath()+" fh"+Hash.hash(folder.getAbsolutePath())); 
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
		for(DatabaseObject dbo: _db.search("songs", "fh"+fh))
			_db.remove("songs", dbo);
		for(File f: all_files)
		{
			callback.progress(i++, n);
			try {
				Track t= new TrackImpl(f);
				_db.store("songs", t.serialize(), t.generate_query() + " fh"+fh);
			} 
			catch (Exception e) {
				callback.message("Skipping "+f.getAbsolutePath()+": "+e.getMessage());
			}
		}
		callback.finished();
		return false;
	}

	@Override
	public boolean folder_remove(File folder) {
		try
		{
			String fh = Hash.hash(folder.getAbsolutePath());
			for(DatabaseObject dbo: _db.search("songs", "fh"+fh))
				_db.remove("songs", dbo);
			DatabaseObject obj = _db.search("folders", "fh"+Hash.hash(folder.getAbsolutePath())).get(0);
			_db.remove("folders", obj);
		}
		catch(Exception e)
		{
			System.out.println(e.getMessage());
			return false;
		}
		return false;
	}

	@Override
	public boolean folder_scan( FolderScanCallback callback) {
		for(DatabaseObject dbo: _db.search("folders", "folder:"))
			folder_scan(new File(dbo.contents()), callback);
		return false;
	}
	@Override
	public List<Track> playlist(String query) {
		try
		{
			List<Track> ans = new ArrayList<Track>();
			for(DatabaseObject dbo: _db.search("songs", query))
				ans.add(new TrackImpl(dbo));
			return ans;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	private static class Playlist implements Serializable
	{
		private static final long serialVersionUID = 6213357899867437099L;
		public String name;
		public String query;
		public static Playlist fromDatabase(DatabaseObject dbo)
		{
			return Tools.Deserialize(dbo.contents());
		}
		Playlist(String name, String query)
		{
			this.name = name;
			this.query = query;
		}
	}
	public String playlist_hash(String name)
	{
		return "pl"+Hash.hash(name);
	}
	@Override
	public String user_playlist(String name) 
	{
		return Playlist.fromDatabase(_db.search("playlists", playlist_hash(name)).get(0)).query;
	}
	@Override
	public List<Track> user_playlist_content(String name) 
	{
		return playlist(user_playlist(name));
	}
	@Override
	public List<String> user_playlist_list() 
	{
		List<String> ans = new ArrayList<String>();
		for(DatabaseObject dbo: _db.search("playlists", playlist_hash("pl")))
			ans.add(Playlist.fromDatabase(dbo).name);
		return ans;
	}
	@Override
	public boolean user_playlist_save(String name, String query) 
	{
		user_playlist_remove(name);
		try
		{
			_db.store("playlists", Tools.Serialize(new Playlist(name, query)), playlist_hash(name));
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	@Override
	public boolean user_playlist_remove(String name) {
		for(DatabaseObject dbo: _db.search("playlists", playlist_hash(name)))
			_db.remove("playlists", dbo);
		return true;
	}
}
