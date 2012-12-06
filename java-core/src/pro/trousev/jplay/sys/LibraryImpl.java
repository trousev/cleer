package pro.trousev.jplay.sys;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import pro.trousev.jplay.Database;
import pro.trousev.jplay.Library;
import pro.trousev.jplay.Database.DatabaseObject;
import pro.trousev.jplay.Playlist;
import pro.trousev.jplay.Track;

public class LibraryImpl implements Library {

	final Database _db;
	String _focus = "default";
	private LibrarySmartPlaylist playlistFromDatabase(DatabaseObject dbo)
	{
		LibrarySmartPlaylist p = Tools.Deserialize(dbo.contents());
		p.setDatabase(_db);
		return p;
	}
	
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
	public Playlist search(String query) {
		Playlist p = new LibrarySmartPlaylist(_db,_focus,query);
		p.save(_focus);
		System.out.println(focus().query());
		return p;
	}

	@Override
	public Playlist playlist(String name) 
	{
		DatabaseObject dbo = _db.search("playlists", Tools.playlist_hash(name)).get(0);
		return playlistFromDatabase(dbo);
	}
	@Override
	public List<Playlist> playlists()
	{
		List<Playlist> ans = new ArrayList<Playlist>();
		for(DatabaseObject dbo: _db.search("playlists", Tools.playlist_hash("pl")))
			ans.add(playlistFromDatabase(dbo));
		return ans;
	}
	public List<String> playlist_names()
	{
		List<String> ans = new ArrayList<String>();
		for(DatabaseObject dbo: _db.search("playlists", Tools.playlist_hash("pl")))
			ans.add(playlistFromDatabase(dbo).title());
		return ans;
	}
	
	@Override
	public boolean playlist_remove(String name) {
		for(DatabaseObject dbo: _db.search("playlists", Tools.playlist_hash(name)))
			_db.remove("playlists", dbo);
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
}
