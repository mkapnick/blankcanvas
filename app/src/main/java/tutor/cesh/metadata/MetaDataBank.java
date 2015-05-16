package tutor.cesh.metadata;

import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public class MetaDataBank
{
    private static HashMap<String, ArrayList<String>>   metaDataMap;
    private static MetaDataBank                         metaDataBank;

    private MetaDataBank(Context c, HashMap<String, ArrayList<String>> data)
    {
        metaDataMap = data;
    }

    public static MetaDataBank getInstance(Context c, HashMap<String, ArrayList<String>> data)
    {
        if (metaDataBank == null)
            metaDataBank = new MetaDataBank(c, data);

        return metaDataBank;
    }

    public static ArrayList<String> getMajors() {
        return metaDataMap.get("majors");
    }

    public static ArrayList<String> getRates() {
        return metaDataMap.get("rates");
    }

    public static ArrayList<String> getYears() {
        return metaDataMap.get("years");
    }

    public static ArrayList<String> getMinors(){
        return metaDataMap.get("minors");
    }
}
