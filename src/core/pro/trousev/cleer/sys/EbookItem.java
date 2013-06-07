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

			class MyContentHandler extends DefaultHandler {

			    private HashMap<String, Object> outlineMap;
			    private HashMap<String, String> outlineData;
			    private String key;

			    public void startDocument() throws SAXException {
			    	// Do something
			    }

			    public void endDocument() throws SAXException {
			    	// Do comething
			    }

			    public void startElement(String uri, String localName, String qName,
			        Attributes atts) throws SAXException {
			    	String tag_name = qName;
			    	String parameter = atts.getValue("parameter");
			    }

			    public void endElement(String uri, String localName, String qName)
			    throws SAXException {
			        if(qName.equalsIgnoreCase("OUTLINE")) {
			            outlineMap.put(key, outlineData);
			        }
			    }
			}
			return null;
		}
	};
}
