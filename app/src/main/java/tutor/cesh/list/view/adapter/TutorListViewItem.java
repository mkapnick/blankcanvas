package tutor.cesh.list.view.adapter;

import android.graphics.drawable.Drawable;

/**
 * A row in the tutor list
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class TutorListViewItem
{
    private String      text;
    private Drawable    drawable;

    public TutorListViewItem(String text, Drawable resourceDrawable)
    {
        this.text   = text;
        this.drawable=resourceDrawable;
    }

    public String getText() {
        return this.text;
    }

    public Drawable getDrawable() {
        return this.drawable;
    }
}
