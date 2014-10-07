package tutor.cesh.list;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tutor.cesh.R;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.CoverImageHandler;
import tutor.cesh.rest.ImageHandler;

/**
 * Created by michaelk18 on 8/10/14.
 */
public class JSONAdapter extends BaseAdapter
{
    private Activity    activity;
    private ArrayList   list;
    private HashMap     map;
    private Context     context;
    private ArrayList<HashMap<String, String>>  data;
    private static LayoutInflater inflater=null;
    private Resources resources;


    public JSONAdapter(Context context, Resources resources, ArrayList<HashMap<String, String>> data)
    {
        this.data       = data;
        this.context    = context;
        this.resources  = resources;
        this.inflater   = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
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
        AsyncDownloader     asyncDownloader;
        ImageHandler        handler;

        view            = convertView;
        map             = new HashMap<String, String>();

        if(convertView == null)
            view = inflater.inflate(R.layout.row_tutor_list_view, parent, false);

        name            = (TextView)view.findViewById(R.id.name);
        rate            = (TextView)view.findViewById(R.id.rate);
        rating          = (TextView)view.findViewById(R.id.rating);
        coverImageView  = (ImageView)view.findViewById(R.id.coverImage);

        handler         = new CoverImageHandler(this.resources, coverImageView,this.context);
        tutorInstance   = data.get(position);

        // Setting all values in listview
        name.setText(tutorInstance.get(TutorListActivity.FIRST_NAME));
        rate.setText(tutorInstance.get(TutorListActivity.RATE));
        rating.setText(tutorInstance.get(TutorListActivity.RATING));;

        asyncDownloader = new AsyncDownloader(tutorInstance.get(TutorListActivity.COVER_IMAGE),
                                            handler, coverImageView.getWidth(),
                                            coverImageView.getHeight(), new ProgressDialog(this.context));
        asyncDownloader.execute();


        return view;
    }

    public ArrayList<HashMap<String, String>> getData()
    {
        return this.data;
    }





}
