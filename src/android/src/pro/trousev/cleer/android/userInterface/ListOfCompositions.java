package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.android.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ListView;

public class ListOfCompositions extends ListFragment {
	String data[] = new String[] { "one", "two", "three", "four", "one", "two",
			"three", "four", "one", "two", "three", "four" };
	final int PLAY = 1;
	final int ADD_TO_QUEUE = 2;
	final int ADD_TO_LIST = 3;

	public ListOfCompositions(List<Item> list) {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListOfCompAdapter adapter = new ListOfCompAdapter(getActivity(), data);
		this.setHasOptionsMenu(true);
		setListAdapter(adapter);
	}

	@Override
	public void onAttach(Activity activcity) {
		super.onAttach(activcity);
		Log.d(Constants.LOG_TAG, "ListOfComp.onAttach()");
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnCreateContextMenuListener(this);
		Log.d(Constants.LOG_TAG, "ListOfComp.onActivityCreated()");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		Log.d(Constants.LOG_TAG, "ListOfComp.onCreateCotextMenu()");
		super.onCreateContextMenu(menu, view, menuInfo);
		menu.add(0, PLAY, 0, "Play");
		menu.add(0, ADD_TO_QUEUE, 0, "Add to queue");
		menu.add(0, ADD_TO_LIST, 0, "Add to list");
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		// TODO send this list to the queue, start playing from the selected
		// item
		Log.d(Constants.LOG_TAG, "onListItemClick()");
	}

}
