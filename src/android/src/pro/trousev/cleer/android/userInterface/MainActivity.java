package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Messaging.Event;
import pro.trousev.cleer.Messaging.Message;
import pro.trousev.cleer.android.AndroidMessages;
import pro.trousev.cleer.android.AndroidMessages.ServiceRequestMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceRespondMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import pro.trousev.cleer.android.Constants;
import pro.trousev.cleer.android.service.AndroidCleerService;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private FragmentTransaction fTrans;
	private Fragment playBar, mainMenu;
	private FragmentManager fragmentManager;
	private ServiceConnection serviceConnection;
	AndroidCleerService service;
	private boolean bound;
	private Intent intent;
	public ServiceTaskMessage taskMessage = new ServiceTaskMessage();
	public ServiceRequestMessage requestMessage = new ServiceRequestMessage();
	private ServiceRespondedEvent serviceRespondedEvent;

	class ServiceRespondedEvent implements Event {
		@Override
		public void messageReceived(Message message) {
			ServiceRespondMessage respondMessage = (ServiceRespondMessage) message;
			switch (respondMessage.typeOfContent) {
			case Compositions:
				setListOfCompositions(respondMessage.list);
				break;
			case Albums:
				setListOfRequests(respondMessage.list, "Album", "Artist");
				break;
			case Playlists:
				// TODO decide how do we want show that
				break;
			case Playlist:
				setListOfCompositions(respondMessage.list);
				break;
			case Genres:
				setListOfRequests(respondMessage.list, "Genre", "Number");
				break;
			case Artists:
				setListOfRequests(respondMessage.list, "Artist", "Number");
				break;
			case Queue:
				setListOfCompositions(respondMessage.list);
				break;
			case PlaylistsInDialog:
				// TODO set dialog here
				// someExperiments
				Dialog dialog = new Dialog(MainActivity.this);
				dialog.show();
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		final PlayBar pb = new PlayBar();
		final MainMenu mm = new MainMenu();
		playBar = pb;
		mainMenu = mm;
		fragmentManager = getSupportFragmentManager();
		fTrans = fragmentManager.beginTransaction();
		fTrans.add(R.id.play_bar, playBar);
		fTrans.add(R.id.work_space, mainMenu);
		fTrans.commit();
		intent = new Intent(
				"pro.trousev.cleer.android.service.AndroidCleerService");

		serviceConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				service = ((AndroidCleerService.CleerBinder) binder)
						.getService();
				Log.d(Constants.LOG_TAG, "MainActivity onServiceConnected");
				bound = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.d(Constants.LOG_TAG, "MainActivity onServiceDisconnected");
				bound = false;
			}
		};
		new Thread(new Runnable() {
			public void run() {
				startService(intent);
			}
		}).run();

		bindService(intent, serviceConnection, 0);
		serviceRespondedEvent = new ServiceRespondedEvent();
		Messaging.subscribe(ServiceRespondMessage.class, serviceRespondedEvent);

	}

	public void clearBackStack() {
		while (fragmentManager.getBackStackEntryCount() != 0) {
			fragmentManager.popBackStackImmediate();
		}
	}

	public void setListOfCompositions(List<Item> list) {
		ListOfCompositions listOfCompositions = new ListOfCompositions(list);
		fTrans = fragmentManager.beginTransaction();
		fTrans.replace(R.id.work_space, listOfCompositions);
		fTrans.addToBackStack(null);
		fTrans.commit();
	}

	public void setListOfRequests(List<Item> item, String firstTagName,
			String secondTagName) {
		ListOfRequests listOfRequests = new ListOfRequests(item, firstTagName,
				secondTagName);
		fTrans = fragmentManager.beginTransaction();
		fTrans.replace(R.id.work_space, listOfRequests);
		fTrans.addToBackStack(null);
		fTrans.commit();
	}

	public void setMainMenu() {
		fTrans = fragmentManager.beginTransaction();
		fTrans.replace(R.id.work_space, mainMenu);
		fTrans.addToBackStack(null);
		fTrans.commit();
	}

	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.queue_btn:
			setListOfCompositions(null);
			break;
		case R.id.main_menu_btn:
			setMainMenu();
			clearBackStack();
			break;
		case R.id.exit_btn:
			if (bound)
				unbindService(serviceConnection);
			stopService(new Intent(this, AndroidCleerService.class));
			bound = false;
			this.finish();
			break;
		default:
			break;
		}
	}
	

	public boolean onCreateOptionsMenu(Menu menu){
		this.getMenuInflater().inflate(R.menu.main_option_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.exit_option_btn:
			if (bound)
				unbindService(serviceConnection);
			stopService(new Intent(this, AndroidCleerService.class));
			bound = false;
			this.finish();
			break;
		case R.id.scanSystem:
			taskMessage.action = AndroidMessages.Action.scanSystem;
			Messaging.fire(taskMessage);
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onDestroy() {
		if (bound)
			unbindService(serviceConnection);
		Messaging.unSubscribe(ServiceRespondMessage.class,
				serviceRespondedEvent);
		Log.d(Constants.LOG_TAG, "MainActivity.onDestoy()");
		System.runFinalization();
		super.onDestroy();
	}
}
