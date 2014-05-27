package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;
import android.os.AsyncTask;

/**
 * Created by michaelk18 on 4/2/14.
 *
 * Makes use of the decorator pattern
 */
public class AsyncConvolution extends AsyncTask <Bitmap, Integer, Bitmap>
{
    private AsyncConvolveOp         asyncConvolveOp;

    public AsyncConvolution(AsyncConvolveOp asyncConvolveOp)
    {
        this.asyncConvolveOp = asyncConvolveOp;
    }

    @Override
    protected Bitmap doInBackground(Bitmap... params)
    {
        return this.asyncConvolveOp.doInBackground(params);
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        super.onPostExecute(result);
        this.asyncConvolveOp.onPostExecute(result);

    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
        this.asyncConvolveOp.onPreExecute();

    }
}
