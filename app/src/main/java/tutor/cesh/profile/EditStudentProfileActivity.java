package tutor.cesh.profile;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import java.io.ByteArrayOutputStream;

import tutor.cesh.R;
import tutor.cesh.Student;
import tutor.cesh.User;
import tutor.cesh.database.DatabaseFactory;
import tutor.cesh.profile.classes.ClassesUtility;
import tutor.cesh.profile.classes.StudentClassesUtility;
import tutor.cesh.rest.BackgroundImageTaskDelegate;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.TaskDelegate;
import tutor.cesh.rest.http.EnrollHttpObject;
import tutor.cesh.rest.http.HttpObject;
import tutor.cesh.rest.http.StudentCourseHttpObject;
import tutor.cesh.rest.http.StudentHttpObject;
import tutor.cesh.rest.http.TutorHttpObject;
import tutor.cesh.sampled.statik.Cropper;

public class EditStudentProfileActivity extends Activity
{
    private static String                           profileImagePath, coverImagePath;
    private Bundle                                  info;
    private EditText                                name, major, year, about,   classes;
    private ImageView                               profileImageView, coverImageView;
    private static GenericTextWatcher               textWatcher;

    /**
     *
     */
    private void doNotSaveUpdatedInfo()
    {
        int             localBackgroundSize;
        ImageController imageController;

        imageController     = ImageController.getInstance();
        localBackgroundSize = imageController.size(ImageLocation.BACKGROUND);

        for(int i = 0; i < localBackgroundSize - 1; i++)
            imageController.pop(ImageLocation.BACKGROUND);
    }
    /**
     *
     * @param inContext
     * @param inImage
     * @return
     */
    public Uri getImageUri(Context inContext, Bitmap inImage)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    /**
     *
     * @param context
     * @param photoUri
     * @return
     */
    private int getOrientation(Context context, Uri photoUri)
    {
        Cursor cursor;
        cursor  = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1)
            return -1;

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

    private String getRealPathFromURI(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /**
     * Initialize main parts of the UI
     *
     */
    private void initializeUI()
    {
        name                    = (EditText)    findViewById(R.id.name);
        major                   = (EditText)    findViewById(R.id.major);
        year                    = (EditText)    findViewById(R.id.year);
        about                   = (EditText)    findViewById(R.id.about);
        classes                 = (EditText)    findViewById(R.id.classes);

        profileImageView        = (ImageView)   findViewById(R.id.profileImage);
        coverImageView          = (ImageView)   findViewById(R.id.profileBackgroundImage);

        textWatcher             = new GenericTextWatcher (getApplicationContext(), classes);
        classes.addTextChangedListener(textWatcher);
    }

    /**
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        TaskDelegate taskDelegate;
        Cropper cropper;
        Bundle extras;

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2)
        {
            if (data != null) {
                updateBackgroundImage(data);
            }
        }
        else
        {
            /*Bitmap croppedImage;
            String tmp;
            extras = data.getExtras();
            croppedImage = extras.getParcelable("data");
            taskDelegate = new ProfileImageTaskDelegate(StudentProfileActivity.profileImageSubject);
            //tmp = getRealPathFromURI(this, Uri.parse(profileImagePath));
            //profileImagePath = tmp;
            taskDelegate.taskCompletionResult(croppedImage, false);*/
        }

