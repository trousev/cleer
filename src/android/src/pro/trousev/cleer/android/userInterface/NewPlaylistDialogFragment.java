package pro.trousev.cleer.android.userInterface;

import java.util.ArrayList;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewPlaylistDialogFragment extends DialogFragment {
	private Item item;
	private View view;

	public static NewPlaylistDialogFragment newInstance(Item item) {
		NewPlaylistDialogFragment frag = new NewPlaylistDialogFragment();
		frag.item = item;
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view = inflater.inflate(R.layout.new_playlist_dialog_view, null);
		builder.setView(view)
				.setPositiveButton(R.string.alert_dialog_ok,
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								EditText tv = (EditText) NewPlaylistDialogFragment.this.view
										.findViewById(R.id.new_playlist_name);
								String string = tv.getText().toString();
								if (string.isEmpty()) {
									Toast.makeText(getActivity(),
											"Invalid playlists name",
											Toast.LENGTH_LONG).show();
								} else {
									ServiceTaskMessage msg = new ServiceTaskMessage();
									msg.action = Action.addToPlaylist;
									msg.list = new ArrayList<Item>();
									msg.list.add(item);
									msg.searchQuery = string;
									Messaging.fire(msg);
								}

							}
						})
				.setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								NewPlaylistDialogFragment.this.getDialog()
										.cancel();
							}
						});
		return builder.create();
	}
}
