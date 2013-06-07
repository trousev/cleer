package pro.trousev.cleer.sys;
import java.io.Serializable;

import pro.trousev.cleer.Item.ReadOnlyTagException;
import pro.trousev.cleer.Item.Tag;
import pro.trousev.cleer.Item.TagType;
import pro.trousev.cleer.Item.ContentType;;
public class TagImpl implements Serializable, Tag
{
	private static final long serialVersionUID = -3740018873748993979L;
	private String _name;
	private String _value;
	private TagType _type;
	private ContentType _content;
	boolean _writeable;
	TagImpl(String tag_name, String tag_value, TagType tag_type, ContentType tag_content_type, boolean is_writeable)
	{
		configure(tag_name, tag_value, tag_type, tag_content_type, is_writeable);
	}
	TagImpl(){}
	private void configure(String tag_name, String tag_value, TagType tag_type, ContentType tag_content_type, boolean is_writeable)
	{
		_name = tag_name;
		_value = tag_value;
		_type = tag_type;
		_content = tag_content_type;
		_writeable = is_writeable;
	}
	@Override
	public String name() {
		return _name;
	}

	@Override
	public String value() {
		return _value;
	}

	@Override
	public TagType type() {
		return _type;
	}

	@Override
	public boolean isWriteable() {
		return _writeable;
	}

	@Override
	public void setValue(String new_value) throws ReadOnlyTagException {
		if(!_writeable) throw new ReadOnlyTagException(name());
		_value = new_value;
	}
	
}
