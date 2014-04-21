package tutor.cesh.profile;

import android.widget.ImageView;

/**
 * Created by michaelk18 on 4/20/14.
 */
public class ImageObserver implements Observer
{
    private ImageView   view;
    private Subject     subject;

    public ImageObserver(ImageView view, Subject subject)
    {
        this.view       = view;
        this.subject    = subject;
        this.subject.add(this);
    }

    @Override
    public void update()
    {
        ImageController controller;

        controller = ImageController.getInstance();
        if(view != null)
            view.setImageBitmap(controller.peek(ImageLocation.PROFILE));
    }
}
