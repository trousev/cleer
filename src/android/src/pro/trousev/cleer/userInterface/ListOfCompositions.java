package pro.trousev.cleer.userInterface;

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

public class ListOfCompositions extends ListFragment {
	String data[] = new String[] { "one", "two", "three", "four", "one", "two",
			"three", "four", "one", "two", "three", "four" };
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ListOfCompAdapter adapter = new ListOfCompAdapter(getActivity(),
				R.layout.list_of_comp_element, data);
		setListAdapter(adapter);
	}
}
