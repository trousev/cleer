package pro.trousev.cleer.sys;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Database.DatabaseObject;
import pro.trousev.cleer.Item.ReadOnlyTagException;
import pro.trousev.cleer.Item.Tag;
import pro.trousev.cleer.Item.TagAlreadyExistsException;
import pro.trousev.cleer.Item.TagType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;

/**
 * 
 * 		@Override
		public Item createItem(File filename) {
			// TODO Auto-generated method stub

			class Fb2Parser extends DefaultHandler {

			    private HashMap<String, Object> outlineMap;
			    private HashMap<String, String> outlineData;
			    private String key;
                boolean is_titleinfo = false;
                boolean is_description = false;
                String[] author = {"","",""}; 
                String[] common_tags = {"genre", "book-title", "date", "lang", "src-lang"};
                String common_value;

                public void declare(String name, String value)
                {
                    System.out.println("DECLARED: "+name+"\t"+value);
                }
			    public void startDocument() throws SAXException {
			    	// Do something
                    is_titleinfo = false;
                    is_description = false;
			    }

			    public void endDocument() throws SAXException {
			    	// Do comething
			    }

			    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException 
                {
                    if(qName.equals("title-info")) is_titleinfo = true;
                    if(qName.equals("annotation")) {
                        is_description = true;
                    }
                    if(!is_titleinfo) return ;
                    if(is_description) return ;
                    common_value = "";
                    //System.out.println("Tag: " + qName);
                    if(qName.equals("author"))
                    {
                        author[0] = "";
                        author[1] = "";
                        author[2] = "";
                    }
                    if(qName.equals("sequence"))
                    {
                        declare("sequence",atts.getValue("name"));
                        declare("sequence-no", atts.getValue("number"));
                    }
			    	//String tag_name = qName;
			    	//String parameter = atts.getValue("parameter");
			    }

                public void characters(char[] stream, int start, int len) throws SAXException
                {
                    if(!is_titleinfo) return ;
                    if(is_description) return ;
                    String text = new String(stream, start, len);
                    //System.out.println("       :: " + text);
                    common_value += text;
                }
			    public void endElement(String uri, String localName, String qName) throws SAXException 
                {
                    if(qName.equals("title-info")) is_titleinfo = false;
                    if(qName.equals("annotation")) is_description = false;
                    if(!is_titleinfo) return ;
                    if(is_description) return ;
                    for(String n:common_tags) 
                        if(n.equals(qName))
                            declare(qName, common_value);
                    if(qName.equals("first-name"))
                        author[0] = common_value;
                    if(qName.equals("middle-name"))
                        author[1] = common_value;
	                if(qName.equals("last-name"))
                        author[2] = common_value;
                    if(qName.equals("author"))
                    {
                        String author_name = "";
                        for(String part: author)
                        {
                            if(part.equals("")) continue;
                            if(!author_name.equals("")) author_name += " ";
                            author_name += part;
                        }
                        if(!author_name.equals(""))
                            declare("author",author_name);

                    }
                    if(qName.equals("keywords"))
                    {
                        for(String kw: common_value.split(","))
                        {
                            kw = kw.trim();
                            declare("keyword",kw);
                        }
                    }
	
			    }
			}
            try
            {
              SAXParserFactory factory = SAXParserFactory.newInstance(); 
              SAXParser aparser = factory.newSAXParser();
              Fb2Parser parser = new Fb2Parser();
              aparser.parse(filename, parser);
              return null;
            } catch (Throwable e) {
              return null;
            }
		}

 *
 */


public class EbookItem implements Item, Serializable
{

	private static final long serialVersionUID = 5722667120772806122L;

	class EbookItemTag implements Tag, Serializable
	{
		private static final long serialVersionUID = 9146097072555981478L;
		
		public String name;
		public String value;
		
		@Override
		public String name() {
			return name;
		}

		@Override
		public String value() {
			return value;
		}

		@Override
		public TagType type() {
			return TagType.SoftlyClassified;
		}

		@Override
		public boolean isWriteable() {
			return false;
		}

		@Override
		public void setValue(String new_value) throws ReadOnlyTagException {
			throw new ReadOnlyTagException(name);
		}

		@Override
		public void setParent(Item parent) {
		}

