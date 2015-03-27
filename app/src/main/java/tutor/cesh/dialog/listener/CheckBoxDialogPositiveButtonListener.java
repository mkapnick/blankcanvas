package tutor.cesh.dialog.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.ArrayList;

import tutor.cesh.profile.transiant.StudentEditMode;
import tutor.cesh.profile.transiant.UserEditMode;

/**
 * Created by michaelkapnick on 3/26/15.
 */
public class CheckBoxDialogPositiveButtonListener implements DialogInterface.OnMultiChoiceClickListener,
                                                             DialogInterface.OnClickListener
{
    private Context context;
    private CharSequence []     data;
    private EditText editText;
    private ArrayList<Integer> checkedPositions;

    public CheckBoxDialogPositiveButtonListener(Context context, CharSequence [] data, EditText editText,
                                        boolean [] checkedItems)
    {
        this.context            = context;
        this.data               = data;
        this.editText           = editText;
        this.checkedPositions   = new ArrayList<Integer>();

        for(int i =0; i < checkedItems.length; i++)
        {
            if(checkedItems[i])
            {
                this.checkedPositions.add(i);
            }
        }
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
                this.checkedPositions.remove((Object)which);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        String          selectedItem, result;
        UserEditMode userEditMode;
        StudentEditMode studentEditMode;

        userEditMode    = UserEditMode.getInstance(this.context);
        studentEditMode = userEditMode.getStudentEditMode();
        result          = "";


        if(this.checkedPositions.size() > 0)
        {
            for(int i =0; i < checkedPositions.size(); i++)
            {
                selectedItem = this.data[checkedPositions.get(i)].toString();
                result       += selectedItem + ", ";
            }

            result = result.substring(0, result.lastIndexOf(","));
            studentEditMode.setMajor(result);
        }

        this.editText.setText(result);
    }
}
