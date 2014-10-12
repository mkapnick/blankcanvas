package tutor.cesh.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import tutor.cesh.R;
import tutor.cesh.profile.util.classes.FormatClassesUtility;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.CoverImageHandler;
import tutor.cesh.rest.ImageHandler;

public class ReadOnlyTutorProfileActivity extends Activity {

    private String      rate, rating, firstName,
                        coverImagePath, major,minor,year, about;

    private TextView    nameTextView, majorTextView,
                        yearTextView, aboutTextView,
                        classesTextView, rateTextView;

    private ImageView   profileImageView, coverImageView;

    public static final String ID             = "id"; // parent node
    public static final String FIRST_NAME     = "firstName";
    public static final String COVER_IMAGE    = "coverImage";
    public static final String RATE           = "rate";
    public static final String RATING         = "rating";
    public static final String MAJOR          = "major";
    public static final String MINOR          = "minor";
    public static final String YEAR           = "year";
    public static final String TUTOR_COURSES  = "tutorCourses";
    public static final String ABOUT          = "about";

    /**
     *
     */
    private void initializeUI()
    {
        nameTextView                = (TextView)    this.findViewById(R.id.name);
        majorTextView               = (TextView)    this.findViewById(R.id.major);
        yearTextView                = (TextView)    this.findViewById(R.id.year);
        aboutTextView               = (TextView)    this.findViewById(R.id.about);
        classesTextView             = (TextView)    this.findViewById(R.id.classes);
        rateTextView                = (TextView)    this.findViewById(R.id.rate);
        profileImageView            = (ImageView)   findViewById(R.id.profileImage);
        coverImageView              = (ImageView)   findViewById(R.id.profileBackgroundImage);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_only_tutor_profile);

        setUpInstanceVariables();
        initializeUI();
        setUpUserInfo();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.read_only_tutor_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     *
     */
    private void setUpInstanceVariables()
    {
        Bundle bundle;
        bundle = getIntent().getExtras();

        this.rate           = bundle.getString(RATE);
        this.rating         = bundle.getString(RATING);
        this.firstName      = bundle.getString(FIRST_NAME);
        this.coverImagePath = bundle.getString(COVER_IMAGE);
        this.major          = bundle.getString(MAJOR);
        this.minor          = bundle.getString(MINOR);
        this.year           = bundle.getString(YEAR);
        this.about          = bundle.getString(ABOUT);
    }

    /**
     *
     */
    private void setUpUserInfo()
    {
        AsyncDownloader asyncDownloader;
        ImageHandler    coverImageHandler;
        String []       tutorCourses;
        Bundle          bundle;

        bundle              = getIntent().getExtras();

        coverImageHandler   = new CoverImageHandler(getResources(), this.coverImageView, this);
        asyncDownloader     = new AsyncDownloader(this.coverImagePath, coverImageHandler, null, new ProgressDialog(this));
        asyncDownloader.execute(); //TODO find a way to cache the bitmap

        nameTextView.setText(this.firstName);
        majorTextView.setText(this.major);
        yearTextView.setText(this.year);
        aboutTextView.setText(this.about);
        rateTextView.setText(this.rate);

        tutorCourses        = bundle.getString(TUTOR_COURSES).split(",");
        FormatClassesUtility.setClassesRegularMode(tutorCourses, this, this.classesTextView);
    }
}
