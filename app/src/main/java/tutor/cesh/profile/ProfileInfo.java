package tutor.cesh.profile;

import android.widget.EditText;

/**
 * Created by michaelkapnick on 3/28/15.
 */
public enum ProfileInfo
{
    MAJOR{
        public void setResult(String result, EditText editText){ show(result, editText); }
        public void update(String result, ProfileInfoBehavior behavior){ updateMajor(result,
                                                                                     behavior);}
    },
    MINOR{
        public void setResult(String result, EditText editText){ show(result, editText); }
        public void update(String result, ProfileInfoBehavior behavior){ updateMinor(result,
                                                                                     behavior); }
    },
    YEAR{
        public void setResult(String result, EditText editText){ show(result, editText); }
        public void update(String result, ProfileInfoBehavior behavior){ updateYear(result,
                                                                                    behavior);}
    },
    RATE{
        public void setResult(String result, EditText editText){ show(result, editText); }
        public void update(String result, ProfileInfoBehavior behavior){ updateRate(result,
                                                                                    behavior); }
    };

    public abstract void setResult(String result, EditText editText);
    public abstract void update(String result, ProfileInfoBehavior behavior);

    /**
     *
     * @param s
     * @param et
     */
    private static void show(String s, EditText et)
    {
        et.setText(s);
    }


    /**
     *
     * @param result
     * @param behavior
     */
    private static void updateMajor(String result, ProfileInfoBehavior behavior)
    {
        behavior.setMajor(result);
    }

    /**
     *
     * @param result
     * @param behavior
     */
    private static void updateMinor(String result, ProfileInfoBehavior behavior)
    {
        behavior.setMinor(result);
    }

    /**
     *
     * @param result
     * @param behavior
     */
    private static void updateRate(String result, ProfileInfoBehavior behavior)
    {
        behavior.setYear(result);
    }

    /**
     *
     * @param result
     * @param behavior
     */
    private static void updateYear(String result, ProfileInfoBehavior behavior)
    {
        behavior.setYear(result);
    }
}
