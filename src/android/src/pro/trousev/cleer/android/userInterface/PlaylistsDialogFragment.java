package pro.trousev.cleer.android.userInterface;

import java.util.ArrayList;
import java.util.List;

import pro.trousev.cleer.Item;
import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.Playlist;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class PlaylistsDialogFragment extends DialogFragment {
	private ArrayList<Playlist> playlists = new ArrayList<Playlist>();
	private Item item;
	private String[] strings = null;
	public static PlaylistsDialogFragment newInstance(List<Playlist> list, Item item) {
        PlaylistsDialogFragment frag = new PlaylistsDialogFragment();
        if(list != null)
        	frag.playlists.addAll(list);
        frag.item = item;
        frag.strings = new String[frag.playlists.size() + 1];
        frag.strings[0] = "New playlist";
        for (Playlist p : frag.playlists){
        	frag.strings[frag.playlists.indexOf(p)] = p.title();
        }
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle(R.string.addToList)
                .setItems( strings, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0){
                        	((MainActivity)getActivity()).showNewPlaylistDialog(item);
                        }else{
                        	ServiceTaskMessage msg = new ServiceTaskMessage();
                        	msg.action = Action.addToPlaylist;
                        	msg.list = new ArrayList<Item>();
                        	msg.list.add(item);
                        	msg.playlist = playlists.get(which);
                        	Messaging.fire(msg);
                        }
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						PlaylistsDialogFragment.this.getDialog().cancel();
					}
				})
                .create();
    }
}
