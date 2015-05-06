package tutor.cesh.widget.textview;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by michaelkapnick on 5/5/15.
 */
public class TextViewWithFont extends TextView
{
    public TextViewWithFont(Context context) {
        super(context);
        setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));

    }

    public TextViewWithFont(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));

    }

    public TextViewWithFont(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));

    }

    public TextViewWithFont(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setTypeface((Typeface.create("sans-serif-light", Typeface.NORMAL)));

    }
}
