package pro.trousev.jplay.sys;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.KeyNotFoundException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.TagField;

public class AudioFileHeader implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7752453454217919971L;
	// Metainfo
	private String _artist;
	private String _album;
	private String _title;
	private String _year;
	private String _track;
	private String _lyrics;
	private String _tags;
	
	// Sourceinfo
	private String _filename;

	// Playinfo
	private String _user_rating;
	
	// Tag Writing Routines
	AudioFile _write_file = null;
	Tag _write_tag = null;
	
	public boolean begin() throws Exception
	{
		if(_write_file != null)
			throw new Exception("Writing transaction is already opened!");
		
		_write_file = AudioFileIO.read(new File(_filename));
		_write_tag = _write_file.getTag();
		return true;
	}
	public boolean commit() throws CannotWriteException
	{
		_write_file.commit();
		_write_tag = null;
		_write_file = null;
		return true;
	}
	
	public String getArtist()
	{
		return _artist;
	}
	public void setArtist(String artist) throws KeyNotFoundException, FieldDataInvalidException
	{
		_artist = artist;
		if(_write_file != null) _write_tag.setField(FieldKey.ARTIST, artist);
	}
	public String getAlbum()
	{
		return _album;
	}
	public void setAlbum(String in) throws KeyNotFoundException, FieldDataInvalidException
	{
		_album = in;
		if(_write_file != null) _write_tag.setField(FieldKey.ALBUM, in);
	}
	public String getTitle()
	{
		return _title;
	}
	public void setTitle(String in) throws KeyNotFoundException, FieldDataInvalidException
	{
		_title = in;
		if(_write_file != null) _write_tag.setField(FieldKey.TITLE, in);
	}
	public String getYear()
	{
		return _year;
	}
	public void setYear(String in) throws KeyNotFoundException, FieldDataInvalidException
	{
		_year = in;
		if(_write_file != null) _write_tag.setField(FieldKey.YEAR, in);
	}
	public String getTrack()
	{
		return _track;
	}
	public void setTrack(String in) throws KeyNotFoundException, FieldDataInvalidException
	{
		_track = in;
		if(_write_file != null) _write_tag.setField(FieldKey.TRACK, in);
	}
	public String getLyrics()
	{
		return _lyrics;
	}
	public void setLyrics(String in) throws KeyNotFoundException, FieldDataInvalidException
	{
		_lyrics = in;
		if(_write_file != null) _write_tag.setField(FieldKey.LYRICS, in);
	}
	public String getTags()
	{
		return _tags;
	}
	public void setFilename(String in) 
	{
		_filename = in;
	}
	public String getFilename()
	{
		return _filename;
	}
	public String getRating()
	{
		return _user_rating;
	}
	public void setRating(String in) throws KeyNotFoundException, FieldDataInvalidException
	{
		_user_rating = in;
		if(_write_file != null) _write_tag.setField(FieldKey.RATING, in);
	}
	public void readFromFile(File file) throws CannotReadException, IOException, TagException, ReadOnlyFileException, InvalidAudioFrameException
	{
		AudioFile f = AudioFileIO.read(file);
		Tag tag = f.getTag();
		//AudioHeader header = f.getAudioHeader();
		_artist = tag.getFirst(FieldKey.ARTIST);
		_album   = tag.getFirst(FieldKey.ALBUM);
		_title = tag.getFirst(FieldKey.TITLE);
		_year = tag.getFirst(FieldKey.YEAR);
		_track = tag.getFirst(FieldKey.TRACK);
		_lyrics = tag.getFirst(FieldKey.LYRICS);
		_tags = tag.getFirst(FieldKey.TAGS);
		_filename = file.getAbsolutePath();
		_user_rating = tag.getFirst(FieldKey.RATING);
		
		//System.out.println(FieldKey.valueOf("TXXX").toString());
		
		//tag.getValue(arg0, arg1)
	}
	public String toString()
	{
		return String.format("%s %s -- %s -- %s",_year, _title,_album,_artist);
	}
	
}
