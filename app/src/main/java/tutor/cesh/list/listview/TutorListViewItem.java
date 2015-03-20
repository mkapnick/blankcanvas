package tutor.cesh.list.listview;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by michaelkapnick on 3/20/15.
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
