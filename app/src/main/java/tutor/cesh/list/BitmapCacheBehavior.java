package tutor.cesh.list;

import android.graphics.Bitmap;

/**
 * Sets behavior for caching bitmaps
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public interface BitmapCacheBehavior
{
    public void cache(String identifier, Bitmap bitmap);
}
