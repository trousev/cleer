package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;


public class ListOfCompositions extends ListFragment {
	String data[] = new String[] { "one", "two", "three", "four", "one", "two",
			"three", "four", "one", "two", "three", "four" };
	public ListOfCompositions(List<Item> list){
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListOfCompAdapter adapter = new ListOfCompAdapter(getActivity(), data);
		setListAdapter(adapter);
	}
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id){
		//TODO send this list to the queue, start playing from the selected item
	}
}
