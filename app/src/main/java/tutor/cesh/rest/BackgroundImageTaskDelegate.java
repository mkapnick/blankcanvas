package tutor.cesh.rest;

import android.app.ProgressDialog;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import org.json.JSONObject;

import tutor.cesh.profile.EditStudentProfileActivity;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class BackgroundImageTaskDelegate implements TaskDelegate
{
    private ImageView view;
    private Resources resources;

    public BackgroundImageTaskDelegate(Resources r, ImageView view)
    {
        this.resources  = r;
        this.view       = view;
    }

    @Override
    public void taskCompletionResult(Bitmap b)
    {
        Drawable drawable;

        drawable = new BitmapDrawable(resources, b);
        view.setBackground(drawable);
        EditStudentProfileActivity.backgroundImageBitmap = b;
    }

    @Override
    public void taskCompletionResult(JSONObject response) {

    }

    @Override
    public void setProgressDialog(ProgressDialog pd) {

    }
}
