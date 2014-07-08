package tutor.cesh.profile.classes;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.widget.EditText;
import android.widget.TextView;

import tutor.cesh.AbstractStudent;
import tutor.cesh.profile.BubbleTextView;

/**
 * Created by michaelk18 on 7/8/14.
 */
public class ClassesUtility
{
    private AbstractStudent student;
    private EditText        textBox;
    private Context         context;

    public ClassesUtility(AbstractStudent student, EditText textBox, Context context)
    {
        this.student = student;
        this.textBox = textBox;
        this.context = context;
    }

    public ClassesUtility(AbstractStudent student, EditText textBox)
    {
        this.student = student;
        this.textBox = textBox;
        this.context = null;
    }

    public String [] formatClassesFrontEnd(String classes)
    {
        System.out.println(classes);
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
               System.out.println("Expected...Exception in cname");
               e.printStackTrace();
        }

        this.student.setClasses(classesFromServer);
        return classesFromServer;
    }

    public String formatClassesBackend()
    {
        String              courseName, jsonArray;
        String              classesEntered;
        String []           classesEnteredArray;

        classesEntered = this.textBox.getText().toString();
        System.out.println("----" + classesEntered);
        jsonArray = "";

        /* Format classes entered by the user correctly for the server to handle */
        //As long as the user entered in the classes he/she is taking
        if (classesEntered.length() > 0)
        {
            //convert to a valid JSON Array to send up to the server
            classesEnteredArray = classesEntered.split("\\s+");
            this.student.setClasses(classesEnteredArray);
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
        classes = this.student.getClasses();

        this.textBox.setText("");
        for(int i =0; i < classes.length; i++)
        {
            this.textBox.append(classes[i] + " ");
        }
    }
    public void setClassesRegularMode()
    {
        String [] classes;
        BubbleTextView bubbleTextView;
        SpannableStringBuilder sb;

        this.textBox.setText("");
        classes = student.getClasses();

        if(classes != null)
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
