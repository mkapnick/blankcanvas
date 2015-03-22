package tutor.cesh.dialog.listener;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public class DialogSingleChoiceButtonListener implements DialogInterface.OnClickListener {

    private CharSequence[]  data;
    private EditText        editText;

    public DialogSingleChoiceButtonListener(CharSequence [] data, EditText editText)
    {
        this.data       = data;
        this.editText   = editText;
    }
    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        int     selectedPosition;
        String  selectedItem;

        selectedItem        = "";
        selectedPosition    = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

        if(selectedPosition >= 0 && selectedPosition < this.data.length)
        {
            dialog.dismiss();
            selectedItem    = this.data[selectedPosition].toString();
            this.editText.setText(selectedItem);
        }
    }
}
