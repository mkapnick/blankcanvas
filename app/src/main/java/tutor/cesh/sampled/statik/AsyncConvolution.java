package tutor.cesh.sampled.statik;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import tutor.cesh.rest.TaskDelegate;

/**
 * Created by michaelk18 on 4/2/14.
 */
public class AsyncConvolution extends AsyncTask <Bitmap, Integer, Bitmap>
{
    private ConvolveOp      op;
    private ProgressDialog  pd;
    private Bitmap          convolvedBitmap;
    private TaskDelegate    taskDelegate;


    public AsyncConvolution(ProgressDialog pd, ConvolveOp op, TaskDelegate td)
    {
        this.op             = op;
        this.pd             = pd;
        this.taskDelegate   = td;
    }

    public AsyncConvolution(ConvolveOp op, TaskDelegate td)
    {
        this.op             = op;
        this.taskDelegate   = td;
    }

    @Override
    protected Bitmap doInBackground(Bitmap... params)
    {
        this.convolvedBitmap = this.op.filter(params[0], null);
        return null;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        super.onPostExecute(result);

        if(pd!=null) {
            pd.dismiss();
        }
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
