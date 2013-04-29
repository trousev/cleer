package pro.trousev.cleer.android.userInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Messaging.Event;
import pro.trousev.cleer.Messaging.Message;
import pro.trousev.cleer.Player.PlayerChangeEvent;
import pro.trousev.cleer.Player.Status;
import pro.trousev.cleer.android.userInterface.R;

public class PlayBar extends Fragment implements OnClickListener {
	private Button playPauseBtn, prevCompBtn, nextCompBtn, queueBtn,
			mainMenuBtn;
	private MainActivity root;
	private int status;
	final int IS_PLAYING = 1;
	final int IS_PAUSED = 0;

	public PlayBar() {
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.play_bar, null, false);
		root = (MainActivity) getActivity();
		playPauseBtn = (Button) view.findViewById(R.id.play_pause_btn);
		prevCompBtn = (Button) view.findViewById(R.id.prev_comp_btn);
		nextCompBtn = (Button) view.findViewById(R.id.next_comp_btn);
		queueBtn = (Button) view.findViewById(R.id.queue_btn);
		mainMenuBtn = (Button) view.findViewById(R.id.main_menu_btn);
		queueBtn.setOnClickListener(root);
		mainMenuBtn.setOnClickListener(root);
		playPauseBtn.setOnClickListener(this);
		prevCompBtn.setOnClickListener(this);
		nextCompBtn.setOnClickListener(this);
		
		Messaging.subscribe(Player.PlayerChangeEvent.class, new Event() {
			@Override
			public void messageReceived(Message message) {
				PlayerChangeEvent ev = (PlayerChangeEvent) message;
				if (ev.status == Status.Playing) {
					changeStatus(IS_PLAYING);
				}else{
					changeStatus(IS_PAUSED);
				}
			}
		});
		
		return view;
	}

	public void changeStatus(int newStatus) {
		status = newStatus;
		String string = null;
		switch (status) {
		case IS_PLAYING:
			string = "pause";
			break;
		case IS_PAUSED:
			string = "play";
			break;
		}
		playPauseBtn.setText(string);
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		int id = view.getId();
		switch (id) {
		case R.id.play_pause_btn:
			if (status == IS_PLAYING) {
				// TODO say to pause
				changeStatus(IS_PAUSED);
			} else if (status == IS_PAUSED) {
				// TODO say to play
				changeStatus(IS_PLAYING);
			}
			break;
		}
	}
}