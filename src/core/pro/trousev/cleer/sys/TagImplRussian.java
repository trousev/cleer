package pro.trousev.cleer.sys;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Item.ContentType;
import pro.trousev.cleer.Item.TagType;

public class TagImplRussian extends TagImpl {

	private static final long serialVersionUID = 1825269849445311835L;

	TagImplRussian(String tag_name, String tag_value, TagType tag_type, ContentType tag_content_type, boolean is_writeable)
	{
		super(tag_name, tag_value, tag_type, tag_content_type, is_writeable);
	}
	TagImplRussian()
	{
		super();
	}
	@Override
	public String value() {
		String was = super.value();
		String now = encoding_change(was);
		return now;
		
	}

	private String encoding_change(String s) {
		if (s == null)
			return null;
		if (s.isEmpty())
			return s;

		try {
			if ((int) s.charAt(0) < 1040)
			{
				String dec = new String(s.getBytes("windows-1252"), "windows-1251");
				return dec;
			} 
			else
			{
				return s;
			}
		} catch (UnsupportedEncodingException e) {
			return s;
		}
	}
}
