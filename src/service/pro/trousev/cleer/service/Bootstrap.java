package pro.trousev.cleer.service;

import java.sql.SQLException;

import pro.trousev.cleer.Database;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Console;
import pro.trousev.cleer.ConsoleOutput;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Plugin;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Library;
import pro.trousev.cleer.Queue;
import pro.trousev.cleer.sys.DatabaseSqlite;
import pro.trousev.cleer.sys.EbookItem;
import pro.trousev.cleer.sys.LibraryImpl;

public class Bootstrap {
	public static Database database;
	public static Library library;
	public static Item.Factory itemFactory;
	
	public static void init() throws SQLException, ClassNotFoundException, DatabaseError
	{
		String dbpath = System.getProperty("user.home") + "/.config/cleer/database-service.sqlite";
		database = new DatabaseSqlite(dbpath);
		itemFactory = EbookItem.Factory;
		library = new LibraryImpl(database, itemFactory);
		
	}
	public static Plugin.Interface Interface = new Plugin.Interface() {
		
		@Override
		public Database storage() {
			return database;
		}
		
		@Override
		public Queue queue() {
			return null;
		}
		
		@Override
		public Player player() {
			return null;
		}
		
		@Override
		public ConsoleOutput output() {
			return null;
		}
		
		@Override
		public Library library() {
			return library;
		}
		
		@Override
		public Console console() {
			return null;
		}
	};
}
