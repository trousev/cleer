package pro.trousev.cleer.android.service;

import pro.trousev.cleer.Player;
import pro.trousev.cleer.Queue;
import pro.trousev.cleer.sys.QueueImpl;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class AndroidCleerService extends Service {
	final String LOG_TAG = "cleerServiceLogs";
	
	final private Player player = new PlayerAndroid();
	final private Queue queue = new QueueImpl(player);
	
	public void onCreate() {
	    super.onCreate();
	    Log.d(LOG_TAG, "onCreate");
	  }
	  
	  public int onStartCommand(Intent intent, int flags, int startId) {
	    Log.d(LOG_TAG, "onStartCommand");
	    return super.onStartCommand(intent, flags, startId);
	  }

	  public void onDestroy() {
	    super.onDestroy();
	    Log.d(LOG_TAG, "onDestroy");
	  }

	  public IBinder onBind(Intent intent) {
	    Log.d(LOG_TAG, "onBind");
	    return null;
	  }
	  
}
