package tutor.cesh.profile.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import tutor.cesh.profile.fragment.observer.TabObserver;

/**
 *
 */
public class TabAdapter extends PagerAdapter
{
    private Activity activity;
    private Context context;
    private List<TabObserver> tabs;
    private int position;

    /**
     *
     * @param activity
     * @param context
     * @param tabs
     */
    public TabAdapter(Activity activity, Context context, List<TabObserver> tabs)
    {
        this.activity       = activity;
        this.context        = context;
        this.tabs           = tabs;
        this.position       = 0;
    }
    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return this.tabs.size();
    }

    /**
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    /**
     *
     */
    @Override
    public CharSequence getPageTitle(int position)
    {
        return this.tabs.get(position).getTitle();
    }

    /**
     *
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position)
    {
        // Inflate a new layout from our resources
        View view = this.activity.getLayoutInflater().inflate(tabs.get(position).getLayout(),
                                                                container, false);
        container.addView(view);

        // Add the newly created View to the ViewPager
        tabs.get(position).initializeUI(view);              //initialize the view
        tabs.get(position).downloadCoverImageFromServer();  //download the tab cover
                                                            //image
        tabs.get(position).setUpUserInfo();                 //apply the data to the view

        // Return the View
        return view;
    }

    /**
     *
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     *
     * @return
     */
    public int getPosition(){
        return this.position;
    }

    /**
     *
     * @return
     */
    public List<TabObserver> getTabs(){
        return this.tabs;
    }
}