package pro.trousev.cleer.android.userInterface;

import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.NoSuchTagException;
import pro.trousev.cleer.android.service.RusTag;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ListOfRequestsAdapter extends ArrayAdapter<String> {
	List<Item> list;
	String firstTag, secondTag;

	public ListOfRequestsAdapter(Context context, List<Item> list,
			String firstTag, String secondTag) {
		super(context, R.layout.list_of_reqv_element);
		this.list = list;
		this.firstTag = firstTag;
		this.secondTag = secondTag;
	}

	public View getView(int position, View convertView, ViewGroup viewgroup) {
		View view;
		if (convertView == null)
			view = ((Activity) getContext()).getLayoutInflater().inflate(
					R.layout.list_of_reqv_element, null, false);
		else
			view = convertView;
		TextView firstTextView = (TextView) view.findViewById(R.id.firstTag);
		TextView secondTextView = (TextView) view.findViewById(R.id.secondTag);
		RusTag rusTag = new RusTag();
		try{
			firstTextView.setText(rusTag.change(list.get(position).tag(firstTag).value()));
			firstTextView.setText(rusTag.change(list.get(position).tag(secondTag).value()));
		}catch(NoSuchTagException e){
			
		}catch(NullPointerException e){
			//FIXME resolve that
		}
			return view;
	}

}
