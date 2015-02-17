package tutor.cesh.list;

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

/**
 * Created by michaelk18 on 8/10/14.
 */
public class JSONAdapter extends BaseAdapter implements Filterable
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


    public JSONAdapter(Context context, Resources resources, ArrayList<HashMap<String, String>> data,
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

    @Override
    public int getCount() {
        return data.size();
    }

    public void updateDBFromListView()
    {
        View v;

        for(int i =0; i < this.listView.getCount(); i++)
        {
            v = this.listView.getChildAt(i);
        }
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
        rating          = (TextView)view.findViewById(R.id.rating);
        coverImageView  = (ImageView)view.findViewById(R.id.coverImage);

        tutorInstance   = data.get(position);

        // Setting values for this row in list view
        name.setText(tutorInstance.get(TutorListActivity.FIRST_NAME));
        rate.setText(tutorInstance.get(TutorListActivity.RATE));
        rating.setText(tutorInstance.get(TutorListActivity.RATING));

        //get the bitmap for this row
        drawable    = new BitmapDrawable(resources,
                          cachedBitmaps.get(tutorInstance.get(TutorListActivity.COVER_IMAGE)));

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
                tmp = (ArrayList<HashMap<String, String>>)results.values;

                for(int i =0; i < tmp.size(); i++)
                    data.add(tmp.get(i));

                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint)
            {
                FilterResults                       results;
                ArrayList<HashMap<String, String>>  filteredData;
                String                              filterString;
                String                              currentName;
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
                        currentName = originalDataReference.get(i).get("firstName");
                        if(currentName.toLowerCase().contains(filterString))
                            filteredData.add(originalDataReference.get(i));
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
     * @param cachedBitmaps
     */
    public void setCachedMap(HashMap<String, Bitmap> cachedBitmaps)
    {
        this.cachedBitmaps = cachedBitmaps;
    }
}
