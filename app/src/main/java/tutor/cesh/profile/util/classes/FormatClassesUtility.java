package tutor.cesh.profile.util.classes;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import tutor.cesh.profile.BubbleTextView;

/**
 * Created by michaelk18 on 10/11/14.
 */
public class FormatClassesUtility
{
    public static void setClassesRegularMode(String [] classes, Context context, TextView tv)
    {
        BubbleTextView bubbleTextView;
        SpannableStringBuilder sb;

        tv.setText("");

        if(classes.length > 0 && !classes[0].equalsIgnoreCase(""))
        {
            try
            {
                for(String c : classes)
                {
                    bubbleTextView = new BubbleTextView(context, new TextView(context));
                    sb = bubbleTextView.createBubbleOverText(c, false);
                    tv.append(sb);
                    tv.append(" ");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
