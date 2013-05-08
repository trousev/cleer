package pro.trousev.cleer.android.service;

import java.io.File;
import java.util.ArrayList;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.sys.TrackImpl;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaScanner {
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

	public ArrayList<Item> scanner() throws MediaScannerException {
		String[] proj = { MediaStore.Audio.Media.DATA };
		Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
		ArrayList<Item> data = new ArrayList<Item>();
		Cursor cursor = context.getContentResolver().query(uri, proj, null,
				null, null);
		if (cursor.getCount() > 0) {
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				int columnIndex = cursor
						.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
				Uri filePathUri = Uri.parse(cursor.getString(columnIndex));
				String filename = filePathUri.getPath().toString();
				File file = new File(filename);
				try { // FIXME Make one exception for TrackImpl
					TrackImpl track = new TrackImpl(file);
					data.add(track);
				} catch (Exception e) {
				}
			}
		}
		return data;

	}

}
