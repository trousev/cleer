package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.android.AndroidMessages.ServiceRequestMessage;
import pro.trousev.cleer.android.AndroidMessages.TypeOfResult;
import pro.trousev.cleer.android.service.FileSystemScanner;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class MainMenu extends Fragment implements OnClickListener {
	MainActivity root;
	public MainMenu(){
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_menu, null, false);
		root=(MainActivity) getActivity();
		Button listsBtn = (Button) view.findViewById(R.id.lists_btn);
		Button compositionsBtn = (Button) view.findViewById(R.id.compositions_btn);
		Button artistsBtn = (Button) view.findViewById(R.id.artists_btn);
		Button queueBtn = (Button) view.findViewById(R.id.queue_btn);
		Button genresBtn = (Button) view.findViewById(R.id.genres_btn);
		Button albumsBtn = (Button) view.findViewById(R.id.albums_btn);
		listsBtn.setOnClickListener(this);
		compositionsBtn.setOnClickListener(this);
		artistsBtn.setOnClickListener(this);
		queueBtn.setOnClickListener(root);
		genresBtn.setOnClickListener(this);
		albumsBtn.setOnClickListener(this);
		return view;
	}
	
	@Override
	public void onClick(View view) {
		int id = view.getId();
		ServiceRequestMessage message = root.requestMessage;
		switch (id) {
		case R.id.compositions_btn:
			message.type=TypeOfResult.Compositions;
			message.searchQuery = null;
			FileSystemScanner scanner = new FileSystemScanner();
			List<Item> list = scanner.scanSystem();
			Messaging.fire(message);
			break;
		case R.id.lists_btn:
			message.type=TypeOfResult.Playlists;
			Messaging.fire(message);
			break;
		case R.id.genres_btn:
			message.type=TypeOfResult.Genres;
			Messaging.fire(message);
			break;
		case R.id.artists_btn:
			message.type=TypeOfResult.Artists;
			Messaging.fire(message);
			break;
		case R.id.albums_btn:
			message.type=TypeOfResult.Albums;
			Messaging.fire(message);
			break;
		default:
			break;	
		}
	}
}