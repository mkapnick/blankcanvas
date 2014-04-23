package tutor.cesh.sampled.statik;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import tutor.cesh.profile.ImageController;
import tutor.cesh.profile.ImageLocation;
import tutor.cesh.rest.TaskDelegate;

/**
 * Created by michaelk18 on 4/2/14.
 */
public class AsyncConvolution extends AsyncTask <Bitmap, Integer, Bitmap>
{
    private ConvolveOp              op;
    private ProgressDialog          pd;
    private Bitmap                  convolvedBitmap;
    private TaskDelegate            taskDelegate;
    private BlurredImageLifeCycle   blurredImageLifeCycle;


    public AsyncConvolution(ProgressDialog pd, ConvolveOp op, TaskDelegate td, BlurredImageLifeCycle blurredImageLifeCycle)
    {
        this.op                     = op;
        this.pd                     = pd;
        this.taskDelegate           = td;
        this.blurredImageLifeCycle  = blurredImageLifeCycle;
    }

    public AsyncConvolution(ConvolveOp op, TaskDelegate td)
    {
        this.op             = op;
        this.taskDelegate   = td;
    }

    @Override
    protected Bitmap doInBackground(Bitmap... params)
    {
        Bitmap currentBitmap, tmp;
        currentBitmap = params[0];

        if(blurredImageLifeCycle != null)
        {
            for (int i = 0; i < 5; i++)
            {
                tmp = this.op.filter(currentBitmap, null);
                currentBitmap = tmp;
                this.blurredImageLifeCycle.add(currentBitmap);
            }
        }
        else
        {
            tmp = this.op.filter(currentBitmap, null);
            currentBitmap = tmp;
        }

        this.convolvedBitmap = currentBitmap;
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        super.onPostExecute(result);

        ImageController controller;
        controller = ImageController.getInstance();

        if(pd!=null) {
            pd.dismiss();
        }

        controller.push(convolvedBitmap, ImageLocation.BACKGROUND);
        taskDelegate.taskCompletionResult(this.convolvedBitmap, true);

    };

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(pd != null) {
            pd.setTitle("Updating...");
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }
}
