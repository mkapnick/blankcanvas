package tutor.cesh.dialog.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;

import tutor.cesh.R;

/**
 * A factory class that builds, creates, and shows dialogs to the user. This class
 * builds radiobuttons (not checkboxes) dialogs
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class DialogSingleChoiceFactory
{
    /**
     *
     * @param context
     * @param title
     * @param positiveButton
     * @param negativeButton
     * @param items
     * @param positiveButtonListener
     * @param negativeButtonListener
     * @param positionSetChecked
     */
    public static void createAndShowDialog(Context context, String title, String positiveButton,
                                           String negativeButton, CharSequence [] items,
                                           DialogInterface.OnClickListener positiveButtonListener,
                                           DialogInterface.OnClickListener negativeButtonListener,
                                           int positionSetChecked)
    {
        AlertDialog         dialog;
        AlertDialog.Builder builder;
        ListView            listView;

        builder = new AlertDialog.Builder(context, R.style.MyDialogStyle);
        builder.setTitle(title);

        builder.setPositiveButton(positiveButton, positiveButtonListener);
        builder.setNegativeButton(negativeButton, negativeButtonListener);

        builder.setSingleChoiceItems(items, positionSetChecked, null);

        dialog      = builder.create();

        dialog.show();
    }
}
