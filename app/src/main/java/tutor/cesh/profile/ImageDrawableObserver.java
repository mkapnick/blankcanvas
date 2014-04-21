package tutor.cesh.profile;

import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by michaelk18 on 4/20/14.
 */
public class ImageDrawableObserver implements Observer
{
    private ImageView   view;
    private Subject     subject;
    private Resources   resources;

    public ImageDrawableObserver(ImageView view, Subject subject, Resources resources)
    {
        this.view       = view;
        this.subject    = subject;
        this.resources  = resources;
        this.subject.add(this);
    }

    @Override
    public void update()
    {
        Drawable drawable;
        ImageController controller;

        controller = ImageController.getInstance();
        drawable = new BitmapDrawable(resources, controller.peek(ImageLocation.BACKGROUND));

        if(view != null)
            view.setBackground(drawable);

    }
}
