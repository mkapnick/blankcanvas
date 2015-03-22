package tutor.cesh.metadata;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public class Year extends Object
{

    private String yearName;

    public Year()
    {
        this("");
    }

    public Year(String name)
    {
        this.yearName = name;
    }

    public String getYearName() {
        return yearName;
    }
}
