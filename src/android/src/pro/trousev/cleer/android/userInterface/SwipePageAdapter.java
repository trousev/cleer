package pro.trousev.cleer.android.userInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SwipePageAdapter extends FragmentPagerAdapter {
	
	public interface SwipePage
	{
		public View getView(LayoutInflater inflater, ViewGroup container);
		public String getName();
		public void onShow();
	};
	private class SwipeFragment extends Fragment
	{
		public static final String SECTION_NUMBER = "section_number";
		public SwipeFragment() {}
		@Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
				int no = getArguments().getInt(SECTION_NUMBER);
				return _pages.get(no).getView(inflater, container);
        }
	};	
	
	List<SwipePage> _pages = new ArrayList<SwipePageAdapter.SwipePage>();
	void addPage(SwipePage np)
	{
		_pages.add(np);
	}

	public SwipePageAdapter(android.support.v4.app.FragmentManager fm)
	{
		super(fm);
	}

	@Override
	public Fragment getItem(int position) 
	{
        Fragment fragment = new SwipeFragment();
        Bundle args = new Bundle();
        args.putInt(SwipeFragment.SECTION_NUMBER, position);
        fragment.setArguments(args);
        return fragment;
	}

	@Override
	public int getCount() {
		return _pages.size();
	}
	
    @Override
    public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            return _pages.get(position).getName();
    }

}
