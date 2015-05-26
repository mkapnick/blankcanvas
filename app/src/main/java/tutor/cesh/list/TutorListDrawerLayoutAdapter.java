package tutor.cesh.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import tutor.cesh.R;
import tutor.cesh.list.view.adapter.TutorListViewItem;

/**
 * Adapter for the drawer layout found in the tutor list activity
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class TutorListDrawerLayoutAdapter extends BaseAdapter
{
    private LayoutInflater                  mInflater;
    private ArrayList<TutorListViewItem>    tutorListViewItems;

    public TutorListDrawerLayoutAdapter(Context context, ArrayList<TutorListViewItem> items)
    {
        mInflater               = LayoutInflater.from(context);
        this.tutorListViewItems = items;
    }

    @Override
    public int getCount()
    {
        return this.tutorListViewItems.size();
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
        View                view;
        ImageView           imageView;
        TextView            textView;
        TutorListViewItem   item;

        view = convertView;
        if (convertView == null)
        {
            view = mInflater.inflate(R.layout.drawerlayout_tutor_list_view, parent, false);
        }

        imageView   = (ImageView) view.findViewById(R.id.icon);
        textView    = (TextView)  view.findViewById(R.id.text);
        item        = this.tutorListViewItems.get(position);

        textView.setText(item.getText());

        if(item.getDrawable() != null)
            imageView.setBackground(item.getDrawable());

        return view;
    }
}
