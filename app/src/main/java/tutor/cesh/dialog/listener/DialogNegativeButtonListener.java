package tutor.cesh.dialog.listener;

import android.content.DialogInterface;

/**
 * Created by michaelkapnick on 3/27/15.
 */
public class DialogNegativeButtonListener implements DialogInterface.OnClickListener
{
    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        dialog.cancel();
    }
}
