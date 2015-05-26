package tutor.cesh.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.widget.ImageView;
import tutor.cesh.profile.Student;
import tutor.cesh.profile.Tutor;
import tutor.cesh.profile.User;
import tutor.cesh.rest.asynchronous.AsyncDownloader;
import tutor.cesh.rest.handler.CoverImageHandler;
import tutor.cesh.rest.handler.ImageHandler;

/**
 * A helper class for downloading images from a server
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class DownloadImageHelper {

    private static Context          context;
    private static ProgressDialog   pd;

    /**
     * Explicit value constructor
     *
     * @param c The context of an activity
     */
    public DownloadImageHelper(Context c)
    {
        this.context = c;
        pd           = new ProgressDialog(context);
        pd.setTitle("Downloading...");
        pd.setMessage("Please wait");
        pd.setCancelable(false);
        pd.setIndeterminate(true);
    }

    /**
     * Downloads the student cover image from our server
     *
     * @param resources A reference to resources
     * @param imageView A reference to an imageView
     */
    public void downloadStudentCoverImageFromServer(Resources resources, ImageView imageView)
    {
        User            user;
        Student         student;
        ImageHandler    handler;
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
     * Downloads the tutor cover image from our server
     *
     * @param resources A reference to resources
     * @param imageView A reference to an imageView
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
