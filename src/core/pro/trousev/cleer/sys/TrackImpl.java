package pro.trousev.cleer.sys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Database.DatabaseObject;

public class TrackImpl implements Item {
	DatabaseObject _link = null;
	File _filename;
	//FIXME: make statistics
	//TODO: add tag genre
	private static final String[] _all_tags = {"album", "artist", "title", "year", "number", "lyrics", "rating", "stat_player", "stat_repeated", "stat_skipped"};
	private Map<String, String> _tags;
	public TrackImpl(Database.DatabaseObject dataObject) throws Exception
	{
		if(!deserialize(dataObject.contents()))
			throw new Exception("Deserialisation failed");
		_link = dataObject;
	}
	private void readMetadataFromFile() throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		AudioFileHeader head = new AudioFileHeader();
		head.readFromFile(_filename);
		_tags = new HashMap<String, String>();
		_tags.put("album", head.getAlbum());
		_tags.put("artist", head.getArtist());
		_tags.put("title", head.getTitle());
		_tags.put("year", head.getYear());
		_tags.put("number", "-1"); //FIXME: fix it
		_tags.put("lyrics", head.getLyrics());
		_tags.put("rating", head.getRating());
	}
	public TrackImpl(File filename) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		_filename = filename;
		readMetadataFromFile();
	}

	@Override
	public String serialize() {
		try {
			ByteArrayOutputStream serializator = new ByteArrayOutputStream(256);
			ObjectOutputStream oos = new ObjectOutputStream(serializator);
			oos.writeObject(_tags);
			oos.writeObject(_filename);
			oos.flush();
			oos.close();
			return Base64.encode(serializator.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean deserialize(String contents) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(contents));
			ObjectInputStream iis = new ObjectInputStream(in);
			_tags = (Map<String, String>) iis.readObject();
			_filename = (File) iis.readObject();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (ClassCastException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	public void setTagValue(String name, String value) throws NoSuchTagException
	{
		if(name.equals("rating"))
		{
			AudioFileHeader head = new AudioFileHeader();
			try {
				head.readFromFile(_filename);
				head.begin();
				head.setRating(value);
				head.commit();
				_tags.put(name, value);
				_link.update(serialize(), getSearchQuery());
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		else throw new NoSuchTagException(name);
	}

	@Override
	public String toString()
	{
		try {
			return getTagValue("title") + " by " + getTagValue("artist");
		} catch (NoSuchTagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	@Override
	public String getSearchQuery() {
		String query = String.format("");
		
		query += " file:"+_filename.getAbsolutePath();
		
		for(String tag: getAllTagNames())
			try {
				query += String.format("%s:%s ",tag,getTagValue(tag));
			} catch (NoSuchTagException e) {
				query += String.format("%s:<NULL> ",tag);
			}

		query += " ";
		int rating;
		try
		{
			rating = new Integer(getTagValue("rating"));
		}
		catch (Exception e)
		{
			rating = 0;
		}
		for(int i=0; i<rating; i++)
			query += "*";
		return query;
	}
	@Override
	public File filename() {
		return _filename;
	}
	@Override
	public DatabaseObject linkedObject() {
		return _link;
	}
	
	@Override
	public String[] getAllTagNames() {
		return _all_tags;
	}
	@Override
	public String getTagValue(String name) throws NoSuchTagException {
		String value = _tags.get(name);
		if(value == null)
			throw new NoSuchTagException(name);
		return value;
	}
	@Override
	public boolean tagIsWriteable(String name) {
		if(name.equals("rating")) return true;
		return false;
	}
	@Override
	public boolean tagIsNumeric(String name) {
		if(name.equals("rating")) return true;
		return false;
	}
	@Override
	public void incrementTagValue(String name) throws NoSuchTagException {
		//FIXME: make statistics
		return ;
	}
	private static class __Factory implements Item.Factory
	{

		@Override
		public Item createTrack(File filename) {
			try {
				return new TrackImpl(filename);
			} catch (CannotReadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReadOnlyFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAudioFrameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}
	public final static __Factory Factory = new __Factory();
}
