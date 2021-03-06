package tutor.cesh.list.view.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import tutor.cesh.R;
import tutor.cesh.list.TutorListActivity;
import tutor.cesh.profile.ProfileInfoBehavior;

/**
 * A custom adapter for the TutorListActivity
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class TutorListAdapter extends BaseAdapter implements Filterable
{
    private Activity                            activity;
    private ArrayList                           list;
    private HashMap<String, String> map;
    private Context                             context;
    private ArrayList<HashMap<String, String>>  data;
    private ArrayList<HashMap<String, String>>  originalDataReference;
    private ArrayList<Drawable>                 cachedCoverImages;
    private static LayoutInflater               inflater=null;
    private Resources                           resources;
    private ListView                            listView;
    private HashMap<String, Bitmap>             cachedBitmaps;


    /**
     *
     * @param context
     * @param resources
     * @param data
     * @param listView
     */
    public TutorListAdapter(Context context, Resources resources, ArrayList<HashMap<String, String>> data,
                            ListView listView)
    {
        this.originalDataReference  = new ArrayList<HashMap<String, String>>(data);
        this.listView               = listView;
        this.data                   = new ArrayList<HashMap<String, String>>();
        this.cachedCoverImages      = new ArrayList<Drawable>();
        this.context                = context;
        this.resources              = resources;
        inflater                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *
     *
     */
    public String applyAdvancedFiltersAndGetToastText()
    {
        String      currentMajor, currentPrice, currentMinor, majors, rates,
                    minors, toastText;
        String []   majorsArray, ratesArray, minorsArray;

        majors  = ProfileInfoBehavior.getFilterableMajor();
        rates   = ProfileInfoBehavior.getFilterableRate();
        minors  = ProfileInfoBehavior.getFilterableMinor();

        //if all filters = 0, then we know we're resetting the filters
        if(majors.length() == 0 && minors.length()== 0 && rates.length() == 0)
        {
            resetFilters();
            toastText = "Reset";
        }
        else
        {
            this.data    = new ArrayList<HashMap<String, String>>(); //update the data!

            majorsArray  = null;
            ratesArray   = null;
            minorsArray  = null;

            if(null != majors)
                majorsArray  = majors.split(",");

            if(null != rates)
                ratesArray   = rates.split(",");

            if(null != minors)
                minorsArray  = minors.split(",");

            for(int i = 0; i < originalDataReference.size(); i++)
            {
                currentMajor    = originalDataReference.get(i).get("major");
                currentPrice    = originalDataReference.get(i).get("rate");
                currentMinor    = originalDataReference.get(i).get("minor");

                if(null != majorsArray)
                {
                    for(String m: majorsArray)
                    {
                        if(currentMajor.trim().equalsIgnoreCase(m.trim()))
                            this.data.add(originalDataReference.get(i));
                    }
                }

                if(null != ratesArray)
                {
                    for(String r: ratesArray)
                    {
                        if(currentPrice.trim().equalsIgnoreCase(r.trim()))
                        {
                            if(!this.data.contains(originalDataReference.get(i)))
                            {
                                this.data.add(originalDataReference.get(i));
                            }
                        }
                    }
                }

                if(null != minorsArray)
                {
                    for(String mi: minorsArray)
                    {
                        if(!currentMinor.trim().equalsIgnoreCase("") && currentMinor.trim().equalsIgnoreCase(mi))
                        {
                            if(!this.data.contains(originalDataReference.get(i)))
                            {
                                this.data.add(originalDataReference.get(i));
                            }
                        }
                    }
                }
            }

            toastText = "Applied";
        }

        notifyDataSetChanged();

        return toastText;
    }

    @Override
    public int getCount()
    {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View                view;
        TextView            name, rate, rating, tutorCourses;
        ImageView           coverImageView;
        Map<String, String> tutorInstance;
        BitmapDrawable      drawable;

        view            = convertView;
        map             = new HashMap<String, String>();

        if(convertView == null)
            view = inflater.inflate(R.layout.row_tutor_list_view, parent, false);

        name            = (TextView)view.findViewById(R.id.name);
        rate            = (TextView)view.findViewById(R.id.rate);
        tutorCourses    = (TextView)view.findViewById(R.id.tutorCoursesId);
        //rating          = (TextView)view.findViewById(R.id.rating);
        coverImageView  = (ImageView)view.findViewById(R.id.coverImage);

        tutorInstance   = data.get(position);

        // Setting values for this row in list view
        name.setText(tutorInstance.get(TutorListActivity.FIRST_NAME));
        rate.setText(tutorInstance.get(TutorListActivity.RATE));
        tutorCourses.setText(tutorInstance.get(TutorListActivity.TUTOR_COURSES));
        //rating.setText(tutorInstance.get(TutorListActivity.RATING));

        //get the bitmap for this row
        drawable    = new BitmapDrawable(resources,
                          cachedBitmaps.get(tutorInstance.get(TutorListActivity.ID)));

        //set the bitmap for this row
        coverImageView.setBackground(drawable);

        return view;
    }

    /**
     *
     * @return
     */
    public ArrayList<HashMap<String, String>> getData()
    {
        return this.data;
    }


    /** Filtering needed in the list view **/
    @Override
    public Filter getFilter()
    {
        return new Filter()
        {
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results)
            {
                ArrayList<HashMap<String, String>> tmp;

                data = new ArrayList<HashMap<String, String>>();
                tmp  = (ArrayList<HashMap<String, String>>)results.values;

                for(int i =0; i < tmp.size(); i++)
                    data.add(tmp.get(i));

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults                       results;
                ArrayList<HashMap<String, String>>  filteredData;
                String                              filterString, currentName, currentAbout,
                                                    currentTutorCourses, currentMajor, currentPrice,
                                                    currentYear;

                results = new FilterResults();

                // if constraint is empty return the original names
                if(constraint.length() == 0 || null == constraint ){
                    results.values  = originalDataReference;
                    results.count   = originalDataReference.size();
                }
                else
                {
                    filteredData = new ArrayList<HashMap<String, String>>();
                    filterString = constraint.toString().toLowerCase();

                    for(int i = 0; i < originalDataReference.size(); i++)
                    {
                        currentName         = originalDataReference.get(i).get("firstName");
                        currentMajor        = originalDataReference.get(i).get("major");
                        currentTutorCourses = originalDataReference.get(i).get("tutorCourses");
                        currentAbout        = originalDataReference.get(i).get("about");
                        currentPrice        = originalDataReference.get(i).get("rate");
                        currentYear         = originalDataReference.get(i).get("year");

                        if(currentName.toLowerCase().contains(filterString)         ||
                           currentMajor.toLowerCase().contains(filterString)        ||
                           currentTutorCourses.toLowerCase().contains(filterString) ||
                           currentAbout.toLowerCase().contains(filterString)        ||
                           currentPrice.toLowerCase().contains(filterString)        ||
                           currentYear.toLowerCase().contains(filterString))
                        {
                            filteredData.add(originalDataReference.get(i));
                        }
                    }

                    results.values =    filteredData;
                    results.count =     filteredData.size();
                }

                return results;
            }
        };
    }

    /**
     *
     */
    public void resetFilters()
    {
        this.data = new ArrayList<HashMap<String, String>>(); //update the data!
        for(int i =0; i < this.originalDataReference.size(); i++)
        {
            data.add(originalDataReference.get(i));
        }
    }
    /**
     *
     * @param cachedBitmaps
     */
    public void setCachedMap(HashMap<String, Bitmap> cachedBitmaps)
    {
        this.cachedBitmaps = cachedBitmaps;
    }
}
