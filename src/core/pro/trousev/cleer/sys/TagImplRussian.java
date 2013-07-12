package pro.trousev.cleer.sys;

import java.io.UnsupportedEncodingException;


import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.ContentType;
import pro.trousev.cleer.Item.TagType;

public class TagImplRussian extends StandardTag {

	private static final long serialVersionUID = 1825269849445311835L;

	TagImplRussian(Item parent,String tag_name, String tag_value, TagType tag_type, ContentType tag_content_type, boolean is_writeable)
	{
		super(parent, tag_name, tag_value, tag_type, tag_content_type, is_writeable);
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
		if (s.length() == 0)
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
