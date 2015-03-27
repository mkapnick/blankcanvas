package tutor.cesh.profile.util.classes;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.TextView;

import java.util.Iterator;

/**
 * Created by michaelk18 on 7/8/14.
 */
public class ClassesUtility
{
    /**
     *
     * @param iterator
     * @return
     */
    public static String formatClassesBackend(Iterator<String> iterator)
    {
        String courseName, jsonArray;

        jsonArray = "";

        if(iterator.hasNext())
        {
            jsonArray = "[";

            while(iterator.hasNext())
            {
                courseName = iterator.next();
                jsonArray += "{\"name\": " + "\"" + courseName + "\"" + "},";
            }

            jsonArray = jsonArray.substring(0, jsonArray.lastIndexOf(","));
            jsonArray += "]";
        }

        return jsonArray;
    }

    /**
     *
     * @param iterator
     * @param context
     * @param tv
     */
    public static void formatClassesFrontEnd(Iterator<String> iterator, Context context, TextView tv)
    {
        BubbleTextView bubbleTextView;
        SpannableStringBuilder sb;

        tv.setText("");

        if(iterator.hasNext())
        {
            try
            {
                while(iterator.hasNext())
                {
                    bubbleTextView = new BubbleTextView(context, new TextView(context));
                    sb = bubbleTextView.createBubbleOverText(iterator.next(), false);
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
