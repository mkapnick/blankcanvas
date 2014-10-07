package tutor.cesh.sampled.statik;

import android.graphics.BitmapFactory;

/**
 * Created by michaelk18 on 8/14/14.
 */
public interface Scale {
    public int calculateInSampleSize( BitmapFactory.Options options, int reqWidth, int reqHeight);
}
