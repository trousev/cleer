package pro.trousev.cleer.sys;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Database.DatabaseObject;

public class MediaItem extends StandardItem {

	public MediaItem(Factory generator, File filename) {
		super(generator, filename);
	}

	public MediaItem(DatabaseObject dataObject) throws Exception {
		super(dataObject);
	}
	
	
    public static  Item.Factory Factory = new Factory() {
		
		private static final long serialVersionUID = 301215204178878981L;

		@Override
		public boolean writeTag(Item item, Tag tag) throws ReadOnlyTagException {
			AudioFileHeader head = new AudioFileHeader();
			try 
			{
				head.readFromFile(item.filename());
				head.begin();
				//head.setRating(tag.value());
				if(tag.name() == "album") head.setAlbum(tag.value());
				else if(tag.name() == "artist") head.setArtist(tag.value());
				else if(tag.name() == "title") head.setTitle(tag.value());
				else if(tag.name() == "year") head.setYear(tag.value());
				else if(tag.name() == "number") throw new ReadOnlyTagException("WTH");
				else if(tag.name() == "lyrics") head.setLyrics(tag.value());
				else if(tag.name() == "genre") head.setGenre(tag.value());
				else if(tag.name() == "rating") head.setRating(tag.value());
				else
				{
					String _tag_string = head.getTags();
					String[] orig_tags = _tag_string.split(" ");
					boolean should_add = true;
					for(int i=0; i<orig_tags.length; i++)
					{
						String one_tag = orig_tags[i];
						if(!one_tag.contains(":")) continue;
						String[] arr = one_tag.split(":");
						if(arr[0].equals(tag.name()))
						{
							should_add = false;
							orig_tags[i] = tag.name()+":"+tag.value();
							break;
						}
					}
					if(should_add)
						_tag_string = _tag_string+" "+tag.name()+":"+tag.value();
					else
					{
						_tag_string = "";
						for(String tt: orig_tags)
							_tag_string += " "+tt;
					}
					head.setTags(_tag_string);
				}
				
				head.commit();
				return true;
			} catch (Throwable e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		
		@Override
		public boolean removeTag(Item item, Tag tag) {
			return false;
		}
		
		@Override
		public Tag createTag(Item item, String name, TagType type)
				throws TagAlreadyExistsException {
			return new StandardTag(item, name, null, type, ContentType.String, true);
		}
		
		@Override
		public Item createItem(File filename) {
			StandardItem s = new StandardItem(this, filename);
			try {
				AudioFileHeader head = new AudioFileHeader();
				head.readFromFile(filename);
				Map<String, Tag> _all_tags = s.getAllTags();
				_all_tags.put("album", new TagImplRussian(s,"album", head.getAlbum(), TagType.StrictlyClassified, ContentType.String, false));
				_all_tags.put("artist", new TagImplRussian(s, "artist", head.getArtist(), TagType.StrictlyClassified, ContentType.String, false));
				_all_tags.put("title", new TagImplRussian(s, "title", head.getTitle(), TagType.StrictlyClassified, ContentType.String, false));
				_all_tags.put("year", new TagImplRussian(s, "year", head.getYear(), TagType.StrictlyClassified, ContentType.Numeric, false));
				_all_tags.put("number", new TagImplRussian(s, "number", head.getTrack(), TagType.StrictlyClassified, ContentType.Numeric, false));
				_all_tags.put("lyrics", new TagImplRussian(s, "lyrics", head.getLyrics(), TagType.StrictlyClassified, ContentType.String, false));
				_all_tags.put("rating", new TagImplRussian(s, "rating", head.getRating(), TagType.StrictlyClassified, ContentType.Numeric, true));
				_all_tags.put("genre", new TagImplRussian(s, "genre", head.getGenre(), TagType.StrictlyClassified, ContentType.String, true));
				for(String facet: head.getTags().split(" "))
				{
					if(facet.contains(":"))
					{
						String[] arr = facet.split(":");
						_all_tags.put(arr[0], new TagImplRussian(s, arr[0], arr[1], TagType.SoftlyClassified, ContentType.String, true));
					}
				}
				return s;
		    } catch (CannotReadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (TagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ReadOnlyFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidAudioFrameException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return null;
		}

		@Override
		public Item createItem(DatabaseObject dbo) {
			try {
				return new MediaItem(dbo);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		}
	};	

}
