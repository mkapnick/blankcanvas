package tutor.cesh.sampled.statik;

import tutor.cesh.exception.NotASquareMatrixException;

/**
 * Created by michaelk18 on 4/2/14.
 */
public class BitmapOpFactory
{

    /**
     *
     * @param size
     * @return
     */
    public static ConvolveOp createBlurOp(int size)
    {
        ConvolveOp op;
        Kernel kernel;

        op = null;
        try
        {
            kernel  = new Kernel(size,size,getBlurValues(size));
            op      = new ConvolveOp(kernel);
        }
        catch(NotASquareMatrixException e)
        {
            e.printStackTrace();
        }

        return op;
    }

    /**
     *
     * @param size
     * @return
     */
    private static double []  getBlurValues(int size)
    {
        double denom;
        double[] result;

        denom = (double)(size*size);
        result = new double[size*size];
        for (int row=0; row<size; row++)
            for (int col=0; col<size; col++)
                result[indexFor(row,col,size)] = 1.0f/denom;

        return result;
    }

    /**
     *
     * @param row
     * @param col
     * @param size
     * @return
     */
    private static int indexFor(int row, int col, int size)
    {
        return row*size + col;
    }


}
