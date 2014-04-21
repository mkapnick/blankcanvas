package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;

import org.json.JSONObject;

import tutor.cesh.profile.ImageController;
import tutor.cesh.profile.ImageLocation;
import tutor.cesh.profile.Subject;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class BackgroundImageTaskDelegate implements TaskDelegate
{
    private Resources       resources;
    private Subject subject;


    public BackgroundImageTaskDelegate(Resources r, Subject subject)
    {
        this.resources  = r;
        this.subject    = subject;
    }

    public BackgroundImageTaskDelegate()
    {

    }

    @Override
    public void taskCompletionResult(Bitmap b, boolean check)
    {
        ImageController controller;

        controller = ImageController.getInstance();
        if(resources != null)
        {
            controller.push(b, ImageLocation.BACKGROUND);
            subject.notifyObservers();
        }
    }

    @Override
    public void taskCompletionResult(JSONObject response) {

    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {

    }
}
