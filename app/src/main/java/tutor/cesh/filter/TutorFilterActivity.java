package tutor.cesh.filter;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nimbusds.jose.util.StringUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import tutor.cesh.R;
import tutor.cesh.dialog.DialogSetterAndPopulator;
import tutor.cesh.profile.ProfileInfo;
import tutor.cesh.profile.ProfileInfoBehavior;
import tutor.cesh.metadata.MetaDataBank;


public class TutorFilterActivity extends Activity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private TextView            arrowBackTextView, majorMinorTextView, rateTextView;
    private Button              applyFilterButton;
    private LinearLayout        majorMinorLayout, rateLayout;
    private ArrayList<String>   filteredMajors, filteredRates;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_tutor_filter);

        //initialize these
        this.filteredMajors  = new ArrayList<String>();
        this.filteredRates   = new ArrayList<String>();

        initializeUI();
        initializeMajorAndRateData();
        setUpMajorAndRateCheckBoxes();
        setUpTypeFaces();
    }

    /**
     *
     */
    private void initializeUI()
    {
        this.arrowBackTextView  = (TextView)findViewById(R.id.arrow_back_image);
        this.majorMinorTextView = (TextView)findViewById(R.id.majorMinorTextView);
        this.rateTextView       = (TextView)findViewById(R.id.rateTextView);
        this.applyFilterButton  = (Button)  findViewById(R.id.applyFilterButton);
        this.applyFilterButton.setAllCaps(false);
        this.applyFilterButton.setTypeface(((Typeface.create("sans-serif-light", Typeface.NORMAL))));

        this.majorMinorLayout    = (LinearLayout) findViewById(R.id.majorMinorLayout);
        this.rateLayout          = (LinearLayout) findViewById(R.id.rateLayout);

        this.applyFilterButton.setOnClickListener(this);
        this.arrowBackTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        String filteredMajors, filteredRates;

        filteredMajors  = "";
        filteredRates   = "";

        switch(v.getId())
        {
            case R.id.arrow_back_image:
                setResult(RESULT_CANCELED);
                finish();
                break;

            case R.id.applyFilterButton:
                setResult(RESULT_OK);

                /******************* Format for enum ******************/
                if(this.filteredMajors.size() > 0)
                {
                    for(String s: this.filteredMajors)
                    {
                        s = s.trim();
                        filteredMajors += s + ", ";
                    }
                    filteredMajors = filteredMajors.substring(0, filteredMajors.lastIndexOf(","));
                }

                /******************* Format for enum *******************/
                if(this.filteredRates.size() > 0)
                {
                    for(String s: this.filteredRates)
                    {
                        s = s.trim();
                        filteredRates += s + ", ";
                    }
                    filteredRates = filteredRates.substring(0, filteredRates.lastIndexOf(","));
                }

                /******************** Set Enums ***********************/
                ProfileInfoBehavior.FILTERABLE.setMajor(filteredMajors);
                ProfileInfoBehavior.FILTERABLE.setRate(filteredRates);
                ProfileInfoBehavior.FILTERABLE.setMinor(filteredMajors);
                finish();
                break;
        }
    }

    /**
     *
     *
     *
     */
    private void setUpMajorAndRateCheckBoxes()
    {
        ArrayList<String>           majors, rates;
        CheckBox                    checkBox;
        LinearLayout.LayoutParams   layoutParams;

        majors  = MetaDataBank.getMajors();
        rates   = MetaDataBank.getRates();

        /*************************** MAJOR CHECK BOXES ****************************/
        for(String s: majors)
        {
            s           = s.trim();
            checkBox    = new CheckBox(this);
            checkBox.setText(s);
            checkBox.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));

            if(this.filteredMajors.size() > 0)
            {
                for(int i =0; i < this.filteredMajors.size(); i++)
                {
                    if(this.filteredMajors.get(i).trim().equalsIgnoreCase(s))
                    {
                        checkBox.setChecked(true);
                        break;
                    }
                }
            }

            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(layoutParams);
            checkBox.setOnCheckedChangeListener(this);

            this.majorMinorLayout.addView(checkBox);
        }

        /************************** RATE CHECK BOXES ********************************/
        for(String s: rates)
        {
            s           = s.trim();
            checkBox    = new CheckBox(this);
            checkBox.setText(s);
            checkBox.setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));

            if(this.filteredRates.size() > 0)
            {
                for(String  str: this.filteredRates)
                {
                    str = str.trim();
                    if(str.equalsIgnoreCase(s))
                    {
                        checkBox.setChecked(true);
                        break;
                    }
                }
            }

            layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                                                         LinearLayout.LayoutParams.WRAP_CONTENT);
            checkBox.setLayoutParams(layoutParams);
            checkBox.setOnCheckedChangeListener(this);

            this.rateLayout.addView(checkBox);
        }
    }
    /**
     *
     */
    private void resetFilters()
    {
        ProfileInfoBehavior.FILTERABLE.setMajor("");
        ProfileInfoBehavior.FILTERABLE.setRate("");
        ProfileInfoBehavior.FILTERABLE.setMinor("");
    }

    /**
     *
     */
    private void initializeMajorAndRateData()
    {
        String majors, rates;
        String [] majorsArray, ratesArray;

        majors  = ProfileInfoBehavior.getFilterableMajor();
        rates   = ProfileInfoBehavior.getFilterableRate();

        /*********************** MAJORS *********************/
        if(null != majors && majors.length() > 0)
        {
            majorsArray = majors.split(",");

            for(String s: majorsArray)
            {
                s = s.trim();
                this.filteredMajors.add(s);
            }
        }

        /*********************** RATES *********************/
        if(null != rates && rates.length() > 0)
        {
            ratesArray  = rates.split(",");

            for(String s: ratesArray)
            {
                s = s.trim();
                this.filteredRates.add(s);
            }
        }
    }

    private void setUpTypeFaces()
    {
        this.majorMinorTextView.setTypeface(((Typeface.create("sans-serif-light", Typeface.NORMAL))));
        this.rateTextView.setTypeface(((Typeface.create("sans-serif-light", Typeface.NORMAL))));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
    {
        String s;

        s = buttonView.getText().toString().trim();

        //determine if we checked a rate or a major/minor
        if(isInteger(s))
        {
            if(isChecked)
            {
                this.filteredRates.add(s);
            }
            else
            {
                this.filteredRates.remove(s);
            }
        }
        else
        {
            if(isChecked)
            {
                this.filteredMajors.add(s);
            }
            else
            {
                this.filteredMajors.remove(s);
            }
        }
    }

    /**
     *
     * @param s
     * @return
     */
    private boolean isInteger(String s) {
        return s.matches("\\d+\\.\\d+") || s.matches("\\d+\\.\\d+\\+");
    }
}
