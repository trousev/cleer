package pro.trousev.jplay.desktop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import pro.trousev.jplay.Console;
import pro.trousev.jplay.Database;
import pro.trousev.jplay.Library;
import pro.trousev.jplay.Player;
import pro.trousev.jplay.Plugin;
import pro.trousev.jplay.Plugin.Interface;
import pro.trousev.jplay.commands.AllCommands;
import pro.trousev.jplay.sys.LibraryImpl;

public class ConsoleClient {
	public static void main(String[] argv) throws SQLException, ClassNotFoundException 
	{
		InputStreamReader inputStreamReader = new InputStreamReader (System.in);
	    BufferedReader stdin = new BufferedReader (inputStreamReader);
	    final Console console = new AllCommands();
	    final Database db = new DatabaseHsql("/tmp/hsql/database.hsql");
	    final Library lib = new LibraryImpl(db);
	    final Player player = new PlayerDesk();
	    
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
		};
		
	    while(true)
	    {
	    	try {
				System.out.println("jplay# ");
	    		String command_line = stdin.readLine();
	    		List<String> args = new ArrayList<String>();
	    		for(String a: command_line.split(" "))
	    			args.add(a);
				String command = args.remove(0);
				try {
					console.invoke(command, args, System.out, iface);
				} catch (pro.trousev.jplay.Console.CommandNotFoundException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
				catch(Throwable t)
				{
					System.out.println("Command failed: "+t.getMessage());
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
	}

}
