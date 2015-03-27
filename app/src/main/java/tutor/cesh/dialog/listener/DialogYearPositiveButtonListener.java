package tutor.cesh.dialog.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import tutor.cesh.profile.transiant.StudentEditMode;
import tutor.cesh.profile.transiant.TutorEditMode;
import tutor.cesh.profile.transiant.UserEditMode;

/**
 * Created by michaelkapnick on 3/27/15.
 */
public class DialogYearPositiveButtonListener extends DialogPositiveButtonListener {

    public DialogYearPositiveButtonListener(Context c, CharSequence[] data, EditText editText) {
        super(c, data, editText);
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        int             selectedPosition;
        String          selectedItem;
        UserEditMode    userEditMode;
        StudentEditMode studentEditMode;

        userEditMode    = UserEditMode.getInstance(super.context);
        studentEditMode = userEditMode.getStudentEditMode();

        selectedItem        = "";
        selectedPosition    = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

        if(selectedPosition >= 0 && selectedPosition < super.data.length)
        {
            dialog.dismiss();
            selectedItem    = super.data[selectedPosition].toString();
        }

        super.editText.setText(selectedItem);
        studentEditMode.setYear(selectedItem);
    }
}