        this.classes.addTextChangedListener(textWatcher);
    }

    @Override
    public void onBackPressed()
    {
        doNotSaveUpdatedInfo();
        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_profile);

        this.info       = getIntent().getExtras();
        initializeUI();
        setUpUserData();
        setUpRelationships();
        setUpUserClasses();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile_student, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int position, id;

        id          = item.getItemId();
        position    = -1;

        if (id == R.id.action_settings)
            return true;

        else if(id == R.id.action_switch_profile)
        {
            doNotSaveUpdatedInfo();
            position = 1;
        }

        onOptionsItemSelected(position);
        return super.onOptionsItemSelected(item);

    }

    /**
     * Responding to clicks from the Action Bar and from
     * the drawer layout
     *
     * @param position The position in the listViewTitles array
     */
    private void onOptionsItemSelected(int position)
    {
        Intent  intent;

        if(position == 1)
        {
            //drawerLayout.closeDrawer(listView);
            intent = new Intent(this, TutorProfileActivity.class);
            intent.putExtras(info);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
    }

    @Override
    public void onRestart()
    {
        super.onRestart();
    }

    /**
     *
     * @param view
     */
    public void onUserClick(View view)
    {
        Intent intent;

        switch(view.getId())
        {
            case R.id.profileImage:
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;

            case R.id.profileBackgroundImageButton:
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2);
                break;

            case R.id.saveButton:
                saveUserProfile(view);
                intent = new Intent();
                intent.putExtras(info);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    private void resetUserImages()
    {
        Bitmap          changedBackgroundImage;
        ImageController imageController;

        imageController = ImageController.getInstance();

        /* Reset Profile and Cover Images */
        changedBackgroundImage  = imageController.pop(ImageLocation.BACKGROUND);
        imageController.clear(ImageLocation.BACKGROUND);
        imageController.push(changedBackgroundImage, ImageLocation.BACKGROUND);
    }
    /**
     * Save user data to the server
     * @param view
     */
    private void saveUserProfile(View view)
    {

        HttpPost            post;
        HttpPut             put;
        String              jsonArray;
        HttpObject          enroll, course, tutor;
        StudentHttpObject   student;
        ClassesUtility      cUtility;
        User                user;

        user = User.getInstance();
        resetUserImages();
        cUtility    = new StudentClassesUtility(user, this.classes);
        jsonArray   = cUtility.formatClassesBackend();

        //update info bundle
        info.putString("firstName", name.getText().toString());
        info.putString("about", about.getText().toString());
        info.putString("year", year.getText().toString());
        info.putString("major", major.getText().toString());
        DatabaseFactory.updateObjects(info);

        //Send a put request with the user's data up to the server
        try
        {
            student     = new StudentHttpObject(user);
            student.setCoverImagePath(coverImagePath);
            student.setProfileImagePath(profileImagePath);

            enroll      = new EnrollHttpObject(user);
            tutor       = new TutorHttpObject(user);
            course      = new StudentCourseHttpObject(user, jsonArray);

            post = course.post();
            new RestClientExecute(post).start();

            put = student.put();
            new RestClientExecute(put).start();

            put = enroll.put();
            new RestClientExecute(put).start();

            put = tutor.put();
            if(put != null) new RestClientExecute(put).start();

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void setUpUserClasses()
    {
        ClassesUtility  cUtility;
        User user;
        Student student;

        user    = User.getInstance();
        student = user.getStudent();

        if(student.getClasses().length > 0)
        {
            cUtility    = new StudentClassesUtility(user, this.classes, this);
            cUtility.setClassesEditMode();
        }

    }

    /**
     *
     */
    private void setUpUserData()
    {
        ImageController imageController;
        User            user;
        Student         student;

        imageController = ImageController.getInstance();

        user = User.getInstance();
        student = user.getStudent();

        //set fields based on data from the bundle
        name.setText(student.getName());
        major.setText(student.getMajor());
        year.setText(student.getYear());
        about.setText(student.getAbout());
        coverImageView.setBackground(new BitmapDrawable(getResources(), imageController.peek(ImageLocation.BACKGROUND)));
    }
    private void setUpRelationships()
    {
        new ImageDrawableObserver(coverImageView, StudentProfileActivity.coverImageSubject, getResources());
    }

    /**
     *
     * @param data
     */
    private void updateBackgroundImage(Intent data)
    {
        TaskDelegate    taskDelegate;
        Bitmap          bitmap;
        int             orientation;
        Matrix          m;

        bitmap          = null;

        try
        {
            coverImagePath = data.getData().getPath();

            if (coverImagePath != null)
            {
                orientation     = getOrientation(this, data.getData());
                bitmap          = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()));

                if (orientation > 0)
                {
                    m = new Matrix();
                    m.postRotate(orientation);
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), m, true);
                }

                Uri uri = getImageUri(this, bitmap);
                coverImagePath = getRealPathFromURI(uri);
            }

            taskDelegate        = new BackgroundImageTaskDelegate(getResources(), StudentProfileActivity.coverImageSubject);
            taskDelegate.taskCompletionResult(bitmap, false);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
