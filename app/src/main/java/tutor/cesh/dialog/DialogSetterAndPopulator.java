package tutor.cesh.dialog;

import android.content.Context;
import android.widget.EditText;

import java.util.ArrayList;

import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.dialog.listener.CheckBoxDialogButtonListener;
import tutor.cesh.dialog.listener.DialogSingleChoiceButtonListener;
import tutor.cesh.metadata.Major;
import tutor.cesh.metadata.MetaDataBank;
import tutor.cesh.metadata.Rate;
import tutor.cesh.metadata.Year;

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
        CheckBoxDialogButtonListener            buttonListener;
        String                                  title, positiveButton, negativeButton, currentMajors;
        MetaDataBank                            metaDataBank;
        ArrayList<Major>                        majors;
        CharSequence []                         array;
        User                                    user;
        Student                                 student;
        boolean []                              checkedItems;
        String []                               majorsArray;

        user            = User.getInstance(c);
        student         = user.getStudent();
        currentMajors   = student.getMajor();

        title                   = "Please select your major(s)";
        positiveButton          = "Ok";
        negativeButton          = "Cancel";
        metaDataBank            = MetaDataBank.getInstance(c, null, null, null);
        majors                  = metaDataBank.getMajors();
        array                   = new CharSequence[majors.size()];
        checkedItems            = new boolean [majors.size()];


        if(currentMajors.contains(","))
        {
            majorsArray = currentMajors.split(",");

            for(int i =0; i < majors.size(); i ++)
            {
                array[i]        = majors.get(i).getMajorName();
                checkedItems[i] = false;

                if(i < majorsArray.length)
                {
                    for(String major: majorsArray)
                    {
                        if(array[i].equals(major))
                            checkedItems[i] = true;
                    }
                }
            }
        }
        else
        {
            for(int i =0; i < majors.size(); i ++)
            {
                array[i]        = majors.get(i).getMajorName();
                checkedItems[i] = false;

                if(array[i].equals(currentMajors))
                    checkedItems[i] = true;
            }
        }

        buttonListener          = new CheckBoxDialogButtonListener(c, array, editText);

        DialogMultiChoiceFactory.createAndShowDialog(c, title, positiveButton, negativeButton,
                array, buttonListener, buttonListener, checkedItems);
    }

    /**
     *
     */
    public static void setRateDialogAndShow(Context c, EditText editText)
    {
        DialogSingleChoiceButtonListener        buttonListener;
        String                                  title, positiveButton, negativeButton, currentRate;
        MetaDataBank                            metaDataBank;
        ArrayList<Rate>                         rates;
        CharSequence []                         array;
        int                                     position;
        User                                    user;
        Tutor                                   tutor;

        user    = User.getInstance(c);
        tutor   = user.getTutor();
        position= -1;

        title                   = "Please select your rate";
        positiveButton          = "Ok";
        negativeButton          = "Cancel";
        currentRate             = tutor.getRate();
        metaDataBank            = MetaDataBank.getInstance(c, null, null, null);
        rates                   = metaDataBank.getRates();
        array                   = new CharSequence[rates.size()];

        for(int i =0; i < rates.size(); i ++)
        {
            array[i] = rates.get(i).getRateName();

            if(array[i].equals(currentRate)) //this is the position in the list we want to set chcked
                position = i;
        }

        buttonListener          = new DialogSingleChoiceButtonListener(array, editText);

        DialogSingleChoiceFactory.createAndShowDialog(c, title, positiveButton, negativeButton,
                array, buttonListener, position);

    }

    /**
     *
     */
    public static void setYearDialogAndShow(Context c, EditText editText)
    {
        DialogSingleChoiceButtonListener        buttonListener;
        String                                  title, positiveButton, negativeButton, currentYear;
        MetaDataBank                            metaDataBank;
        ArrayList<Year>                         years;
        CharSequence []                         array;
        int                                     position;
        User                                    user;
        Student                                 student;

        user        = User.getInstance(c);
        student     = user.getStudent();
        currentYear = student.getYear();
        position    = -1;

        title                   = "Please select your graduation year ";
        positiveButton          = "Ok";
        negativeButton          = "Cancel";
        metaDataBank            = MetaDataBank.getInstance(c, null, null, null);
        years                   = metaDataBank.getYears();

        array                   = new CharSequence[years.size()];

        for(int i =0; i < years.size(); i ++)
        {
            array[i] = years.get(i).getYearName();

            if(array[i].equals(currentYear))
                position = i;
        }

        buttonListener          = new DialogSingleChoiceButtonListener(array, editText);

        DialogSingleChoiceFactory.createAndShowDialog(c, title, positiveButton, negativeButton,
                array, buttonListener, position);
    }
}
