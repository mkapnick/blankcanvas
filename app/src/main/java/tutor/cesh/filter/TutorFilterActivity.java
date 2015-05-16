package tutor.cesh.filter;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import tutor.cesh.R;
import tutor.cesh.dialog.DialogSetterAndPopulator;
import tutor.cesh.profile.ProfileInfo;
import tutor.cesh.profile.ProfileInfoBehavior;
import tutor.cesh.metadata.MetaDataBank;


public class TutorFilterActivity extends Activity implements View.OnClickListener {

    private TextView                            arrowBackTextView, resetTextView,
                                                majorFilterTextView, rateFilterTextView,
                                                yearFilterTextView, minorFilterTextView;

    private EditText                            majorEditText, rateEditText, yearEditText,
                                                minorEditText;
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
        setUpTypeFaces();
        //setUpActionBar();
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
        this.minorEditText      = (EditText) findViewById(R.id.editTextFilterMinor);

        this.arrowBackTextView  = (TextView) findViewById(R.id.arrow_back_image);
        this.resetTextView      = (TextView) findViewById(R.id.resetTextViewFilter);
        this.majorFilterTextView= (TextView) findViewById(R.id.majorFilterTextView);
        this.rateFilterTextView = (TextView) findViewById(R.id.rateFilterTextView);
        this.yearFilterTextView = (TextView) findViewById(R.id.yearFilterTextView);
        this.minorFilterTextView= (TextView) findViewById(R.id.minorFilterTextView);


        this.applyFilterButton  = (Button) findViewById(R.id.applyFilterButton);
        this.applyFilterButton.setAllCaps(false);
        this.applyFilterButton.setTypeface(((Typeface.create("sans-serif-light", Typeface.NORMAL))));

        this.majorEditText.setOnClickListener(this);
        this.rateEditText.setOnClickListener(this);
        this.yearEditText.setOnClickListener(this);
        this.applyFilterButton.setOnClickListener(this);
        this.arrowBackTextView.setOnClickListener(this);
        this.resetTextView.setOnClickListener(this);
        this.minorEditText.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        String              majorTitle, rateTitle, yearTitle, minorTitle;
        ArrayList<String>   allData, allMajors, allRates, allYears, allMinors;
        String              thisData;

        majorTitle  = "Filter by major(s)";
        rateTitle   = "Filter by rate(s)";
        yearTitle   = "Filter by year(s)";
        minorTitle  = "Filter by minor(s)";

        allMajors   = MetaDataBank.getMajors();
        allRates    = MetaDataBank.getRates();
        allYears    = MetaDataBank.getYears();
        allMinors   = MetaDataBank.getMinors();

        allData     = new ArrayList<String>();

        switch(v.getId())
        {
            case R.id.arrow_back_image:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.applyFilterButton:
                setResult(RESULT_OK);
                ProfileInfoBehavior.FILTERABLE.setMajor(this.majorEditText.getText().toString());
                ProfileInfoBehavior.FILTERABLE.setRate(this.rateEditText.getText().toString());
                ProfileInfoBehavior.FILTERABLE.setYear(this.yearEditText.getText().toString());
                ProfileInfoBehavior.FILTERABLE.setMinor(this.minorEditText.getText().toString());
                finish();
                break;

            case R.id.resetTextViewFilter:
                resetFilters();
                //Toast.makeText(this, "Filters reset", Toast.LENGTH_SHORT).show();
                break;

            case R.id.editTextFilterMajor:

                shallowCopy(allMajors, allData);

                thisData = ProfileInfoBehavior.getFilterableMajor();
                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.majorEditText,
                                                                     majorTitle,
                                                                     ProfileInfo.MAJOR,
                                                                     ProfileInfoBehavior.FILTERABLE,
                                                                     allData,
                                                                     thisData);
                break;

            case R.id.editTextFilterRate:

                shallowCopy(allRates, allData);

                thisData = ProfileInfoBehavior.getFilterableRate();
                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.rateEditText,
                                                                     rateTitle,
                                                                     ProfileInfo.RATE,
                                                                     ProfileInfoBehavior.FILTERABLE,
                                                                     allData,
                                                                     thisData);
                break;

            case R.id.editTextFilterYear:

                shallowCopy(allYears, allData);

                thisData = ProfileInfoBehavior.getFilterableYear();
                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.yearEditText,
                                                                     yearTitle,
                                                                     ProfileInfo.YEAR,
                                                                     ProfileInfoBehavior.FILTERABLE,
                                                                     allData,
                                                                     thisData);
                break;

            case R.id.editTextFilterMinor:

                shallowCopy(allMinors, allData);

                thisData = ProfileInfoBehavior.getFilterableMinor();
                DialogSetterAndPopulator.setMultiChoiceDialogAndShow(this, this.minorEditText,
                                                                     minorTitle,
                                                                     ProfileInfo.MINOR,
                                                                     ProfileInfoBehavior.FILTERABLE,
                                                                     allData,
                                                                     thisData);
                break;
        }

    }

    /**
     *
     * @param originalData
     * @param newData
     */
    private void shallowCopy(ArrayList<String> originalData, ArrayList<String> newData)
    {
        if(null != originalData && originalData.size() > 0)
        {
            for(int i =0; i < originalData.size(); i++)
            {
                newData.add(originalData.get(i));
            }
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
        ProfileInfoBehavior.FILTERABLE.setMinor("");

        this.majorEditText.setText("");
        this.rateEditText.setText("");
        this.yearEditText.setText("");
        this.minorEditText.setText("");
    }

    /**
     *
     */
    private void setData()
    {
        String majors, rates, years, result, minors;
        String [] majorsArray, ratesArray, yearsArray, minorsArray;

        majors  = ProfileInfoBehavior.getFilterableMajor();
        rates   = ProfileInfoBehavior.getFilterableRate();
        years   = ProfileInfoBehavior.getFilterableYear();
        minors  = ProfileInfoBehavior.getFilterableMinor();

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

        if(null != minors && minors.length() > 0)
        {
            minorsArray = minors.split(",");
            result      = formatData(minorsArray);

            this.minorEditText.setText(result);
        }
    }

    private void setUpTypeFaces()
    {
        this.majorEditText.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));
        this.rateEditText.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));
        this.yearEditText.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));
        this.minorEditText.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));
        this.resetTextView.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));

        this.majorFilterTextView.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));
        this.rateFilterTextView.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));
        this.yearFilterTextView.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));
        this.minorFilterTextView.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));

    }

    /**
     *
     */
    /*private void setUpActionBar()
    {
        LayoutInflater inflator;
        View           v;
        this.actionBar = getSupportActionBar();
        this.actionBar.setDisplayShowCustomEnabled(true);
        this.actionBar.setDisplayShowHomeEnabled(false);
        this.actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.action_bar_background));


        inflator    = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v           = inflator.inflate(R.layout.action_bar_tutor_filter, null);

        this.actionBar.setCustomView(v);
    }*/

}
