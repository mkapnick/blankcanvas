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
import tutor.cesh.Tutor;
import tutor.cesh.User;
import tutor.cesh.profile.classes.ClassesUtility;
import tutor.cesh.profile.classes.TutorClassesUtility;
import tutor.cesh.rest.AsyncDownloader;
import tutor.cesh.rest.CoverImageHandler;
import tutor.cesh.rest.ImageHandler;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.http.EnrollHttpObject;
import tutor.cesh.rest.http.HttpObject;
import tutor.cesh.rest.http.StudentHttpObject;
import tutor.cesh.rest.http.TutorCourseHttpObject;
import tutor.cesh.rest.http.TutorHttpObject;

public class EditTutorProfileActivity extends Activity
{

    private static String                   profileImagePath,  coverImagePath;
    private Bundle                          info;
    private EditText                        name, major, year, about, rate, tutorClasses;
    private ImageView                       profileImageView, coverImageView;

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

    /**
     *
     * @param uri
     * @return
     */
    private String getRealPathFromURI(Uri uri)
    {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    /**
     *
     */
    private void initializeUI()
    {
        name                = (EditText)    findViewById(R.id.name);
        major               = (EditText)    findViewById(R.id.major);
        year                = (EditText)    findViewById(R.id.year);
        about               = (EditText)    findViewById(R.id.about);
        rate                = (EditText)    findViewById(R.id.rate);
        tutorClasses        = (EditText)    findViewById(R.id.classes);

        profileImageView    = (ImageView)   findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   findViewById(R.id.profileBackgroundImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2)
        {
            if (data != null)
                updateBackgroundImage(data);
        }
    }
    @Override
    public void onBackPressed()
    {
        //doNotSaveUpdatedInfo();
        super.onBackPressed();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_tutor_profile);

        this.info = getIntent().getExtras();
        initializeUI();
        setUpUserData();
        setUpRelationships();
        setUpUserClasses();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile_tutor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int position, id;

        id          = item.getItemId();
        position    = -1;

        if (id == R.id.action_settings)
            return true;

        else if(id == R.id.action_switch_profile)
            position = 1;


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
            intent = new Intent(this, StudentProfileActivity.class);
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
        /*Bitmap          changedBackgroundImage;
        ImageController imageController;

        imageController = ImageController.getInstance();

        /* Reset Profile and Cover Images
        changedBackgroundImage  = imageController.pop(ImageLocation.BACKGROUND);
        imageController.clear(ImageLocation.BACKGROUND);
        imageController.push(changedBackgroundImage, ImageLocation.BACKGROUND);
        */
    }

    /**
     * Save user data to the server
     * @param view
     */
    private void saveUserProfile(View view)
    {
        HttpPost post;
        HttpPut             put;
        String              jsonArray;
        HttpObject          enroll, course;
        TutorHttpObject     tutorHttp;
        StudentHttpObject   studentHttp;
        ClassesUtility      cUtility;
        User                user;
        Student             student;
        Tutor               tutor;
        ImageHandler        handler;
        AsyncDownloader     asyncDownloader;

        user    = User.getInstance();
        student = user.getStudent();
        tutor   = user.getTutor();
        handler = new CoverImageHandler(getResources(), this.coverImageView);

        resetUserImages();
        cUtility    = new TutorClassesUtility(user, this.tutorClasses);
        jsonArray   = cUtility.formatClassesBackend();

        //update student and tutor attributes
        info.putString("firstName", name.getText().toString());
        student.setName(name.getText().toString());

        info.putString("tutorabout", about.getText().toString());
        tutor.setAbout(about.getText().toString());

        info.putString("year", year.getText().toString());
        student.setYear(year.getText().toString());

        info.putString("major", major.getText().toString());
        student.setMajor(major.getText().toString());

        info.putString("rate", rate.getText().toString());
        tutor.setRate(rate.getText().toString());

        //Send a put request with the user's data up to the server
        try
        {
            studentHttp     = new StudentHttpObject(user);
            enroll          = new EnrollHttpObject(user);

            tutorHttp       = new TutorHttpObject(user);
            tutorHttp.setCoverImagePath(coverImagePath);
            tutorHttp.setProfileImagePath(profileImagePath);

            course      = new TutorCourseHttpObject(user, jsonArray);

            post = course.post();
            new RestClientExecute(post).start();

            put = studentHttp.put();
            new RestClientExecute(put).start();

            put = enroll.put();
            new RestClientExecute(put).start();

            put = tutorHttp.put();
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
        user    = User.getInstance();

        cUtility    = new TutorClassesUtility(user, this.tutorClasses, this);
        cUtility.setClassesEditMode();

    }

    /**
     *
     */
    private void setUpUserData()
    {
        User            user;
        Student         student;
        Tutor           tutor;

        user = User.getInstance();
        student = user.getStudent();
        tutor   = user.getTutor();

        //set fields based on data from the bundle
        name.setText(student.getName());
        major.setText(student.getMajor());
        year.setText(student.getYear());
        about.setText(tutor.getAbout());
        rate.setText(tutor.getRate());
        coverImageView.setBackground(new BitmapDrawable(getResources(), tutor.getCoverImage()));
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
        Bitmap          bitmap;
        int             orientation;
        Matrix          m;

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

                coverImageView.setBackground(new BitmapDrawable(getResources(), bitmap));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }
}
