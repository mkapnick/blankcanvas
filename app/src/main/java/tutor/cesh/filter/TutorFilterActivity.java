package tutor.cesh.filter;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import tutor.cesh.R;
import tutor.cesh.dialog.DialogSetterAndPopulator;
import tutor.cesh.dialog.ProfileInfo;
import tutor.cesh.dialog.ProfileInfoBehavior;
import tutor.cesh.metadata.Major;
import tutor.cesh.metadata.MetaDataBank;
import tutor.cesh.metadata.Rate;
import tutor.cesh.metadata.Year;

public class TutorFilterActivity extends ActionBarActivity implements View.OnClickListener {

    private TextView                            arrowBackTextView;
    private android.support.v7.app.ActionBar    actionBar;
    private EditText                            majorEditText, rateEditText, yearEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutor_filter);

        initializeUI();
    }

    private void initializeUI()
    {
        LayoutInflater  inflator;
        View            v;


        this.majorEditText  = (EditText) findViewById(R.id.editTextFilterMajor);
        this.rateEditText   = (EditText) findViewById(R.id.editTextFilterRate);
        this.yearEditText   = (EditText) findViewById(R.id.editTextFilterYear);

        this.majorEditText.setOnClickListener(this);
        this.rateEditText.setOnClickListener(this);
        this.yearEditText.setOnClickListener(this);

        inflator            = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v                   = inflator.inflate(R.layout.action_bar_tutor_filter, null);

        this.arrowBackTextView  = (TextView) v.findViewById(R.id.arrow_back_image);
        this.arrowBackTextView.setOnClickListener(this);
        this.actionBar          = getSupportActionBar();
        this.actionBar.setDisplayShowCustomEnabled(true);
        this.actionBar.setDisplayShowHomeEnabled(false);
        this.actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));

        this.actionBar.setCustomView(v);
    }

    @Override
    public void onClick(View v)
    {
        String majorTitle, rateTitle, yearTitle;
        ArrayList<String>   allData;
        ArrayList<Major>    allMajors;
        ArrayList<Rate>     allRates;
        ArrayList<Year>     allYears;
        String              thisData;
        MetaDataBank        metaDataBank;

        majorTitle = "Filter by major(s)";
        rateTitle  =  "Filter by rate(s)";
        yearTitle   = "Filter by year(s)";

        metaDataBank= MetaDataBank.getInstance(this, null, null, null);
        allMajors   = metaDataBank.getMajors();
        allRates    = metaDataBank.getRates();
        allYears    = metaDataBank.getYears();

        allData     = new ArrayList<String>();
        thisData    = "";


        switch(v.getId())
        {
            case R.id.arrow_back_image:
                finish();
                break;

            case R.id.editTextFilterMajor:

                for(int i =0; i < allMajors.size(); i++)
                    allData.add(allMajors.get(i).getMajorName());

                thisData = ProfileInfoBehavior.getFilterableMajor();
                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.majorEditText,
                                                                     majorTitle,
                                                                     ProfileInfo.MAJOR,
                                                                     ProfileInfoBehavior.FILTERABLE,
                                                                     allData,
                                                                     thisData);
                break;

            case R.id.editTextFilterRate:

                for(int i =0; i < allRates.size(); i++)
                    allData.add(allRates.get(i).getRateName());

                thisData = ProfileInfoBehavior.getFilterableRate();
                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.rateEditText,
                                                                      rateTitle,
                                                                      ProfileInfo.RATE,
                                                                      ProfileInfoBehavior.FILTERABLE,
                                                                      allData,
                                                                      thisData);
                break;

            case R.id.editTextFilterYear:

                for(int i =0; i < allYears.size(); i++)
                    allData.add(allYears.get(i).getYearName());

                thisData = ProfileInfoBehavior.getEditableYear();
                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.yearEditText,
                                                                      yearTitle,
                                                                      ProfileInfo.YEAR,
                                                                      ProfileInfoBehavior.FILTERABLE,
                                                                      allData,
                                                                      thisData);
                break;
        }

    }
}
