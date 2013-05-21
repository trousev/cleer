package pro.trousev.cleer.android.service;

import java.util.ArrayList;
import java.util.List;

import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.android.Constants;
import android.content.Context;

public class ListImpl {
	DatabaseImpl database;
	
	public ListImpl(String path, Context context) {
		database = new DatabaseImpl(Constants.DATABASE_PATH, context);
	}
	
	public List<String> getList(String section) throws DatabaseError{
		List<String> elements = new ArrayList<String>();
		try {
			elements = database.search(section);
		} catch (DatabaseError e) {
			e.printStackTrace();
		}
		return elements;
	}

	public boolean addIntoList(String section, String content) throws DatabaseError{
		try {
			database.store(section, content);
		} catch (DatabaseError e) {
			e.printStackTrace();
		}
		return true;
	}
	
	
}
