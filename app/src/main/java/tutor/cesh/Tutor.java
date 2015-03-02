package tutor.cesh;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.HashMap;

import tutor.cesh.database.LocalDatabaseHelper;

/**
 * Created by michaelk18 on 7/10/14.
 */
public class Tutor implements Profile
{
    private String              rate,rating, id, about, coverImageUrl, profileImageUrl;
    private String []           classes;
    private ArrayList<String>   currentClasses, pastClasses;
    private Bitmap              profileImage, coverImage;
    private LocalDatabaseHelper localDatabaseHelper;
    private boolean             isPublic;

    public Tutor(Context c)
    {
        classes = new String[0];
        this.localDatabaseHelper = new LocalDatabaseHelper(c);
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
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
        localDatabaseHelper.saveTutorCoverImageRecord(coverImage);
    }

    public void setCurrentClasses(ArrayList<String> classes)
    {
        this.currentClasses = classes;
    }

    public ArrayList<String> getCurrentClasses()
    {
        return this.currentClasses;
    }

    public void setPastClasses(ArrayList<String> pastClasses)
    {
        this.pastClasses = pastClasses;
    }

    public ArrayList<String> getPastClasses()
    {
        return this.pastClasses;
    }
}
