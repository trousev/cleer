package pro.trousev.cleer.commands;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.util.List;


import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Database.DatabaseObject;
import pro.trousev.cleer.Database.SearchLanguage;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Plugin.Interface;
import pro.trousev.cleer.sys.MediaItem;

public class Library extends Command {

	public static class Sync extends Command
	{
		public static void copyFile(File sourceFile, File destFile) throws IOException {
		    if(!destFile.exists()) {
		        destFile.createNewFile();
		    }

		    FileChannel source = null;
		    FileChannel destination = null;

		    try {
		        source = new FileInputStream(sourceFile).getChannel();
		        destination = new FileOutputStream(destFile).getChannel();
		        destination.transferFrom(source, 0, source.size());
		    }
		    finally {
		        if(source != null) {
		            source.close();
		        }
		        if(destination != null) {
		            destination.close();
		        }
		    }
		}
		
		@Override
		public String name() {
			return "sync";
		}

		@Override
		public String help(boolean is_short) {
			return "Test";
		}

		@Override
		public boolean main(List<String> args, PrintStream stdout,
				Interface iface) throws Exception {
			File folder = new File(args.get(0));
			if(folder.exists() && !folder.isDirectory())
				throw new Exception("");
			File playlist = new File(args.get(0)+"/playlist.m3u");
			//if(!playlist.canWrite()) 
			//	throw new Exception("Folder is not writeable");
			for(Item item: iface.library().focus().contents())
			{
				File target = new File(args.get(0)+"/"+item.filename().getName());
				if(!target.exists())
					try
					{
						copyFile(item.filename(), target);
						System.out.println(item.filename());
					}
					catch (Throwable t)
					{
						System.out.println("WARNING: "+t.getMessage()+" in file: "+item.filename());
					}
			}
			return true;
		}
		
	}
	
	
	@Override
	public String name() {
		return "library";
	}

	@Override
	public String help(boolean is_short) {
		if(is_short) return "Media Library management tool";
		return 
				"Media Library management tool\n" +
				"You may use these subcommands:\n" +
				"    library list             -- list all folders in library\n" +
				"    library add <folder>     -- add new folder to library\n" +
				"    library remove <folder>  -- remove folder from library\n" +
				"    library scan <folder>    -- scan/rescan selected folder\n" +
				"    library refresh          -- refresh search query on all library (w/o fs scanning)\n" +
				"    library scan             -- scan/rescan all library\n";
	}
	public String make_folder(String in)
	{
		if(in.startsWith("~"))
		{
			in = in.replaceAll("^~", System.getProperty("user.home"));
		}
		return in;
	}
	@Override
	public boolean main(List<String> args, final PrintStream stdout, Interface iface) {
		String command;
		try
		{
			command = args.remove(0);
		}
		catch(Exception e)
		{
			stdout.println("Need subcommand. ");
			return false;
		}
		if(command.equals("list"))
		{
			for(File f: iface.library().folders())
			{
				stdout.println(f.getAbsolutePath());
			}
		}
		if(command.equals("add"))
		{
			String folder;
			try
			{
				folder = make_folder(args.remove(0));
			}
			catch (Exception e)
			{
				stdout.println("Folder required.");
				return false;
			}
			iface.library().folder_add(new File(folder));
		}
		if(command.equals("remove"))
		{
			String folder;
			try
			{
				folder = make_folder(args.remove(0));
			}
			catch (Exception e)
			{
				stdout.println("Folder required.");
				return false;
			}
			iface.library().folder_remove(new File(folder));
		}
		if(command.equals("refresh"))
		{
			try {
				iface.storage().begin();
				for (DatabaseObject dbo: iface.storage().search("songs", "",SearchLanguage.SearchDirectMatch) )
				{
					MediaItem t = new MediaItem(dbo);
					System.out.println(t.getSearchQuery());
					dbo.update_search(t.getSearchQuery());
				}
				iface.storage().commit();
			} catch (DatabaseError e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(command.equals("scan"))
		{
			String folder;
			try
			{
				folder = make_folder(args.remove(0));
			}
			catch (Exception e)
			{
				folder = null;
			}
			
			pro.trousev.cleer.Library.FolderScanCallback callback = new pro.trousev.cleer.Library.FolderScanCallback() {
				int c=0;
				int m=0;
				@Override
				public void started() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void progress(int current, int maximum) {
					c = current;
					m = maximum;
					if(current % 100 == 0)
						stdout.println(String.format("Overall progress : "+current+"/"+maximum));
				}
				
				@Override
				public void message(String message) {
					stdout.println(String.format("%d/%d %s",c,m,message));
				}
				
				@Override
				public void finished() {
					stdout.println("Scanning complete.");
				}
			};
			if(folder == null) iface.library().folder_scan(callback);
			else iface.library().folder_scan(new File(folder), callback);
		}
		return true;
	}

}
