package tutor.cesh.profile;

import android.app.Activity;
import android.content.ActivityNotFoundException;
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
import android.widget.Toast;

import org.apache.http.client.methods.HttpPut;

import java.util.ArrayList;
import java.util.List;

import tutor.cesh.R;
import tutor.cesh.rest.BackgroundImageTaskDelegate;
import tutor.cesh.rest.ProfileImageTaskDelegate;
import tutor.cesh.rest.RestClientExecute;
import tutor.cesh.rest.RestClientFactory;
import tutor.cesh.rest.TaskDelegate;
import tutor.cesh.sampled.statik.AsyncConvolution;
import tutor.cesh.sampled.statik.BitmapOpFactory;
import tutor.cesh.sampled.statik.BlurredImageLifeCycle;
import tutor.cesh.sampled.statik.ConvolveOp;
import tutor.cesh.sampled.statik.Cropper;

public class EditTutorProfileActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener{

    private static String                   profileImagePath,  coverImagePath;
    private Bundle                          info;
    private EditText                        name, major, year, about, rate;
    private SeekBar                         seekBar;
    private ImageView                       profileImageView, coverImageView;
    private ImageController                 controller;
    private static final int                CONVULTION_KERNEL_SIZE = 3;
    public  static BlurredImageLifeCycle    blurredImageLifeCycle;
    private int                             originalSizeOfProfileStack, originalSizeOfBackgroundStack;
    public  static Bitmap                   currentProfileBitmapFromStack, currentBackgroundBitmapFromStack;
    private Button                          blackAndWhite, exceptOrange, exceptBlue,
                                            exceptGreen, exceptYellow,exceptRed;


