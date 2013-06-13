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


import pro.trousev.cleer.Database;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Database.DatabaseObject;

public class StandardItem implements Item {
	DatabaseObject _link = null;
	File _filename;
    Item.Factory _generator;
	private Map<String, Tag> _all_tags; 
	public  Map<String, Tag> getAllTags()
	{
		return _all_tags;
	}
	
	public StandardItem(Database.DatabaseObject dataObject) throws Exception
	{
		if(!deserialize(dataObject.contents()))
			throw new Exception("Deserialisation failed");
		_link = dataObject;
	}
    public StandardItem(Item.Factory generator, File filename)
    {
        _filename = filename;
        _generator = generator;
        _all_tags = new HashMap<String, Tag>();
    }

	@Override
	public String serialize() {
		try {
			ByteArrayOutputStream serializator = new ByteArrayOutputStream(256);
			ObjectOutputStream oos = new ObjectOutputStream(serializator);
			oos.writeObject(_all_tags);
			oos.writeObject(_filename);
            oos.writeObject(_generator);
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
			for(Tag t: _all_tags.values())
				t.setParent(this);
			_filename = (File) iis.readObject();
            _generator = (Item.Factory) iis.readObject();
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
		Map<String, String> dbtags = new HashMap<String, String>();
		for(Tag tag: tags())
			dbtags.put(tag.name(), tag.value());
		_link.update_tags(dbtags);
	}
	@Override
	public void setTagValue(String name, String value) throws NoSuchTagException, ReadOnlyTagException, DatabaseError
	{
		Tag tag = firstTag(name);
        String prev = firstTag(name).value();
		tag.doSetValue(value);
        if(!_generator.writeTag(this, tag))
        {
            tag.doSetValue(prev);
            throw new ReadOnlyTagException(tag.name());
        }
        updateTagInDatabase();
	}

	@Override
	public String toString()
	{
        //FIXME: make more proper toString() call
		try {
			return firstTag("title") + " by " + tag("artist");
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
				query += String.format("%s:%s ",tag,firstTag(tag).value());
			} catch (NoSuchTagException e) {
				query += String.format("%s:<NULL> ",tag);
			}

		query += " ";
		int rating;
		try
		{
			rating = new Integer(firstTag("rating").value());
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
	
	@Override
	public boolean addTag(String name, String value) throws TagAlreadyExistsException, ReadOnlyTagException 
    {
        Tag t = _generator.createTag(this, name, TagType.SoftlyClassified);
        t.setValue(value);
        _all_tags.put(name, t);
        return true;
	}
	@Override
	public List<Tag> tag(String name) 
	{
		List<Tag> ans = new ArrayList<Item.Tag>();
		try {
			ans.add(firstTag(name));
		} catch (NoSuchTagException e) {
			// This can and should be silently ingnored. No tags -- OK, return empty array.
		}
		return ans;
	}
	@Override
	public Tag firstTag(String name) throws NoSuchTagException {
	    Tag ans = _all_tags.get(name);
		if(ans == null)
			throw new NoSuchTagException(name);
		return ans;
	}
	@Override
	public Collection<Tag> tags(TagType type) {
		List<Tag> ans = new ArrayList<Tag>();
		for(Tag t: _all_tags.values())
            if(t.type() == type)
                ans.add(t);
		return ans;
	}
	@Override
	public Collection<Tag> tags() {
		return _all_tags.values();
	}
	@Override
	public List<String> tagNames(TagType type) {
        List<String> ans = new ArrayList<String>();
        for(Tag t: _all_tags.values())
            if(t.type() == type || type == null)
                ans.add(t.name());
        return ans;
	}
	@Override
	public List<String> tagNames() {
		//FIXME: This is generally invalid!
		return tagNames(null);
	}
	@Override
	public boolean removeTag(String name) throws NoSuchTagException {
        if(_generator.removeTag(this, firstTag(name)))
        {
            _all_tags.remove(name);
            return true;
        }
		return false;
	}

	@Override
	public boolean removeTag(Tag tag) throws NoSuchTagException {
        return removeTag(tag.name());
	}
	@Override
	public boolean addTag(Tag tag) {
		// TODO Auto-generated method stub
		return false;
	}
}
