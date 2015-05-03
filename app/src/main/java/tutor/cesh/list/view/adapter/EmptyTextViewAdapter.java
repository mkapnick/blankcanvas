package tutor.cesh.list.view.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import tutor.cesh.R;

/**
 * Created by michaelkapnick on 5/3/15.
 */
public class EmptyTextViewAdapter extends BaseAdapter
{
    private Context context;

    public EmptyTextViewAdapter(Context context)
    {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        // Check if an existing view is being reused, otherwise inflate the view
        View            view;
        LayoutInflater  inflater;
        TextView        textView;

        view        = convertView;
        inflater    = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {
            view = inflater.inflate(R.layout.row_empty_text_view, parent, false);
        }

        view.setEnabled(false);
        view.setOnClickListener(null);

        return view;
    }
}