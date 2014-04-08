package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.widget.ImageView;

import org.json.JSONObject;

import tutor.cesh.profile.EditStudentProfileActivity;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class ProfileImageTaskDelegate implements TaskDelegate
{
    private ImageView   view;
    private boolean     editing;

    public ProfileImageTaskDelegate(ImageView view) //boolean editing)
    {
        this.view       = view;
        //this.editing    = editing;
    }

    @Override
    public void taskCompletionResult(Bitmap b)
    {
        view.setImageBitmap(b);
        EditStudentProfileActivity.profileImageBitmap = b;

    }

    @Override
    public void taskCompletionResult(JSONObject response) {

    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {

    }
}