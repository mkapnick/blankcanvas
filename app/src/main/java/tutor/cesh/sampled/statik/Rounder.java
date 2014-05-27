package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;
import android.graphics.Color;

import tutor.cesh.math.EuclideanMetric;
import tutor.cesh.math.Metric;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class Rounder
{
    private Metric metric;
    private int []  point;
    private int threshold = 70;

    public Rounder(Metric metric)
    {
        this.metric = metric;
    }

    public Rounder()
    {
        this.metric = new EuclideanMetric();
    }

    public Bitmap round(Bitmap src)
    {
        int     width, height;
        double  distance;
        double []  center, currentPoint;
        Bitmap  dst;

        center          = new double[2];
        currentPoint    = new double[2];

        width           = src.getWidth();
        height          = src.getHeight();

        System.out.println("Width is: " + width);
        System.out.println("Height is: " + height);
        dst             = Bitmap.createBitmap(  src.getWidth(),
                                                src.getHeight(),
                                                Bitmap.Config.ARGB_8888);
        center[0] = width / 2;
        center[1] = height / 2;

        for(int x = 0; x < width; x++)
        {
            for(int y =0; y < height; y++)
            {
                currentPoint[0] = x;
                currentPoint[1] = y;
                distance = distance(center, currentPoint);

                if(distance <= threshold)
                    dst.setPixel(x,y, src.getPixel(x,y));
                else
                    dst.setPixel(x,y, Color.TRANSPARENT);
            }
        }

        return dst;
    }

    private double distance(double [] center, double [] currentPoint)
    {
        return metric.distance(center, currentPoint);
    }
}
