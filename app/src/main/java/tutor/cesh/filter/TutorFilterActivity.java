package tutor.cesh.filter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import tutor.cesh.R;
import tutor.cesh.dialog.DialogSetterAndPopulator;
import tutor.cesh.dialog.ProfileInfo;
import tutor.cesh.dialog.ProfileInfoBehavior;
import tutor.cesh.metadata.Major;
import tutor.cesh.metadata.MetaDataBank;
import tutor.cesh.metadata.Rate;
import tutor.cesh.metadata.Year;

public class TutorFilterActivity extends Activity implements View.OnClickListener {

    private TextView                            arrowBackTextView, resetTextView;
    private android.support.v7.app.ActionBar    actionBar;
    private EditText                            majorEditText, rateEditText, yearEditText;
    private Button                              applyFilterButton;

    /**
     *
     * @param data
     * @return
     */
    private String formatData(String [] data)
    {
        String result, tmp;

        result = "";

        for (int i =0; i < data.length; i++)
        {
            tmp = data[i];
            result += tmp + ", ";
        }

        result = result.substring(0, result.lastIndexOf(","));

        return result;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutor_filter);

        initializeUI();
        setData();
    }

    /**
     *
     */
    private void initializeUI()
    {
        this.majorEditText      = (EditText) findViewById(R.id.editTextFilterMajor);
        this.rateEditText       = (EditText) findViewById(R.id.editTextFilterRate);
        this.yearEditText       = (EditText) findViewById(R.id.editTextFilterYear);
        this.arrowBackTextView  = (TextView) findViewById(R.id.arrow_back_image);
        this.resetTextView      = (TextView) findViewById(R.id.resetTextViewFilter);
        this.applyFilterButton  = (Button) findViewById(R.id.applyFilterButton);
        this.applyFilterButton.setAllCaps(false);

        this.majorEditText.setOnClickListener(this);
        this.rateEditText.setOnClickListener(this);
        this.yearEditText.setOnClickListener(this);
        this.applyFilterButton.setOnClickListener(this);
        this.arrowBackTextView.setOnClickListener(this);
        this.resetTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        String              majorTitle, rateTitle, yearTitle;
        ArrayList<String>   allData;
        ArrayList<Major>    allMajors;
        ArrayList<Rate>     allRates;
        ArrayList<Year>     allYears;
        String              thisData;
        MetaDataBank        metaDataBank;

        majorTitle  = "Filter by major(s)";
        rateTitle   = "Filter by rate(s)";
        yearTitle   = "Filter by year(s)";

        metaDataBank= MetaDataBank.getInstance(this, null, null, null);
        allMajors   = metaDataBank.getMajors();
        allRates    = metaDataBank.getRates();
        allYears    = metaDataBank.getYears();

        allData     = new ArrayList<String>();

        switch(v.getId())
        {
            case R.id.arrow_back_image:
                setResult(RESULT_CANCELED);
                //resetFilters();
                Toast.makeText(this, "No filters applied", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.applyFilterButton:
                setResult(RESULT_OK);
                ProfileInfoBehavior.FILTERABLE.setMajor(this.majorEditText.getText().toString());
                ProfileInfoBehavior.FILTERABLE.setRate(this.rateEditText.getText().toString());
                ProfileInfoBehavior.FILTERABLE.setYear(this.yearEditText.getText().toString());

                finish();
                break;

            case R.id.resetTextViewFilter:
                resetFilters();
                Toast.makeText(this, "Filters reset", Toast.LENGTH_SHORT).show();
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

                thisData = ProfileInfoBehavior.getFilterableYear();
                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.yearEditText,
                                                                     yearTitle,
                                                                     ProfileInfo.YEAR,
                                                                     ProfileInfoBehavior.FILTERABLE,
                                                                     allData,
                                                                     thisData);
                break;
        }

    }


    /**
     *
     */
    private void resetFilters()
    {
        ProfileInfoBehavior.FILTERABLE.setMajor("");
        ProfileInfoBehavior.FILTERABLE.setRate("");
        ProfileInfoBehavior.FILTERABLE.setYear("");

        this.majorEditText.setText("");
        this.rateEditText.setText("");
        this.yearEditText.setText("");
    }

    /**
     *
     */
    private void setData()
    {
        String majors, rates, years, result;
        String [] majorsArray, ratesArray, yearsArray;

        majors  = ProfileInfoBehavior.getFilterableMajor();
        rates   = ProfileInfoBehavior.getFilterableRate();
        years   = ProfileInfoBehavior.getFilterableYear();

        if(null != majors && majors.length() > 0)
        {
            majorsArray = majors.split(",");
            result      = formatData(majorsArray);

            this.majorEditText.setText(result);
        }

        if(null != rates && rates.length() > 0)
        {
            ratesArray  = rates.split(",");
            result      = formatData(ratesArray);

            this.rateEditText.setText(result);
        }

        if(null != years && years.length() > 0)
        {
            yearsArray = years.split(",");
            result      = formatData(yearsArray);

            this.yearEditText.setText(result);
        }
    }
}
