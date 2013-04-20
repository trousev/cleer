package pro.trousev.cleer.userInterface;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import pro.trousev.cleer.CoreItems;
import pro.trousev.cleer.Plugin;
import pro.trousev.cleer.Console;
import pro.trousev.cleer.ConsoleOutput;
import pro.trousev.cleer.Database;
import pro.trousev.cleer.Library;
import pro.trousev.cleer.Player;
import pro.trousev.cleer.Queue;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import pro.trousev.cleer.userInterface.R;

public class MainActivity extends FragmentActivity implements OnClickListener{
	private FragmentTransaction fTrans;
	private Fragment playBar, mainMenu;
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        final PlayBar pb = new PlayBar(this);
        final MainMenu mm = new MainMenu(this);
        playBar=pb;
        mainMenu=mm;
        fTrans=getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.play_bar, playBar).commit();
        fTrans=getSupportFragmentManager().beginTransaction();
        fTrans.add(R.id.work_space, mainMenu).commit();
        // TODO set implementations here
        final Database db = null;
		final Library lib = null;
	    final Player player = null;
	    final Queue queue = null;
	    CoreItems.setCoreItems(new Plugin.Interface() {
			
			@Override
			public Database storage() {
				return db;
			}
			
			@Override
			public Library library() {
				return lib;
			}

			@Override
			public Console console() {
				return null;
			}

			@Override
			public Player player() {
				return player;
			}

			@Override
			public Queue queue() {
				return queue;
			}

			@Override
			public ConsoleOutput output(){
				return null;
			}
	    });
    }
	public void onClick(View view){
		int id=view.getId();
		switch(id){
		case R.id.queue_btn:
			ListOfCompositions listOfCompositions = new ListOfCompositions();
			getSupportFragmentManager().beginTransaction().replace(R.id.work_space, listOfCompositions).commit();
			break;
		case R.id.main_menu_btn:
			getSupportFragmentManager().beginTransaction().replace(R.id.work_space, mainMenu).commit();
			break;
		default:
			break;
		}
	}

	public void onExit(View sender) {
		System.exit(0);
	}
}
