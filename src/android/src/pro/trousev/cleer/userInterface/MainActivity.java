package pro.trousev.cleer.userInterface;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import pro.trousev.cleer.userInterface.R;

public class MainActivity extends FragmentActivity{
	private FragmentTransaction fTrans;
	private Fragment playBar, mainMenu;
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        playBar = new PlayBar();
        mainMenu = new MainMenu();
        fTrans=getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.play_bar, playBar).commit();
        fTrans=getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.work_space, mainMenu).commit();
    }
    
    public void onExit(View sender)
    {
    	System.exit(0);
    }
}
