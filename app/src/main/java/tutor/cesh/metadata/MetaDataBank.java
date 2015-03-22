package tutor.cesh.metadata;

import android.content.Context;

import java.util.ArrayList;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public class MetaDataBank
{
    private ArrayList<Major>    majors;
    private ArrayList<Rate>     rates;
    private ArrayList<Year>     years;

    private static MetaDataBank metaDataBank;

    private MetaDataBank(Context c, ArrayList<Major> majors, ArrayList<Rate> rates,
                        ArrayList<Year> years)
    {
        this.majors = majors;
        this.rates  = rates;
        this.years  = years;
    }

    public static MetaDataBank getInstance(Context c, ArrayList<Major> majors,
                                           ArrayList<Rate> rates, ArrayList<Year> years)
    {
        if (metaDataBank == null)
            metaDataBank = new MetaDataBank(c, majors, rates, years);

        return metaDataBank;
    }
}
