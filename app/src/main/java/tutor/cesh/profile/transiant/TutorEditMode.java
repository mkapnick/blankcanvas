package tutor.cesh.profile.transiant;

import android.content.Context;

/**
 * Created by michaelkapnick on 3/25/15.
 */
public class TutorEditMode
{
    private String rate;
    private Context context;

    public TutorEditMode(Context context)
    {
        this.context = context;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }
}
