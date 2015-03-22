package tutor.cesh.metadata;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public class Major
{
    private String majorName;

    public Major()
    {
        this("");
    }

    public Major(String name)
    {
        this.majorName = name;
    }

    public String getMajorName() {
        return majorName;
    }
}
