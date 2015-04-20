package tutor.cesh.dialog;

/**
 * Created by michaelkapnick on 3/29/15.
 */
public enum ProfileInfoBehavior
{
    FILTERABLE{
        public void setMajor(String tmpMajor) {
            setFilterableVariables(tmpMajor, null, null);
        }
        public void setRate(String tmpRate){
            setFilterableVariables(null, tmpRate, null);
        }
        public void setYear(String tmpYear){
            setFilterableVariables(null, null, tmpYear);
        }
    },
    EDITABLE{
        public void setMajor(String tmpMajor) {
            setEditableVariables(tmpMajor, null, null);
        }
        public void setRate(String tmpRate){
            setEditableVariables(null, tmpRate, null);
        }
        public void setYear(String tmpYear){
            setEditableVariables(null, null, tmpYear);
        }
    };

    private static String filterableMajor, filterableRate, filterableYear;
    private static String editableMajor, editableRate, editableYear;

    public abstract void setMajor(String tmpMajor);
    public abstract void setRate(String tmpMajor);
    public abstract void setYear(String tmpMajor);

    public static String getEditableMajor() {
        return editableMajor;
    }

    public static String getEditableRate() {
        return editableRate;
    }

    public static String getEditableYear() {
        return editableYear;
    }

    public static String getFilterableMajor() {
        return filterableMajor;
    }

    public static String getFilterableRate() {
        return filterableRate;
    }

    public static String getFilterableYear() {
        return filterableYear;
    }

    /**
     *
     * @param tmpMajor
     * @param tmpRate
     * @param tmpYear
     */
    private static void setFilterableVariables(String tmpMajor, String tmpRate, String tmpYear)
    {
        if (null != tmpMajor)
            filterableMajor = tmpMajor;
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
    private static void setEditableVariables(String tmpMajor, String tmpRate, String tmpYear)
    {
        if (null != tmpMajor)
            editableMajor = tmpMajor;
        if (null != tmpRate)
            editableRate  = tmpRate;
        if (null != tmpYear)
            editableYear  = tmpYear;
    }
}
