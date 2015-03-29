package tutor.cesh.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.ArrayList;

import tutor.cesh.dialog.factory.DialogMultiChoiceFactory;
import tutor.cesh.dialog.factory.DialogSingleChoiceFactory;
import tutor.cesh.dialog.listener.CheckBoxDialogPositiveButtonListener;
import tutor.cesh.dialog.listener.DialogNegativeButtonListener;
import tutor.cesh.dialog.listener.DialogPositiveButtonListener;

/**
 * Created by michaelkapnick on 3/25/15.
 */
public class DialogSetterAndPopulator
{
    /**
     *
     * @param c
     * @param editText
     * @param title
     * @param profileInfo
     * @param behavior
     * @param allData
     * @param thisData
     */
    public static void setMultiChoiceDialogAndShow(Context c, EditText editText, String title,
                                                   ProfileInfo profileInfo,
                                                   ProfileInfoBehavior behavior,
                                                   ArrayList<String> allData,
                                                   String thisData)
    {
        CheckBoxDialogPositiveButtonListener        positiveButtonListener;
        DialogInterface.OnMultiChoiceClickListener  multiChoiceClickListener;
        DialogNegativeButtonListener                negativeButtonListener;
        String                                      positiveButton, negativeButton;
        CharSequence []                             items;
        boolean []                                  checkedItems;
        String []                                   thisDataArray;

        positiveButton          = "Ok";
        negativeButton          = "Cancel";
        items                   = new CharSequence[allData.size()];
        checkedItems            = new boolean [allData.size()];

        for(int i =0; i < checkedItems.length; i++)
        {
            items[i]        = allData.get(i);
            checkedItems[i] = false;
        }

        if(null != thisData)
        {
            if(thisData.contains(","))
            {
                thisDataArray = thisData.split(",");

                for(int i =0; i < allData.size(); i ++)
                {
                    for(String currentMajor: thisDataArray)
                    {
                        if(currentMajor.trim().contentEquals(items[i]))
                            checkedItems[i] = true;
                    }
                }
            }
            else
            {
                for(int i =0; i < allData.size(); i ++)
                {
                    if(thisData.trim().contentEquals(items[i]))
                        checkedItems[i] = true;
                }
            }
        }

        positiveButtonListener  = new CheckBoxDialogPositiveButtonListener(c, items, editText,
                                                                          checkedItems, profileInfo,
                                                                          behavior);
        multiChoiceClickListener= positiveButtonListener;
        negativeButtonListener  = new DialogNegativeButtonListener();

        DialogMultiChoiceFactory.createAndShowDialog(c, title, positiveButton, negativeButton,
                                                     items, positiveButtonListener,
                                                     negativeButtonListener,
                                                     multiChoiceClickListener,
                                                     checkedItems);
    }

    /**
     *
     * @param c
     * @param editText
     * @param title
     * @param profileInfo
     * @param behavior
     * @param allData
     * @param thisData
     */
    public static void setSingleChoiceDialogAndShow(Context c, EditText editText, String title,
                                                    ProfileInfo profileInfo,
                                                    ProfileInfoBehavior behavior,
                                                    ArrayList<String> allData,
                                                    String thisData)
    {
        DialogPositiveButtonListener                positiveButtonListener;
        DialogNegativeButtonListener                negativeButtonListener;
        String                                      positiveButton, negativeButton;
        CharSequence []                             items;
        int                                         position;

        position        = -1;

        positiveButton          = "Ok";
        negativeButton          = "Cancel";
        items                   = new CharSequence[allData.size()];

        for(int i =0; i < allData.size(); i++)
            items[i] = allData.get(i);

        if(null != thisData)
        {
            for(int i =0; i < allData.size(); i ++)
            {
                if(thisData.trim().contentEquals(items[i])) //this is the position in the list we want to set chcked
                    position = i;
            }
        }

        positiveButtonListener  = new DialogPositiveButtonListener(c, items, editText,
                                                                   profileInfo,
                                                                   behavior);
        negativeButtonListener  = new DialogNegativeButtonListener();

        DialogSingleChoiceFactory.createAndShowDialog(c, title, positiveButton, negativeButton,
                                                      items, positiveButtonListener,
                                                      negativeButtonListener,
                                                      position);
    }
}
