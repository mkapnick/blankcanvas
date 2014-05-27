package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * Created by michaelk18 on 4/24/14.
 */
public class AsyncConvolveImplicit implements AsyncConvolveOp
{
    private ConvolveOp              op;
    private BlurredImageContainer   container;

    public AsyncConvolveImplicit(ConvolveOp op)
    {
        this.op                     = op;
        this.container              = BlurredImageContainer.getInstance();
        this.container.reset();
    }
    @Override
    public Bitmap doInBackground(Bitmap... params)
    {
        Bitmap currentBitmap, tmp;
        Matrix matrix;

        currentBitmap = params[0];
        matrix = new Matrix();

        //matrix.postScale(.8f,.8f);
        //currentBitmap = Bitmap.createBitmap(currentBitmap,0,0,currentBitmap.getWidth(), currentBitmap.getHeight(), matrix,true);
        for (int i = 0; i < 5; i++)
        {
            tmp = this.op.filter(currentBitmap, null);
            currentBitmap = tmp;
            this.container.add(currentBitmap);

        }

        System.out.println("DONE! -----");


        return null;
    }

    @Override
    public void onPostExecute(Bitmap result)
    {
        //nothing
    }

    @Override
    public void onPreExecute()
    {
        //nothing
    }
}
