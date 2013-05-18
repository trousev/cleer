package pro.trousev.cleer.android.userInterface;

import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.AndroidMessages.ServiceRequestMessage;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import pro.trousev.cleer.android.AndroidMessages.TypeOfResult;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;


public class MainMenu extends ListFragment {
	MainActivity root = null;
	private String[] menuPoints;
	public MainMenu(){
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		root = (MainActivity) getActivity();
		String[] strings = {root.getResources().getString(R.string.compositions), root.getResources().getString(R.string.lists),
				root.getResources().getString(R.string.queue), root.getResources().getString(R.string.artists), 
				root.getResources().getString(R.string.genres), root.getResources().getString(R.string.albums), 
				root.getResources().getString(R.string.refresh), root.getResources().getString(R.string.exit)};
		menuPoints = strings;
		MainMenuAdapter adapter = new MainMenuAdapter(root, menuPoints );
		setListAdapter(adapter);
	}
	
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		int viewId = view.getId();
		ServiceRequestMessage message = root.requestMessage;
		switch (viewId) {
		case 0:
			message.type=TypeOfResult.Compositions;
			message.searchQuery = null;
			Messaging.fire(message);
			break;
		case 1:
			message.type=TypeOfResult.Queue;
			Messaging.fire(message);
		case 2:
			message.type=TypeOfResult.Playlists;
			Messaging.fire(message);
			break;
		case 4:
			message.type=TypeOfResult.Genres;
			Messaging.fire(message);
			break;
		case 3:
			message.type=TypeOfResult.Artists;
			Messaging.fire(message);
			break;
		case 5:
			message.type=TypeOfResult.Albums;
			Messaging.fire(message);
			break;
		case 6:
			ServiceTaskMessage mes = new ServiceTaskMessage();
			mes.action = Action.scanSystem;
			Messaging.fire(mes);
			break;
		case 7:
			root.exit();
			break;
		default:
			break;	
		}
	}
}