package tutor.cesh;

import android.graphics.Bitmap;

import java.util.HashMap;

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
