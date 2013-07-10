package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Player.Reason;
import pro.trousev.cleer.Queue.EnqueueMode;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.android.Constants;
import pro.trousev.cleer.android.service.AndroidCleerService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private ServiceConnection serviceConnection;
	public static AndroidCleerService service;
	private boolean serviceIsBound;
	private Intent serviceIntent;
	SwipePageAdapter main_pager_adapter;
	ViewPager main_pager;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
		
		serviceIntent = new Intent(
				"pro.trousev.cleer.android.service.AndroidCleerService");

		serviceConnection = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName name, IBinder binder) {
				service = ((AndroidCleerService.CleerBinder) binder)
						.getService();
				serviceIsBound = true;
				
				Playlist p = service.library().search("****");
				service.queue().enqueue(p.contents(), EnqueueMode.Immidiaely);
				service.player().stop(Reason.UserBreak);
				postInit();
			}

			@Override
			public void onServiceDisconnected(ComponentName name) {
				serviceIsBound = false;
			}
		};

		startService(serviceIntent);
		bindService(serviceIntent, serviceConnection, 0);
	}


	/// This is initialized when Library is initialized
	public void postInit()
	{
		main_pager_adapter = new SwipePageAdapter(getSupportFragmentManager());
		
		main_pager_adapter.addPage(new QueueView());
		main_pager_adapter.addPage(new PlaylistsView());
        
		// Set up the ViewPager with the sections adapter.
		main_pager = (ViewPager) findViewById(R.id.pager);
		main_pager.setAdapter(main_pager_adapter);
	}
	public void onClick(View view) {
		int id = view.getId();
	}

	public void exit() {
		if (serviceIsBound)
			unbindService(serviceConnection);
		stopService(new Intent(this, AndroidCleerService.class));
		serviceIsBound = false;
		this.finish();
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
		this.getMenuInflater().inflate(R.menu.main_option_menu, menu);
		System.out.println("[cleer] Menu Created.");
		return super.onPrepareOptionsMenu(menu);
		
	}

	
	@Override
	public void onDestroy() {
		if (serviceIsBound)
			unbindService(serviceConnection);
		Log.d(Constants.LOG_TAG, "MainActivity.onDestoy()");
		System.runFinalization();
		super.onDestroy();
	}
}
