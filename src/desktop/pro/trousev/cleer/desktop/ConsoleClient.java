package pro.trousev.cleer.desktop;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jline.console.ConsoleReader;

import pro.trousev.cleer.Console;
import pro.trousev.cleer.ConsoleOutput;
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

			@Override
			public ConsoleOutput output() {
				return new ConsoleOutput() {

					@Override
					public void printMessage(String message, Type messageType,
							Callback dismissAction) {
						if(messageType == Type.MessageTypeError)
							System.out.print("[ERROR] ");
						System.out.println(message);
					}

					@Override
					public void printException(Throwable t,
							String additionalMessage, Callback dismissAction) {
						if(additionalMessage != null)
							System.out.println("[ERROR] "+additionalMessage);
						t.printStackTrace();
					}

					public String StringSetPrinter(int size, int[] limit, List<String[]> strings)
					{
						System.out.println("Preparing output...");
						int sizes [] = new int[size]; 
						for(String[] subset: strings)
						{
							for(int i=0; i<size; i++)
								if(subset[i] != null && subset[i].length() > sizes[i])
									sizes[i] = subset[i].length();
						}
						String ans = "";
						for(int i=0; i<size; i++)
							if(sizes[i] > limit[i]) sizes[i] = limit[i];
						for(String[] subset: strings)
						{
							for(int i=0; i<size; i++)
							{
								String sub = subset[i];
								if(sub.length() > limit[i]) sub = sub.substring(0, limit[i]);
								int addsize =  sizes[i] - sub.length() + 1;
								ans += sub;
								char[] array = new char[addsize];
								Arrays.fill(array, ' ');
								ans += new String(array);
							}
							ans += "\n";
						}
						return ans;
					}
					@Override
					public void printTrackList(List<Track> tracks,
							int selected_track, Callback songSelectAction) {
						
						List<String[]> matrix = new ArrayList<String[]>();
						int no=-1;
						for(Track t: tracks)
						{
							no++;
							if(selected_track>=0 && no > (selected_track+20)) continue;
							if(selected_track>=0 && no < (selected_track-20)) continue;
							if(selected_track<0 && no > 200) continue;
							String rating_str = "N/R";
							if(t.user_rating() > 0)
							{
								char[] array = new char[t.user_rating()];
								Arrays.fill(array, '*');
								rating_str = new String(array);
							}
							
							String[] arr ;
							if(selected_track >= 0)
							{
								String[] _arr = {(selected_track == no ? "===>":"    "), rating_str, t.title(), t.artist(), t.album()};
								arr = _arr;
							}
							else
							{
								String[] _arr = {rating_str, t.title(), t.artist(), t.album()};
								arr = _arr;
							}
							//System.out.println(t.toString());
							matrix.add(arr);
						}
						int col_count = 4;
						int[] limits = {5,40,20,20};
						if(selected_track >= 0)
						{
							int[] _limits = {5,5,40,20,20};
							limits = _limits;
							col_count++;
						}
						System.out.println(StringSetPrinter(col_count,limits, matrix));
						System.out.println("Size: "+tracks.size());
					}

					@Override
					public void printTrackList(Playlist tracks,
							int selected_track, Callback songSelectAction) {
						printTrackList(tracks.contents(), selected_track, songSelectAction);
					}
					
				};
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
