package tutor.cesh.rest.handler;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import tutor.cesh.Profile;
import tutor.cesh.rest.handler.ImageHandler;

/**
 * Created by michaelk18 on 7/12/14.
 */
public class CoverImageHandler implements ImageHandler
{
    private ImageView           view;
    private Resources           resources;
    private Context             context;

    public CoverImageHandler(Resources resources, ImageView view, Context c)
    {
        this.resources    = resources;
        this.view = view;
        this.context    = c;
    }

    @Override
    public void handle(Bitmap b, Profile profile)
    {
        BitmapDrawable  drawable;
        drawable    = new BitmapDrawable(resources, b);

        if(view != null)
            view.setBackground(drawable);

        if(profile != null)
            profile.setCoverImage(b);
    }
}
