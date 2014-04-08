package tutor.cesh.sampled.statik;

import tutor.cesh.exception.NotASquareMatrixException;
import tutor.cesh.math.Matrix;

/**
 * Created by michaelk18 on 4/2/14.
 */
public class Kernel
{
    private Matrix matrix;

    public Kernel(int rows, int columns, double [] values) throws NotASquareMatrixException
    {
        if (rows == columns)
        {
            matrix = new Matrix(rows, columns);
            setValues(values);
        }

        else
            throw new NotASquareMatrixException("Rows and columns must be the same");
    }

    private void setValues(double [] values)
    {
        int count = 0;
        for(int r =0; r < matrix.getRows(); r++)
        {
            for(int c = 0; c < matrix.getColumns(); c++)
            {
                this.matrix.setValue(r,c, values[count]);
                count++;
            }
        }
    }

    public int getRows()
    {
        return matrix.getRows();
    }

    public int getColumns()
    {
        return matrix.getColumns();
    }
    public Matrix getMatrix()
    {
        return this.matrix;
    }
}
