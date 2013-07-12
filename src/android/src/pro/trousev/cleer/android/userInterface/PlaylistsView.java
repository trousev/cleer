package pro.trousev.cleer.android.userInterface;

import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.Messaging.Message;
import pro.trousev.cleer.Playlist.Type;
import pro.trousev.cleer.Queue.EnqueueMode;
import pro.trousev.cleer.Queue;
import pro.trousev.cleer.android.userInterface.SwipePageAdapter.SwipePage;

public class PlaylistsView extends SwipePage implements OnItemClickListener{

	PlaylistsView(SwipePageAdapter parent) {
		super(parent);
	}

	public static final String PLAYLIST_ADD_STRING = "<Add new playlist>";
	static private class PlaylistViewAdapter 
		extends ArrayAdapter<Playlist> 
	{
		Context _context;
		int _resource_id;
		public void refresh()
		{
			clear();
		    for(Playlist p: MainActivity.service.library().playlists())
		    {
		    	System.out.println("[cleer] Adding Playlist: "+p);
		    	add(p);
		    }
	    	System.out.println("[cleer] Adding Custom Add Playlist ");
		    add(new Playlist() {
				
				@Override
				public Type type() {
					return Type.PredefinedPlaylist;
				}
				
				@Override
				public String title() {
					return PLAYLIST_ADD_STRING;
				}
				
				@Override
				public Playlist setType(Type new_type) {
					return this;
				}
				
				@Override
				public boolean save(String name) {
					return false;
				}
				
				@Override
				public String query() {
					return "";
				}
				
				@Override
				public List<Item> contents() {
					return null;
				}
			});
			
		}
		public PlaylistViewAdapter(Context context) 
		{
		    super(context, R.layout.playlist_item);
		    _context = context;
		    _resource_id = R.layout.playlist_item;
		    refresh();
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View rowView ;
		    if(convertView == null)
		    {
		    	LayoutInflater inflater = (LayoutInflater) _context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    	rowView = inflater.inflate(_resource_id, parent, false);
		    }
		    else 
		    	rowView = convertView;
		    
		    TextView textView = (TextView) rowView.findViewById(R.id.playlist_title);
		    TextView qView = (TextView) rowView.findViewById(R.id.playlist_query);
		    Playlist i = getItem(position);
		    /*System.out.println("[cleer] Populating position: "+position);
		    System.out.println("[cleer]    =>>      item: "+i);
		    System.out.println("[cleer]    =>>      title: "+i.title());
		    System.out.println("[cleer]    =>>      textView: "+textView);*/
		    
		    textView.setText(i.title());
		    qView.setText(i.query());
		    return rowView;
		  }
	}
	private class PlayListPopup extends SimplePopup implements OnClickListener
	{
		Button _ok;
		Button _cancel;
		EditText _title;
		EditText _query;
		public PlayListPopup(int target_layout, View parent_view) {
			super(target_layout, parent_view);
			_ok = (Button) getView().findViewById(R.id.playlist_add_ok_button);
			_cancel = (Button)getView().findViewById(R.id.playlist_add_cancel_button);
			_title = (EditText) getView().findViewById(R.id.playlist_add_title);
			_query = (EditText) getView().findViewById(R.id.playlist_add_query);
			_ok.setOnClickListener(this);
			_cancel.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			System.out.println("[cleer] popupOnClick");
			if(v == _ok)
			{
				addPlaylist(_title.getText().toString(), _query.getText().toString());
				hide();
			}
			if(v == _cancel)
			{
				hide();
			}
		}
		
	}
	private PlaylistViewAdapter _adapter; 
	private LayoutInflater _inflater = null;
	private View _selfView = null;
	private ViewGroup _container = null;
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container) {
		_inflater = inflater;
		_container = container;
		View rootView = inflater.inflate(R.layout.playlist_layout, container, false);
		System.out.println("[cleer] rootView= "+rootView);
		ListView viewPlaylists = (ListView) rootView.findViewById(R.id.view_playlists);
		System.out.println("[cleer] viewPlaylists = "+viewPlaylists);
		
		//ArrayAdapter <String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.playlist_item);
		_adapter = new PlaylistViewAdapter(rootView.getContext());
		
		viewPlaylists.setAdapter(_adapter);
		viewPlaylists.setOnItemClickListener(this);
		_selfView = rootView;
		return rootView;
	}
	
	@Override
	public String getName() {
		return "Playlists";
	}
	
	@Override
	public void onShow() {
		// TODO Auto-generated method stub
	}
	@Override
	public Menu updateMenu(Menu m)
	{
		return m;
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		System.out.println("[cleer] Item clicked: "+arg2);
		Playlist pl = _adapter.getItem(arg2);
		if(pl.title().equals(PLAYLIST_ADD_STRING))
		{
			PlayListPopup p = new PlayListPopup(R.layout.playlist_add_dialog, _selfView);
			p.show();
		}
		else
		{
			QueueView qv = (QueueView)sibling(QueueView.class);
			//qv.focus(pl);
			MainActivity.service.queue().enqueue(pl.contents(), EnqueueMode.ReplaceAll);
			swipe(sibling(QueueView.class));
		}
	}
	public void addPlaylist(String name, String query)
	{
		MainActivity.service.library().setFocus(name);
		MainActivity.service.library().search(query);
		
		_adapter.refresh();
		System.out.println("[cleer] NOT IMPLEMENTED YET");
	}

}
