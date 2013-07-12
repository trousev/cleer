package pro.trousev.cleer.android.userInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SwipePageAdapter extends FragmentPagerAdapter {
	
	public static abstract class SwipePage
	{
		SwipePageAdapter _parent;
		SwipePage(SwipePageAdapter parent)
		{
			_parent = parent;
		}
		public View getView(LayoutInflater inflater, ViewGroup container){return null;}
		public String getName(){ return "";}
		public void onShow(){}
		public View getParentView() { return _parent.getPager(); };
		public SwipePage sibling(Class type)
		{
			for(SwipePage page: _parent._pages)
			{
				if(page.getClass().equals(type))
					return page;
			}
			return null;
		}
		public void swipe(SwipePage page)
		{
			for(int i=0; i<_parent._pages.size(); i++)
			{
				if(_parent._pages.get(i).equals(page))
				{
					_parent._pager.setCurrentItem(i, true);
					return ;
				}
			}
		}
		public void onDestroy() {}
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
	ViewPager _pager = null;
	View getPager()
	{
		return _pager;
	}
	void setPager(ViewPager v)
	{
		_pager = v;
	}
			
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
    
    public void onDestroy()
    {
    	for(SwipePage p: _pages)
    		p.onDestroy();
    }

}
