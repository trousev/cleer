package pro.trousev.cleer.service;
import java.io.File;
import java.sql.SQLException;

import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Library;
class ServiceMain
{
    public static void main(String[] argv) throws SQLException, ClassNotFoundException, DatabaseError
    {
    	Bootstrap.init();
    	Bootstrap.library.folder_add(new File("/home/doctor/test/kuku"));
    	Bootstrap.library.folder_scan(new Library.FolderScanCallback() {
			
			@Override
			public void started() {
				System.out.println("Scanning started");
			}
			
			@Override
			public void progress(int current, int maximum) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void message(String message) {
				// TODO Auto-generated method stub
				System.out.println("Scanning: "+message);
				
			}
			
			@Override
			public void finished() {
				System.out.println("Scanning complete.");
				
			}
		});
    }
}
