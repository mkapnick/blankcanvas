package tutor.cesh.sampled.statik;

/**
 * Created by michaelk18 on 4/14/14.
 */
public enum LocalColors
{
    ORANGE(new int[]{255,165,0}),
    GREEN(new int[]{0,128,0}),
    BLUE(new int[]{0,0,255}),
    YELLOW(new int[]{255,255,0}),
    RED(new int[]{255,0,0}),
    PURPLE(new int[]{128,0,128});

    private int [] rgb;
    LocalColors(int [] rgb)
    {
        this.rgb = rgb;
    }

    public int [] getRgb()
    {
        return this.rgb;
    }
}
