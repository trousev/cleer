package pro.trousev.cleer.android.userInterface;

import java.util.List;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Plugin;
import pro.trousev.cleer.Console;
import pro.trousev.cleer.ConsoleOutput;
import pro.trousev.cleer.Database;
import pro.trousev.cleer.Library;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Queue;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import pro.trousev.cleer.android.userInterface.R;

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
			clearBackStack();
			break;
		default:
			break;	
		}
	}

	public void onExit(View sender) {
		System.exit(0);
	}
}
