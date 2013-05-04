package pro.trousev.cleer;

import java.io.File;
import java.util.Collection;

import pro.trousev.cleer.Database.DatabaseObject;

/**
 * Эта штуковина репрезентует один музыкальный трек коллекции, связанный с тем или иным объектом ФС
 * @author Alexander Trousevich
 *
 */
public interface Item {
	/**
	 * Список типов тегов.
	 * 
	 * SystemTag -- системный тег: разрешение картинки, битрейт и т.п.
	 * StrictlyClassified -- тег, который навязан имплементацией: имя артиста, название трека и т.п.
	 * SoftlyClassified -- тег, заданный пользователем
	 * @author doctor
	 */
	static enum TagType
	{
		SystemTag, StrictlyClassified, SoftlyClassified
	}
	static enum ContentType
	{
		String, Numeric, Blob
	}
	
	static interface Tag
	{
		/**
		 * Название тега. 
		 * @return
		 */
		public String name();
		/** 
		 * Значение тега. Если тип тега -- blob, то должна отдаваться строка в Base64 представлении
		 * @return
		 */
		public String value();
		/**
		 * Тип тега
		 * @return
		 */
		public TagType type();
		/**
		 * Указывает, является ли тег записываемым (изменяемым).
		 * @return
		 */
		public boolean isWriteable();
		/**
		 * Задает новое значение тега.
		 * @param new_value
		 */
		public void setValue(String new_value) throws ReadOnlyTagException;
		public String toString();

		public static class ReadOnlyTagException extends Exception
		{
			private static final long serialVersionUID = -3294597277551186761L;
			public ReadOnlyTagException(String tagName) {
				super(String.format("Tag is read-only: "+tagName));
			}
		}
	}
	
	/**
	 * Отдает тег по заданному имени
	 * @param name
	 * @return
	 */
	public Tag tag(String name) throws NoSuchTagException;
	/**
	 * Отдает список всех тегов заданного типа
	 * @param type
	 * @return
	 */
	public Collection<Tag> tags(TagType type);
	/**
	 * Отдает список абсолютно всех тегов
	 * @return
	 */
	public Collection<Tag> tags();
	
	public String[] tagNames(TagType type);
	public String[] tagNames();
	public boolean addTag(Tag tag);
	public boolean addTag(String name, String value);
	public boolean removeTag(String name);
	public boolean removeTag(Tag tag);
	
	public File filename();
	

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
	/// Factory
	public interface Factory
	{
		Item createTrack(File filename);
	}
}
