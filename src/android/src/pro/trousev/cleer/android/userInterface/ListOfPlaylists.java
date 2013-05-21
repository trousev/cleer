package pro.trousev.cleer.android.userInterface;

import java.util.ArrayList;
import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.AndroidMessages.ServiceRequestMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import pro.trousev.cleer.android.AndroidMessages.TypeOfResult;
import pro.trousev.cleer.android.Constants;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ListOfPlaylists extends ListFragment {
	List<Playlist> playlists;
	private ArrayAdapter<String> adapter = null;

	public ListOfPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		List<String> playlistNames = new ArrayList<String>();
		for (Playlist pl : playlists) {
			playlistNames.add(pl.title());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, playlistNames);
		this.setHasOptionsMenu(true);
		setListAdapter(adapter);
	}

	public ArrayAdapter<String> getAdapter() {
		return adapter;
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
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
				.getMenuInfo();
		ServiceTaskMessage message = ((MainActivity) getActivity()).taskMessage;
		List<Item> tracks = new ArrayList<Item>();
		switch (item.getItemId()) {
		case R.id.play:
			message.action = Action.setToQueue;
			tracks = playlists.get(acmi.position).contents();
			message.list = tracks;
			message.position = 0;
			Messaging.fire(message);
			break;
		case R.id.addToQueue:
			message.action = Action.addToQueue;
			tracks = playlists.get(acmi.position).contents();
			message.list = tracks;
			Messaging.fire(message);
			break;
		}
		return true;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		MainActivity activity = ((MainActivity) getActivity());
		activity.setListOfCompositions(playlists.get(position).contents());
	}
}
