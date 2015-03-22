package tutor.cesh.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;

import tutor.cesh.Student;
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.rest.asynchronous.AsyncDownloader;
import tutor.cesh.rest.handler.CoverImageHandler;
import tutor.cesh.rest.handler.ImageHandler;
import tutor.cesh.rest.delegate.TaskDelegate;

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
}
