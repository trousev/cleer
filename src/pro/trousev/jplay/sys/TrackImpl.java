package pro.trousev.jplay.sys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.TagException;

import pro.trousev.jplay.Database;
import pro.trousev.jplay.Track;

public class TrackImpl implements Track {

	AudioFileHeader _header = null;
	public TrackImpl(Database.DatabaseObject dataObject) throws Exception
	{
		_header = new AudioFileHeader();
		if(!deserialize(dataObject.contents()))
			throw new Exception("Deserialisation failed");
	}
	public TrackImpl(File filename) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		_header = new AudioFileHeader();
		_header.readFromFile(filename);
	}
	@Override
	public String artist() {
		return _header.getArtist();
	}

	@Override
	public String album() {
		return _header.getAlbum();
	}

	@Override
	public String title() {
		return _header.getTitle();
	}

	@Override
	public String year() {
		return _header.getYear();
	}

	@Override
	public String sequence_number() {
		return _header.getTrack();
	}

	@Override
	public String lyrics() {
		return _header.getLyrics();
	}

	@Override
	public List<String> tags() {
		return new ArrayList<String>(Arrays.asList(_header.getTags().split(" ")));
	}

	@Override
	public int user_rating() {
		try 
		{
			return new Integer(_header.getRating());
		}
		catch  (Throwable t)
		{
			return 0;
		}
	}

	@Override
	public int play_count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int skip_count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int repeat_count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int auto_rating() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String serialize() {
		try {
			ByteArrayOutputStream serializator = new ByteArrayOutputStream(256);
			ObjectOutputStream oos = new ObjectOutputStream(serializator);
			oos.writeObject(_header);
			oos.flush();
			oos.close();
			return Base64.encode(serializator.toByteArray());
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public boolean deserialize(String contents) {
		try {
			ByteArrayInputStream in = new ByteArrayInputStream(Base64.decode(contents));
			ObjectInputStream iis = new ObjectInputStream(in);
			_header = (AudioFileHeader) iis.readObject();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void set_user_rating(int rating) {
		try {
			_header.setRating(String.format("%d",rating));
		} catch (KeyNotFoundException e) {
			e.printStackTrace();
		} catch (FieldDataInvalidException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void stat_played() {
		// TODO Auto-generated method stub
	}

	@Override
	public void stat_skipped() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void stat_repeated() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String toString()
	{
		return _header.toString();
	}
	@Override
	public String generate_query() {
		String query = String.format("title:%s artist:%s album:%s", title(), artist(), album());
		
		query += " file:"+_header.getFilename();
		
		for(String tag: tags())
			query += "tag:"+tag;
		for(String tag: tags())
			query += " "+tag;
		
		query += " ";
		for(int i=0; i<user_rating(); i++)
			query += "*";
		return query;
	}
	@Override
	public File filename() {
		return new File(_header.getFilename());
	}
	

}
