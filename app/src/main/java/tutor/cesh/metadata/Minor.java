package tutor.cesh.metadata;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public class Minor
{
    private String minorName;

    public Minor()
    {
        this("");
    }

    public Minor(String name)
    {
        this.minorName = name;
    }

    public String getMinorName() {
        return minorName;
    }
}
