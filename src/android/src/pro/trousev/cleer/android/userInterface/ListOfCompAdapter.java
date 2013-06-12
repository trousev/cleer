package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.NoSuchTagException;
import pro.trousev.cleer.sys.TagImplRussian;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ListOfCompAdapter extends ArrayAdapter<Item> {

	public ListOfCompAdapter(Context context, List<Item> items) {
		super(context, R.layout.list_of_comp_element, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView == null)
			view = ((Activity) getContext()).getLayoutInflater().inflate(
					R.layout.list_of_comp_element, null, false);
		else
			view = convertView;
		TextView artistName = (TextView) view
				.findViewById(R.id.comp_artist_name);
		TextView compName = (TextView) view.findViewById(R.id.comp_name);
		
		if(getItem(position) == (PlayBar.currentTrack)){
			ImageView icon = (ImageView) view.findViewById(R.id.is_played);
			icon.setBackgroundResource(android.R.drawable.ic_media_play);
		}else{
			ImageView icon = (ImageView) view.findViewById(R.id.is_played);
			icon.setBackgroundResource(0);
		}
		ImageView icon = (ImageView) view.findViewById(R.id.is_played);
		icon.setBackgroundResource(0);
		try {
			compName.setText(getItem(position).firstTag("title").value());
			artistName.setText(getItem(position).firstTag("artist").value());
		} catch (NoSuchTagException e) {
			e.printStackTrace();
		}
		return view;
	}

}
