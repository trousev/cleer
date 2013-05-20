package pro.trousev.cleer.android;

import java.util.List;

import android.widget.ProgressBar;
import android.widget.SeekBar;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Playlist;

public interface AndroidMessages {
	enum TypeOfResult {
		Compositions, Albums, Genres, Artists, Playlists, Queue, Playlist, PlaylistsInDialog, QueueChanged
	}

	enum Action {
		Play, Pause, Resume, Stop, Next, Previous, setToQueue, setToQueueBySearchQuery, addToQueue, addToQueueBySearchQuery, addToPlayListBySeachQuery, addToPlaylist, createNewList, scanSystem
	}

	//set ProgressBar
	public static class ProgressBarMessage implements Messaging.Message {
		public ProgressBar progressBar;
	}
	
	//set currentPosition in Player
	public static class SeekBarMessage implements Messaging.Message {
		public SeekBar seekBar;
		int position;
	}

	public static class ServiceRequestMessage implements Messaging.Message {
		public String searchQuery;
		public TypeOfResult type;
	}

	public static class ServiceRespondMessage implements Messaging.Message {
		public List<Playlist> playlists;
		public List<Item> list;
		public TypeOfResult typeOfContent;
	}

	public static class ServiceTaskMessage implements Messaging.Message {
		public Playlist playlist;
		public List<Item> list;
		public String searchQuery;
		public int position;
		public Action action;
		// could we find Playlist with only its' title?
	}
}
