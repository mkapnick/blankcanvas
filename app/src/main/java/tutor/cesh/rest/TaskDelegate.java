package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.graphics.Bitmap;

import org.json.JSONObject;

/**
 * Created by michaelk18 on 4/7/14.
 */
public interface TaskDelegate
{
    public void taskCompletionResult(Bitmap b);
    public void taskCompletionResult(JSONObject response);
    public void setProgressDialog(ProgressDialog pd);

}