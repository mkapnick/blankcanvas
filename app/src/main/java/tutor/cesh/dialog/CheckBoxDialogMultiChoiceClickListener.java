package tutor.cesh.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by michaelkapnick on 3/21/15.
 */
public class CheckBoxDialogMultiChoiceClickListener implements DialogInterface.OnMultiChoiceClickListener
{
    private Context context;

    public CheckBoxDialogMultiChoiceClickListener(Context context)
    {
        this.context = context;
    }


    @Override
    public void onClick(DialogInterface dialog, int which, boolean isChecked)
    {
        System.out.println("here");
        switch (which)
        {
            case 0:
                Toast.makeText(this.context, "CASE 0 was clicked! in multi", Toast.LENGTH_SHORT);
                break;

            case 1:
                Toast.makeText(this.context, "CASE 1 was clicked! in multi", Toast.LENGTH_SHORT);
                break;
        }

        Toast.makeText(this.context, "we got inside onClick!! in multi", Toast.LENGTH_SHORT);
    }
}
