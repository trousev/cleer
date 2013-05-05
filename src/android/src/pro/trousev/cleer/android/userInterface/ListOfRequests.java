package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.AndroidMessages.ServiceRequestMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import pro.trousev.cleer.android.AndroidMessages.TypeOfResult;
import pro.trousev.cleer.android.Constants;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

public class ListOfRequests extends ListFragment {
	String data[] = new String[] { "one", "two", "three", "four", "one", "two",
			"three", "four", "one", "two", "three", "four" };
	List<Item> list;
	public ListOfRequests(List<Item> arg, String firstTagANme, String secondTagName){
		list = arg;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListOfRequestsAdapter adapter = new ListOfRequestsAdapter(getActivity(), data);
		setListAdapter(adapter);
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setOnCreateContextMenuListener(this);
		Log.d(Constants.LOG_TAG, "ListOfRequest.onActivityCreated()");
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		Log.d(Constants.LOG_TAG, "ListOfComp.onCreateCotextMenu()");
		super.onCreateContextMenu(menu, view, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.requestlist_context_menu, menu);
	}
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
				.getMenuInfo();
		ServiceTaskMessage message = ((MainActivity) getActivity()).taskMessage;
		Item track;
		switch (item.getItemId()) {
		case R.id.play:
			message.action = Action.setToQueueBySearchQuery;
//			track = list.get(acmi.position);
//			message.list = null;
//			message.list.add(track);
//			message.position=list.indexOf(track);
			Messaging.fire(message);
			break;
		case R.id.addToQueue:
			message.action = Action.addToQueueBySearchQuery;
//			track = list.get(acmi.position);
//			message.searchQuery = track.getSearchQuery();
			Messaging.fire(message);
			break;
		case R.id.addToList:
			//TODO What to do?
			break;
		}
		return true;
	}
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id){
		//Item item = (Item)listView.getItemAtPosition(position);
		//String searchQuery = item.getSearchQuery();
		ServiceRequestMessage message = new ServiceRequestMessage();
		message.type=TypeOfResult.Compositions;
		//message.searchQuery=searchQuery;
		Messaging.fire(message);
		//TODO get information from that object and give request to library
	}
}
