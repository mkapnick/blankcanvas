package tutor.cesh.rest;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import tutor.cesh.Profile;

/**
 * Created by michaelk18 on 7/12/14.
 */
public class CoverImageHandler implements ImageHandler
{
    private ImageView view;
    private Resources  resources;

    public CoverImageHandler(Resources resources, ImageView view)
    {
        this.resources    = resources;
        this.view = view;
    }

    @Override
    public void handle(Bitmap b, Profile profile)
    {
        BitmapDrawable  drawable;
        drawable = new BitmapDrawable(resources, b);

        if(view != null)
            view.setBackground(drawable);

        profile.setCoverImage(b);

    }
}
