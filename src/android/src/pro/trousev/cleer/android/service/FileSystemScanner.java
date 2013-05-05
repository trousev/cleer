package pro.trousev.cleer.android.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.sys.TrackImpl;
import android.os.Environment;

public class FileSystemScanner {
	private File root = Environment.getExternalStorageDirectory();

	
	private List<File> scanDirectory(File directory) {
		List<File> list = new ArrayList<File>();
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; ++i) {
			if ((files[i].isFile()) && (files[i].getName().endsWith(".mp3"))) {
				list.add(files[i]);
			}
			if (files[i].isDirectory())
				list.addAll(scanDirectory(files[i]));
		}
		return list;
	}
	public List<Item> scanSystem(){
		List<Item> listItems = new ArrayList<Item>();
		List<File> listFiles = scanDirectory(root);
		for (File inFile : listFiles){
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
	public List<String> getSerializedItems(){
		List<Item> items = scanSystem();
		List<String> serializedItems = new ArrayList<String>();
		for(Item inItem : items){
			serializedItems.add(inItem.serialize());
		}
		return serializedItems;
	}
}
