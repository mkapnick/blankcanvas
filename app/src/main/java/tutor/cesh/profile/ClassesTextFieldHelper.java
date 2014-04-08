package tutor.cesh.profile;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;

/**
 * Created by michaelk18 on 3/30/14.
 */
public class ClassesTextFieldHelper implements TextFieldHelper {

    private Context context;

    public ClassesTextFieldHelper(Context context)
    {
        this.context = context;

    }

    @Override
    public void help(EditText classesTextField, GenericTextWatcher textWatcher, String[] classes, boolean isEditable)
    {

        BubbleTextView          bubbleTextView;
        String                  courseName;
        SpannableStringBuilder  sb;
        int                     start, end;
        TextView                tv;

        start       = 0;
        end         = 0;
        classesTextField.setText("");

        for (int i = 0; i < classes.length; i++)
        {
            courseName = classes[i];
            courseName = courseName.replaceAll("\\]", "");
            courseName = courseName.replaceAll("\\[", "");
            courseName = courseName.replaceAll("\"", "");

            tv = new TextView(context);
            bubbleTextView = new BubbleTextView(context, tv);

            if(isEditable) {
                sb = bubbleTextView.createBubbleOverText(courseName, false);
            }
            else
                sb = bubbleTextView.createBubbleOverText(courseName, false);

            end += sb.length();

            classesTextField.append(sb);
            classesTextField.getText().replace(start, end, sb, 0, sb.length());


            if (isEditable)
            {
                textWatcher.addTextView(tv);
                textWatcher.addLength(sb.length());
                textWatcher.setPreviousEndPosition(end);
                textWatcher.setPreviousStartPosition(end - sb.length());
                textWatcher.addStartingPosition(end - sb.length());
            }
            start += sb.length();
        }
    }

    @Override
    public String help(EditText classesTextField, GenericTextWatcher textWatcher, JSONArray jsonArray) {
        BubbleTextView bubbleTextView;
        SpannableStringBuilder sb;
        String courseName, formattedClasses;
        int start, end;

        start = 0;
        end = 0;

        formattedClasses = "[";
        classesTextField.setText("");


        try
        {
            for (int i = 0; i < jsonArray.length(); i++)
            {
                bubbleTextView = new BubbleTextView(context, new TextView(context));
                courseName = jsonArray.getString(i);
                courseName = courseName.substring(courseName.lastIndexOf(':') + 1);
                courseName = courseName.replaceAll("\\}", "");

                if (i != jsonArray.length() - 1)
                    formattedClasses += courseName + ",";
                else
                    formattedClasses += courseName + "]";

                courseName = courseName.replaceAll("\"", "");

                sb = bubbleTextView.createBubbleOverText(courseName, false);
                end += sb.length();
                classesTextField.append(sb);
                classesTextField.getText().replace(start, end, sb, 0, sb.length());
                start += sb.length();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return formattedClasses;
    }
}
