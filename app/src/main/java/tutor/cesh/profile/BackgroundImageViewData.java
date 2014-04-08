package tutor.cesh.profile;

/**
 * Created by michaelk18 on 4/5/14.
 */
public class BackgroundImageViewData
{
    private static int width;
    private static int height;
    private static BackgroundImageViewData b;

    private BackgroundImageViewData(int width, int height)
    {
        this.width  = width;
        this.height = height;
    }

    public static BackgroundImageViewData getInstance(int width, int height)
    {
        if (b == null)
        {
            b = new BackgroundImageViewData(width, height);
        }
        return b;
    }

    public int getWidth()
    {
        return this.width;
    }

    public int getHeight()
    {
        return this.height;
    }
}
