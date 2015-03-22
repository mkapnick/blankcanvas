package tutor.cesh.metadata;

/**
 * Created by michaelkapnick on 3/22/15.
 */
public class Rate
{
    private String rateName;

    public Rate()
    {
        this("");
    }

    public Rate(String name)
    {
        this.rateName = name;
    }

    public String getRateName() {
        return rateName;
    }
}
