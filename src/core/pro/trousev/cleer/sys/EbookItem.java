package pro.trousev.cleer.sys;

import java.io.File;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Database.DatabaseObject;
import pro.trousev.cleer.Item.ReadOnlyTagException;
import pro.trousev.cleer.Item.Tag;
import pro.trousev.cleer.Item.TagAlreadyExistsException;
import pro.trousev.cleer.Item.TagType;

import java.util.HashMap;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;

public class EbookItem extends StandardItem {

	public EbookItem(DatabaseObject dataObject) throws Exception {
		super(dataObject);
		// TODO Auto-generated constructor stub
	}
	public EbookItem(Factory generator, File filename) {
		super(generator, filename);
		// TODO Auto-generated constructor stub
	}
	public static Item.Factory Factory = new Factory() {
		

		private static final long serialVersionUID = 4420965059846952745L;

		@Override
		public boolean writeTag(Item item, Tag tag) throws ReadOnlyTagException {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public boolean removeTag(Item item, Tag tag) {
			// TODO Auto-generated method stub
			return false;
		}
		
		@Override
		public Tag createTag(Item item, String name, TagType type)
				throws TagAlreadyExistsException {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
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
	};
}
