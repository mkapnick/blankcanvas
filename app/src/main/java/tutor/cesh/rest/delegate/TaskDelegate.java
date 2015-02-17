package tutor.cesh.rest.delegate;

import android.app.ProgressDialog;

/**
 * Created by michaelk18 on 4/7/14.
 */
public interface TaskDelegate
{
    public void taskCompletionResult(Object response);
    public void setProgressDialog(ProgressDialog pd);

}