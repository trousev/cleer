package pro.trousev.jplay;

import java.io.File;
import java.util.List;

import pro.trousev.jplay.Database.DatabaseObject;

/**
 * Эта штуковина репрезентует один музыкальный трек коллекции. 
 * @author doctor
 *
 */
public interface Track {
	// Meta
	public String artist();
	public String album();
	public String title();
	public String year();
	public String sequence_number();
	public String lyrics();
	public List<String> tags();
	public int user_rating();
	public File filename();
	// Statistics
	public int play_count();
	public int skip_count();
	public int repeat_count();
	
	// Reporting
	public int auto_rating();
	
	// Save & restore
	String serialize();
	boolean deserialize(String contents);
	String generate_query();
	
	// Modifications
	void set_user_rating(int rating);
	void stat_played();
	void stat_skipped();
	void stat_repeated();
	
	// Link
	DatabaseObject linkedObject();
}