		@Override
		public void doSetValue(String new_value) {
		}
	}
	
	
	HashMap<String, ArrayList<Tag> > _tags; 
	File _filename;
	DatabaseObject _link;
	public EbookItem(DatabaseObject dbo)
	{
		deserialize(dbo.contents());
	}
	public EbookItem(File f)
	{
		_filename = f;
		_tags = new HashMap<String, ArrayList<Tag>>();
		// TODO Auto-generated method stub

		class Fb2Parser extends DefaultHandler {

		    private HashMap<String, Object> outlineMap;
		    private HashMap<String, String> outlineData;
		    private String key;
            boolean is_titleinfo = false;
            boolean is_description = false;
            String[] author = {"","",""}; 
            String[] common_tags = {"genre", "book-title", "date", "lang", "src-lang"};
            String common_value;

            public void declare(String name, String value)
            {
                //System.out.println("DECLARED: "+name+"\t"+value);
                createTag(name, value);
            }
		    public void startDocument() throws SAXException {
		    	// Do something
                is_titleinfo = false;
                is_description = false;
		    }

		    public void endDocument() throws SAXException {
		    	// Do comething
		    }
		    private void createTag(String name, String value)
		    {
		    	ArrayList<Tag> l = _tags.get(name);
		    	if(l == null)
		    	{
		    		l = new ArrayList<Item.Tag>();
		    		_tags.put(name, l);
		    	}
		    	EbookItemTag t = new EbookItemTag();
		    	t.name = name;
		    	t.value = value;
		    	l.add(t);
		    }

		    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException 
            {
                if(qName.equals("title-info")) is_titleinfo = true;
                if(qName.equals("annotation")) {
                    is_description = true;
                }
                if(!is_titleinfo) return ;
                if(is_description) return ;
                common_value = "";
                //System.out.println("Tag: " + qName);
                if(qName.equals("author"))
                {
                    author[0] = "";
                    author[1] = "";
                    author[2] = "";
                }
                if(qName.equals("sequence"))
                {
                    declare("sequence",atts.getValue("name"));
                    declare("sequence-no", atts.getValue("number"));
                }
		    	//String tag_name = qName;
		    	//String parameter = atts.getValue("parameter");
		    }

            public void characters(char[] stream, int start, int len) throws SAXException
            {
                if(!is_titleinfo) return ;
                if(is_description) return ;
                String text = new String(stream, start, len);
                //System.out.println("       :: " + text);
                common_value += text;
            }
		    public void endElement(String uri, String localName, String qName) throws SAXException 
            {
                if(qName.equals("title-info")) is_titleinfo = false;
                if(qName.equals("annotation")) is_description = false;
                if(!is_titleinfo) return ;
                if(is_description) return ;
                for(String n:common_tags) 
                    if(n.equals(qName))
                        declare(qName, common_value);
                if(qName.equals("first-name"))
                    author[0] = common_value;
                if(qName.equals("middle-name"))
                    author[1] = common_value;
                if(qName.equals("last-name"))
                    author[2] = common_value;
                if(qName.equals("author"))
                {
                    String author_name = "";
                    for(String part: author)
                    {
                        if(part.equals("")) continue;
                        if(!author_name.equals("")) author_name += " ";
                        author_name += part;
                    }
                    if(!author_name.equals(""))
                        declare("author",author_name);
                }
                if(qName.equals("keywords"))
                {
                    for(String kw: common_value.split(","))
                    {
                        kw = kw.trim();
                        declare("keyword",kw);
                    }
                }
		    }
		}
        try
        {
          SAXParserFactory factory = SAXParserFactory.newInstance(); 
          SAXParser aparser = factory.newSAXParser();
          Fb2Parser parser = new Fb2Parser();
          aparser.parse(_filename, parser);
        } catch (Throwable e) {
        	e.printStackTrace();
        }
	}
	@Override
	public Tag firstTag(String name) throws NoSuchTagException {
		List<Tag> l = tag(name);
		if(l == null || l.isEmpty())
			throw new NoSuchTagException(name);
		return l.get(0);
	}

	@Override
	public List<Tag> tag(String name) throws NoSuchTagException {
		List<Tag> ans = _tags.get(name);
		if(ans == null) ans = new ArrayList<Tag>();
		return ans;
	}

	@Override
	public Collection<Tag> tags(TagType type) {
		List<Tag> ans = new ArrayList<Tag>();
		if(type == TagType.SoftlyClassified)
			for(List<Tag> l: _tags.values())
				for(Tag t: l)
					ans.add(t);
		return ans;
	}

	@Override
	public Collection<Tag> tags() {
		return tags(TagType.SoftlyClassified);
	}

	@Override
	public List<String> tagNames(TagType type) {
		List<String> ans = new ArrayList<String>();
		if(type == TagType.SoftlyClassified)
			for(String name: _tags.keySet())
				ans.add(name);
		return ans;
	}

	@Override
	public List<String> tagNames() {
		return tagNames(TagType.SoftlyClassified);
	}

	@Override
	public boolean addTag(Tag tag) {
		return false;
	}

	@Override
	public boolean addTag(String name, String value)
			throws TagAlreadyExistsException, ReadOnlyTagException {
		return false;
	}

	@Override
	public boolean removeTag(String name) throws NoSuchTagException {
		return false;
	}

	@Override
	public boolean removeTag(Tag tag) throws NoSuchTagException {
		return false;
	}

	@Override
	public void setTagValue(String name, String value)
			throws NoSuchTagException, ReadOnlyTagException, DatabaseError {
	}

	@Override
	public File filename() {
		return _filename;
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
			_tags = (HashMap<String, ArrayList<Tag>>) iis.readObject();
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
		return query;
	}

	@Override
	public DatabaseObject linkedObject() {
		return _link;
	}
	
	public static Item.Factory Factory = new Factory() {
		
		private static final long serialVersionUID = 4420965059846952745L;

		@Override
		public boolean writeTag(Item item, Tag tag) throws ReadOnlyTagException {
			return false;
		}
		
		@Override
		public boolean removeTag(Item item, Tag tag) {
			return false;
		}
		
		@Override
		public Tag createTag(Item item, String name, TagType type)
				throws TagAlreadyExistsException {
			return null;
		}
		
		@Override
		public Item createItem(File filename) {
			return new EbookItem(filename);
		}

		@Override
		public Item createItem(DatabaseObject dbo) {
			return new EbookItem(dbo);
		}
	}; 

}
