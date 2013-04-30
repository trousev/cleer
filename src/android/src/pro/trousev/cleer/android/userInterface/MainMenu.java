package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import pro.trousev.cleer.android.userInterface.R;


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
		switch (id) {
		case R.id.compositions_btn:
			root.setListOfCompositions(null);
			break;
		case R.id.lists_btn:
			root.setListOfRequests(null);
			break;
		case R.id.genres_btn:
			root.setListOfRequests(null);
			break;
		case R.id.artists_btn:
			root.setListOfRequests(null);
			break;
		case R.id.albums_btn:
			root.setListOfRequests(null);
			break;
		default:
			break;	
		}
	}
}