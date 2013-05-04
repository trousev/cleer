package pro.trousev.cleer.sys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Database.DatabaseObject;
import pro.trousev.cleer.Item.Tag.ReadOnlyTagException;

public class TrackImpl implements Item {
	
	DatabaseObject _link = null;
	File _filename;
	//FIXME: make statistics
	//TODO: add tag "genre"
	//TODO: add service tag "type"
	private static final String[] _strict_tags = {"album", "artist", "title", "year", "number", "lyrics", "rating", "genre", "stat_player", "stat_repeated", "stat_skipped"};
	//ВНИМАНИЕ, ДУБЛИРОВАНИЕ ИНФОРМАЦИИ!
	// В интересах быстродействия я оставляю правило "Индекс в этом массиве должен в точности совпадать с Tag.name()
	// за этим придется следить из кода -- ВНИМАТЕЛЬНО.
	// Иными словами, никакими методами, кроме интерфейсных, в теги писать НЕЛЬЗЯ
	private Map<String, Tag> _all_tags; 
	
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
		_all_tags = new HashMap<String, Tag>();
		_all_tags.put("album", new TagImpl("album", head.getAlbum(), TagType.StrictlyClassified, ContentType.String, false));
		_all_tags.put("artist", new TagImpl("artist", head.getArtist(), TagType.StrictlyClassified, ContentType.String, false));
		_all_tags.put("title", new TagImpl("title", head.getTitle(), TagType.StrictlyClassified, ContentType.String, false));
		_all_tags.put("year", new TagImpl("year", head.getYear(), TagType.StrictlyClassified, ContentType.Numeric, false));
		_all_tags.put("number", new TagImpl("number", head.getTrack(), TagType.StrictlyClassified, ContentType.Numeric, false));
		_all_tags.put("lyrics", new TagImpl("lyrics", head.getLyrics(), TagType.StrictlyClassified, ContentType.String, false));
		_all_tags.put("rating", new TagImpl("rating", head.getRating(), TagType.StrictlyClassified, ContentType.Numeric, true));
		_all_tags.put("genre", new TagImpl("genre", head.getGenre(), TagType.StrictlyClassified, ContentType.String, true));
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
			oos.writeObject(_all_tags);
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
			_all_tags = (Map<String, Tag>) iis.readObject();
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
	private void updateTagInDatabase() throws DatabaseError
	{
		_link.update(serialize(), getSearchQuery());
	}
	public void setTagValue(String name, String value) throws NoSuchTagException, ReadOnlyTagException
	{
		Tag tag = tag(name);
		tag.setValue(value);
		AudioFileHeader head = new AudioFileHeader();
		try {
			head.readFromFile(_filename);
			head.begin();
			head.setRating(value);
			head.commit();
			updateTagInDatabase();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	@Override
	public String toString()
	{
		try {
			return tag("title") + " by " + tag("artist");
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
		
		for(String tag: tagNames())
			try {
				query += String.format("%s:%s ",tag,tag(tag).value());
			} catch (NoSuchTagException e) {
				query += String.format("%s:<NULL> ",tag);
			}

		query += " ";
		int rating;
		try
		{
			rating = new Integer(tag("rating").value());
		}
		catch (Exception e)
		{
			rating = 0;
		}
		for(int i=0; i<rating; i++)
			query += "*";
		query += String.format(" rating:%d",rating);
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
	@Override
	public boolean addTag(String name, String value) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Tag tag(String name) throws NoSuchTagException {
		Tag ans = _all_tags.get(name);
		if(ans == null)
			throw new NoSuchTagException(name);
		return ans;
	}
	@Override
	public Collection<Tag> tags(TagType type) {
		if(type == TagType.SystemTag) 
			return new ArrayList<Item.Tag>();
		if(type == TagType.StrictlyClassified)
		{
			List<Tag> ans = new ArrayList<Tag>();
			for(String s: _strict_tags)
				try {
					ans.add(tag(s));
				} catch (NoSuchTagException e) {
					// This SHOULD be silently ignored, all O'k
				}
			return ans;
		}
		if(type == TagType.SoftlyClassified)
		{
			//FIXME: Implement it!
			return null;
		}
		return null;
	}
	@Override
	public Collection<Tag> tags() {
		return _all_tags.values();
	}
	@Override
	public String[] tagNames(TagType type) {
		if(type == TagType.SystemTag) 
			return new String[0];
		if(type == TagType.StrictlyClassified)
		{
			return _strict_tags;
		}
		if(type == TagType.SoftlyClassified)
		{
			//FIXME: Implement it!
			return null;
		}
		return null;
	}
	@Override
	public String[] tagNames() {
		//FIXME: This is generally invalid!
		return _strict_tags;
	}
	@Override
	public boolean addTag(Tag tag) {
		// _all_tags.put(tag.name(), tag);
		//FIXME: implement it!
		return false;
	}
	@Override
	public boolean removeTag(String name) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public boolean removeTag(Tag tag) {
		// TODO Auto-generated method stub
		return false;
	}
}
