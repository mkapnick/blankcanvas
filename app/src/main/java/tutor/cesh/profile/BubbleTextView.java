package tutor.cesh.profile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private Context     context;
    private TextView    tv;

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
    public SpannableStringBuilder createBubbleOverText(String text, int start)
    {
        BitmapDrawable drawable;
        SpannableStringBuilder sb;

        this.tv = initializeTextView(text);

        sb       = new SpannableStringBuilder();
        drawable = convertTextViewToDrawable();

        System.out.println("checkpoint 4");

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        sb.append(text);
        sb.setSpan(new ImageSpan(drawable), 0, sb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        System.out.println("checkpoint 5");

        return sb;

    }

    /**
     * Initializes properties of the textview attribute
     * @param text
     * @return
     */
    private TextView initializeTextView(String text)
    {
        tv.setText(text);
        tv.setTextSize(12);
        tv.setTextColor(Color.parseColor("#FFFFFF"));
        tv.setBackgroundResource(R.drawable.oval);
        tv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.abc_ic_clear_search_api_holo_light, 0);
        return tv;
    }

    /**
     * Converts the text view to a Bitmap drawable
     *
     * @return A compatible BitmapDrawable instance
     */
    private BitmapDrawable convertTextViewToDrawable()
    {
        System.out.println("inside convert edit text to drawable");
        int                     spec;
        Bitmap                  bm, cacheBmp, viewBmp;
        Canvas                  canvas;

        spec    = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        tv.measure(spec, spec);
        tv.layout(0, 0, tv.getMeasuredWidth(), tv.getMeasuredHeight());

        System.out.println("checkpoint 1");

        bm      = Bitmap.createBitmap(tv.getWidth(), tv.getHeight(),Bitmap.Config.ARGB_8888);

        canvas  = new Canvas(bm);
        canvas.translate(-tv.getScrollX(), -tv.getScrollY());
        tv.draw(canvas);

        System.out.println("checkpoint 2");

        tv.setDrawingCacheEnabled(true);
        cacheBmp= tv.getDrawingCache();
        viewBmp = cacheBmp.copy(Bitmap.Config.ARGB_8888, true);
        tv.destroyDrawingCache();

        System.out.println("checkpoint 3");

        return new BitmapDrawable(context.getResources(), viewBmp);
    }

    public void setTextView(TextView tv)
    {
        this.tv = tv;
    }
}

