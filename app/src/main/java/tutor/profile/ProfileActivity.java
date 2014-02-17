package tutor.profile;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageButton;
import android.widget.ImageView;

import org.json.JSONArray;

import java.io.FileNotFoundException;

import tutor.cesh.R;

public class ProfileActivity extends Activity implements View.OnClickListener
{
    private Bundle info;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        info = getIntent().getExtras();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        setUpListeners();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Uri         targetUri;
        Bitmap      bitMap;
        ImageView   background;

        super.onActivityResult(requestCode, resultCode, data);
        background = (ImageView) findViewById(R.id.profileBackgroundImage);

        if (requestCode == 1)
        {
            if (resultCode == RESULT_OK)
            {
                targetUri = data.getData();
                try
                {
                    bitMap = BitmapFactory.decodeStream(getContentResolver().openInputStream(targetUri));
                    background.setImageBitmap(bitMap);

                    //post data to the server and update users background image
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.profileImageButton:
                Intent intent;
                intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 1);
                break;
        }

    }

    /**
     * Set listeners in XML layout for user to change
     * and customize to their liking
     */
    private void setUpListeners()
    {
        ImageButton ib;
        ib = (ImageButton) findViewById(R.id.profileImageButton);

        ib.setOnClickListener((View.OnClickListener) this);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
            return rootView;
        }
    }

}
