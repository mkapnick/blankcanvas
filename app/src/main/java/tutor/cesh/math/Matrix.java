package tutor.cesh.math;

import tutor.cesh.exception.InvalidMatrixOperation;
import tutor.cesh.exception.LengthErrorException;

/**
 * Created by michaelk18 on 4/2/14.
 */
public class Matrix
{
    private double [][] values;
    private int rows;
    private int columns;

    public Matrix(int r, int c)
    {
        this.rows       = r;
        this.columns    = c;
        values = new double[rows][columns];
        initializeMatrixToZero();
    }

    /**
     *
     * @return
     */
    public int getRows()
    {
        return this.rows;
    }

    /**
     *
     * @return
     */
    public int getColumns()
    {
        return this.columns;
    }

    /**
     *
     */
    private void initializeMatrixToZero()
    {
        for(int r = 0; r < this.rows; r++)
        {
            for(int c =0; c < this.columns; c++)
            {
                values[r][c] = 0;
            }
        }
    }

    /**
     * Multiply the RL x CLRR Matrix a and the CLRR x CR Matrix b
     * to create a RL CR Matrix
     *
     * @param b   The Matrix to multiply by
     * @return    The resulting Matrix
     */
    public Matrix multiply(Matrix b) throws InvalidMatrixOperation
    {
        Matrix result;
        result = new Matrix(this.rows, b.columns);

        if (this.columns == b.rows)
        {
            for (int r =0; r < this.rows; r ++)
            {
                for(int c =0; c < b.columns; c++)
                {
                    for (int i=0; i<this.columns; i++)
                    {
                        values[r][c] += this.values[r][i] * b.values[i][c];
                    }
                }
            }
        }
        else
            throw new InvalidMatrixOperation("columns of matrix a are not the same as the rows of matrix b");

        return result;
    }

    /**
     * Calculate the dot product (more commonly known as the scalar product)
     * of two R x C matrices
     *
     * Note: While the steps in this calculation are the same as
     * in matrix multiplication (of a 1xn and nx1 Matrix), the result is
     * a scalar, not a Matrix. Hence, the need for this method.
     *
     * We could return (trans(a)*b).get(0,0) but this would create
     * two matrices needlessly.
     *
     * @param b  The Matrix to multiply by
     * @return   The scalar product
     */

    public double dot(Matrix b) throws LengthErrorException
    {
        double result;
        result = 0.0;

        if(this.rows == b.getRows() && this.columns == b.getColumns())
        {
            for (int r = 0; r < b.getRows(); r++)
                for (int c = 0; c < b.getColumns(); c++)
                    result += this.values[r][c] * b.values[r][c];
        }
        else throw new LengthErrorException("Length of matrices are incompatible");

        return result;
    }
    /**
     * Multiply a scalar and an R x C Matrix
     *
     * @param k   The scalar
     * @return    The resulting Matrix
     */

    Matrix multiply(double k)
    {
        Matrix    result;
        result = new Matrix(this.rows, this.columns);

        for (int r=0; r<this.rows; r++)
        {
            for (int c=0; c<this.columns; c++)
            {
                result.values[r][c] += k * this.values[r][c];
            }
        }

        return result;
    }

    /**
     *
     * @param row
     * @param column
     * @param value
     */
    public void setValue(int row, int column, double value)
    {
        if(row < this.rows && column < this.columns)
        {
            this.values[row][column] = value;
        }
        else
            throw new IndexOutOfBoundsException("row column is outside the bounds of this matrix");
    }

    /**
     *
     * @param row
     * @param column
     * @return
     */
    public double getValue(int row, int column)
    {
        if(row < this.rows && column < this.columns)
        {
            return this.values[row][column];
        }
        else
            throw new IndexOutOfBoundsException("row column is outside the bounds of this matrix");
    }

    public static Matrix toMatrix(int [][] array, int rows, int columns)
    {
        Matrix m;
        m = new Matrix(rows, columns);

        for(int r =0; r < rows; r++)
        {
            for(int c =0; c < columns; c++)
            {
                m.setValue(r,c, array[r][c]);
            }
        }

        return m;
    }
}
