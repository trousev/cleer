package pro.trousev.cleer.userInterface;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListOfRequestsAdapter extends ArrayAdapter<String> {

	public ListOfRequestsAdapter(Context context, int viewResourceId,
			String[] objects) {
		super(context, viewResourceId, objects);
		// TODO Auto-generated constructor stub
	}
	public View getView(int position, View convertView, ViewGroup viewgroup){
		View view;
		if (convertView==null)
			view = ((Activity)getContext()).getLayoutInflater().inflate(R.layout.list_of_reqv_element, null, false);
		else 
			view = convertView;
		TextView txtView = (TextView) view.findViewById(R.id.reqv_name);
		txtView.setText(getItem(position));
		return view;
	}

}
