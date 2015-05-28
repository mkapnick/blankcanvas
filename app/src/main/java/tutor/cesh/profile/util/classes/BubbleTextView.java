package tutor.cesh.profile.util.classes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import tutor.cesh.R;

/**
 * Inserting a bubbled textview into an edit text field
 */
public class BubbleTextView extends MultiAutoCompleteTextView
{
    private Context context;
    private TextView tv;

    /**
     * Initializes a BubbleTextView object
     *
     * @param c The context
     * @param tv A newly instantiated TextView object
     */
    public BubbleTextView(Context c, TextView tv)
    {
        super(c);
        this.context = c;
        this.tv = tv;
    }

    /**
     * Create a bounding bubble over some text
     */
    public SpannableStringBuilder createBubbleOverText(String text, boolean edit)
    {
        BitmapDrawable drawable;
        SpannableStringBuilder sb;

        text = text.toUpperCase();

        this.tv = initializeTextView(text, edit);

        sb = new SpannableStringBuilder();
        drawable = convertTextViewToDrawable();

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        sb.append(text);
        sb.setSpan(new ImageSpan(drawable), 0, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return sb;

    }

    /**
     * Initializes properties of the textview attribute
     * @param text
     * @return
     */
    private TextView initializeTextView(String text, boolean edit)
    {
        tv.setText(text);
        tv.setTextSize(12);
        tv.setTextColor(getResources().getColor(R.color.black));
        tv.setBackgroundResource(R.drawable.oval_experiment);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.oval_experiment, 0);

        return tv;
    }

    /**
     * Converts the text view to a Bitmap drawable
     *
     * @return A compatible BitmapDrawable instance
     */
    private BitmapDrawable convertTextViewToDrawable()
    {
        int spec;
        Bitmap bm, cacheBmp, viewBmp;
        Canvas canvas;

        spec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        tv.measure(spec, spec);
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        bm = Bitmap.createBitmap(tv.getWidth(), tv.getHeight(),Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bm);
        canvas.translate(-tv.getScrollX(), -tv.getScrollY());
        tv.draw(canvas);

        tv.setDrawingCacheEnabled(true);
        cacheBmp= tv.getDrawingCache();
        viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        tv.destroyDrawingCache();

        return new BitmapDrawable(context.getResources(), viewBmp);
    }

    public void setTextView(TextView tv)
    {
        this.tv = tv;
    }
}