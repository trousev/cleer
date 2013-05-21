package pro.trousev.cleer.android.userInterface;

import pro.trousev.cleer.Messaging;
import pro.trousev.cleer.android.AndroidMessages.Action;
import pro.trousev.cleer.android.AndroidMessages.ServiceTaskMessage;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class NeedScanDialogFragment extends DialogFragment {
	public static NeedScanDialogFragment newInstance(int title) {
		NeedScanDialogFragment frag = new NeedScanDialogFragment();
		Bundle args = new Bundle();
		args.putInt("title", title);
		frag.setArguments(args);
		return frag;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		int title = getArguments().getInt("title");

		return new AlertDialog.Builder(getActivity())
				.setTitle(title)
				.setMessage(R.string.need_scan)
				.setPositiveButton(R.string.scan,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								ServiceTaskMessage mes = new ServiceTaskMessage();
								mes.action = Action.scanSystem;
								Messaging.fire(mes);
							}
						})
				.setNegativeButton(R.string.alert_dialog_cancel,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								NeedScanDialogFragment.this.getDialog()
										.cancel();
							}
						})

				.create();
	}
}
