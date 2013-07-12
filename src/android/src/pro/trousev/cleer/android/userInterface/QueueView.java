package pro.trousev.cleer.android.userInterface;

import java.util.List;


import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SeekBar;
import android.widget.TextView;
import pro.trousev.cleer.Database.DatabaseError;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.ReadOnlyTagException;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Queue;
import pro.trousev.cleer.Item.NoSuchTagException;
import pro.trousev.cleer.Messaging.Message;
import pro.trousev.cleer.android.userInterface.SwipePageAdapter.SwipePage;

public class QueueView extends SwipePage implements OnItemClickListener, OnClickListener{
	
	static private class QueueViewAdapter
		extends ArrayAdapter<Item>
		implements OnRatingBarChangeListener
	{
		private RatingBar _star = null;
		Context _context;
		int _resource_id;
		public QueueViewAdapter(Context context) 
		{
		    super(context, R.layout.queue_item);
		    _context = context;
		    _resource_id = R.layout.queue_item;
		}
		public void focus(List<Item> list)
		{
			clear();
			for(Item i: list)
				add(i);
		}
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View rowView ;
		    if(convertView == null)
		    {
		    	LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	rowView = inflater.inflate(_resource_id, parent, false);
		    }
		    else 
		    	rowView = convertView;

		    if(_star == null)
		    {
		    	LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	_star = (RatingBar) inflater.inflate(R.layout.pretty_rating_bar, parent, false);
		    	//_star = new RatingBar(_context);
		    }
		    
		    TextView title  = (TextView) rowView.findViewById(R.id.queue_title);
		    TextView artist = (TextView) rowView.findViewById(R.id.queue_artist);
		    //TextView album  = (TextView) rowView.findViewById(R.id.queue_album);
		    TextView time   = (TextView) rowView.findViewById(R.id.queue_len);
		    ImageView icon = (ImageView) rowView.findViewById(R.id.queue_icon);
		    LinearLayout star_box  = (LinearLayout) rowView.findViewById(R.id.queue_star_container);
		    Item i = getItem(position);
		    try 
		    {
		    	if(MainActivity.service.player().now_playing().equals(i))
		    	{
		    		icon.setVisibility(View.VISIBLE);
		    		ViewGroup p = ((ViewGroup)_star.getParent());
		    		if(p!=null)p.removeView(_star);
		    		star_box.addView(_star);
		    	}
		    	else
		    	{
		    		icon.setVisibility(View.INVISIBLE);
		    		star_box.removeAllViews();
		    	}
				title.setText(i.firstTag("title").value());
			    artist.setText(i.firstTag("artist").value());
			    float r = 0;
			    String tag = i.firstTag("rating").value();
			    if(tag.length() > 0) r = new Float(tag);
			    //album.setText(i.firstTag("album").value());
			    //album.setText("");
			} 
		    catch (NoSuchTagException e) 
			{
				e.printStackTrace();
			}
		    time.setText("");
		    //textView.setText(i.title());
		    //qView.setText(i.query());
		    return rowView;
		  }
		@Override
		public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				int new_rating = (int) rating;
				System.out.println("New rating: "+new_rating+"fromUser: "+fromUser);
				if(!fromUser) return ;
				Item itm = MainActivity.service.queue().playing_track();
				int old_rating = 0;
				try
				{
					old_rating = new Integer(itm.firstTag("rating").value());
				} catch (Throwable t) {}
				System.out.println("Old = "+old_rating);
				System.out.println("New = "+new_rating);
				if(new_rating == old_rating) return ;
				try {
					System.out.println("RATING UPDATED FOR SONG = "+itm.toString());
					itm.setTagValue("rating", String.format("%d", new_rating));
				} catch (NoSuchTagException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ReadOnlyTagException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (DatabaseError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	QueueViewAdapter _adapter;
	ImageButton _prev;
	ImageButton _next;
	ImageButton _play;
	ImageButton _shuffle;
	SeekBar _seek;
	Boolean _seekBarFlag = true;
	Thread _seekBarUpdater = null;
	
	Messaging.Event _queueChangedEvent = new Messaging.Event() {
		
		@Override
		public void messageReceived(Message message) {
			_adapter.focus(MainActivity.service.queue().queue());
		}
	};
	
	Messaging.Event _songChangeEvent = new Messaging.Event() {
		@Override
		public void messageReceived(Message message) {
			int no = ((Queue.QueueSongChangedMessage) message ).track_number;
			System.out.println("[cleer] Focus is on: "+no);
			_adapter.notifyDataSetChanged();
		}
	};
	
	
	private boolean subscribed = false;
	QueueView(SwipePageAdapter parent) {
		super(parent);
	}

	View _view = null;

	@Override
	public String getName() {
		return "Queue";
	}

	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.queue_view, container, false);
		
        ListView queueListView = (ListView) rootView.findViewById(R.id.queue_list);
		System.out.println("[cleer] queueListView = "+queueListView);
		
		//ArrayAdapter <String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.playlist_item);
		_adapter = new QueueViewAdapter(rootView.getContext());
		
		queueListView.setAdapter(_adapter);
		queueListView.setOnItemClickListener(this);
		_prev = (ImageButton) rootView.findViewById(R.id.queue_prev);
		_play = (ImageButton) rootView.findViewById(R.id.queue_play);
		_next = (ImageButton) rootView.findViewById(R.id.queue_next);
		_shuffle = (ImageButton) rootView.findViewById(R.id.queue_shuffle);
		_seek = (SeekBar) rootView.findViewById(R.id.queue_seek);
		_seek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				//System.out.println("[cleer] seek="+seekBar.getProgress());
				int np = seekBar.getProgress();
				MainActivity.service.player().setCurrentPosition(np);
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

			}
		});
		_prev.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.service.queue().prev();
			}
		});
		_next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.service.queue().next();
			}
		});
		_play.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(MainActivity.service.player().getStatus() == Player.Status.Playing)
				{
					MainActivity.service.player().pause();
				}
				else
					MainActivity.service.player().resume();
			}
		});
		_shuffle.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MainActivity.service.queue().shuffle();
			}
		});
		//_selfView = rootView;
		if(!subscribed)
		{
			subscribed = true;
			Messaging.subscribe(Queue.QueueChangedMessage.class, _queueChangedEvent);
			Messaging.subscribe(Queue.QueueSongChangedMessage.class, _songChangeEvent);
		}
		subscribed = true;
		
		if(_seekBarUpdater == null)
		{
			System.out.println("[cleer] Creating thread...");
			_seekBarUpdater = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while(true)
						{
							synchronized (_seekBarFlag) {
								if(!_seekBarFlag)
									return ;
							}
							//System.out.println("[cleer] Worker Thread Call");
							_seek.setMax(MainActivity.service.player().getDuration());
							_seek.setProgress(MainActivity.service.player().getCurrentPosition());
							Thread.sleep(300);
						}
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			_seekBarUpdater.start();
		}

		Messaging.fire(new Queue.QueueChangedMessage());
		return rootView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		int cur = MainActivity.service.queue().playing_index();
		MainActivity.service.queue().seek(arg2 - cur);
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		System.out.println();
	}
	
	@Override
	public void onDestroy()
	{
		Messaging.unSubscribe(Queue.QueueChangedMessage.class, _queueChangedEvent);
		Messaging.unSubscribe(Queue.QueueSongChangedMessage.class, _songChangeEvent);
		synchronized(_seekBarFlag)
		{
			_seekBarFlag = false;
		}
	}
	/*public void focus(Playlist p)
	{
		_adapter.focus(p);
	}*/


}
