package tutor.cesh.sampled.statik;

import android.graphics.Bitmap;
import android.graphics.Color;

import tutor.cesh.exception.LengthErrorException;
import tutor.cesh.math.Matrix;


/**
 * Created by michaelk18 on 4/2/14.
 */
public class ConvolveOp
{
    private Kernel kernel;
    private double divider;

    public ConvolveOp(Kernel kernel)
    {
        this.kernel     = kernel;
        this.divider    = 1;
    }

    /**
     * Filters pixel by pixel a source image into a new destination image depending on
     * the kernel
     *
     * @param src
     * @param dst
     * @return
     */
    public Bitmap filter(Bitmap src, Bitmap dst)
    {
        Matrix          border;
        int             startY, endX, endY, tempX, tempY,totalRed, totalGreen, totalBlue,
                        kernelRows, kernelColumns, entryPoint;
        int [] []       red, green, blue;

        Matrix          mRed, mBlue, mGreen, kernelMatrix;

        kernelRows      = this.kernel.getRows();
        kernelColumns   = this.kernel.getColumns();
        kernelMatrix    = this.kernel.getMatrix();
        entryPoint      = ((int)Math.floor(kernelRows / 2));

        red             = new int [kernelRows][kernelColumns];
        green           = new int [kernelRows][kernelColumns];
        blue            = new int [kernelRows][kernelColumns];

        border          = new Matrix(kernelRows, kernelColumns);
        startY          = (int)Math.round(src.getHeight() / divider);
        endY            = src.getHeight();
        endX            = src.getWidth();

        if (dst == null)
        {
            dst = Bitmap.createBitmap(src.getWidth(), src.getHeight(), Bitmap.Config.ARGB_8888);
        }

        //Copy pixel by pixel the src image into the dst image, up to the calculated y
        for(int xx =0; xx < endX; xx++)
            for(int yy = 0; yy < startY; yy++)
                dst.setPixel(xx,yy, src.getPixel(xx, yy));

        //blur the rest of the dst image
        for(int x = 0; x < endX; x++) {
            for (int y = startY; y < endY; y++) {
                tempY = y - entryPoint;

                //fill in the border surrounding this pixel
                for (int r = 0; r < kernelRows && tempY < endY; r++) {
                    tempX = x - entryPoint;
                    if (tempX < 0) tempX = 0;
                    if (tempY < 0) tempY = 0;
                    for (int c = 0; c < kernelColumns && tempX < endX; c++) {
                        border.setValue(r, c, src.getPixel(tempX, tempY));
                        red[r][c] = Color.red(src.getPixel(tempX, tempY));
                        green[r][c] = Color.green(src.getPixel(tempX, tempY));
                        blue[r][c] = Color.blue(src.getPixel(tempX, tempY));
                        tempX++;
                    }
                    tempY++;
                }

                //covert 2D array of specific color to a Matrix
                mRed = Matrix.toMatrix(red, kernelRows, kernelColumns);
                mGreen = Matrix.toMatrix(green, kernelRows, kernelColumns);
                mBlue = Matrix.toMatrix(blue, kernelRows, kernelColumns);

                try {
                    // take the dot product b/w each color and the kernel
                    totalRed = (int) mRed.dot(kernelMatrix);
                    totalGreen = (int) mGreen.dot(kernelMatrix);
                    totalBlue = (int) mBlue.dot(kernelMatrix);
                    //set the new color value at this point
                    dst.setPixel(x, y, Color.rgb(totalRed, totalGreen, totalBlue));
                } catch (LengthErrorException e) {
                    e.printStackTrace();
                }
            }
        }
        return dst;
    }

    /**
     *
     * @param divider
     */
    public void setDivider(double divider)
    {
        this.divider = divider;
    }
}
