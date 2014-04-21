package tutor.cesh.profile;

import android.graphics.Bitmap;

import java.util.Stack;

/**
 * Created by michaelk18 on 4/20/14.
 */
public class ImageController
{
    private Stack<Bitmap> profileImages, coverImages;
    private static ImageController imageController;

    private ImageController()
    {
        profileImages   = new Stack<Bitmap>();
        coverImages     = new Stack<Bitmap>();
    }

    public void push(Bitmap b, ImageLocation location)
    {
        switch(location)
        {
            case PROFILE:
                profileImages.push(b);
                break;
            case BACKGROUND:
                coverImages.push(b);
                break;
        }
    }
    public Bitmap pop(ImageLocation location)
    {
        Bitmap b;
        b = null;
        switch(location)
        {
            case PROFILE:
                if(profileImages.size() > 0)
                    b = profileImages.pop();
                break;
            case BACKGROUND:
                if(coverImages.size() > 0)
                    b = coverImages.pop();
                break;
        }

        return b;
    }
    public Bitmap peek(ImageLocation location)
    {
        Bitmap b;
        b = null;

        switch(location)
        {
            case PROFILE:
                if(profileImages.size() > 0)
                    b = profileImages.peek();
                break;
            case BACKGROUND:
                if(coverImages.size() > 0)
                    b = coverImages.peek();
                break;
        }

        return b;

    }

    public static ImageController getInstance()
    {
        if(imageController == null)
        {
            imageController = new ImageController();
        }
        return imageController;
    }

    public int size(ImageLocation location)
    {
        switch (location)
        {
            case PROFILE:
                return profileImages.size();
            case BACKGROUND:
                return coverImages.size();
        }

        return -1;
    }

    public void clear(ImageLocation location)
    {
        switch (location)
        {
            case PROFILE:
                profileImages.clear();
            case BACKGROUND:
                coverImages.clear();
        }
    }

}
