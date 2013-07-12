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
import android.view.Menu;
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
		public Menu updateMenu(Menu m){return m;}
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
	Menu _menu;
	View getPager()
	{
		return _pager;
	}
	void setMenu(Menu m)
	{
		_menu = m;
		updateMenu();
	}
	void changePageEvent(int arg0)
	{
		System.out.println("[cleer] onPageChanged"+arg0);
		_pages.get(arg0).onShow();
		if(_menu != null)
			updateMenu();
	}
	public void updateMenu()
	{
		_menu.clear();
		_pages.get(_pager.getCurrentItem()).updateMenu(_menu);
		MainActivity.singletone.getMenuInflater().inflate(R.menu.main_option_menu, _menu);
	}
	void setPager(ViewPager v)
	{
		_pager = v;
		_pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				changePageEvent(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		changePageEvent(_pager.getCurrentItem());
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
