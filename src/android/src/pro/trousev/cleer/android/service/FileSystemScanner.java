package pro.trousev.cleer.android.service;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.android.Constants;
import pro.trousev.cleer.sys.TrackImpl;
import android.os.Environment;
import android.util.Log;

public class FileSystemScanner {
	private File root = new File(Environment.getExternalStorageDirectory()
			.getAbsolutePath());

	private List<File> scanDirectory(File directory) {
		List<File> list = new ArrayList<File>();
		List<File> directories = new ArrayList<File>();
		if (root.isDirectory())
			Log.d(Constants.LOG_TAG,
					"scanDirectory.enter()" + directory.getName());
		File[] files = directory.listFiles();
		Log.d(Constants.LOG_TAG, "scanDirectory.getListFiles()");
		if (files != null)
			for (int i = 0; i < files.length; i++) {
				Log.d(Constants.LOG_TAG, "scanDirectory.trying file "
						+ files[i].getName());
				if (files[i].isDirectory())
					directories.add(files[i]);
			}
		FilenameFilter filter = new FilenameFilter(){
			@Override
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".mp3");
			}
		};
		files = directory.listFiles(filter);
		if (files != null){
			for(File infile : files){
				Log.d(Constants.LOG_TAG, "ScanDirectory().added file " + infile.getName());
			}
		}
		for(File secondaryDirectory : directories){
			list.addAll(scanDirectory(secondaryDirectory));
		}
		return list;
	}

	public List<Item> scanSystem() {
		List<Item> listItems = new ArrayList<Item>();
		List<File> listFiles = scanDirectory(root);
		for (File inFile : listFiles) {
			try {
				listItems.add(new TrackImpl(inFile));
			} catch (CannotReadException e) {
			} catch (IOException e) {
			} catch (TagException e) {
			} catch (ReadOnlyFileException e) {
			} catch (InvalidAudioFrameException e) {
			}
		}
		return listItems;
	}

	public List<String> getSerializedItems() {
		List<Item> items = scanSystem();
		List<String> serializedItems = new ArrayList<String>();
		for (Item inItem : items) {
			serializedItems.add(inItem.serialize());
		}
		return serializedItems;
	}
}
