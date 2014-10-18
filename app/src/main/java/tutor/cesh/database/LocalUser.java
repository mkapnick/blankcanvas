package tutor.cesh.database;

import android.graphics.Bitmap;

/**
 * Created by michaelk18 on 10/15/14.
 */
public class LocalUser {

    private Bitmap studentCoverImage, studentProfileImage, tutorCoverImage,
                    tutorProfileImage;

    public LocalUser(){

    }

    public Bitmap getStudentCoverImage() {
        return studentCoverImage;
    }

    public void setStudentCoverImage(Bitmap studentCoverImage) {
        this.studentCoverImage = studentCoverImage;
    }

    public Bitmap getStudentProfileImage() {
        return studentProfileImage;
    }

    public void setStudentProfileImage(Bitmap studentProfileImage) {
        this.studentProfileImage = studentProfileImage;
    }

    public Bitmap getTutorCoverImage() {
        return tutorCoverImage;
    }

    public void setTutorCoverImage(Bitmap tutorCoverImage) {
        this.tutorCoverImage = tutorCoverImage;
    }

    public Bitmap getTutorProfileImage() {
        return tutorProfileImage;
    }

    public void setTutorProfileImage(Bitmap tutorProfileImage) {
        this.tutorProfileImage = tutorProfileImage;
    }
}
