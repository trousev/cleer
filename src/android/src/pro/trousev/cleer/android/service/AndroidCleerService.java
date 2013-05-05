package pro.trousev.cleer.android.service;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Messaging.Message;
import pro.trousev.cleer.Player.PlayerChangeEvent;
import pro.trousev.cleer.Player.Status;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.android.AndroidMessages;
import pro.trousev.cleer.android.AndroidMessages.ServiceRequestMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceRespondMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import pro.trousev.cleer.android.AndroidMessages.TypeOfResult;
import pro.trousev.cleer.android.Constants;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AndroidCleerService extends Service {
	private static ServiceRespondMessage respondMessage = new ServiceRespondMessage();

	// Binder allow us get Service.this from the Activity
	public class CleerBinder extends Binder {
		public AndroidCleerService getService() {
			return AndroidCleerService.this;
		}
	}

	public void onCreate() {
		super.onCreate();
		Messaging.subscribe(AndroidMessages.ServiceRequestMessage.class, new Messaging.Event(){

			@Override
			public void messageReceived(Message message) {
				//TODO end implementation of that event
				ServiceRequestMessage mes =  (ServiceRequestMessage) message;
				if (mes.type == TypeOfResult.Compositions);
				switch (mes.type){
				case Compositions:
					respondMessage.typeOfContent=TypeOfResult.Compositions;
					break;
				case Queue:
					respondMessage.typeOfContent=TypeOfResult.Queue;
					break;
				case Albums:
					respondMessage.typeOfContent=TypeOfResult.Albums;
					break;
				case Genres:
					respondMessage.typeOfContent=TypeOfResult.Genres;
					break;
				case Artists:
					respondMessage.typeOfContent=TypeOfResult.Artists;
					break;
				case Playlists:
					respondMessage.typeOfContent=TypeOfResult.Playlists;
					break;
				case Playlist:
					respondMessage.typeOfContent=TypeOfResult.Compositions;
					break;
				case PlaylistsInDialog:
					respondMessage.typeOfContent=TypeOfResult.PlaylistsInDialog;
					break;
				}
				Messaging.fire(respondMessage);
			}
		});
		Messaging.subscribe(AndroidMessages.ServiceTaskMessage.class, new Messaging.Event(){

			@Override
			public void messageReceived(Message message) {
				//TODO end implementation of that event
				ServiceTaskMessage mes =  (ServiceTaskMessage) message;
				switch (mes.action){
				case Play:
					play();
					break;
				case Pause:
					pause();
					break;
				case Next:
					next();
					break;
				case Previous:
					prev();
					break;
				case addToQueue:
					break;
				case setToQueue:
					break;
					//TODO add others...
				}
			}
		});
		Log.d(Constants.LOG_TAG, "Service.onCreate()");
	}

	public void play() {
		//TODO delete that
		PlayerChangeEvent changeEvent = new PlayerChangeEvent(); 
		changeEvent.status=Status.Playing;
		Messaging.fire(changeEvent);
		Log.d(Constants.LOG_TAG, "Service.play()");
	}

	public void pause() {
		//TODO delete that
		PlayerChangeEvent changeEvent = new PlayerChangeEvent(); 
		changeEvent.status=Status.Paused;
		Messaging.fire(changeEvent);
		Log.d(Constants.LOG_TAG, "Service.pause()");
	}

	public void next() {
		Log.d(Constants.LOG_TAG, "Service.next()");
	}

	public void prev() {
		Log.d(Constants.LOG_TAG, "Service.prev()");
	}
	//adds songs at the end of queue
	public void addToQueue(List<Item> tracks){
		
	}
	//clear queue, set list<item> in it, start playing song with current index
	public void setToQueue(List<Item> tracks, int index){
		Log.d(Constants.LOG_TAG, "Service.setToQueue");
	}
	//returns songs from the queue
	public List<Item> getQueueItems(){
		return null;
	}
	//add item into the user playlist
	public void addItemToList(Item item, Playlist playlist){
		
	}
	//create new user playlist
	public void createNewList(String name){
		// Do we need that method?
	}
	//returns list of user playlists
	public List<Playlist> getPlaylists(){
		return null;
	}
	//returns list of Items from database, which suit to the searchQuery
	public List<Item> getListOfTracks(String searchQuery){
		return null;
	}
	//Returns list of items with all represented values of some tag tagName
	public List<Item> getListOfTagValues(String tagName){
		
		return null;
	}
	// return all albums with information about artist
	// album == item
	public List<Item> getListOfAlbums(){
		return null;
	}
	
	
	// This method is called every time UI starts
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.d(Constants.LOG_TAG, "Service.onStartCommand()");
		return super.onStartCommand(intent, flags, startId);
	}
	public void onDestroy() {
		Log.d(Constants.LOG_TAG, "Service.onDestroy()");
		super.onDestroy();
	}
	public IBinder onBind(Intent intent) {
		Log.d(Constants.LOG_TAG, "Service.onBind()");
		return new CleerBinder();
	}
	public boolean onUnbind(Intent intent) {
		Log.d(Constants.LOG_TAG, "Service.onUnbind()");
		return true; //for onServiceConnected
	}
	public void onRebind(Intent intent){
		Log.d(Constants.LOG_TAG, "Service.onRebind()");
	}
}
