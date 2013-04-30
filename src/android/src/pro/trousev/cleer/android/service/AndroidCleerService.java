package pro.trousev.cleer.android.service;

import pro.trousev.cleer.Player;
import pro.trousev.cleer.Queue;
import pro.trousev.cleer.android.Constants;
import pro.trousev.cleer.sys.QueueImpl;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class AndroidCleerService extends Service {	
	final private Player player = new PlayerAndroid();
	final private Queue queue = new QueueImpl(player);
	
	public void onCreate() {
	    super.onCreate();
	    Log.d(Constants.LOG_TAG, "Service.onCreate()");
	  }
	  
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
	    return new Binder();
	  }
	  
}
