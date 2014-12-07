package tutor.cesh.profile.fragment;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * The {@link android.support.v4.view.PagerAdapter} used to display pages in this sample.
 * The individual pages are simple and just display two lines of text. The important section of
 * this class is the {@link #getPageTitle(int)} method which controls what is displayed in the
 * {SlidingTabLayout}.
 */
public class SamplePagerAdapter extends PagerAdapter
{

    private Activity activity;
    private Context context;
    private List<FragmentTabBehavior> tabs;
    private int position;

    public SamplePagerAdapter(Activity activity, Context context, List<FragmentTabBehavior> tabs)
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
     * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
     * same object as the {@link View} added to the {ViewPager}.
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    // BEGIN_INCLUDE (pageradapter_getpagetitle)
    /**
     * Return the title of the item at {@code position}. This is important as what this method
     * returns is what is displayed in the {SlidingTabLayout}.
     * <p>
     * Here we construct one using the position value, but for real application the title should
     * refer to the item's contents.
     */
    @Override
    public CharSequence getPageTitle(int position) {

        return this.tabs.get(position).getTitle();
    }
    // END_INCLUDE (pageradapter_getpagetitle)

    /**
     * Instantiate the {@link View} which should be displayed at {@code position}. Here we
     * inflate a layout from the apps resources and then change the text view to signify the position.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
        View view = this.activity.getLayoutInflater().inflate(tabs.get(position).getLayout(),
                container, false);
        container.addView(view);

        // Add the newly created View to the ViewPager
        tabs.get(position).initializeUI(view); //initialize the view
        tabs.get(position).downloadCoverImageFromServer(); //download the tab cover
                                                            //image
        tabs.get(position).downloadDataFromServer(); //download the tab data
        tabs.get(position).setUpUserInfo(); //apply the data to the view

        // Return the View
        return view;
    }


    /**
     `* Destroy the item from the {ViewPager}. In our case this is simply removing the
     * {@link View}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        //Log.i(LOG_TAG, "destroyItem() [position: " + position + "]");
    }

    public int getPosition(){
        return this.position;
    }

    public List<FragmentTabBehavior> getTabs(){
        return this.tabs;
    }
}