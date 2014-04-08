package tutor.cesh.math;

public class SupremumMetric
        implements Metric
{
    public double distance(double[] x, double[] y)
    {
        double result, term;
        int n;
        result = Math.abs(x[0]-y[0]);
        n = Math.min(x.length, y.length);
        for (int i=1; i<n; i++)
        {
            term = Math.abs(x[i]-y[i]);
            if (term > result) result = term;
        }
        return result;
    }
}