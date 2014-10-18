package tutor.cesh;

import android.content.Context;
import android.graphics.Bitmap;

import tutor.cesh.database.LocalDatabaseHelper;

/**
 * Created by michaelk18 on 7/2/14.
 */
public class Student implements Profile
{
    private String name, major, year, about, profileImageUrl,
            coverImageUrl, id, enrollId, tutorId, schoolId;
    private String [] classes;
    private Bitmap coverImage, profileImage;
    private LocalDatabaseHelper localDatabaseHelper;

    public Student(Context c)
    {
        this.classes                = new String[0];
        this.localDatabaseHelper    = new LocalDatabaseHelper(c);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getClasses() {
        return classes;
    }

    public void setClasses(String[] classes) {
        this.classes = classes;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getTutorId() {
        return tutorId;
    }

    public void setTutorId(String tutorId) {
        this.tutorId = tutorId;
    }

    public String getEnrollId() {
        return enrollId;
    }

    public void setEnrollId(String enrollId) {
        this.enrollId = enrollId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public Bitmap getCoverImage() {

        return coverImage;
    }

    public void setCoverImage(Bitmap coverImage)
    {
        this.coverImage = coverImage;
        localDatabaseHelper.saveStudentCoverImageRecord(coverImage);
    }
}
