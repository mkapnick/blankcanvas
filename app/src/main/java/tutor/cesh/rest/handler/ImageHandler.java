package tutor.cesh.rest.handler;

import android.graphics.Bitmap;

import tutor.cesh.profile.persistant.Profile;

/**
 * Created by michaelk18 on 4/7/14.
 */
public interface ImageHandler
{
   public void handle(Bitmap b, Profile profile);

}