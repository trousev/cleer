package pro.trousev.cleer.userInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class MainMenu extends Fragment implements OnClickListener {
	private MainActivity root;
	public MainMenu(MainActivity mainActivity){
		root=mainActivity;
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.main_menu, null, false);
		Button listsBtn = (Button) view.findViewById(R.id.lists_btn);
		Button compositionsBtn = (Button) view.findViewById(R.id.compositions_btn);
		Button artistsBtn = (Button) view.findViewById(R.id.artists_btn);
		Button queueBtn = (Button) view.findViewById(R.id.queue_btn);
		Button genresBtn = (Button) view.findViewById(R.id.genres_btn);
		Button albumsBtn = (Button) view.findViewById(R.id.albums_btn);
		Button filesBtn = (Button) view.findViewById(R.id.files_btn);
		Button equalizerBtn = (Button) view.findViewById(R.id.equalizer_btn);
		listsBtn.setOnClickListener(this);
		compositionsBtn.setOnClickListener(this);
		artistsBtn.setOnClickListener(this);
		queueBtn.setOnClickListener(root);
		genresBtn.setOnClickListener(this);
		albumsBtn.setOnClickListener(this);
		filesBtn.setOnClickListener(this);
		equalizerBtn.setOnClickListener(this);
		return view;
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
}