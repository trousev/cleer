package pro.trousev.cleer.android.userInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import pro.trousev.cleer.android.userInterface.R;

public class ListOfRequests extends ListFragment {
	String data[] = new String[] { "one", "two", "three", "four", "one", "two",
			"three", "four", "one", "two", "three", "four" };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListOfRequestsAdapter adapter = new ListOfRequestsAdapter(getActivity(),
				R.layout.list_of_reqv_element, data);
		setListAdapter(adapter);
	}
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id){
		//getting here object that we got from lib
		listView.getItemAtPosition(position);
		//TODO get information from that object and give request to library
		((MainActivity)getActivity()).setListOfCompositions(null);
	}
}
