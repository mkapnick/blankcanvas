package tutor.cesh.sampled.statik;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import tutor.cesh.math.EuclideanMetric;
import tutor.cesh.rest.TaskDelegate;

/**
 * Created by michaelk18 on 2/2/14.
 */
public class AsyncProfileImage extends AsyncTask<Bitmap, Void, Bitmap>
{
    private Context         context;
    private ProgressDialog  pd;
    private TaskDelegate    taskDelegate;
    private Bitmap          roundedBitmap;


    public AsyncProfileImage(Context c, TaskDelegate td)
    {
        this.context        = c;
        this.taskDelegate   = td;
        pd                  = new ProgressDialog(c);

    }

    @Override
    protected Bitmap doInBackground(Bitmap... bitmaps)
    {
        Rounder rounder;

        rounder         = new Rounder(new EuclideanMetric());
        roundedBitmap   = rounder.round(bitmaps[0]);

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        //super.onPostExecute(result);
        if(pd!=null)
        {
            pd.dismiss();
            taskDelegate.taskCompletionResult(roundedBitmap);
        }
        super.onPostExecute(result);
    };

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();
        pd.setTitle("Updating...");
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
        pd.show();
        super.onPreExecute();
    }

}