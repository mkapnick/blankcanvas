package tutor.cesh.rest.delegate;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import tutor.cesh.metadata.ServerDataBank;


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
        JSONObject          majorsObject, minorsObject, ratesObject, yearsObject;
        JSONArray           jsonArray, majorsArray, minorsArray, ratesArray, yearsArray;

        HashMap<String, ArrayList<String>> map;
        ArrayList<String>   majors, rates, years, minors;

        map = new HashMap<String, ArrayList<String>>();
        try
        {
            jsonArray       = (JSONArray) response;
            majorsObject    = jsonArray.getJSONObject(0);
            minorsObject    = jsonArray.getJSONObject(1);
            ratesObject     = jsonArray.getJSONObject(2);
            yearsObject     = jsonArray.getJSONObject(3);

            majorsArray     = majorsObject.getJSONArray("majors");
            minorsArray     = minorsObject.getJSONArray("minors");
            ratesArray      = ratesObject.getJSONArray("rates");
            yearsArray      = yearsObject.getJSONArray("years");

            majors          = getDataAsList(majorsArray, "major");
            minors          = getDataAsList(minorsArray, "minor");
            rates           = getDataAsList(ratesArray, "rate");
            years           = getDataAsList(yearsArray, "year");

            map.put("majors", majors);
            map.put("minors", minors);
            map.put("rates", rates);
            map.put("years", years);

            //initialize the MetaDataBank class
            ServerDataBank.getInstance(this.context, map);
        }
        catch(JSONException jsone)
        {

        }
    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {

    }

    private ArrayList<String> getDataAsList(JSONArray jsonArray, String key) throws JSONException
    {
        String                  tmp;
        ArrayList<String>       values;

        values  = new ArrayList<String>();

        for(int i =0; i < jsonArray.length(); i++)
        {
            tmp     = jsonArray.getJSONObject(i).getString(key);
            values.add(tmp);
        }

        return values;
    }
}
