package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.android.service.AndroidCleerService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;

public class MainActivity extends FragmentActivity implements OnClickListener {
	private FragmentTransaction fTrans;
	private Fragment playBar, mainMenu;
	private FragmentManager fragmentManager;
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
		startService(new Intent(this, AndroidCleerService.class));
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

	public void setListOfRequests(List<String> list) {
		ListOfRequests listOfRequests = new ListOfRequests();
		fTrans = fragmentManager.beginTransaction();
		fTrans.replace(R.id.work_space, listOfRequests);
		fTrans.addToBackStack(null);
		fTrans.commit();
	}
	public void setMainMenu(){
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
			stopService(new Intent(this, AndroidCleerService.class));
			clearBackStack();
			break;
		case R.id.exit_btn:
			stopService(new Intent(this, AndroidCleerService.class));
			System.exit(0);
			break;
		default:
			break;	
		}
	}

}
