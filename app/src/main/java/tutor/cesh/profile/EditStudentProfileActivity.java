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
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPut;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import tutor.cesh.R;
import tutor.cesh.rest.BackgroundImageTaskDelegate;
import tutor.cesh.rest.ProfileImageTaskDelegate;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.rest.TaskDelegate;
import tutor.cesh.sampled.statik.AsyncConvolution;
import tutor.cesh.sampled.statik.AsyncProfileImage;
import tutor.cesh.sampled.statik.BitmapOpFactory;
import tutor.cesh.sampled.statik.ConvolveOp;

public class EditStudentProfileActivity extends Activity
{
    private String                          profileImagePath, coverImagePath;
    private Bundle                          info;
    private EditText                        name, major, year, about,   classes;
    private ImageView                       profileImageView, coverImageView;
    private static GenericTextWatcher       textWatcher;
    private static final int                CONVULTION_KERNEL_SIZE = 7;
    public  static Bitmap                   profileImageBitmap, backgroundImageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_profile);

        this.info       = getIntent().getExtras();
        initializeUI();
        setUpUserData();
        setUpUserClasses();
    }


    /**
     * Initialize main parts of the UI
     *
     */
    private void initializeUI()
    {
        name                = (EditText)    findViewById(R.id.name);
        major               = (EditText)    findViewById(R.id.major);
        year                = (EditText)    findViewById(R.id.year);
        about               = (EditText)    findViewById(R.id.about);
        classes             = (EditText)    findViewById(R.id.classes);

        profileImageView    = (ImageView)   findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   findViewById(R.id.profileBackgroundImage);

        textWatcher         = new GenericTextWatcher (getApplicationContext(), classes);
        classes.addTextChangedListener(textWatcher);
    }

    /**
     *
     */
    private void setUpUserData()
    {
        //set fields based on data from the bundle
        name.setText(info.getString("firstName"));
        major.setText(info.getString("major"));
        year.setText(info.getString("year"));
        about.setText(info.getString("about"));
        profileImageView.setImageBitmap(profileImageBitmap);
        coverImageView.setBackground(new BitmapDrawable(getResources(), backgroundImageBitmap));
    }

    /**
     *
     */
    private void setUpUserClasses()
    {
        TextFieldHelper                     classesTextFieldHelper;
        String []                           classesFromBundle;

        classesTextFieldHelper = new ClassesTextFieldHelper(this);
        classesFromBundle = info.getString("classes").split(",");

        classesTextFieldHelper.help(this.classes, this.textWatcher, classesFromBundle, true);
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
        Bitmap                  bitmap;
        BitmapFactory.Options   options;
        ConvolveOp              convolveOp;
        AsyncConvolution        asyncConvolution;
        AsyncProfileImage       asyncProfileImage;
        int                     orientation;
        Matrix                  m;
        TaskDelegate            taskDelegate;


        super.onActivityResult(requestCode, resultCode, data);

        try
        {
            /******* PROFILE IMAGE CHANGE ***********************************************************/
            if (requestCode == 1)
            {
                taskDelegate        = new ProfileImageTaskDelegate(profileImageView);
                profileImagePath    = data.getData().getPath();

                if(profileImagePath != null)
                {
                    profileImagePath            = getRealPathFromURI(this, data.getData());
                    orientation                 = getOrientation(this, data.getData());

                    options                     = new BitmapFactory.Options();
                    options.inSampleSize        = 20;
                    bitmap                      = BitmapFactory.decodeStream(
                                                    getContentResolver().openInputStream
                                                            (data.getData()),
                                                                        null, options);
                    if(orientation > 0)
                    {
                        m = new Matrix();
                        m.postRotate(orientation);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), m, true);
                    }

                    asyncProfileImage              = new AsyncProfileImage(this, taskDelegate);
                    asyncProfileImage.execute(bitmap);
                }
            }
            /******* BACKGROUND IMAGE CHANGE ***********************************************************/
            else
            {
                taskDelegate        = new BackgroundImageTaskDelegate(getResources(), coverImageView);
                convolveOp          = BitmapOpFactory.createBlurOp(CONVULTION_KERNEL_SIZE);
                convolveOp.setDivider(1.5);
                coverImagePath      = data.getData().getPath();

                if(coverImagePath != null)
                {
                    coverImagePath              = getRealPathFromURI(this, data.getData());
                    orientation                 = getOrientation(this, data.getData());
                    options                     = new BitmapFactory.Options();
                    options.inSampleSize        = 20;
                    bitmap                      = BitmapFactory.decodeStream(
                                                    getContentResolver().openInputStream
                                                                        (data.getData()),
                                                                        null, options);
                    if(orientation > 0)
                    {
                        m = new Matrix();
                        m.postRotate(orientation);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), m, true);
                    }

                    //blur the background image
                    asyncConvolution    = new AsyncConvolution(this, convolveOp, taskDelegate);
                    try {
                        asyncConvolution.execute(bitmap);
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        catch(IOException io)
        {
            io.printStackTrace();
        }
        this.classes.addTextChangedListener(textWatcher);
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

            case R.id.profileBackgroundImage:
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                this.classes.removeTextChangedListener(textWatcher);
                startActivityForResult(intent, 2);
                break;

            case R.id.saveButton:
                saveUserProfile(view);
                intent = new Intent();
                intent.putExtras(info);
                setResult(RESULT_OK, intent);
                profileImageBitmap      = null;
                backgroundImageBitmap   = null;
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
        ArrayList<HttpPut>  puts;
        ArrayList<TextView> tvClasses;
        String              id, enrollId, courseName, jsonArray;
        JSONArray           classesAsStudent;

        id                  = info.getString("id");
        enrollId            = info.getString("enrollId");
        tvClasses           = textWatcher.getTextViews();
        jsonArray           = null;
        classesAsStudent    = null;

        //As long as the user entered in the classes he/she is taking
        if (tvClasses.size() > 0)
        {
            //convert to a valid JSON Array to send up to the server
            jsonArray      = "[";
            for (int i = 0; i < tvClasses.size(); i++)
            {
                courseName = tvClasses.get(i).getText().toString();
                if (i != tvClasses.size() - 1)
                    jsonArray += "{\"name\": " + "\"" + courseName + "\"" + "},";
                else
                    jsonArray += "{\"name\": " + "\"" + courseName + "\""+ "}";
            }
            jsonArray += "]";
        }

        //Send a put request with the user's data up to the server
        try
        {
            if (jsonArray != null)
                classesAsStudent = new JSONArray(jsonArray);

            puts        = RestClientFactory.put(id, enrollId, coverImagePath, profileImagePath,
                    name.getText().toString(), major.getText().toString(),
                    year.getText().toString(), about.getText().toString(),
                    classesAsStudent);

            for (int i =0; i < puts.size(); i++)
                new RestClientExecute(puts.get(i)).start();

            info.putString("firstName", name.getText().toString());
            info.putString("about", about.getText().toString());
            info.putString("year", year.getText().toString());
            info.putString("major", major.getText().toString());
            info.putString("classes", jsonArray);

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    public void onBackPressed()
    {
        profileImageBitmap      = null;
        backgroundImageBitmap   = null;

        super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
            return true;
        return super.onOptionsItemSelected(item);
    }

    /**
     *
     * @param context
     * @param contentUri
     * @return
     */
    private String getRealPathFromURI(Context context, Uri contentUri)
    {
        Cursor  cursor;
        int     columnIndex;
        cursor = null;
        try
        {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri,proj, null, null, null);
            columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(columnIndex);
        }
        finally
        {
            if (cursor != null)
                cursor.close();
        }
    }

    /**
     *
     * @param context
     * @param photoUri
     * @return
     */
    private  int getOrientation(Context context, Uri photoUri)
    {
        Cursor cursor;
        cursor  = context.getContentResolver().query(photoUri,
                new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);

        if (cursor.getCount() != 1)
            return -1;

        cursor.moveToFirst();
        return cursor.getInt(0);
    }

}
