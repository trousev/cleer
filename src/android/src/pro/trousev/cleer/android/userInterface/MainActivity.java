package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Messaging.Event;
import pro.trousev.cleer.Messaging.Message;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.android.AndroidMessages;
import pro.trousev.cleer.android.AndroidMessages.ServiceRequestMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceRespondMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import pro.trousev.cleer.android.AndroidMessages.TypeOfResult;
import pro.trousev.cleer.android.Constants;
import pro.trousev.cleer.android.service.AndroidCleerService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private FragmentTransaction fTrans;
	private Fragment playBar, mainMenu;
	private FragmentManager fragmentManager;
	private ServiceConnection serviceConnection;
	AndroidCleerService service;
	ListOfCompositions queue;
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
				setListOfPlaylists(respondMessage.playlists);
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
				setQueue(respondMessage.list);
				break;
			case PlaylistsInDialog:
				showPlaylistsDialog(respondMessage.playlists,
						respondMessage.item);
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		/*DatabaseAndroidImpl db = new DatabaseAndroidImpl();
		new TestDatabase(db).run();*/
		
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
				bound = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				bound = false;
			}
		};

		startService(intent);

		bindService(intent, serviceConnection, 0);
		serviceRespondedEvent = new ServiceRespondedEvent();
		Messaging.subscribe(ServiceRespondMessage.class, serviceRespondedEvent);
	}

	public void clearBackStack() {
		while (fragmentManager.getBackStackEntryCount() != 0) {
			fragmentManager.popBackStackImmediate();
		}
	}

	void showNewPlaylistDialog(Item item) {
		DialogFragment dialog = NewPlaylistDialogFragment.newInstance(item);
		dialog.show(fragmentManager, "dialog");
	}

	void showClearQueueDialog() {
		DialogFragment dialog = ClearQueueDialogFragment.newInstance();
		dialog.show(fragmentManager, "dialog");
	}

	void showPlaylistsDialog(List<Playlist> playlists, Item item) {
		DialogFragment dialog = PlaylistsDialogFragment.newInstance(playlists,
				item);
		dialog.show(fragmentManager, "dialog");
	}

	void showScanAlertDialog() {
		DialogFragment newFragment = NeedScanDialogFragment
				.newInstance(R.string.need_scan_dialog_title);
		newFragment.show(fragmentManager, "dialog");
	}

	public void setListOfPlaylists(List<Playlist> playlists) {
		if (playlists == null){
			showScanAlertDialog();
			return;
		}
		ListOfPlaylists listOfPlaylists = new ListOfPlaylists(playlists);
		fTrans = fragmentManager.beginTransaction();
		fTrans.replace(R.id.work_space, listOfPlaylists);
		fTrans.addToBackStack(null);
		fTrans.commit();
	}

	public void setListOfCompositions(List<Item> list) {
		if ((list.isEmpty()) || (list == null)) {
			showScanAlertDialog();
			return;
		}
		ListOfCompositions listOfCompositions = new ListOfCompositions(list);
		fTrans = fragmentManager.beginTransaction();
		fTrans.replace(R.id.work_space, listOfCompositions);
		fTrans.addToBackStack(null);
		fTrans.commit();
	}

	public void setListOfRequests(List<Item> list, String firstTagName,
			String secondTagName) {
		if ((list.isEmpty()) || (list == null)) {
			showScanAlertDialog();
			return;
		}
		ListOfRequests listOfRequests = new ListOfRequests(list, firstTagName,
				secondTagName);
		fTrans = fragmentManager.beginTransaction();
		fTrans.replace(R.id.work_space, listOfRequests);
		fTrans.addToBackStack(null);
		fTrans.commit();
	}

	public void setQueue(List<Item> list) {
		if (list == null) {
			return;
		}
		queue = new Queue(list);
		fTrans = fragmentManager.beginTransaction();
		fTrans.replace(R.id.work_space, queue);
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
			requestMessage.type = TypeOfResult.Queue;
			Messaging.fire(requestMessage);
			break;
		case R.id.main_menu_btn:
			setMainMenu();
			clearBackStack();
			break;
		default:
			break;
		}
	}

	public void exit() {
		if (bound)
			unbindService(serviceConnection);
		stopService(new Intent(this, AndroidCleerService.class));
		bound = false;
		this.finish();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		this.getMenuInflater().inflate(R.menu.main_option_menu, menu);
		System.out.println("[cleer] Menu Created.");
		return super.onPrepareOptionsMenu(menu);
		
	}
	/*public boolean onCreateOptionsMenu(Menu menu) {
	}*/

	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.exit_option_btn:
			if (bound)
				unbindService(serviceConnection);
			stopService(new Intent(this, AndroidCleerService.class));
			bound = false;
			this.finish();
			break;
		case R.id.scanSystem:
			Toast.makeText(this, "Library is scanning...", Toast.LENGTH_LONG)
					.show();
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
