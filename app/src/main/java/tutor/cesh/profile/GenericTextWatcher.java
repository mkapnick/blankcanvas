package tutor.cesh.profile;

import android.content.Context;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

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
    private ArrayList<Integer>          lengths, startingPositions;
    private ArrayList<TextView>         textViews;
    private int                         previousEndPosition, previousStartPosition;

    public GenericTextWatcher(Context c, EditText e)
    {
        this.context                = c;
        this.editText               = e;
        this.lengths                = new ArrayList<Integer>();
        this.startingPositions      = new ArrayList<Integer>();
        this.textViews              = new ArrayList<TextView>();
        this.previousEndPosition    = 0;
        this.bubbleTextView         = new BubbleTextView(c, null);
        this.previousStartPosition  = 0;
    }

    @Override
    public void afterTextChanged(Editable s)
    {
        //nothing
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after)
    {
        //nothing
    }

    /**
     * Get the TextViews created dynamically by the user
     *
     * @return An Arraylist of textviews associated with the classes entered by the user
     */
    public ArrayList<TextView> getTextViews()
    {
        return this.textViews;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count)
    {
        int                     cursorPosition;
        String                  course;
        SpannableStringBuilder  sb;
        TextView                textViewOverString;

        cursorPosition  = 0;

        //user is backspacing on a textview
        if (start == previousStartPosition && before != 0 && count == 0 && this.lengths.size() != 0)
        {
            //set cursor position
            this.editText.setSelection(previousStartPosition);

            //update previousEndPosition
            previousEndPosition = previousStartPosition;

            //update lengths ArrayList
            this.lengths.remove(this.lengths.size() - 1);

            //update startingPositions ArrayList
            this.startingPositions.remove(this.startingPositions.size() - 1);

            //update textviews array
            this.textViews.remove(this.textViews.size() - 1);

            //update previousStartPosition
            if (this.startingPositions.size() != 0)
                previousStartPosition = this.startingPositions.get(this.startingPositions.size()-1);
            else
                previousStartPosition = 0;
        }

        else if(s.length() > 0)
        {
            //the string in question
            s = s.toString().substring(previousEndPosition, s.length());

            //if user just enters the space bar
            if (s.toString().contains(" ") && s.length() == 1)
            {
                this.editText.getText().replace(previousEndPosition, previousEndPosition + 1, "", 0, "".length());
            }

            //if user enters a valid string followed by a space
            else if ((s.toString().contains(" ") && s.length() > 1 && s.toString().substring(0,1).matches("^[a-zA-Z0-9]*$")))
            {
                course              = s.toString();
                course              = course.trim();
                course              = course.replaceAll("[^A-Za-z0-9]", "");
                textViewOverString  = new TextView(context);
                this.textViews.add(textViewOverString);

                //create the "bubble" over the string
                this.bubbleTextView.setTextView(textViewOverString);
                sb = this.bubbleTextView.createBubbleOverText(course, true);

                //remove the text watch listener
                this.editText.removeTextChangedListener(this);

                // add the spannable text to the edit text field in the correct position
                this.editText.getText().replace(previousEndPosition, previousEndPosition + s.length(), sb, 0, sb.length());

                //add the current spannable text length to the ArrayList of lengths
                this.lengths.add(sb.length());
                for (Integer i : lengths)
                {
                    cursorPosition += i;
                }
                previousEndPosition     = cursorPosition;

                //update previousStartPosition
                previousStartPosition   = previousEndPosition - sb.length();

                //add new previousStartPosition to the ArrayList of startPositions
                this.startingPositions.add(previousStartPosition);

                //set the correct cursor position
                this.editText.setSelection(cursorPosition);

                //add back the text watch listener
                this.editText.addTextChangedListener(this);
            }
        }
    }

    /**
     * Set the value for the most recent end position
     *
     * @param previousEndPosition The most recent end position
     */
    public void setPreviousEndPosition(int previousEndPosition)
    {
        this.previousEndPosition = previousEndPosition;
    }

    /**
     * Set the value for the most recent starting position
     *
     * @param previousStartPosition The most recent starting position
     */
    public void setPreviousStartPosition(int previousStartPosition)
    {
        this.previousStartPosition = previousStartPosition;
    }

    /**
     *
     * @param length
     */
    public void addLength(int length)
    {
        this.lengths.add(length);
    }

    /**
     *
     * @param startingPosition
     */
    public void addStartingPosition(int startingPosition)
    {
        this.startingPositions.add(startingPosition);
    }

    public ArrayList<Integer> getLengths()
    {
        return this.lengths;
    }

    public void addTextView(TextView tv)
    {
        this.textViews.add(tv);
    }
}
