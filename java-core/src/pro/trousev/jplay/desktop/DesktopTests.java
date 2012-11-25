package pro.trousev.jplay.desktop;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import pro.trousev.jplay.Database;
import pro.trousev.jplay.Library;
import pro.trousev.jplay.sys.LibraryImpl;

public class DesktopTests {

	public static void dbshell() throws Exception
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		
		DatabaseHsql d = new DatabaseHsql("/tmp/hsql/database.hsql");
		d.declare_section("test");
		//DatabaseObject h = d.search("h").get(0);
		//h.update_search("Hahaha");
		//d.remove(h);
		while (true)
	    {
	    	String command = in.readLine();
	    	if(command.equals("exit"))
	    		break;
	    	if(command.equals("clear"))
	    		d.clear_section("test");
	    	if(command.equals("store"))
	    	{
		    	System.out.println("Enter contents: ");
		    	String contents = in.readLine();
		    	System.out.println("Enter search: ");
		    	String search = in.readLine();
		    	d.store("test",contents, search);
	    	}
	    	if(command.equals("search"))
	    	{
	    		System.out.println("Enter search: ");
	    		String search = in.readLine();
	    		for (Database.DatabaseObject object: d.search("test",search))
	    		{
	    			System.out.println(object.toString());
	    		}
	    	}
	    	
	    }

		d.close();
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		Database d = new DatabaseHsql("/tmp/hsql/database.hsql");
		Library l = new LibraryImpl(d);
		Library.FolderScanCallback cb = new Library.FolderScanCallback() {
			
			@Override
			public void started() {
				
				
			}
			
			@Override
			public void progress(int current, int maximum) {
				
				System.out.println(String.format("%d/%d",current,maximum));
			}
			
			@Override
			public void message(String message) {
				System.out.println(message);
				
			}
			
			@Override
			public void finished() {
				
				
			}
		};
		l.folder_add(new File("/home/doctor/Music/Test/B"));
		l.folder_add(new File("/home/doctor/Music/Test/A"));
		l.folder_scan(cb);
		//l.folder_scan(new File("/home/doctor/Music"), cb);
		//l.folder_remove(new File("/home/doctor/Music"));
		//l.folder_remove(new File("/home/doctor/Music"));
		d.close();
		
	}

}
