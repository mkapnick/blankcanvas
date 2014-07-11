package tutor.cesh;

/**
 * Created by michaelk18 on 7/10/14.
 */
public class Tutor
{
    private String rate;
    private String rating;
    private String id;
    private String about;
    private String [] classes;

    public Tutor()
    {
        classes = new String[0];
    }
    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String[] getClasses() {
        return classes;
    }

    public void setClasses(String[] classes) {
        this.classes = classes;
    }
}
