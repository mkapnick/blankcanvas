package tutor.cesh.dialog.factory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import tutor.cesh.R;

/**
 * A factory class that builds, creates, and shows dialogs to the user. This class
 * builds checkboxes (not radio buttons) dialogs
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class DialogMultiChoiceFactory
{
    /**
     *
     * @param context
     * @param title
     * @param isCancelable
     * @param positiveButton
     * @param negativeButton
     * @param buttonListener
     * @param adapter
     * @param adapterListener
     */
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

    /**
     *
     * @param context
     * @param title
     * @param positiveButton
     * @param negativeButton
     * @param listItems
     * @param positiveButtonListener
     * @param negativeButtonListener
     * @param multiChoiceClickListener
     * @param checkedItems
     */
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
