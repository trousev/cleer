package pro.trousev.cleer.sys;

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

	private static final long serialVersionUID = -7752453454217919971L;
	// Metainfo
	private String _artist;
	private String _album;
	private String _title;
	private String _year;
	private String _track;
	private String _lyrics;
	private String _tags;
	private String _genre;
	
	// Sourceinfo
	private String _filename;

	// Playinfo
	private String _user_rating;

	// Tag Writing Routines
	AudioFile _write_file = null;
	Tag _write_tag = null;

	public boolean begin() throws Exception {
		if (_write_file != null)
			throw new Exception("Writing transaction is already opened!");

		_write_file = AudioFileIO.read(new File(_filename));
		_write_tag = _write_file.getTag();
		return true;
	}

	public boolean commit() throws CannotWriteException {
		_write_file.commit();
		_write_tag = null;
		_write_file = null;
		return true;
	}

	public String getGenre() {
		return _genre;
	}

	public void setGenre(String genre) throws KeyNotFoundException,
			FieldDataInvalidException {
		_genre = genre;
		if (_write_file != null)
			_write_tag.setField(FieldKey.GENRE, genre);
	}

	public String getArtist() {
		return _artist;
	}

	public void setArtist(String artist) throws KeyNotFoundException,
			FieldDataInvalidException {
		_artist = artist;
		if (_write_file != null)
			_write_tag.setField(FieldKey.ARTIST, artist);
	}

	public String getAlbum() {
		return _album;
	}

	public void setAlbum(String in) throws KeyNotFoundException,
			FieldDataInvalidException {
		_album = in;
		if (_write_file != null)
			_write_tag.setField(FieldKey.ALBUM, in);
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String in) throws KeyNotFoundException,
			FieldDataInvalidException {
		_title = in;
		if (_write_file != null)
			_write_tag.setField(FieldKey.TITLE, in);
	}

	public String getYear() {
		return _year;
	}

	public void setYear(String in) throws KeyNotFoundException,
			FieldDataInvalidException {
		_year = in;
		if (_write_file != null)
			_write_tag.setField(FieldKey.YEAR, in);
	}

	public String getTrack() {
		return _track;
	}

	public void setTrack(String in) throws KeyNotFoundException,
			FieldDataInvalidException {
		_track = in;
		if (_write_file != null)
			_write_tag.setField(FieldKey.TRACK, in);
	}

	public String getLyrics() {
		return _lyrics;
	}

	public void setLyrics(String in) throws KeyNotFoundException,
			FieldDataInvalidException {
		_lyrics = in;
		if (_write_file != null)
			_write_tag.setField(FieldKey.LYRICS, in);
	}

	public void setTags(String in) throws KeyNotFoundException, FieldDataInvalidException {
		_tags = in;
		if (_write_file != null)
			_write_tag.setField(FieldKey.TAGS, in);
	}
	public String getTags() {
		return _tags;
	}

	public void setFilename(String in) {
		_filename = in;
	}

	public String getFilename() {
		return _filename;
	}

	public String getRating() {
		return _user_rating;
	}

	public void setRating(String in) throws KeyNotFoundException,
			FieldDataInvalidException {
		_user_rating = in;
		if (_write_file != null)
			_write_tag.setField(FieldKey.RATING, in);
	}

	public void readFromFile(File file) throws CannotReadException,
			IOException, TagException, ReadOnlyFileException,
			InvalidAudioFrameException {
		// FIXME Normal exception catch/throw
		AudioFile f = null;
		try {
			f = AudioFileIO.read(file);
		} 
		catch (NoClassDefFoundError e)
		{
			System.out.println("[pro.trousev.cleer] " + e.getClass().toString() + " : " + e.getMessage());
			e.printStackTrace();
			return ;
		}
		catch (Throwable e)
		{
			System.out.println("No tags for "+file.getName()+", falling to filenames.");
			_title = file.getName();
			try
			{
				_album = "sugg:"+file.getParentFile().getName();
			} catch(Throwable e1) { _album = "Unknown Album"; }
			try
			{
				_artist = "sugg:"+file.getParentFile().getParentFile().getName();
			} catch(Throwable e1) { _artist = "Unknown Artist"; }
			_filename = file.getAbsolutePath();
			return ; // FIXME: make "unknown" genre etc
		}
		Tag tag = null;
		try {
			tag = f.getTag();
		} catch (NullPointerException e) {
		}
		if (tag == null) {
			//TODO make normal extentions cat
			_title = file.getName();
			return;
		}
		// AudioHeader header = f.getAudioHeader();
		_artist = tag.getFirst(FieldKey.ARTIST);
		_album = tag.getFirst(FieldKey.ALBUM);
		_title = tag.getFirst(FieldKey.TITLE);
		_year = tag.getFirst(FieldKey.YEAR);
		_track = tag.getFirst(FieldKey.TRACK);
		_lyrics = tag.getFirst(FieldKey.LYRICS);
		_tags = tag.getFirst(FieldKey.TAGS);
		_genre = tag.getFirst(FieldKey.GENRE);
		_filename = file.getAbsolutePath();
		_user_rating = tag.getFirst(FieldKey.RATING);

		try {
			if (_user_rating == null || _user_rating.length() == 0  ) {
				Iterator<TagField> iter = tag.getFields();
				while (iter.hasNext()) {
					TagField field = iter.next();
					if (field.getId().equals("TXXX")) {
						String s = field.toString();
						s = s.replaceAll("Description=\"rating\"; Text=\"", "");
						if (s.contains("Description"))
							continue;
						s = s.replaceAll("[^0-9]+", "");
						_user_rating = s;
						// if(_user_rating <= 0) _user_rating = s;
					}
				}
			}
		} catch (Throwable t) {
		}
		// //

		// // This is Compability Layer with Winamp Comment Format
		try {
			if (_user_rating == null || _user_rating.length() == 0 ) {
				String comm = tag.getFirst(FieldKey.COMMENT);
				if (comm.contains("*****"))
					_user_rating = "5";
				else if (comm.contains("****"))
					_user_rating = "4";
				else if (comm.contains("***"))
					_user_rating = "3";
				else if (comm.contains("**"))
					_user_rating = "2";
				else if (comm.contains("*"))
					_user_rating = "1";
			}
		} catch (Throwable t) {
		}
		// //

		// tag.getValue(arg0, arg1)
	}

	public String toString() {
		return String.format("%s %s -- %s -- %s (r:%s)", _year, _title, _album,
				_artist, _user_rating);
	}

}
