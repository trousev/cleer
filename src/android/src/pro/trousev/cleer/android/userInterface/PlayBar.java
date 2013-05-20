package pro.trousev.cleer.android.userInterface;

import java.util.Timer;
import java.util.TimerTask;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.NoSuchTagException;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Messaging.Event;
import pro.trousev.cleer.Messaging.Message;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Player.PlayerChangeEvent;
import pro.trousev.cleer.Player.Status;
import pro.trousev.cleer.android.AndroidMessages.PlayBarAction;
import pro.trousev.cleer.android.AndroidMessages.PlayBarMessage;
import pro.trousev.cleer.android.AndroidMessages.ProgressBarMessage;
import pro.trousev.cleer.android.AndroidMessages.SeekBarMessage;
import pro.trousev.cleer.android.Constants;
import pro.trousev.cleer.android.service.RusTag;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class PlayBar extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {
	private Button queueBtn, mainMenuBtn;
	private ImageView playPauseBtn, prevCompBtn, nextCompBtn;
	private TextView compName;
	static Item currentTrack = null;
	private MainActivity root;
	final int PLAYING = 1;
	final int STOPPED = 0;
	final int PAUSED = 2;
	private int status = STOPPED;
	private SeekBar progressBar;
	private SeekBarMessage seekBarMessage = null;
	private ProgressBarMessage progressBarMessage = null;
	private PlayBarMessage playBarMessage = null;
	Timer progressBarTimer = null;
	TimerTask progressBarTimerTask = null;
	private PlayerChangedStatusEvent playerChangedStatusEvent;

	public PlayBar() {
	}

	private class PlayerChangedStatusEvent implements Event {
		@Override
		public void messageReceived(Message message) {
			PlayerChangeEvent ev = (PlayerChangeEvent) message;
			if ((ev.status == Status.Playing)
					|| (ev.status == Status.Processing)) {
				changeStatus(PLAYING);
			} else {
				if (ev.status == Status.Paused)
					changeStatus(PAUSED);
				else
					changeStatus(STOPPED);
			}
			if (ev.track != null) {
				try {
					currentTrack = ev.track;
					RusTag rusTag = new RusTag();
					compName.setText(rusTag.change(ev.track.tag("title")
							.value()));
				} catch (NoSuchTagException e) {
					e.printStackTrace();
				}
			}
			if (root.queue != null)
				root.queue.getAdapter().notifyDataSetChanged();
			Messaging.fire(progressBarMessage);
		}
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.play_bar, null, false);
		root = (MainActivity) getActivity();
		compName = (TextView) view.findViewById(R.id.playing_compotision_name);
		playPauseBtn = (ImageView) view.findViewById(R.id.play_pause_btn);
		prevCompBtn = (ImageView) view.findViewById(R.id.prev_comp_btn);
		nextCompBtn = (ImageView) view.findViewById(R.id.next_comp_btn);
		queueBtn = (Button) view.findViewById(R.id.queue_btn);
		mainMenuBtn = (Button) view.findViewById(R.id.main_menu_btn);
		progressBar = (SeekBar) view.findViewById(R.id.player_progress_bar);
		progressBar.setOnSeekBarChangeListener(this);
		queueBtn.setOnClickListener(root);
		mainMenuBtn.setOnClickListener(root);
		playPauseBtn.setOnClickListener(this);
		prevCompBtn.setOnClickListener(this);
		nextCompBtn.setOnClickListener(this);
		playPauseBtn.setBackgroundResource(R.drawable.play_btn);
		prevCompBtn.setBackgroundResource(R.drawable.prev);
		nextCompBtn.setBackgroundResource(R.drawable.next);
		playerChangedStatusEvent = new PlayerChangedStatusEvent();
		Messaging.subscribe(Player.PlayerChangeEvent.class,
				playerChangedStatusEvent);
		
		playBarMessage = new PlayBarMessage();
		progressBarMessage = new ProgressBarMessage();
		seekBarMessage = new SeekBarMessage();
		progressBarMessage.progressBar = this.progressBar;
		progressBarTimerTask = new TimerTask() {

			@Override
			public void run() {
				Messaging.fire(progressBarMessage);
			}
		};
		progressBarTimer = new Timer();
		progressBarTimer.scheduleAtFixedRate(progressBarTimerTask, 0,
				Constants.PROGRESSBAR_TIMER_RATE);
		return view;
	}

	public void changeStatus(int newStatus) {
		status = newStatus;
		switch (status) {
		case PLAYING:
			playPauseBtn.setBackgroundResource(R.drawable.pause);
			break;
		case PAUSED:
			playPauseBtn.setBackgroundResource(R.drawable.play_btn);
			break;
		case STOPPED:
			playPauseBtn.setBackgroundResource(R.drawable.play_btn);
			break;
		}
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.play_pause_btn:
			switch (status) {
			case PLAYING:
				playBarMessage.action = PlayBarAction.Pause;
				break;
			case PAUSED:
				playBarMessage.action = PlayBarAction.Resume;
				break;
			case STOPPED:
				playBarMessage.action = PlayBarAction.Play;
				break;
			}
			break;
		case R.id.next_comp_btn:
			playBarMessage.action = PlayBarAction.Next;
			break;
		case R.id.prev_comp_btn:
			playBarMessage.action = PlayBarAction.Previous;
			break;
		default:
			Log.e(Constants.LOG_TAG, "PlayBar: Illegal state id");
			break;
		}
		Messaging.fire(playBarMessage);
	}

	@Override
	public void onDestroy() {
		progressBar = null; // Allows Service to know is Activity running or not
		Log.d(Constants.LOG_TAG, "PlayBar.onDestroy()");
		Messaging.unSubscribe(Player.PlayerChangeEvent.class,
				playerChangedStatusEvent);
		progressBarMessage.progressBar = null;
		progressBarTimerTask.cancel();
		progressBarTimerTask = null;
		progressBarTimer.cancel();
		progressBarTimer = null;
		super.onDestroy();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		seekBarMessage.seekBar = seekBar;
		seekBarMessage.value = seekBar.getProgress();
		Messaging.fire(seekBarMessage);
	}
}