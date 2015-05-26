package tutor.cesh.dialog.listener;

import android.content.DialogInterface;

/**
 * Negative button listener for alert dialogs
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class DialogNegativeButtonListener implements DialogInterface.OnClickListener
{
    @Override
    public void onClick(DialogInterface dialog, int which)
    {
        dialog.cancel();
    }
}
