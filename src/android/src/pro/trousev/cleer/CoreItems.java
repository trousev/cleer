package pro.trousev.cleer;

import pro.trousev.cleer.Plugin;
import pro.trousev.cleer.Database;
import pro.trousev.cleer.Library;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Queue;

public class CoreItems{
	private static Plugin.Interface coreItems;
	public static void setCoreItems(Plugin.Interface obj){
		coreItems=obj;
	}
	public static Plugin.Interface getCoreItems(){
		return coreItems;
	}
	public static Library getLibrary(){
		return coreItems.library();
	}
	public static Database getDatabase(){
		return coreItems.storage();
	}
	public static Player getPlayer(){
		return coreItems.player();
	}
	public static Queue getQueue(){
		return coreItems.queue();
	}
}