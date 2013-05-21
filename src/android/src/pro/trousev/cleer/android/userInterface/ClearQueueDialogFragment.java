package pro.trousev.cleer.android.userInterface;

import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class ClearQueueDialogFragment extends DialogFragment {
	public static ClearQueueDialogFragment newInstance() {
		ClearQueueDialogFragment frag = new ClearQueueDialogFragment();
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		return new AlertDialog.Builder(getActivity())
				.setMessage(R.string.clear_queue)
				.setPositiveButton(R.string.alert_dialog_ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								ServiceTaskMessage msg = new ServiceTaskMessage();
								msg.action = Action.clearQueue;
								Messaging.fire(msg);
							}
						})
				.setNegativeButton(R.string.alert_dialog_cancel,new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,
							int whichButton) {
						ClearQueueDialogFragment.this.getDialog().cancel();
					}
				})
				
				.create();
	}
}
