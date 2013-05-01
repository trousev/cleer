package pro.trousev.cleer.android.service;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.android.Constants;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AndroidCleerService extends Service {

	// Binder позвол€ет получить ссылку в активити на сервис
	public class CleerBinder extends Binder {
		public AndroidCleerService getService() {
			return AndroidCleerService.this;
		}
	}

	public void onCreate() {
		super.onCreate();
		Log.d(Constants.LOG_TAG, "Service.onCreate()");
	}

	public void play() {
		Log.d(Constants.LOG_TAG, "Service.play()");
	}

	public void pause() {
		Log.d(Constants.LOG_TAG, "Service.pause()");
	}

	public void next() {
		Log.d(Constants.LOG_TAG, "Service.next()");
	}

	public void prev() {
		Log.d(Constants.LOG_TAG, "Service.prev()");
	}
	//ƒобавление трека в пользовательский список
	public void addItemToList(Item item, Playlist playlist){
		
	}
	
	public void createNewList(String name){
		// ¬ нашей реализации вообще такой метод нужен?
	}
	//¬озвращает список пользовательских плейлистов
	public List<Playlist> getPlaylists(){
		return null;
	}
	//ѕо запросу возвращает список треков, удоблетвор€ющих запросу
	public List<Item> getListOfTracks(String searchQuery){
		//TODO  акой аргумент сюда передавать?
		return null;
	}
	//¬озвращает все возможные значени€ определенного тега
	public List<Item> getListOfTagValues(String tagName){
		// Case of Album is special!
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
		return true; // значение true позвол€ет фиксировать переподключение UI к интерфейсу
	}
	public void onRebind(Intent intent){
		Log.d(Constants.LOG_TAG, "Service.onRebind()");
	}
}
