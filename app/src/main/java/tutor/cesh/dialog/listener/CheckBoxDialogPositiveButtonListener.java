package tutor.cesh.dialog.listener;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;
import java.util.ArrayList;
import tutor.cesh.profile.ProfileInfo;
import tutor.cesh.profile.ProfileInfoBehavior;

/**
 * Created by michaelkapnick on 3/26/15.
 */
public class CheckBoxDialogPositiveButtonListener implements DialogInterface.OnMultiChoiceClickListener,
                                                             DialogInterface.OnClickListener
{
    private Context             context;
    private CharSequence []     data;
    private EditText            editText;
    private ArrayList<Integer>  checkedPositions;
    private ProfileInfo         profileInfo;
    private ProfileInfoBehavior behavior;


    public CheckBoxDialogPositiveButtonListener(Context context, CharSequence [] data, EditText editText,
                                                boolean [] checkedItems, ProfileInfo profileInfo,
                                                ProfileInfoBehavior behavior)
    {
        this.context            = context;
        this.data               = data;
        this.editText           = editText;
        this.checkedPositions   = new ArrayList<Integer>();
        this.profileInfo        = profileInfo;
        this.behavior           = behavior;

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
        result          = "";

        if(this.checkedPositions.size() > 0)
        {
            for(int i =0; i < checkedPositions.size(); i++)
            {
                selectedItem = this.data[checkedPositions.get(i)].toString();
                result       += selectedItem + ", ";
            }

            result = result.substring(0, result.lastIndexOf(","));
        }

        setResult(result);
    }

    /**
     *
     * @param selectedItem
     */
    private void setResult(String selectedItem)
    {
        switch (this.profileInfo)
        {
            case MAJOR:
                ProfileInfo.MAJOR.setResult(selectedItem, this.editText);
                this.behavior.setMajor(selectedItem);
                break;
            case MINOR:
                ProfileInfo.MINOR.setResult(selectedItem, this.editText);
                this.behavior.setMinor(selectedItem);
                break;
            case YEAR:
                ProfileInfo.YEAR.setResult(selectedItem, this.editText);
                this.behavior.setYear(selectedItem);
                break;
            case RATE:
                ProfileInfo.RATE.setResult(selectedItem, this.editText);
                this.behavior.setRate(selectedItem);
                break;
        }
    }
}
