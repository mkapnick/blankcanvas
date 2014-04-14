package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;
import android.graphics.Color;

import tutor.cesh.math.EuclideanMetric;
import tutor.cesh.math.Metric;

/**
 * Created by michaelk18 on 4/8/14.
 */
public class GrayExceptOp
{
    private int [] highlightColor;
    private final int TOLERANCE = 210;
    private double [] x, y;
    private Metric metric;

    public GrayExceptOp(int [] highlightColor)
    {
        this.highlightColor = highlightColor;
        this.x              = new double[3];
        this.y              = new double[3];
        this.metric         = new EuclideanMetric();
    }

    private boolean areSimilar(int [] a, int [] b)
    {
        boolean similar = false;
        for(int i =0; i < 3; i++)
        {
            this.x[i] = a[i];
            this.y[i] = b[i];
        }

        if(metric.distance(x,y) <= TOLERANCE)
            similar = true;

        return similar;
    }

    public Bitmap filter(Bitmap src, Bitmap dst)
    {
        int []  rgb;
        int []  grey;

        rgb     = new int[3];
        grey    = new int[3];

        if(dst == null)
            dst = Bitmap.createBitmap(src.getWidth(),
                    src.getHeight(),
                    Bitmap.Config.ARGB_8888);

        for(int r =0; r < src.getWidth(); r++)
        {
            for(int c =0; c < src.getHeight(); c++)
            {
                rgb[0] = Color.red(src.getPixel(r,c));
                rgb[1] = Color.green(src.getPixel(r,c));
                rgb[2] = Color.blue(src.getPixel(r,c));

                if(areSimilar(rgb, highlightColor))
                    dst.setPixel(r,c, Color.rgb(rgb[0], rgb[1], rgb[2]));
                else
                {
                    grey[0] = (rgb[0] + rgb[1] + rgb[2]) / 3;
                    grey[1] = grey[0];
                    grey[2] = grey[0];

                    dst.setPixel(r,c,Color.rgb(grey[0], grey[1], grey[2]));
                }

            }
        }
        return dst;
    }
}
