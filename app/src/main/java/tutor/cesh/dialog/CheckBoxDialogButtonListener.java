package tutor.cesh.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by michaelkapnick on 3/21/15.
 */
public class CheckBoxDialogButtonListener implements DialogInterface.OnClickListener
{
    private Context context;

    public CheckBoxDialogButtonListener(Context context)
    {
        this.context = context;
    }

    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        switch (which)
        {
            case 0:
                Toast.makeText(this.context, "CASE 0 was clicked!", Toast.LENGTH_SHORT);
                break;

            case 1:
                Toast.makeText(this.context, "CASE 1 was clicked!", Toast.LENGTH_SHORT);
                break;
        }

        Toast.makeText(this.context, "we got inside onClick!!", Toast.LENGTH_SHORT);
    }
}
