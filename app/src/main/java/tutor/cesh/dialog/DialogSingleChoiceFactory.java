package tutor.cesh.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ListView;

/**
 * Created by michaelkapnick on 3/21/15.
 */
public class DialogSingleChoiceFactory
{

    public static void createAndShowDialog(Context context, String title, String positiveButton,
                                           String negativeButton, CharSequence [] items,
                                           DialogInterface.OnClickListener buttonListener,
                                           int positionSetChecked)
    {
        AlertDialog         dialog;
        AlertDialog.Builder builder;
        ListView            listView;

        builder = new AlertDialog.Builder(context);
        builder.setTitle(title);

        builder.setPositiveButton(positiveButton, buttonListener);
        builder.setNegativeButton(negativeButton, buttonListener);

        builder.setSingleChoiceItems(items, positionSetChecked, null);

        dialog      = builder.create();

        dialog.show();
    }
}
