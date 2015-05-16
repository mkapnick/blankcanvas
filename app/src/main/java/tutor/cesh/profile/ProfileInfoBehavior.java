package tutor.cesh.profile;

/**
 * Created by michaelkapnick on 3/29/15.
 */
public enum ProfileInfoBehavior
{
    FILTERABLE{
        public void setMajor(String tmpMajor) {setFilterableVariables(tmpMajor, null, null, null);}
        public void setRate(String tmpRate){setFilterableVariables(null, tmpRate, null, null);}
        public void setYear(String tmpYear){
            setFilterableVariables(null, null, tmpYear, null);
        }
        public void setMinor(String tmpMinor) {setFilterableVariables(null, null, null, tmpMinor);}
    },
    EDITABLE{
        public void setMajor(String tmpMajor) {setEditableVariables(tmpMajor, null, null, null);}
        public void setRate(String tmpRate){setEditableVariables(null, tmpRate, null, null);}
        public void setYear(String tmpYear){
            setEditableVariables(null, null, tmpYear, null);
        }
        public void setMinor(String tmpMinor) {setEditableVariables(null, null, null, tmpMinor);}

    };

    private static String filterableMajor, filterableRate, filterableYear, filterableMinor;
    private static String editableMajor, editableRate, editableYear, editableMinor;

    public abstract void setMajor(String tmpMajor);
    public abstract void setRate(String tmpMajor);
    public abstract void setYear(String tmpMajor);
    public abstract void setMinor(String tmpMinor);


    public static String getEditableMajor() {
        return editableMajor;
    }

    public static String getEditableRate() {
        return editableRate;
    }

    public static String getEditableYear() {
        return editableYear;
    }

    public static String getEditableMinor() {return editableMinor; }

    public static String getFilterableMajor() {
        return filterableMajor;
    }

    public static String getFilterableRate() {
        return filterableRate;
    }

    public static String getFilterableYear() {
        return filterableYear;
    }

    public static String getFilterableMinor() {
        return filterableMinor;
    }


    /**
     *
     * @param tmpMajor
     * @param tmpRate
     * @param tmpYear
     */
    private static void setFilterableVariables(String tmpMajor, String tmpRate,
                                               String tmpYear, String tmpMinor)
    {
        if (null != tmpMajor)
            filterableMajor = tmpMajor;
        if(null != tmpMinor)
            filterableMinor = tmpMinor;
        if(null != tmpRate)
            filterableRate  = tmpRate;
        if(null != tmpYear)
            filterableYear  = tmpYear;
    }

    /**
     *
     * @param tmpMajor
     * @param tmpRate
     * @param tmpYear
     */
    private static void setEditableVariables(String tmpMajor, String tmpRate, String tmpYear,
                                             String tmpMinor)
    {
        if (null != tmpMajor)
            editableMajor = tmpMajor;
        if(null != tmpMinor)
            editableMinor = tmpMinor;
        if (null != tmpRate)
            editableRate  = tmpRate;
        if (null != tmpYear)
            editableYear  = tmpYear;
    }
}
