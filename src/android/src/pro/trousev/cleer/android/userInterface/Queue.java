package pro.trousev.cleer.android.userInterface;

import java.util.ArrayList;
import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.NoSuchTagException;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Messaging.Event;
import pro.trousev.cleer.Messaging.Message;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.android.AndroidMessages;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.AndroidMessages.ServiceRequestMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import pro.trousev.cleer.android.AndroidMessages.TypeOfResult;
import pro.trousev.cleer.android.Constants;
import pro.trousev.cleer.android.service.RusTag;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Queue extends ListOfCompositions {

	public Queue(List<Item> arg) {
		super(arg);
	}

	public Queue(Playlist playlist) {
		super(playlist);
	}
	
	private class QueueAdapter extends ListOfCompAdapter {

		public QueueAdapter(Context context, List<Item> items) {
			super(context, items);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = super.getView(position, convertView, parent);
			if (getItem(position) == (PlayBar.currentTrack)) {
				ImageView icon = (ImageView) view.findViewById(R.id.is_played);
				icon.setBackgroundResource(android.R.drawable.ic_media_play);
			} else {
				ImageView icon = (ImageView) view.findViewById(R.id.is_played);
				icon.setBackgroundResource(0);
			}
			return view;
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		adapter = new QueueAdapter(getActivity(), list);
		setListAdapter(adapter);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View view,
			ContextMenuInfo menuInfo) {
		Item item = (Item) list
				.get(((AdapterContextMenuInfo) menuInfo).position);
		try {
			RusTag rusTag = new RusTag();
			menu.setHeaderTitle(rusTag.change(item.tag("title").value()));
		} catch (NoSuchTagException e) {
			e.printStackTrace();
		}
		super.onCreateContextMenu(menu, view, menuInfo);
		menu.clear();
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.queue_context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo acmi = (AdapterContextMenuInfo) item
				.getMenuInfo();
		ServiceTaskMessage message = ((MainActivity) getActivity()).taskMessage;
		Item track;
		switch (item.getItemId()) {
		case R.id.play:
			message.action = Action.setToQueue;
			track = list.get(acmi.position);
			message.list = new ArrayList<Item>();
			message.list.add(track);
			message.position = 0;
			Messaging.fire(message);
			break;
		case R.id.clearQueue:
			message.action = Action.clearQueue;
			Messaging.fire(message);
			list.clear();
			getAdapter().notifyDataSetChanged();
			break;
		case R.id.addToList:
			ServiceRequestMessage msg = ((MainActivity) getActivity()).requestMessage;
			msg.type = TypeOfResult.PlaylistsInDialog;
			msg.item = list.get(acmi.position);
			Messaging.fire(msg);
			// TODO What to do?
			break;
		}
		return true;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		ServiceTaskMessage message = ((MainActivity) getActivity()).taskMessage;
		message.action = Action.setToQueue;
		message.list = list;
		message.position = position;
		Messaging.fire(message);
	}

}
