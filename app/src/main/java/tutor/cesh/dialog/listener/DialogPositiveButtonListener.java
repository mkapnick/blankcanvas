package tutor.cesh.dialog.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import tutor.cesh.dialog.ProfileInfo;
import tutor.cesh.dialog.ProfileInfoBehavior;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public class DialogPositiveButtonListener implements DialogInterface.OnClickListener {

    protected CharSequence[]        data;
    protected Context               context;
    protected EditText              editText;
    protected boolean               shouldUpdateEditMode;
    protected ProfileInfo           profileInfo;
    protected ProfileInfoBehavior   behavior;

    /**
     *
     * @param c
     * @param data
     * @param profileInfo
     */
    public DialogPositiveButtonListener(Context c, CharSequence[] data, EditText editText,
                                        ProfileInfo profileInfo,
                                        ProfileInfoBehavior behavior)
    {
        this.context                = c;
        this.data                   = data;
        this.editText               = editText;
        this.profileInfo            = profileInfo;
        this.behavior               = behavior;
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        int             selectedPosition;
        String          selectedItem;

        selectedItem        = "";
        selectedPosition    = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

        if(selectedPosition >= 0 && selectedPosition < this.data.length)
        {
            dialog.dismiss();
            selectedItem    = this.data[selectedPosition].toString();
        }

        setResult(selectedItem);
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
