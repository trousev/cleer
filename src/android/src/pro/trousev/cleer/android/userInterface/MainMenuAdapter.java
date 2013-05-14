package pro.trousev.cleer.android.userInterface;

import java.util.ArrayList;
import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Item.NoSuchTagException;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MainMenuAdapter extends ArrayAdapter<String>{
	private String[] res;
	private Context context;
	public MainMenuAdapter(Context context, String[] menuPoints) {
		super(context, R.layout.main_menu_row, menuPoints);
		res = menuPoints;
		this.context =context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Here will be my description of how i create my View
		View view;
		if (convertView == null)
			view = ((Activity) getContext()).getLayoutInflater().inflate(
					R.layout.main_menu_row, null, false);
		else
			view = convertView;
		TextView title = (TextView) view.findViewById(R.id.main_menu_row_text);
		ImageView image = (ImageView) view.findViewById(R.id.main_menu_row_icon);
		title.setText(new String(res[position]));
		if(res[position] == context.getResources().getString(R.string.compositions))
			view.setId(0);
		if(res[position] == context.getResources().getString(R.string.queue))
			view.setId(1);
		if(res[position] == context.getResources().getString(R.string.lists))
			view.setId(2);
		if(res[position] == context.getResources().getString(R.string.artists))
			view.setId(3);
		if(res[position] == context.getResources().getString(R.string.genres))
			view.setId(4);
		if(res[position] == context.getResources().getString(R.string.albums))
			view.setId(5);
		if(res[position] == context.getResources().getString(R.string.refresh))
			view.setId(6);
		if(res[position] == context.getResources().getString(R.string.exit))
			view.setId(7);
		switch(view.getId()){
		case 0:
			image.setBackgroundResource(R.drawable.compositions_icon);
			break;
		case 2:
			image.setBackgroundResource(R.drawable.lists_icon);
			break;
		case 1:
			image.setBackgroundResource(R.drawable.queue_icon);
			break;
		case 3:
			image.setBackgroundResource(R.drawable.artist_icon);
			break;
		case 4:
			image.setBackgroundResource(R.drawable.genres_icon);
			break;
		case 5:
			image.setBackgroundResource(R.drawable.album_icon);
			break;
		case 6:
			image.setBackgroundResource(R.drawable.refresh_icon);
			break;
		default:
			image.setBackgroundResource(R.drawable.exit_icon);
			break;
		}
		return view;
	}

}
