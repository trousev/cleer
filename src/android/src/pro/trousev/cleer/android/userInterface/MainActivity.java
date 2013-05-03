package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.android.Constants;
import pro.trousev.cleer.android.service.AndroidCleerService;
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
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private FragmentTransaction fTrans;
	private Fragment playBar, mainMenu;
	private FragmentManager fragmentManager;
	private ServiceConnection serviceConnection;
	AndroidCleerService service;
	private boolean bound;
	Intent intent;

	/** Called when the activity is first created. */
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
				service = ((AndroidCleerService.CleerBinder)binder).getService();
				Log.d(Constants.LOG_TAG, "MainActivity onServiceConnected");
				bound = true;
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				Log.d(Constants.LOG_TAG, "MainActivity onServiceDisconnected");
				bound = false;
			}
		};
		startService(intent);
		bindService(intent, serviceConnection, 0);
		// TODO set implementations here
		// TODO initialize Service
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

	public void setListOfRequests(List<Item> item, String firstTagName, String secondTagName) {
		ListOfRequests listOfRequests = new ListOfRequests(item, firstTagName, secondTagName);
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
			if(bound)
				unbindService(serviceConnection);
			stopService(new Intent(this, AndroidCleerService.class));
			bound=false;
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public void onDestroy() {
		if(bound)
			unbindService(serviceConnection);
		Log.d(Constants.LOG_TAG, "MainActivity.onDestoy()");
		super.onDestroy();
	}
}
