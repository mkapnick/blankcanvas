package tutor.cesh.dialog.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by michaelkapnick on 3/21/15.
 */
public class CheckBoxDialogButtonListener implements DialogInterface.OnMultiChoiceClickListener,
                                                     DialogInterface.OnClickListener
{
    private Context             context;
    private CharSequence []     data;
    private EditText            editText;
    private ArrayList<Integer>  checkedPositions;

    public CheckBoxDialogButtonListener(Context context, CharSequence [] data, EditText editText)
    {
        this.context            = context;
        this.data               = data;
        this.editText           = editText;
        this.checkedPositions   = new ArrayList<Integer>();
    }

    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked)
    {
        if(isChecked)
        {
            if(!this.checkedPositions.contains(which))
                this.checkedPositions.add(which);
        }
        else
        {
            if(this.checkedPositions.contains(which))
                this.checkedPositions.remove(which);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        String selectedItem, result;

        result = "";
        if(this.checkedPositions.size() > 0)
        {
            for(int i =0; i < checkedPositions.size(); i++)
            {
                selectedItem = this.data[checkedPositions.get(i)].toString();
                result       += selectedItem + ", ";
            }

            result = result.substring(0, result.lastIndexOf(","));
            this.editText.setText(result);
        }
    }
}
