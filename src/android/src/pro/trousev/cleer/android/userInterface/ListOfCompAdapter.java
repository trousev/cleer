package pro.trousev.cleer.android.userInterface;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListOfCompAdapter extends ArrayAdapter<String>{

	public ListOfCompAdapter(Context context, String[] strings) {
		super(context, R.layout.list_of_comp_element, strings);
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
		artistName.setText("artist name" + getItem(position));
		compName.setText("compName");
		return view;
	}

}
