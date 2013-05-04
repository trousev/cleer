package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import pro.trousev.cleer.android.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ListView;

public class ListOfCompositions extends ListFragment {
	String data[] = new String[] { "one", "two", "three", "four", "one", "two",
			"three", "four", "one", "two", "three", "four" };
	List<Item> list;

	public ListOfCompositions(List<Item> arg) {
		list = arg;
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
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.compositionlist_context_menu, menu);
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		ServiceTaskMessage message = ((MainActivity) getActivity()).taskMessage;
		message.action = Action.setToQueue;
		message.list=list;
		message.position = position;
		Messaging.fire(message);
		Log.d(Constants.LOG_TAG, "onListItemClick()");
	}

}
