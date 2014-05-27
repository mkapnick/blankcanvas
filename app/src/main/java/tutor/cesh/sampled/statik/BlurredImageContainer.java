package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by michaelk18 on 4/24/14.
 */
public class BlurredImageContainer
{
    private ArrayList<Bitmap> bitmaps;
    private static BlurredImageContainer container;
    private final int THRESHOLD = 5;

    private BlurredImageContainer()
    {
        this.bitmaps = new ArrayList<Bitmap>();
    }

    public static BlurredImageContainer getInstance()
    {
        if(container == null)
        {
            container = new BlurredImageContainer();
        }
        return container;
    }

    public void add(Bitmap bitmap)
    {
        if(this.bitmaps.size() < THRESHOLD)
            bitmaps.add(bitmap);
        else
            System.out.println("Not adding to blurredImages.. greater than threshold (5)");
    }

    public void remove(Bitmap bitmap)
    {
        if(bitmaps.contains(bitmap))
        {
            bitmaps.remove(bitmap);
        }
    }
    public ArrayList<Bitmap> getList()
    {
        return bitmaps;
    }

    public void reset()
    {
        if(this.bitmaps.size() > 0)
            this.bitmaps.clear();
    }
    public Bitmap get(int index)
    {
        if(index >= 0 && index < this.bitmaps.size())
        {
            return this.bitmaps.get(index);
        }

        return null;
    }
    public int getSize()
    {
        return this.bitmaps.size();
    }

}
