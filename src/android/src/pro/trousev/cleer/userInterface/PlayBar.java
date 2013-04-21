package pro.trousev.cleer.userInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class PlayBar extends Fragment implements OnClickListener{
	private Button playPauseBtn, prevCompBtn, nextCompBtn, queueBtn, mainMenuBtn;
	private MainActivity root;
	public PlayBar(){
	}
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.play_bar, null, false);
		root = (MainActivity) getActivity();
		playPauseBtn= (Button) view.findViewById(R.id.play_pause_btn);
		prevCompBtn= (Button) view.findViewById(R.id.prev_comp_btn);
		nextCompBtn= (Button) view.findViewById(R.id.next_comp_btn);
		queueBtn= (Button) view.findViewById(R.id.queue_btn);
		mainMenuBtn= (Button) view.findViewById(R.id.main_menu_btn);
		queueBtn.setOnClickListener(root);
		mainMenuBtn.setOnClickListener(root);
		playPauseBtn.setOnClickListener(this);
		prevCompBtn.setOnClickListener(this);
		nextCompBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		
	}
}