package tutor.cesh.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import org.apache.http.client.methods.HttpGet;

import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.rest.asynchronous.AsyncDownloader;
import tutor.cesh.rest.asynchronous.AsyncGet;
import tutor.cesh.rest.handler.CoverImageHandler;
import tutor.cesh.rest.handler.ImageHandler;
import tutor.cesh.rest.delegate.TaskDelegate;
import tutor.cesh.rest.http.EnrollHttpObject;
import tutor.cesh.rest.http.StudentCourseHttpObject;
import tutor.cesh.rest.http.TutorCourseHttpObject;
import tutor.cesh.rest.http.TutorHttpObject;

/**
 * Created by michaelk18 on 10/18/14.
 */
public class GlobalDatabaseHelper {

    private static Context context;
    private static ProgressDialog pd;

    /**
     *
     * @param c
     */
    public GlobalDatabaseHelper(Context c)
    {
        this.context = c;
        pd           = new ProgressDialog(context);
        pd.setTitle("Downloading...");
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
    }

    /**
     *
     * @param resources
     * @param imageView
     */
    public void downloadStudentCoverImageFromServer(Resources resources, ImageView imageView)
    {
        User            user;
        Student         student;
        ImageHandler handler;
        AsyncDownloader asyncDownloader;

        user    = User.getInstance(context);
        student = user.getStudent();

        // Download cover image from server, belongs to student
        handler             = new CoverImageHandler(resources,  imageView, context);
        asyncDownloader     = new AsyncDownloader(student.getCoverImageUrl(), handler,
                                                    student, new ProgressDialog(context));
        asyncDownloader.execute();
    }

    /**
     *
     * @param resources
     * @param imageView
     */
    public void downloadTutorCoverImageFromServer(Resources resources, ImageView imageView)
    {
        User            user;
        Tutor           tutor;
        ImageHandler    handler;
        AsyncDownloader asyncDownloader;

        user    = User.getInstance(context);
        tutor   = user.getTutor();

        // Download cover image from server, belongs to tutor
        handler             = new CoverImageHandler(resources,  imageView, context);
        asyncDownloader     = new AsyncDownloader(tutor.getCoverImageUrl(), handler,
                tutor, new ProgressDialog(context));
        asyncDownloader.execute();
    }























    /**
     *
     * @param delegate
     */
    public void downloadStudentDataFromServer(TaskDelegate delegate)
    {
        /*HttpGet get;
        User user;

        user = User.getInstance(context);

        try
        {
             Data that needs to be queried from the server

            get = new StudentCourseHttpObject(user).get();
            new AsyncGet(context, delegate, pd).execute(get);
        }
        catch(Exception e)
        {

        }*/
    }

    /**
     *
     * @param delegate
     */
    public void downloadTutorDataFromServer(TaskDelegate delegate)
    {
        /*HttpGet get;
        User user;

        user = User.getInstance(context);

        try
        {
            get = new TutorHttpObject(user).get();
            new AsyncGet(context, delegate, pd).execute(get);

            get = new TutorCourseHttpObject(user).get();
            new AsyncGet(context, delegate, pd).execute(get);
        }
        catch(Exception e){

        }*/
    }


}
