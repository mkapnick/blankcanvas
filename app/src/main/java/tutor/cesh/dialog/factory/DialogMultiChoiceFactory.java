package tutor.cesh.dialog.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import tutor.cesh.R;

/**
 * Created by michaelkapnick on 3/21/15.
 */
public class DialogMultiChoiceFactory
{
    public static void createAndShowDialog(Context context, String title,
                                    boolean isCancelable, String positiveButton,
                                    String negativeButton,
                                    DialogInterface.OnClickListener buttonListener,
                                    ListAdapter adapter, AdapterView.OnItemClickListener
                                    adapterListener)
    {
        AlertDialog         dialog;
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context, R.style.MyDialogStyle);

        builder.setTitle(title);
        builder.setCancelable(isCancelable);
        builder.setAdapter(adapter, null);

        //Set positive and negative buttons
        builder.setPositiveButton(positiveButton, buttonListener);
        builder.setNegativeButton(negativeButton, buttonListener);

        //set list adapter and listener
        dialog = builder.create();

        dialog.getListView().setItemsCanFocus(false);
        dialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        dialog.getListView().setOnItemClickListener(adapterListener);

        dialog.show();

    }

    public static void createAndShowDialog(Context context, String title, String positiveButton,
                                           String negativeButton, CharSequence [] listItems,
                                           DialogInterface.OnClickListener positiveButtonListener,
                                           DialogInterface.OnClickListener negativeButtonListener,
                                           DialogInterface.OnMultiChoiceClickListener
                                           multiChoiceClickListener, boolean [] checkedItems)
    {
        AlertDialog         dialog;
        AlertDialog.Builder builder;

        builder = new AlertDialog.Builder(context, R.style.MyDialogStyle);
        builder.setTitle(title);

        builder.setPositiveButton(positiveButton, positiveButtonListener);
        builder.setNegativeButton(negativeButton, negativeButtonListener);

        builder.setMultiChoiceItems(listItems, checkedItems, multiChoiceClickListener);

        dialog      = builder.create();

        dialog.show();
    }
}
