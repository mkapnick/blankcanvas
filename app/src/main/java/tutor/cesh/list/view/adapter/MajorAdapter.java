package tutor.cesh.list.view.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import tutor.cesh.R;

/**
 * Created by michaelkapnick on 3/21/15.
 */
public class MajorAdapter extends BaseAdapter implements AdapterView.OnItemClickListener
{
    private Context             context;
    private ArrayList<String>   data;
    private ArrayList<Integer>  selectedIds;

    public MajorAdapter(Context context, ArrayList<String> data)
    {
        this.context    = context;
        this.data       = data;
        this.selectedIds= new ArrayList<Integer>();
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
        View            view;
        TextView        textView;
        LayoutInflater  inflater;

        view        = convertView;
        inflater    = (LayoutInflater)
                      this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if(convertView == null)
            view    = inflater.inflate(R.layout.array_adapter_view_major, parent, false);

        textView    = (TextView) view.findViewById(R.id.edit_student_tutor_major);

        if(this.selectedIds.contains(position))
            textView.setBackgroundColor(this.context.getResources().getColor(R.color.bootstrap_primary));
        else
            textView.setBackgroundColor(this.context.getResources().getColor(android.R.color.transparent));

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Integer pos;
        pos = new Integer(position);

        if(this.selectedIds.contains(pos))
        {
            selectedIds.remove(pos);
        }
        else
        {
            selectedIds.add(pos);
        }

        this.notifyDataSetChanged();
    }
}
