package pro.trousev.cleer.android.service;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.NoSuchTagException;
import pro.trousev.cleer.Item.Tag;
import pro.trousev.cleer.Item.Tag.ReadOnlyTagException;
import pro.trousev.cleer.sys.AudioFileHeader;
import pro.trousev.cleer.sys.TrackImpl;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaScanner extends pro.trousev.cleer.sys.TrackImpl{
	private Context context;

	public MediaScanner(Context cont) {
		context = cont;
	}

	public static class MediaScannerException extends Exception {
		private static final long serialVersionUID = -1028575879236687565L;

		MediaScannerException(String reason) {
			super(String.format("MediaScannerException: ", reason));
		}

	}
	
	/*public TrackImpl TagChanger(TrackImpl track){
		try {
			String a=new String(track.tag("title").toString().getBytes("windows-1251"),"utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchTagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return track;
	};*/
	
	public void setTagValue(String name, String value) throws ReadOnlyTagException, NoSuchTagException
	{
		Tag tag = tag(name);
		tag.setValue(value);
		/*AudioFileHeader head = new AudioFileHeader();
		try {
			head.readFromFile(_filename);
			head.begin();
			head.setRating(value);
			head.commit();
			updateTagInDatabase();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
	}
	public ArrayList<Item> scanner() throws MediaScannerException {
		String[] proj = { MediaStore.Audio.Media.DATA };
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		ArrayList<Item> data = new ArrayList<Item>();
		Cursor cursor = context.getContentResolver().query(uri, proj, null,
				null, null);
		/*if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {*/
		if (cursor==null){
			//query fail
		}else if(!cursor.moveToFirst()){
			//no audio on device
		}else{
				int columnIndex = cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
				do{
				Uri filePathUri = Uri.parse(cursor.getString(columnIndex));
				String filename = filePathUri.getPath().toString();
				File file = new File(filename);
				try { // FIXME Make one exception for TrackImpl
					TrackImpl track = new TrackImpl(file);
					track.setTagValue("title", new String(track.tag("title").toString().getBytes("windows-1251"),"utf-8"));
					data.add(track);
				} catch (Exception e) {
				}
			}while (cursor.moveToNext());
		}
		return data;

	}

}
