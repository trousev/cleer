package pro.trousev.cleer;

import java.io.File;
import java.util.List;

import pro.trousev.cleer.Database.DatabaseObject;

/**
 * Эта штуковина репрезентует один музыкальный трек коллекции, связанный с тем или иным объектом ФС
 * @author doctor
 *
 */
public interface Track {
	// Meta
	public String[] getAllTagNames();
	public String getTagValue(String name) throws NoSuchTagException;
	public void setTagValue(String name, String value) throws NoSuchTagException;
	public void incrementTagValue(String name) throws NoSuchTagException;
	public File filename();
	public boolean tagIsWriteable(String name) throws NoSuchTagException;
	public boolean tagIsNumeric(String name) throws NoSuchTagException;

	// Save & restore
	String serialize();
	boolean deserialize(String contents);
	String getSearchQuery();
	
	// Link
	DatabaseObject linkedObject();

	/// Exceptions
	public static class NoSuchTagException extends Exception
	{
		private static final long serialVersionUID = -3294597277551186761L;
		public NoSuchTagException(String tagName) {
			super(String.format("Cannot find tag: ",tagName));
		}
	}
}
