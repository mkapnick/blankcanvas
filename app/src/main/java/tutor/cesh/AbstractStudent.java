package tutor.cesh;

/**
 * Created by michaelk18 on 7/2/14.
 */
public abstract class AbstractStudent
{
    private String id, enrollId, tutorId, schoolId, coverImagePath,
            profileImagePath, name, major, year, about, rate,
            rating;

    private String [] classes;

    public AbstractStudent()
    {
        this.id = "";
        this.enrollId = "";
        this.tutorId = "";
        this.schoolId = "";
        this.coverImagePath = "";
        this.profileImagePath = "";
        this.name = "";
        this.major = "";
        this.year = "";
        this.about = "";
        this.rate = "";
        this.rating = "";
        this.classes = null;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
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

    public void setClasses(String [] classes) {
        this.classes = classes;
    }
}
