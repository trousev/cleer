package pro.trousev.cleer.android.userInterface;

import android.view.View;
import android.widget.ProgressBar;
import pro.trousev.cleer.Library;
public class LibraryUpdatePopup extends SimplePopup {

	ProgressBar _progress;
	public LibraryUpdatePopup(int target_layout, View parent_view) {
		super(target_layout, parent_view);
		_progress = (ProgressBar) getView().findViewById(R.id.library_popup_progress);
	}
	
	public void update()
	{
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				MainActivity.service.library().folder_scan(new Library.FolderScanCallback() {
					
					@Override
					public void started() {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void progress(int current, int maximum) {
						_progress.setMax(maximum);
						_progress.setProgress(current);
					}
					
					@Override
					public void message(String message) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void finished() {
						MainActivity.singletone.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								hide();
							}
						});
					}
				});
			}
		});
		t.start();
	}

}
