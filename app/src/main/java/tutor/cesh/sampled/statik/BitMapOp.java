package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by michaelk18 on 3/1/14.
 */
public class BitMapOp
{

    public static Bitmap getResizedBitmap(Bitmap image, float scaledWidth, float scaledHeight)
    {
        Matrix  matrix;
        Bitmap  resizedBitmap;



        // create a matrix for the manipulation
        matrix          = new Matrix();
        // resize the bit map
        matrix.setScale(scaledWidth, scaledHeight);
        // recreate the new Bitmap
        resizedBitmap   = Bitmap.createBitmap(image, 0, 0, image.getWidth(), image.getHeight(),
                matrix, true);

        return resizedBitmap;
    }


}
