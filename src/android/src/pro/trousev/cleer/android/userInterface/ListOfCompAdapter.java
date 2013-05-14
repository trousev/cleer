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

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import org.jaudiotagger.tag.FieldKey;

public class ListOfCompAdapter extends ArrayAdapter<Item> {
	

	private final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	String decodeUTF8(byte[] bytes) {
	    return new String(bytes, UTF8_CHARSET);
	}
	byte[] encodeUTF8(String string) {
	    return string.getBytes(UTF8_CHARSET);
	}


	public ListOfCompAdapter(Context context, List<Item> items) {
		super(context, R.layout.list_of_comp_element, items);
		// TODO Auto-generated constructor stub
	}

	Charset C= Charset.forName("windows-1251");
	ByteBuffer buffer=null;
	
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
					+ (C.decode(buffer.wrap(getItem(position).tag("artist").value().getBytes("UTF-16LE")))));
			compName.setText(getItem(position).tag("title").value());
		} catch (NoSuchTagException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

}
