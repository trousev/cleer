package pro.trousev.cleer.android.userInterface;

import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Messaging.Event;
import pro.trousev.cleer.Messaging.Message;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Player.PlayerChangeEvent;
import pro.trousev.cleer.Player.Status;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.Constants;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

public class PlayBar extends Fragment implements OnClickListener {
	private Button playPauseBtn, prevCompBtn, nextCompBtn, queueBtn,
			mainMenuBtn;
	private MainActivity root;
	final int PLAYING = 1;
	final int NOT_PLAYING = 0;
	private int status = NOT_PLAYING;
	public static ProgressBar progressBar;
	private PlayerChangedStatusEvent playerChangedStatusEvent;
	
	public PlayBar() {
	}
	private class PlayerChangedStatusEvent implements Event{
	@Override
	public void messageReceived(Message message) {
		PlayerChangeEvent ev = (PlayerChangeEvent) message;
		if (ev.status == Status.Playing) {
			changeStatus(PLAYING);
		} else {
			changeStatus(NOT_PLAYING);
		}
		Log.d(Constants.LOG_TAG, "PlayBar.messageREceived()");
	}
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
		progressBar = (ProgressBar) view.findViewById(R.id.player_progress_bar);
		queueBtn.setOnClickListener(root);
		mainMenuBtn.setOnClickListener(root);
		playPauseBtn.setOnClickListener(this);
		prevCompBtn.setOnClickListener(this);
		nextCompBtn.setOnClickListener(this);
		playerChangedStatusEvent = new PlayerChangedStatusEvent();
		Messaging.subscribe(Player.PlayerChangeEvent.class, playerChangedStatusEvent);

		return view;
	}

	public void changeStatus(int newStatus) {
		status = newStatus;
		String string = null;
		switch (status) {
		case PLAYING:
			string = "pause";
			break;
		case NOT_PLAYING:
			string = "play";
			break;
		}
		playPauseBtn.setText(string);
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		Log.d(Constants.LOG_TAG, "status = " + status);
		switch (id) {
		case R.id.play_pause_btn:
			if (status == PLAYING) {
				root.taskMessage.action=Action.Pause;
				Messaging.fire(root.taskMessage);
			} else if (status == NOT_PLAYING) {
				root.taskMessage.action=Action.Play;
				Messaging.fire(root.taskMessage);
			}
			break;
		case R.id.next_comp_btn:
				root.taskMessage.action=Action.Next;
				Messaging.fire(root.taskMessage);
			break;
		case R.id.prev_comp_btn:
			root.taskMessage.action=Action.Previous;
			Messaging.fire(root.taskMessage);
			break;
		default:
			break;
		}
	}
	@Override
	public void onDestroy(){
		progressBar=null; // Allows Service to know is Activity running or not
		Log.d(Constants.LOG_TAG, "PlayBar.onDestroy()");
		Messaging.unSubscribe(Player.PlayerChangeEvent.class, playerChangedStatusEvent);
		super.onDestroy();
	}
}