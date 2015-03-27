package tutor.cesh.profile.transiant;

import android.content.Context;

/**
 * Created by michaelkapnick on 3/25/15.
 */
public class StudentEditMode {

    private String major, year;
    private Context context;

    public StudentEditMode(Context context)
    {
        this.context = context;
    }
    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
