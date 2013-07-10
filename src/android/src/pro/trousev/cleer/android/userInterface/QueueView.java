package pro.trousev.cleer.android.userInterface;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import pro.trousev.cleer.android.userInterface.SwipePageAdapter.SwipePage;

public class QueueView implements SwipePage{
	View _view = null;
	public QueueView() {
	}


	@Override
	public String getName() {
		return "Queue";
	}

	@Override
	public void onShow() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getView(LayoutInflater inflater, ViewGroup container) {
        View rootView = inflater.inflate(R.layout.test_fragment, container, false);
        System.out.println("[cleer] "+rootView);
        return rootView;
	}
}
