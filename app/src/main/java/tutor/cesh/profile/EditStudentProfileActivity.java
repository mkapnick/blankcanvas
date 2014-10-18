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
import tutor.cesh.profile.util.classes.ClassesUtility;
import tutor.cesh.profile.util.classes.StudentClassesUtility;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.http.EnrollHttpObject;
import tutor.cesh.rest.http.HttpObject;
import tutor.cesh.rest.http.StudentCourseHttpObject;
import tutor.cesh.rest.http.StudentHttpObject;

public class EditStudentProfileActivity extends Activity
{
    private static String                           profileImagePath, coverImagePath;
    private Bundle                                  info;
    private EditText                                name, major, year, about,   classes;
    private ImageView                               profileImageView, coverImageView;

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_profile);

        this.info       = getIntent().getExtras();
        initializeUI();
        setUpUserData();
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

    /**
     * Save user data to the server
     * @param view
     */
    private void saveUserProfile(View view)
    {

        HttpPost            post;
        HttpPut             put;
        String              jsonArray;
        HttpObject          enroll, course;
        StudentHttpObject   studentHttp;
        Student             student;
        ClassesUtility      cUtility;
        User                user;

        user    = User.getInstance(this);
        student = user.getStudent();

        cUtility    = new StudentClassesUtility(user, this.classes);
        jsonArray   = cUtility.formatClassesBackend();

        //update student attributes
        info.putString("firstName", name.getText().toString());
        student.setName(name.getText().toString());

        info.putString("about", about.getText().toString());
        student.setAbout(about.getText().toString());

        info.putString("year", year.getText().toString());
        student.setYear(year.getText().toString());

        info.putString("major", major.getText().toString());
        student.setMajor(major.getText().toString());

        //Send a put request with the user's data up to the server
        try
        {
            studentHttp     = new StudentHttpObject(user);
            studentHttp.setCoverImagePath(coverImagePath);
            studentHttp.setProfileImagePath(profileImagePath);

            enroll      = new EnrollHttpObject(user);
            course      = new StudentCourseHttpObject(user, jsonArray);

            post = course.post();
            new RestClientExecute(post).start();

            put = studentHttp.put();
            new RestClientExecute(put).start();

            put = enroll.put();
            new RestClientExecute(put).start();

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

        user    = User.getInstance(this);

        cUtility    = new StudentClassesUtility(user, this.classes, this);
        cUtility.setClassesEditMode();
    }

    /**
     *
     */
    private void setUpUserData()
    {
        User            user;
        Student         student;

        user    = User.getInstance(this);
        student = user.getStudent();

        //set fields based on data from the bundle
        name.setText(student.getName());
        major.setText(student.getMajor());
        year.setText(student.getYear());
        about.setText(student.getAbout());
        coverImageView.setBackground(new BitmapDrawable(getResources(), student.getCoverImage()));
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
