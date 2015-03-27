package tutor.cesh.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.EditText;

import java.util.ArrayList;

import tutor.cesh.dialog.listener.CheckBoxDialogPositiveButtonListener;
import tutor.cesh.dialog.listener.DialogNegativeButtonListener;
import tutor.cesh.dialog.listener.DialogPositiveButtonListener;
import tutor.cesh.dialog.listener.DialogRatePositiveButtonListener;
import tutor.cesh.dialog.listener.DialogYearPositiveButtonListener;
import tutor.cesh.profile.persistant.Student;
import tutor.cesh.profile.persistant.User;
import tutor.cesh.metadata.Major;
import tutor.cesh.metadata.MetaDataBank;
import tutor.cesh.metadata.Rate;
import tutor.cesh.metadata.Year;
import tutor.cesh.profile.transiant.StudentEditMode;
import tutor.cesh.profile.transiant.TutorEditMode;
import tutor.cesh.profile.transiant.UserEditMode;

/**
 * Created by michaelkapnick on 3/25/15.
 */
public class DialogSetterAndPopulator
{
    /**
     *
     */
    public static void setMajorDialogAndShow(Context c, EditText editText)
    {
        CheckBoxDialogPositiveButtonListener    positiveButtonListener;
        DialogInterface.OnMultiChoiceClickListener multiChoiceClickListener;
        DialogNegativeButtonListener            negativeButtonListener;
        String                                  title, positiveButton, negativeButton, currentMajors;
        MetaDataBank                            metaDataBank;
        ArrayList<Major>                        metaDataBankMajors;
        CharSequence []                         items;
        UserEditMode                            userEditMode;
        StudentEditMode                         studentEditMode;
        boolean []                              checkedItems;
        String []                               currentMajorsArray;

        userEditMode            = UserEditMode.getInstance(c);
        studentEditMode         = userEditMode.getStudentEditMode();
        currentMajors           = studentEditMode.getMajor();

        title                   = "Please select your major(s)";
        positiveButton          = "Ok";
        negativeButton          = "Cancel";
        metaDataBank            = MetaDataBank.getInstance(c, null, null, null);
        metaDataBankMajors      = metaDataBank.getMajors();
        items                   = new CharSequence[metaDataBankMajors.size()];
        checkedItems            = new boolean [metaDataBankMajors.size()];

        if(currentMajors.contains(","))
        {
            currentMajorsArray = currentMajors.split(",");

            for(int i =0; i < metaDataBankMajors.size(); i ++)
            {
                items[i]        = metaDataBankMajors.get(i).getMajorName();
                checkedItems[i] = false;

                for(String currentMajor: currentMajorsArray)
                {
                    if(currentMajor.trim().contentEquals(items[i]))
                        checkedItems[i] = true;
                }
            }
        }
        else
        {
            for(int i =0; i < metaDataBankMajors.size(); i ++)
            {
                items[i]        = metaDataBankMajors.get(i).getMajorName();
                checkedItems[i] = false;

                if(currentMajors.trim().contentEquals(items[i]))
                    checkedItems[i] = true;
            }
        }

        positiveButtonListener  = new CheckBoxDialogPositiveButtonListener(c, items, editText,
                                                                          checkedItems);
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
     */
    public static void setRateDialogAndShow(Context c, EditText editText)
    {
        DialogPositiveButtonListener                positiveButtonListener;
        DialogNegativeButtonListener                negativeButtonListener;
        String                                      title, positiveButton, negativeButton, currentRate;
        MetaDataBank                                metaDataBank;
        ArrayList<Rate>                             rates;
        CharSequence []                             items;
        int                                         position;
        UserEditMode                                userEditMode;
        TutorEditMode                               tutorEditMode;

        userEditMode    = UserEditMode.getInstance(c);
        tutorEditMode   = userEditMode.getTutorEditMode();
        position        = -1;

        title                   = "Please select your rate";
        positiveButton          = "Ok";
        negativeButton          = "Cancel";
        currentRate             = tutorEditMode.getRate();
        metaDataBank            = MetaDataBank.getInstance(c, null, null, null);
        rates                   = metaDataBank.getRates();
        items                   = new CharSequence[rates.size()];

        for(int i =0; i < rates.size(); i ++)
        {
            items[i] = rates.get(i).getRateName();

            if(currentRate.trim().contentEquals(items[i])) //this is the position in the list we want to set chcked
                position = i;
        }

        positiveButtonListener  = new DialogRatePositiveButtonListener(c, items, editText);
        negativeButtonListener  = new DialogNegativeButtonListener();

        DialogSingleChoiceFactory.createAndShowDialog(c, title, positiveButton, negativeButton,
                items, positiveButtonListener, negativeButtonListener, position);

    }

    /**
     *
     */
    public static void setYearDialogAndShow(Context c, EditText editText)
    {
        DialogPositiveButtonListener                positiveButtonListener;
        DialogNegativeButtonListener                negativeButtonListener;
        String                                      title, positiveButton, negativeButton, currentYear;
        MetaDataBank                                metaDataBank;
        ArrayList<Year>                             years;
        CharSequence []                             items;
        int                                         position;
        UserEditMode                                userEditMode;
        StudentEditMode                             studentEditMode;

        userEditMode        = UserEditMode.getInstance(c);
        studentEditMode     = userEditMode.getStudentEditMode();
        currentYear         = studentEditMode.getYear();
        position            = -1;

        title                   = "Please select your graduation year ";
        positiveButton          = "Ok";
        negativeButton          = "Cancel";
        metaDataBank            = MetaDataBank.getInstance(c, null, null, null);
        years                   = metaDataBank.getYears();

        items                   = new CharSequence[years.size()];

        for(int i =0; i < years.size(); i ++)
        {
            items[i] = years.get(i).getYearName();

            if(currentYear.trim().contentEquals(items[i]))
                position = i;
        }

        positiveButtonListener  = new DialogYearPositiveButtonListener(c, items, editText);
        negativeButtonListener  = new DialogNegativeButtonListener();

        DialogSingleChoiceFactory.createAndShowDialog(c, title, positiveButton, negativeButton,
                items, positiveButtonListener, negativeButtonListener, position);
    }
}
