package pro.trousev.cleer.android.userInterface;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;


public class SimplePopup {
	private PopupWindow pw;
	private View _parent_view;
	private View _dialogView;
	
	private static Point getDisplaySize(final Display display) {
	    final Point point = new Point();
	    try {
	        display.getSize(point);
	    } catch (java.lang.NoSuchMethodError ignore) { // Older device
	        point.x = display.getWidth();
	        point.y = display.getHeight();
	    }
	    return point;
	}
	
	public SimplePopup(int target_layout, View parent_view)
	{
		_parent_view = parent_view;
		Context context = parent_view.getContext();
		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = getDisplaySize(display);
		size.x = (size.x * 80 )/ 100;
		size.y = (size.y * 50 )/ 100;
		System.out.println("[cleer] Size is "+size);

	    LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    _dialogView = inflater.inflate(target_layout, null, false);
	    System.out.println("[cleer] "+_dialogView.getWidth()+"x"+_dialogView.getHeight());
	    pw = new PopupWindow(
	    		_dialogView, 
	    	       size.x, 
	    	       size.y, 
	    	       true);
	    
	    // The code below assumes that the root container has an id called 'main'
		
	}
	public void show()
	{
	    pw.showAtLocation(_parent_view, Gravity.CENTER, 0, 0); 			
	}
	public void hide()
	{
		pw.dismiss();
	}
	public View getView()
	{
		return _dialogView;
	}
}
