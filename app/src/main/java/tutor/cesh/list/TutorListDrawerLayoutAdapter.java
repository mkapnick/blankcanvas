package tutor.cesh.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import tutor.cesh.R;

/**
 * Created by michaelkapnick on 3/14/15.
 */
public class TutorListDrawerLayoutAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;

    public TutorListDrawerLayoutAdapter(Context context)
    {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount()
    {
        return 1;
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View view;

        if (convertView == null)
        {
            view = mInflater.inflate(R.layout.drawerlayout_tutor_list_activity, parent, false);
        }
        else
        {
            view = convertView;
        }

        return view;
    }
}
