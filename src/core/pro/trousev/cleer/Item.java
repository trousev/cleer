package pro.trousev.cleer;

import java.io.File;
import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Database.DatabaseObject;
import pro.trousev.cleer.Item.NoSuchTagException;
import pro.trousev.cleer.Item.ReadOnlyTagException;

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
		void setParent(Item parent);
		void doSetValue(String new_value);

	}
	
	/**
	 * Отдает первый тег по заданному имени
	 * @throws NoSuchTagException 
	 */
	public Tag firstTag(String name) throws NoSuchTagException;
	/**
	 * Отдает список тегов по заданному имени
	 * @param name
	 * @return
	 */
	public List<Tag> tag(String name) throws NoSuchTagException;
	
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
	
	public List<String> tagNames(TagType type);
	public List<String> tagNames();
	public boolean addTag(Tag tag);
	public boolean addTag(String name, String value) throws TagAlreadyExistsException, ReadOnlyTagException;
	public boolean removeTag(String name) throws NoSuchTagException;
	public boolean removeTag(Tag tag) throws NoSuchTagException;
	void setTagValue(String name, String value) throws NoSuchTagException, ReadOnlyTagException, DatabaseError;
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
			super(String.format("Cannot find tag: %s",tagName));
		}
	}
    public static class TagAlreadyExistsException extends Exception
    {
		private static final long serialVersionUID = -7471467624443067651L;

		public TagAlreadyExistsException(String tagName) {
            super(String.format("Tag already exists: %s",tagName));
        }
    }
	public static class ReadOnlyTagException extends Exception
	{
		private static final long serialVersionUID = -3294597277551186761L;
		public ReadOnlyTagException(String tagName) {
			super(String.format("Tag is read-only: "+tagName));
		}
	}
/// Factory
	public interface Factory extends Serializable
	{
        /**
         * This method should create valid item from given filename
         **/
		Item createItem(File filename);
        /**
         * This method should update item's file according to new tag value.
         * This method should be called from item's setValue(...) method.
         **/
        boolean writeTag(Item item, Tag tag) throws ReadOnlyTagException;
        /** This method should create new shiny tag, suitable for something useful.
         * Also, it must update item's file properly. Will be called from addTag(..) method;
         **/
        Tag createTag(Item item, String name, TagType type) throws TagAlreadyExistsException; 
        /** 
         * This method should remove tag from item's file.
         **/
        boolean removeTag(Item item, Tag tag);

	}

}

