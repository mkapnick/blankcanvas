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

/**
 * Created by michaelk18 on 8/10/14.
 */
public class TutorListAdapter extends BaseAdapter implements Filterable
{
    private Activity                            activity;
    private ArrayList                           list;
    private HashMap                             map;
    private Context                             context;
    private ArrayList<HashMap<String, String>>  data;
    private ArrayList<HashMap<String, String>>  originalDataReference;
    private ArrayList<Drawable>                 cachedCoverImages;
    private static LayoutInflater               inflater=null;
    private Resources                           resources;
    private ListView                            listView;
    private HashMap<String, Bitmap>             cachedBitmaps;


    public TutorListAdapter(Context context, Resources resources, ArrayList<HashMap<String, String>> data,
                            ListView listView)
    {
        this.originalDataReference  = new ArrayList<HashMap<String, String>>(data);
        this.listView               = listView;
        this.data                   = new ArrayList<HashMap<String, String>>();

        this.cachedCoverImages      = new ArrayList<Drawable>();
        this.context                = context;
        this.resources              = resources;
        this.inflater               = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *
     * @param majors
     * @param rates
     * @param years
     */
    public void applySpecificFilters(String majors, String rates, String years)
    {
        ArrayList<HashMap<String, String>>  filteredData;
        String                              currentMajor, currentPrice,
                currentYear;
        String []                           majorsArray, ratesArray, yearsArray;

        filteredData = new ArrayList<HashMap<String, String>>();
        majorsArray  = null;
        ratesArray   = null;
        yearsArray   = null;

        if(null != majors)
            majorsArray  = majors.split(",");

        if(null != rates)
            ratesArray   = rates.split(",");

        if(null != years)
            yearsArray   = years.split(",");

        for(int i = 0; i < originalDataReference.size(); i++)
        {
            currentMajor        = originalDataReference.get(i).get("major");
            currentPrice        = originalDataReference.get(i).get("rate");
            currentYear         = originalDataReference.get(i).get("year");

            if(null != majorsArray)
            {
                for(String m: majorsArray)
                {
                    if(currentMajor.trim().equalsIgnoreCase(m))
                        filteredData.add(originalDataReference.get(i));
                }
            }

            if(null != ratesArray)
            {
                for(String r: ratesArray)
                {
                    if(currentPrice.trim().equalsIgnoreCase(r))
                        filteredData.add(originalDataReference.get(i));
                }
            }

            if(null != yearsArray)
            {
                for(String y: yearsArray)
                {
                    if(currentYear.trim().equalsIgnoreCase(y))
                        filteredData.add(originalDataReference.get(i));
                }
            }

        }

        this.data = new ArrayList<HashMap<String, String>>(); //update the data!
        for(int i =0; i < filteredData.size(); i++)
            data.add(filteredData.get(i));

        notifyDataSetChanged();
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
        TextView            name, rate, rating;
        ImageView           coverImageView;
        Map<String, String> tutorInstance;
        BitmapDrawable      drawable;

        view            = convertView;
        map             = new HashMap<String, String>();

        if(convertView == null)
            view = inflater.inflate(R.layout.row_tutor_list_view, parent, false);

        name            = (TextView)view.findViewById(R.id.name);
        rate            = (TextView)view.findViewById(R.id.rate);
        //rating          = (TextView)view.findViewById(R.id.rating);
        coverImageView  = (ImageView)view.findViewById(R.id.coverImage);

        tutorInstance   = data.get(position);

        // Setting values for this row in list view
        name.setText(tutorInstance.get(TutorListActivity.FIRST_NAME));
        rate.setText(tutorInstance.get(TutorListActivity.RATE));
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
        // TODO Auto-generated method stub
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
                View                                view;


                results             = new FilterResults();

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
            data.add(originalDataReference.get(i));

        notifyDataSetChanged();
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
