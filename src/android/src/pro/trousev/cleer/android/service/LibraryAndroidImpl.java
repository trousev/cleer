package pro.trousev.cleer.android.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Database.DatabaseObject;
import pro.trousev.cleer.Database.SearchLanguage;
import pro.trousev.cleer.Item.Factory;
import pro.trousev.cleer.android.userInterface.MainActivity;
import pro.trousev.cleer.sys.Hash;
import pro.trousev.cleer.sys.LibraryImpl;

public class LibraryAndroidImpl extends LibraryImpl {

	private Context _context;
	public LibraryAndroidImpl(Database db, Factory item_factory, Context context)
			throws DatabaseError {
		super(db, item_factory);
		_context = context;
	}
	public boolean folder_scan(FolderScanCallback callback)
	{
		
		callback.started();
		List<File> all_files = new ArrayList<File>(); // Ща получим!

		String[] proj = { MediaStore.Audio.Media.DATA };
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		Cursor cursor = _context.getContentResolver().query(uri, proj, null, null, null);
		/*if (cursor.getCount() > 0) {
		for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
		.moveToNext()) {*/
		if (cursor==null)
		{
			callback.message("Failed to get media files on Your device.");
			callback.finished();
			return false;
		}
		else if(!cursor.moveToFirst()){
			callback.message("No media files on Your device.");
			callback.finished();
			return false;
		}
		else
		{
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
			do
			{
				Uri filePathUri = Uri.parse(cursor.getString(columnIndex));
				try // FIXME Make one exception for TrackImpl
				{
					String filename = filePathUri.getPath().toString(); 
					File file = new File(filename);
					all_files.add(file);
				}
				catch (Exception e)
				{
					System.out.println("[pro.trousev.cleer] " + filePathUri + " caused exception: " + e.getMessage());
				}
			}
			while(cursor.moveToNext());
		}

		int n = all_files.size();
		int i=0;
		String fh = Hash.hash("!!AndroidMedia");
		try {
			_db.begin();
			for(DatabaseObject dbo: _db.search("songs", "fh"+fh,SearchLanguage.SearchSqlLike))
				_db.remove("songs", dbo);
			for(File f: all_files)
			{
				callback.progress(i++, n);
				System.out.println("Filename: "+f.toString());
				try {
					Item t= _item_factory.createItem(f);
					_db.store("songs", t.serialize(), t.getSearchQuery() + " fh"+fh);
				} 
				catch (Throwable e) {
					//e.printStackTrace();
					callback.message("Skipping "+f.getAbsolutePath()+": "+e.getMessage()+" ("+e.getClass().toString()+")");
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
	

}
