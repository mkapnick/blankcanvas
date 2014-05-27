package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.graphics.Bitmap;

import org.json.JSONObject;

import tutor.cesh.profile.ImageController;
import tutor.cesh.profile.ImageLocation;
import tutor.cesh.profile.Subject;
import tutor.cesh.sampled.statik.Rounder;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class ProfileImageTaskDelegate implements TaskDelegate
{
    private Subject subject;

    public ProfileImageTaskDelegate(Subject subject)
    {
        this.subject = subject;
    }

    @Override
    public void taskCompletionResult(Bitmap b, boolean check)
    {
        Rounder         rounder;
        Bitmap          roundedBitmap,tmp;
        ImageController controller;

        controller = ImageController.getInstance();
        tmp         = Bitmap.createBitmap(b,0,0,160,160);
        rounder         = new Rounder();
        roundedBitmap   = rounder.round(tmp);

        controller.push(roundedBitmap, ImageLocation.PROFILE);
        subject.notifyObservers();
    }

    @Override
    public void taskCompletionResult(JSONObject response) {

    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {

    }
}