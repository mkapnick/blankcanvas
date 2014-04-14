package tutor.cesh.profile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.methods.HttpPut;
import org.json.JSONArray;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import tutor.cesh.R;
import tutor.cesh.rest.BackgroundImageTaskDelegate;
import tutor.cesh.rest.ProfileImageTaskDelegate;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.rest.TaskDelegate;
import tutor.cesh.sampled.statik.AsyncConvolution;
import tutor.cesh.sampled.statik.AsyncProfileImage;
import tutor.cesh.sampled.statik.BitmapOpFactory;
import tutor.cesh.sampled.statik.BlurredImageLifeCycle;
import tutor.cesh.sampled.statik.ConvolveOp;
import tutor.cesh.sampled.statik.GrayExceptOp;
import tutor.cesh.sampled.statik.LocalColors;
import tutor.cesh.sampled.statik.Posterizer;

public class EditStudentProfileActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener
{
    private String                                  profileImagePath, coverImagePath;
    private Bundle                                  info;
    private EditText                                name, major, year, about,   classes;
    private ImageView                               profileImageView, coverImageView;
    private SeekBar                                 seekBar;
    private Button                                  blackAndWhite, exceptOrange, exceptBlue,
                                                    exceptGreen, exceptYellow,exceptRed;
    private static GenericTextWatcher               textWatcher;
    private static final int                        CONVULTION_KERNEL_SIZE = 3;
    private static final String                     BLACK_AND_WHITE = "Black and White Cover Image";
    private static final String                     BLACK_AND_WHITE_EXCEPT = "Black and White Except";

    public  static Bitmap                           profileImageBitmap, backgroundImageBitmap;
    public  static BlurredImageLifeCycle            blurredImageLifeCycle;


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

        name                    = (EditText)    findViewById(R.id.name);
        major                   = (EditText)    findViewById(R.id.major);
        year                    = (EditText)    findViewById(R.id.year);
        about                   = (EditText)    findViewById(R.id.about);
        classes                 = (EditText)    findViewById(R.id.classes);

        profileImageView        = (ImageView)   findViewById(R.id.profileImage);
        coverImageView          = (ImageView)   findViewById(R.id.profileBackgroundImage);

        blackAndWhite           = (Button)      findViewById(R.id.blackAndWhite);
        exceptOrange            = (Button)      findViewById(R.id.exceptOrange);
        exceptBlue              = (Button)      findViewById(R.id.exceptBlue);
        exceptGreen             = (Button)      findViewById(R.id.exceptGreen);
        exceptYellow            = (Button)      findViewById(R.id.exceptYellow);
        exceptRed               = (Button)      findViewById(R.id.exceptRed);


        blackAndWhite.setOnClickListener(this);
        exceptOrange.setOnClickListener(this);
        exceptBlue.setOnClickListener(this);
        exceptGreen.setOnClickListener(this);
        exceptYellow.setOnClickListener(this);
        exceptRed.setOnClickListener(this);

        seekBar                 = (SeekBar)     findViewById(R.id.seekBar);
        seekBar.setProgress(0);
        seekBar.setMax(100);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setOnSeekBarChangeListener(this);

