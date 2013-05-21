package pro.trousev.cleer.android.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import pro.trousev.cleer.Database;
import pro.trousev.cleer.Library;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.android.Constants;
import pro.trousev.cleer.android.service.DatabaseImpl.DatabaseObject;

public class ListImpl implements Library {
	private int count;
	DatabaseImpl database;

	public ListImpl(String path, Context context) {
		this.count = 0;
		database = new DatabaseImpl(Constants.DATABASE_PATH, context);
	}

	public int countCompositions() {
		return this.count;
	}

	public boolean addIntoTable(String section, String content)
			throws DatabaseError {
		try {
			database.db
					.execSQL("CREATE TABLE IF NOT EXISTS "
							+ section
							+ " (`id` integer primary key autoincrement, `name` text);");
			database.db.execSQL("insert into " + section + " `name` values "
					+ content + ");");
		} catch (SQLException e) {
			throw new DatabaseError(e);
		}
		return true;
	}

	public boolean deleteFromTable(String section, String content)
			throws DatabaseError {
		try {
			database.db.execSQL("delete * from " + section + "where content = "
					+ content + ";");
		} catch (SQLException e) {
			throw new DatabaseError(e);
		}
		return true;
	}

	public List<String> ListOfMainNotice(String section, String where) {
		
		List<String> answer = new ArrayList<String>();
		Cursor c;
		String[] columns = { "id", "name" };

		c = database.db.query(section, columns, where, null, null, null, null);
		while (c.moveToNext()) {
			answer.add(c.getColumnName(1));
		}
		c.close();
		return answer;
	}
	
	public List<pro.trousev.cleer.Database.DatabaseObject> ListOfNotice(String section, String where) {
		
		List<pro.trousev.cleer.Database.DatabaseObject> answer = new ArrayList<Database.DatabaseObject>();
		Cursor c;
		String[] columns = { "id", "name" };

		c = database.db.query(section, columns, where, null, null, null, null);
		while (c.moveToNext()) {
			answer.add(database.db.new DatabaseObject(section, null, c.getColumnName(0), c
					.getColumnName(1)));
		}
		c.close();
		return answer;
	}

}
