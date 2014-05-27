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
        return toBlackAndWhite(image, -100);
    }

    public Bitmap toBlackAndWhite(Bitmap image, double percentage) {

        int[]   rgb, grey;
        Bitmap  dst;
        int     startingHeight;
        double  distance, multiplier;

        if (percentage < 0)
            startingHeight = 0;
        else
            startingHeight = (int) Math.floor(image.getHeight() * (1 - percentage));

        dst = Bitmap.createBitmap(image.getWidth(),
                image.getHeight(),
                Bitmap.Config.ARGB_8888);

        rgb             = new int[3];
        grey            = new int[3];
        distance        = 0;

        if(startingHeight != 0) {
            for (int r = 0; r < image.getWidth(); r++) {
                for (int c = 0; c < startingHeight; c++) {
                    dst.setPixel(r, c, image.getPixel(r, c));

                }
            }
        }

        for (int r = 0; r < image.getWidth(); r++)
        {
            for (int c = startingHeight; c < image.getHeight(); c++)
            {

                distance = this.metric.distance(new double[]{r, c}, new double[]{r, image.getHeight()});
                rgb[0] = Color.red(image.getPixel(r, c));
                rgb[1] = Color.green(image.getPixel(r, c));
                rgb[2] = Color.blue(image.getPixel(r,c));

                multiplier = ((1-(1/distance)));
                multiplier -= .5;

                rgb[0] = (int)(rgb[0] * multiplier);
                rgb[1] = (int)(rgb[1] * multiplier);
                rgb[2] = (int)(rgb[2] * multiplier);



                /*grey[0] = (rgb[0] + rgb[1] + rgb[2]) / 3;
                grey[1] = grey[0];
                grey[2] = grey[0];*/

                dst.setPixel(r, c, Color.rgb(rgb[0], rgb[1],rgb[2]));

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
