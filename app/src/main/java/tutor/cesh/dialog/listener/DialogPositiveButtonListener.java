package tutor.cesh.dialog.listener;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

import tutor.cesh.profile.transiant.StudentEditMode;
import tutor.cesh.profile.transiant.TutorEditMode;
import tutor.cesh.profile.transiant.UserEditMode;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public abstract class DialogPositiveButtonListener implements DialogInterface.OnClickListener {

    protected CharSequence[]    data;
    protected EditText          editText;
    protected Context           context;

    public DialogPositiveButtonListener(Context c, CharSequence[] data, EditText editText)
    {
        this.context    = c;
        this.data       = data;
        this.editText   = editText;
    }

}
