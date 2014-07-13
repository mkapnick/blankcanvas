package tutor.cesh.profile.classes;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;

import tutor.cesh.User;
import tutor.cesh.profile.BubbleTextView;

/**
 * Created by michaelk18 on 7/8/14.
 */
public abstract class ClassesUtility
{
    protected User user;
    protected EditText        textBox;
    protected Context         context;

    public ClassesUtility(User user, EditText textBox, Context context)
    {
        this.user = user;
        this.textBox = textBox;
        this.context = context;
    }

    public ClassesUtility(User user, EditText textBox)
    {
        this.user = user;
        this.textBox = textBox;
        this.context = null;
    }

    public String [] formatClassesFrontEnd(String classes)
    {
        String [] classesFromServer;
        String    course;

        classesFromServer = null;

        try
        {
            classesFromServer = classes.split(",");

            for(int i =0; i < classesFromServer.length; i++)
            {
                course = classesFromServer[i];
                course = course.replaceAll("\\]", "");
                course = course.replaceAll("\\[", "");
                course = course.replaceAll("\"", "");
                classesFromServer[i] = course;
            }
        }
        catch(Exception e)
        {
               e.printStackTrace();
        }

        setClasses(classesFromServer);
        return classesFromServer;
    }

    public abstract void setClasses(String [] classesFromServer);
    public abstract String [] getClasses();

    public String formatClassesBackend()
    {
        String              courseName, jsonArray;
        String              classesEntered;
        String []           classesEnteredArray;

        classesEntered = this.textBox.getText().toString();
        jsonArray = "";

        /* Format classes entered by the user correctly for the server to handle */
        //As long as the user entered in the classes he/she is taking
        if (classesEntered.length() > 0)
        {
            //convert to a valid JSON Array to send up to the server
            classesEnteredArray = classesEntered.split("\\s+");
            setClasses(classesEnteredArray);
            jsonArray      = "[";

            for (int i = 0; i < classesEnteredArray.length; i++)
            {
                courseName = classesEnteredArray[i];
                if (i != classesEnteredArray.length - 1)
                    jsonArray += "{\"name\": " + "\"" + courseName + "\"" + "},";
                else
                    jsonArray += "{\"name\": " + "\"" + courseName + "\""+ "}";
            }
            jsonArray += "]";
        }

        return jsonArray;
    }

    public void setClassesEditMode()
    {
        String [] classes;
        classes = getClasses();

        this.textBox.setText("");

        if(!classes[0].equalsIgnoreCase(""))
        {
            for (int i = 0; i < classes.length; i++) {
                this.textBox.append(classes[i].toUpperCase()     + " ");
            }
        }
    }
    public void setClassesRegularMode()
    {
        String [] classes;
        BubbleTextView bubbleTextView;
        SpannableStringBuilder sb;

        this.textBox.setText("");
        classes = getClasses();

        if(classes.length > 0 && !classes[0].equalsIgnoreCase(""))
        {
            try
            {
                for(String c : classes)
                {
                    bubbleTextView = new BubbleTextView(context, new TextView(context));
                    sb = bubbleTextView.createBubbleOverText(c, false);
                    this.textBox.append(sb);
                    this.textBox.append(" ");
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