    /**
     * Google code, calculate inSampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return
     */
    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight)
    {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
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
     */
    private void initializeUI()
    {
        name                = (EditText)    findViewById(R.id.name);
        major               = (EditText)    findViewById(R.id.major);
        year                = (EditText)    findViewById(R.id.year);
        about               = (EditText)    findViewById(R.id.about);
        rate                = (EditText)    findViewById(R.id.rate);

        profileImageView    = (ImageView)   findViewById(R.id.profileImage);
        coverImageView      = (ImageView)   findViewById(R.id.profileBackgroundImage);

        /*blackAndWhite           = (Button)      findViewById(R.id.blackAndWhite);
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
        seekBar.setOnSeekBarChangeListener(this);*/
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        TaskDelegate            taskDelegate;
        Cropper                 cropper;
        Bundle                  extras;

        super.onActivityResult(requestCode, resultCode, data);


        /******* PROFILE IMAGE CHANGE ***********************************************************/
        if (requestCode == 1)
        {
            if(data != null) {
                cropper = new Cropper();
                try {
                    profileImagePath = data.getData().toString();
                    Intent intent;
                    intent = cropper.performCrop();
                    startActivityForResult(intent, 3);
                } catch (ActivityNotFoundException anfe) {
                    // display an error message
                    String errorMessage = "Your device doesn't support the crop action!";
                    Toast toast = Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        /******* BACKGROUND IMAGE CHANGE ***********************************************************/
        else if(requestCode == 2)
        {
            if(data != null)
            {
                setUpBlurredBackground(data, null);
            }
        }
        else
        {
            Bitmap              croppedImage;
            String              tmp;
            extras              = data.getExtras();
            croppedImage        = extras.getParcelable("data");
            taskDelegate        = new ProfileImageTaskDelegate(StudentProfileActivity.profileImageSubject);
            tmp                 = getRealPathFromURI(this, Uri.parse(profileImagePath));
            profileImagePath    = tmp;
            taskDelegate.taskCompletionResult(croppedImage, false);
        }

    }
    @Override
    public void onBackPressed()
    {
        int localProfileSize, localBackgroundSize, difference;

        currentProfileBitmapFromStack      = null;
        currentBackgroundBitmapFromStack   = null;

        localProfileSize = controller.size(ImageLocation.PROFILE);
        localBackgroundSize = controller.size(ImageLocation.BACKGROUND);

        difference = localProfileSize- originalSizeOfProfileStack;

        for(int i = 0; i < difference; i++)
            controller.pop(ImageLocation.PROFILE);

        difference = localBackgroundSize - originalSizeOfBackgroundStack;

        for(int i =0; i < difference; i++)
            controller.pop(ImageLocation.BACKGROUND);

        super.onBackPressed();
    }

    @Override
    public void onClick(View v)
    {
        /*Posterizer      posterizer;
        GrayExceptOp    greyExceptOp;
        Bitmap          newBitmap;
        Drawable        drawable;

        newBitmap = null;
        switch(v.getId())
        {
            case R.id.blackAndWhite:
                posterizer  = new Posterizer();
                newBitmap   = posterizer.toBlackAndWhite(currentBackgroundBitmapFromStack);
                break;
            case R.id.exceptOrange:
                greyExceptOp    = new GrayExceptOp(LocalColors.ORANGE.getRgb());
                newBitmap       = greyExceptOp.filter(currentBackgroundBitmapFromStack, null);
                break;
            case R.id.exceptPurple:
                greyExceptOp    = new GrayExceptOp(LocalColors.PURPLE.getRgb());
                newBitmap       = greyExceptOp.filter(currentBackgroundBitmapFromStack, null);
                break;
            case R.id.exceptBlue:
                greyExceptOp    = new GrayExceptOp(LocalColors.BLUE.getRgb());
                newBitmap       = greyExceptOp.filter(currentBackgroundBitmapFromStack, null);
                break;
            case R.id.exceptGreen:
                greyExceptOp    = new GrayExceptOp(LocalColors.GREEN.getRgb());
                newBitmap       = greyExceptOp.filter(currentBackgroundBitmapFromStack, null);
                break;
            case R.id.exceptYellow:
                greyExceptOp    = new GrayExceptOp(LocalColors.YELLOW.getRgb());
                newBitmap       = greyExceptOp.filter(currentBackgroundBitmapFromStack, null);
                break;
            case R.id.exceptRed:
                greyExceptOp    = new GrayExceptOp(LocalColors.RED.getRgb());
                newBitmap       = greyExceptOp.filter(currentBackgroundBitmapFromStack, null);
                break;
        }

        if(newBitmap!=null)
        {
            drawable    = new BitmapDrawable(getResources(), newBitmap);
            coverImageView.setBackground(drawable);
            currentBackgroundBitmapFromStack = newBitmap;
        }*/
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
        setUpBlurredBackground(null, currentBackgroundBitmapFromStack);

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
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {
        List<Bitmap>    blurredImages;
        Drawable        drawable;

        blurredImages   = blurredImageLifeCycle.getList();
        drawable        = null;

        if(progress >= 0 && progress <= 24)
        {
            if(blurredImages.size() > 0) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(0));
                currentBackgroundBitmapFromStack = blurredImages.get(0);
            }
        }
        else if(progress >= 25 && progress <= 49)
        {
            if(blurredImages.size() > 1) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(1));
                currentBackgroundBitmapFromStack = blurredImages.get(1);
            }
        }
        else if (progress >= 50 && progress <= 74)
        {
            if(blurredImages.size() > 2) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(2));
                currentBackgroundBitmapFromStack = blurredImages.get(2);
            }
        }
        else if (progress >= 75 && progress <= 90)
        {
            if(blurredImages.size() > 3) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(3));
                currentBackgroundBitmapFromStack = blurredImages.get(3);
            }
        }
        else
        {
            if(blurredImages.size() > 4) {
                drawable = new BitmapDrawable(getResources(), blurredImages.get(4));
                currentBackgroundBitmapFromStack = blurredImages.get(4);
            }
        }

        if(drawable != null)
            coverImageView.setBackground(drawable);
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

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

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
                startActivityForResult(intent, 2);
                break;

            case R.id.saveButton:
                saveUserProfile(view);
                intent = new Intent();
                intent.putExtras(info);
                setResult(RESULT_OK, intent);
                currentProfileBitmapFromStack      = null;
                currentBackgroundBitmapFromStack   = null;
                finish();
                break;
        }
    }

    /**
     * Save user data to the server and exit activity
     *
     * @param view
     */
    private void saveUserProfile(View view)
    {
        /** Fix this guy up! */

        ArrayList<HttpPut>  puts;
        RestClientExecute   rce;
        String              id, enrollId;

        id              = info.getString("id");
        enrollId        = info.getString("enrollId");

        try
        {
            puts        = RestClientFactory.put(id, enrollId, coverImagePath, profileImagePath,
                    name.getText().toString(), major.getText().toString(),
                    year.getText().toString(), about.getText().toString(),
                    null, rate.getText().toString());

            for (int i  = 0; i < puts.size(); i++)
            {
                rce = new RestClientExecute(puts.get(i));
                rce.start();
            }

            info.putString("firstName", name.getText().toString());
            info.putString("about", about.getText().toString());
            info.putString("year", year.getText().toString());
            info.putString("major", major.getText().toString());
            info.putString("rate", rate.getText().toString());

            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param data
     * @param localBitmap
     */
    private void setUpBlurredBackground(Intent data, Bitmap localBitmap)
    {
        TaskDelegate taskDelegate;
        Bitmap bitmap;
        BitmapFactory.Options options;
        ConvolveOp convolveOp;
        AsyncConvolution asyncConvolution;
        int orientation;
        Matrix m;

        taskDelegate    = new BackgroundImageTaskDelegate(getResources(), StudentProfileActivity.coverImageSubject);
        convolveOp      = BitmapOpFactory.createBlurOp(CONVULTION_KERNEL_SIZE);
        convolveOp.setDivider(1.6);
        bitmap          = null;

        if (data != null)
        {
            try
            {
                coverImagePath = data.getData().getPath();
                if (coverImagePath != null) {
                    coverImagePath = getRealPathFromURI(this, data.getData());
                    orientation = getOrientation(this, data.getData());

                    options = new BitmapFactory.Options();
                    options.inSampleSize = 20;
                    bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(data.getData()), null, options);

                    if (orientation > 0) {
                        m = new Matrix();
                        m.postRotate(orientation);
                        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                                bitmap.getHeight(), m, true);
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            m = new Matrix();
            m.postScale(.4f, .4f);
            bitmap = Bitmap.createBitmap(localBitmap,0,0,localBitmap.getWidth(), localBitmap.getHeight(), m ,true);
        }

        //blur the background image
        blurredImageLifeCycle.reset();
        //asyncConvolution = new AsyncConvolution(new ProgressDialog(this), convolveOp, taskDelegate, blurredImageLifeCycle);

        try
        {
            seekBar.setVisibility(View.VISIBLE);
            seekBar.setProgress(100);
            //asyncConvolution.execute(bitmap);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    /**
     *
     */
    private void setUpUserData()
    {
        controller = ImageController.getInstance();

        originalSizeOfProfileStack      = controller.size(ImageLocation.PROFILE);
        originalSizeOfBackgroundStack   = controller.size(ImageLocation.BACKGROUND);
        blurredImageLifeCycle           = new BlurredImageLifeCycle();

        name.setText(info.getString("firstName"));
        major.setText(info.getString("major"));
        year.setText(info.getString("year"));
        about.setText(info.getString("about"));
        rate.setText(info.getString("rate"));
        profileImageView.setImageBitmap(controller.peek(ImageLocation.PROFILE));
        coverImageView.setBackground(new BitmapDrawable(getResources(), controller.peek(ImageLocation.BACKGROUND)));

        currentBackgroundBitmapFromStack   = controller.peek(ImageLocation.BACKGROUND);
        currentProfileBitmapFromStack      = controller.peek(ImageLocation.PROFILE);
    }

    private void setUpRelationships()
    {
        new ImageObserver(profileImageView, StudentProfileActivity.profileImageSubject);
        new ImageDrawableObserver(coverImageView, StudentProfileActivity.coverImageSubject, getResources());
    }


}
