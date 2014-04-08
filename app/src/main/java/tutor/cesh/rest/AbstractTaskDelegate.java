package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.graphics.Bitmap;

import org.json.JSONObject;

/**
 * Created by michaelk18 on 4/7/14.
 */
public abstract class AbstractTaskDelegate implements TaskDelegate {

    protected ProgressDialog pd;

    @Override
    public void taskCompletionResult(Bitmap b)
    {
        //have children implement as necessary
    }

    @Override
    public void taskCompletionResult(JSONObject response)
    {
        //have children implement as necessary
    }

    public void setProgressDialog(ProgressDialog pd)
    {
        this.pd = pd;
    }
}
