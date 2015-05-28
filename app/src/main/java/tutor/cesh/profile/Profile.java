package tutor.cesh.profile;

import android.graphics.Bitmap;

/**
 * Created by michaelk18 on 7/12/14.
 */
public interface Profile
{
    public void                     setCoverImage(Bitmap b);
    public Bitmap                   getCoverImage();
    public void                     setProfileImage(Bitmap b);
    public Bitmap                   getProfileImage();
}
