package tutor.cesh.list.view.adapter.listener;

import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;

/**
 * Created by michaelkapnick on 3/21/15.
 */
public class MajorAdapterItemListener implements MultipleOnItemClickListener,
                                                 AdapterView.OnItemClickListener
{

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // Manage selected items here
        //System.out.println("clicked" + position);
        CheckedTextView textView = (CheckedTextView) view;
        if(textView.isChecked()) {

        } else {

        }
    }
}
