package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;
import android.graphics.Color;

import tutor.cesh.math.EuclideanMetric;
import tutor.cesh.math.Metric;

/**
 * Created by michaelk18 on 4/8/14.
 */
public class Posterizer
{
    private double [] x,y;
    private Metric metric;

    private static final int[] BLACK = { 0, 0, 0};
    private static final int[] WHITE = {255,255,255};

    public Posterizer(Metric metric)
    {
        this.x      = new double[3];
        this.y      = new double[3];
        this.metric = metric;
    }
    public Posterizer()
    {
        this.x      = new double[3];
        this.y      = new double[3];
        this.metric = new EuclideanMetric();
    }

    private double distance(int [] a, int [] b)
    {
        for(int i =0; i < 3; i++)
        {
            this.x[i] = a[i];
            this.y[i] = b[i];
        }
        return metric.distance(x,y);
    }

    public Bitmap toBlackAndWhite(Bitmap image)
    {
        double  distanceFromBlack, distanceFromWhite;
        int []  rgb, grey;
        Bitmap  dst;

        dst = Bitmap.createBitmap(image.getWidth(),
                image.getHeight(),
                Bitmap.Config.ARGB_8888);

        rgb     = new int[3];
        grey    = new int[3];

        for (int r = 0; r < image.getWidth(); r++)
        {
            for(int c=0; c < image.getHeight(); c++)
            {
                rgb[0] = Color.red(image.getPixel(r, c));
                rgb[1] = Color.green(image.getPixel(r, c));
                rgb[2] = Color.blue(image.getPixel(r,c));

                grey[0] = (rgb[0] + rgb[1] + rgb[2]) / 3;
                grey[1] = grey[0];
                grey[2] = grey[0];

                dst.setPixel(r,c,Color.rgb(grey[0], grey[1], grey[2]));

                //distanceFromBlack   = distance(rgb, BLACK);
                //distanceFromWhite   = distance(rgb, WHITE);

                /*if(distanceFromBlack < distanceFromWhite)
                    dst.setPixel(r,c, Color.BLACK);
                else
                    dst.setPixel(r,c,Color.WHITE);*/
            }
        }
        return dst;
    }


}
