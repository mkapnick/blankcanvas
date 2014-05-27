package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;

import org.json.JSONObject;

import tutor.cesh.profile.ImageController;
import tutor.cesh.profile.ImageLocation;
import tutor.cesh.profile.Subject;
import tutor.cesh.sampled.statik.Posterizer;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class BackgroundImageTaskDelegate implements TaskDelegate
{
    private Resources       resources;
    private Subject         subject;
    private static final int CONVOLUTION_KERNEL_SIZE = 3;


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
        ImageController             controller;
        Posterizer                  posterizer;
        Bitmap                      resultBitmap;

        posterizer      = new Posterizer();
        resultBitmap    = posterizer.toBlackAndWhite(b, .12);

        //Every background image should have black pixels on the bottom

        controller          = ImageController.getInstance();

        if(resources != null)
        {
            controller.push(resultBitmap, ImageLocation.BACKGROUND);
            subject.notifyObservers();
        }
    }

    @Override
    public void taskCompletionResult(JSONObject response)
    {
        //nothing
    }

    @Override
    public void setProgressDialog(ProgressDialog pd)
    {
        //nothing
    }
}
