package pro.trousev.cleer.android.userInterface;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import pro.trousev.cleer.Item;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.Playlist.Type;
import pro.trousev.cleer.android.userInterface.SwipePageAdapter.SwipePage;

public class PlaylistsView implements SwipePage, OnItemClickListener{

	static private class PlaylistViewAdapter 
		extends ArrayAdapter<Playlist> 
	{
		Context _context;
		int _resource_id;
		public PlaylistViewAdapter(Context context) 
		{
		    super(context, R.layout.playlist_item);
		    _context = context;
		    _resource_id = R.layout.playlist_item;
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
					return "<Add New Playlist>";
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
	
	@Override
	public View getView(LayoutInflater inflater, ViewGroup container) {
		View rootView = inflater.inflate(R.layout.playlist_layout, container, false);
		ListView viewPlaylists = (ListView) rootView.findViewById(R.id.view_playlists);
		System.out.println("[cleer] "+viewPlaylists);
		
		//ArrayAdapter <String> adapter = new ArrayAdapter<String>(rootView.getContext(), R.layout.playlist_item);
		PlaylistViewAdapter adapter = new PlaylistViewAdapter(rootView.getContext());
		
		viewPlaylists.setAdapter(adapter);
		viewPlaylists.setOnItemClickListener(this);
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		System.out.println("[cleer] Item clicked: "+arg2);
	}


}
