package tutor.cesh.rest.delegate;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import tutor.cesh.metadata.Major;
import tutor.cesh.metadata.MetaDataBank;
import tutor.cesh.metadata.Rate;
import tutor.cesh.metadata.Year;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public class MetaDataTaskDelegate implements TaskDelegate
{

    private Context context;

    public MetaDataTaskDelegate(Context c)
    {
        this.context = c;
    }

    @Override
    public void taskCompletionResult(Object response)
    {
        JSONObject          majorsObject, ratesObject, yearsObject;
        JSONArray           jsonArray, majorsArray, ratesArray, yearsArray;
        MetaDataBank        metaDataBank;
        ArrayList<Major>    majors;
        ArrayList<Rate>     rates;
        ArrayList<Year>     years;

        try
        {
            jsonArray       = (JSONArray) response;
            majorsObject    = jsonArray.getJSONObject(0);
            ratesObject     = jsonArray.getJSONObject(1);
            yearsObject     = jsonArray.getJSONObject(2);

            majorsArray     = majorsObject.getJSONArray("majors");
            ratesArray      = ratesObject.getJSONArray("rates");
            yearsArray      = yearsObject.getJSONArray("years");

            majors          = setMajors(majorsArray);
            rates           = setRates(ratesArray);
            years           = setYears(yearsArray);

            metaDataBank = MetaDataBank.getInstance(this.context, majors, rates, years);
        }
        catch(JSONException jsone)
        {

        }
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {

    }

    /**
     *
     * @param majorsArray
     * @return
     * @throws JSONException
     */
    private ArrayList<Major> setMajors(JSONArray majorsArray) throws JSONException
    {
        String              tmp;
        Major               major;
        ArrayList<Major>    majors;

        majors  = new ArrayList<Major>();

        for(int i =0; i < majorsArray.length(); i++)
        {
            tmp     = majorsArray.getJSONObject(i).getString("major");
            major   = new Major(tmp);

            majors.add(major);
        }

        return majors;
    }

    /**
     *
     * @param ratesArray
     * @return
     * @throws JSONException
     */
    private ArrayList<Rate> setRates(JSONArray ratesArray) throws JSONException
    {
        String              tmp;
        Rate                rate;
        ArrayList<Rate>     rates;

        rates   = new ArrayList<Rate>();

        for(int i =0; i < ratesArray.length(); i++)
        {
            tmp     = ratesArray.getJSONObject(i).getString("rate");
            rate    = new Rate(tmp);

            rates.add(rate);
        }

        return rates;
    }

    /**
     *
     * @param yearsArray
     * @return
     * @throws JSONException
     */
    private ArrayList<Year> setYears(JSONArray yearsArray) throws JSONException
    {
        String              tmp;
        Year                year;
        ArrayList<Year>     years;

        years   = new ArrayList<Year>();

        for(int i =0; i < yearsArray.length(); i++)
        {
            tmp     = yearsArray.getJSONObject(i).getString("year");
            year    = new Year(tmp);

            years.add(year);
        }

        return years;
    }
}
