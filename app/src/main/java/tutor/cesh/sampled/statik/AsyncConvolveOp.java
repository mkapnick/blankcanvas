package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;

/**
 * Created by michaelk18 on 4/24/14.
 */
public interface AsyncConvolveOp
{
    public Bitmap doInBackground(Bitmap...params);
    public void onPostExecute(Bitmap result);
    public void onPreExecute();
}
