package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.NoSuchTagException;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListOfCompAdapter extends ArrayAdapter<Item> {

	public ListOfCompAdapter(Context context, List<Item> items) {
		super(context, R.layout.list_of_comp_element, items);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Here will be my description of how i create my View
		View view;
		if (convertView == null)
			view = ((Activity) getContext()).getLayoutInflater().inflate(
					R.layout.list_of_comp_element, null, false);
		else
			view = convertView;
		TextView artistName = (TextView) view
				.findViewById(R.id.comp_artist_name);
		TextView compName = (TextView) view.findViewById(R.id.comp_name);
		// TODO set text from TrackImpl
		try {
			artistName.setText("artist name"
					+ getItem(position).tag("artist").value());
			compName.setText(getItem(position).tag("title").value());
		} catch (NoSuchTagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

}
