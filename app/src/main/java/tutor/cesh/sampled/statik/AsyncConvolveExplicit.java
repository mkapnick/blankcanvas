package tutor.cesh.sampled.statik;

import android.app.ProgressDialog;
import android.graphics.Bitmap;

import tutor.cesh.rest.TaskDelegate;

/**
 * Created by michaelk18 on 4/24/14.
 */
public class AsyncConvolveExplicit implements AsyncConvolveOp
{

    private ConvolveOp              op;
    private ProgressDialog          pd;
    private Bitmap                  convolvedBitmap;
    private TaskDelegate            taskDelegate;
    private BlurredImageContainer   container;


    public AsyncConvolveExplicit(ProgressDialog pd, ConvolveOp op, TaskDelegate td)
    {
        this.op                     = op;
        this.pd                     = pd;
        this.taskDelegate           = td;
        this.container              = BlurredImageContainer.getInstance();
    }

    @Override
    public Bitmap doInBackground(Bitmap... params)
    {
        //Eventually the taskdelegate will call BackgroundTaskDelegate, and from there,
        // the blurred images will get updated
        Bitmap currentBitmap, tmp;
        currentBitmap = params[0];

        tmp = this.op.filter(currentBitmap, null);
        currentBitmap = tmp;
        this.container.add(tmp);

        this.convolvedBitmap = currentBitmap;
        return null;
    }

    @Override
    public void onPostExecute(Bitmap result)
    {
        if(pd!=null) {
            pd.dismiss();
        }

        taskDelegate.taskCompletionResult(this.convolvedBitmap, true);
    }

    @Override
    public void onPreExecute()
    {
        if(pd != null)
        {
            pd.setTitle("Updating...");
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.setIndeterminate(true);
            pd.show();
        }
    }
}
