package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by michaelk18 on 4/9/14.
 */
public class BlurredImageLifeCycle
{
    private static List<Bitmap> list;
    private static int          threshold;

    public BlurredImageLifeCycle()
    {
        this.list       = Collections.synchronizedList(new ArrayList<Bitmap>());
        this.threshold  = 5;
    }

    public void add(Bitmap b) throws IllegalArgumentException
    {
        if(this.list.size() - 1 < threshold)
            this.list.add(b);
        else
            throw new IllegalArgumentException("index exceeds threshold");
    }

    public List<Bitmap> getList()
    {
        return this.list;
    }

    public void reset()
    {
        if(this.list.size() > 0)
            this.list.clear();
    }





}
