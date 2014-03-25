package tutor.cesh.profile;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by michaelk18 on 3/19/14.
 */
public class GenericTextWatcher implements TextWatcher
{
    private static GenericTextWatcher   textWatcher;
    private Context                     context;
    private EditText                    editText;
    private BubbleTextView              bubbleTextView;
    private ArrayList<Integer>          lengths;
    int                                 currentPosition, previousEndPosition;

    private GenericTextWatcher(Context c, EditText e)
    {
        this.context                = c;
        this.editText               = e;
        this.lengths                = new ArrayList<Integer>();
        this.previousEndPosition    = 0;
        this.bubbleTextView         = new BubbleTextView(c, null);
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        System.out.println(s.length());
        System.out.println("START -- " + start);
        System.out.println("BEFORE --- " + before);

        if(s.length() >= 0)
        {
            s = s.toString().substring(previousEndPosition, s.length());

            int cursorPosition;
            String course;
            SpannableStringBuilder sb;


            cursorPosition = 0;
            System.out.println("Character seq. is : " + s);
            System.out.println("Count is: " + count + " previousEndPosition is: " + previousEndPosition);

            if (s.length() == 0 && count == 1) //space with nothing entered
            {
                this.editText.setSelection(0);
                Toast.makeText(context, "Enter a course", Toast.LENGTH_SHORT);
                this.editText.setSelection(currentPosition);
            }
            else if (s.length() == 1 && !(s.toString().matches("^[a-zA-Z0-9]*$"))) // first thing typed is not alphanumeric
            {
                this.editText.setText("");
                this.editText.setSelection(0);
                Toast.makeText(context, "Enter a course", Toast.LENGTH_SHORT);
                this.editText.setSelection(currentPosition);
            }
            else
            {
                if ((s.toString().contains(" ")) || !(s.toString().matches("^[a-zA-Z0-9]*$"))) {

                    course = s.toString();
                    course = course.trim();
                    course = course.replaceAll("[^A-Za-z0-9]", "");
                    System.out.println("Course is -------------------------------" + course);

                    this.bubbleTextView.setTextView(new TextView(context));
                    sb = this.bubbleTextView.createBubbleOverText(course, previousEndPosition);

                    this.editText.removeTextChangedListener(this);
                    this.editText.getText().replace(previousEndPosition, previousEndPosition + s.length(), sb, 0, sb.length());

                    this.lengths.add(sb.length());
                    for (Integer i : lengths) {
                        cursorPosition += i;
                    }
                    previousEndPosition = cursorPosition;
                    this.editText.setSelection(cursorPosition);
                    this.editText.addTextChangedListener(this);
                }
            }
        }

        System.out.println(s);
    }

    @Override
    public void afterTextChanged(Editable s)
    {

    }

    /**
     * Singleton pattern. Return one instance of the GenericTextWatcher class
     *
     * @param c the Context we are working with
     * @param e the EditText we are working with
     *
     * @return A GenericTextWatcher instance
     */
    public static GenericTextWatcher getInstance(Context c, EditText e)
    {
        if (textWatcher == null)
        {
            textWatcher = new GenericTextWatcher(c, e);
        }
        return textWatcher;
    }
}
