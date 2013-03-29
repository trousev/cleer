package pro.trousev.cleer.desktop;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import jline.console.ConsoleReader;

import pro.trousev.cleer.Console;
import pro.trousev.cleer.Database;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Library;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.Plugin;
import pro.trousev.cleer.Queue;
import pro.trousev.cleer.Track;
import pro.trousev.cleer.Plugin.Interface;
import pro.trousev.cleer.commands.AllCommands;
import pro.trousev.cleer.sys.LibraryImpl;
import pro.trousev.cleer.sys.QueueImpl;

public class ConsoleClient {
	static String prompt(Plugin.Interface iface)
	{
		String pl = "[no playlist]";
		Playlist focus = iface.library().focus();
		if(focus != null)
			pl = focus.title()+" '"+focus.query()+"' ("+focus.contents().size()+")";
		
		String np = "[No Song]";
		Track t = iface.queue().playing_track();
		if(t != null)
			np = t.title();
		
		String sz = String.format("%d/%d",iface.queue().playing_index(), iface.queue().size());
		return pl + " | " + sz + " " + np + " # ";
	}
	public static void main(String[] argv) throws SQLException, ClassNotFoundException, IOException, DatabaseError 
	{
		//InputStreamReader inputStreamReader = new InputStreamReader (System.in);
	    //BufferedReader stdin = new BufferedReader (inputStreamReader);

	    final Console console = new AllCommands();
	    String dbpath = System.getProperty("user.home") + "/.config/cleer/database.sqlite";
	    
	    final Database db = new DatabaseSqlite(dbpath);
	    
		final Library lib = new LibraryImpl(db);
	    final Player player = new PlayerDesk();
	    final Queue queue = new QueueImpl(player);

	    Plugin.Interface iface = new Interface() {
			
			@Override
			public Database storage() {
				return db;
			}
			
			@Override
			public Library library() {
				return lib;
			}

			@Override
			public Console console() {
				return console;
			}

			@Override
			public Player player() {
				return player;
			}

			@Override
			public Queue queue() {
				return queue;
			}
		};
		ConsoleReader reader = new ConsoleReader();
	    while(true)
	    {
	    	reader.setPrompt(prompt(iface));
			String command_line = reader.readLine(); 

			List<String> args = new ArrayList<String>();
			for(String a: command_line.split(" "))
				args.add(a);
			String command = args.remove(0);
			try {
				console.invoke(command, args, System.out, iface);
			} catch (pro.trousev.cleer.Console.CommandNotFoundException e) {
				System.out.println(e.getMessage());
			}
			catch(Throwable t)
			{
				System.out.println("Command failed: "+t.getMessage());
				t.printStackTrace();
			}
	    }
	}

}
