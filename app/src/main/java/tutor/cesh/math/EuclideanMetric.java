package tutor.cesh.math;

/**
 * Created by michaelk18 on 4/7/14.
 */
public class EuclideanMetric
        implements Metric
{
    public double distance(double[] x, double[] y)
    {
        double result;
        int n;
        result = 0.0;
        n = Math.min(x.length, y.length);
        for (int i=0; i<n; i++)
        {
            result += Math.pow(x[i]-y[i], 2.0);
        }
        return Math.sqrt(result);
    }
}