        textWatcher             = new GenericTextWatcher (getApplicationContext(), classes);
        classes.addTextChangedListener(textWatcher);
    }

    /**
     *
     */
    private void setUpUserData()
    {
        //set fields based on data from the bundle
        EditStudentProfileActivity.blurredImageLifeCycle = new BlurredImageLifeCycle();
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
     * @param bitmap
     */
    public static void setUpBlurredBackgroundImages(Bitmap bitmap)
    {

        blurredImageLifeCycle.add(bitmap);
        ConvolveOp                  op;
        BackgroundImageTaskDelegate bt;
        AsyncConvolution            blur;

        op = BitmapOpFactory.createBlurOp(3);
        op.setDivider(1.5);

        bt      = new BackgroundImageTaskDelegate();
        blur    = new AsyncConvolution(op, bt);
        blur.execute(bitmap);
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
                    asyncConvolution    = new AsyncConvolution(new ProgressDialog(this), convolveOp, taskDelegate);
                    try {
                        seekBar.setVisibility(View.VISIBLE);
                        seekBar.setProgress(0);
                        blurredImageLifeCycle.reset();
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

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        List<Bitmap> blurredImages;
        Drawable            drawable;

        blurredImages   = blurredImageLifeCycle.getList();
        drawable        = null;
        if(progress >= 0 && progress <= 24)
        {
            if(blurredImages.size() > 0) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(0));
                EditStudentProfileActivity.backgroundImageBitmap = blurredImages.get(0);
            }
        }
        else if(progress >= 25 && progress <= 49)
        {
            if(blurredImages.size() > 1) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(1));
                EditStudentProfileActivity.backgroundImageBitmap = blurredImages.get(1);
            }
        }
        else if (progress >= 50 && progress <= 74)
        {
            if(blurredImages.size() > 2) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(2));
                EditStudentProfileActivity.backgroundImageBitmap = blurredImages.get(2);
            }
        }
        else if (progress >= 75 && progress <= 90)
        {
            if(blurredImages.size() > 3) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(3));
                EditStudentProfileActivity.backgroundImageBitmap = blurredImages.get(3);
            }
        }
        else
        {
            if(blurredImages.size() > 4) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(4));
                EditStudentProfileActivity.backgroundImageBitmap = blurredImages.get(4);
            }
        }

        if(drawable != null)
            coverImageView.setBackground(drawable);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        //nothing
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        //nothing
    }

    @Override
    public void onClick(View v)
    {
        Posterizer      posterizer;
        GrayExceptOp    greyExceptOp;
        Bitmap          newBitmap;
        Drawable        drawable;

        newBitmap = null;
        switch(v.getId())
        {
            case R.id.blackAndWhite:
                posterizer  = new Posterizer();
                newBitmap   = posterizer.toBlackAndWhite(EditStudentProfileActivity.backgroundImageBitmap);
                break;
            case R.id.exceptOrange:
                greyExceptOp    = new GrayExceptOp(LocalColors.ORANGE.getRgb());
                newBitmap       = greyExceptOp.filter(EditStudentProfileActivity.backgroundImageBitmap, null);
                break;
            case R.id.exceptPurple:
                greyExceptOp    = new GrayExceptOp(LocalColors.PURPLE.getRgb());
                newBitmap       = greyExceptOp.filter(EditStudentProfileActivity.backgroundImageBitmap, null);
                break;
            case R.id.exceptBlue:
                greyExceptOp    = new GrayExceptOp(LocalColors.BLUE.getRgb());
                newBitmap       = greyExceptOp.filter(EditStudentProfileActivity.backgroundImageBitmap, null);
                break;
            case R.id.exceptGreen:
                greyExceptOp    = new GrayExceptOp(LocalColors.GREEN.getRgb());
                newBitmap       = greyExceptOp.filter(EditStudentProfileActivity.backgroundImageBitmap, null);
                break;
            case R.id.exceptYellow:
                greyExceptOp    = new GrayExceptOp(LocalColors.YELLOW.getRgb());
                newBitmap       = greyExceptOp.filter(EditStudentProfileActivity.backgroundImageBitmap, null);
                break;
            case R.id.exceptRed:
                greyExceptOp    = new GrayExceptOp(LocalColors.RED.getRgb());
                newBitmap       = greyExceptOp.filter(EditStudentProfileActivity.backgroundImageBitmap, null);
                break;
        }

        if(newBitmap!=null)
        {
            drawable    = new BitmapDrawable(getResources(), newBitmap);
            coverImageView.setBackground(drawable);
            EditStudentProfileActivity.backgroundImageBitmap = newBitmap;
        }
    }
}
