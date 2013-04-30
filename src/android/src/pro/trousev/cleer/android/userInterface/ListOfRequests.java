package pro.trousev.cleer.android.userInterface;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

public class ListOfRequests extends ListFragment {
	String data[] = new String[] { "one", "two", "three", "four", "one", "two",
			"three", "four", "one", "two", "three", "four" };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListOfRequestsAdapter adapter = new ListOfRequestsAdapter(getActivity(), data);
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