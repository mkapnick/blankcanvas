package tutor.cesh.sampled.statik;

import android.content.Intent;

/**
 * Created by michaelk18 on 4/16/14.
 */
public class Cropper
{

    public Cropper()
    {
    }
    public Intent performCrop()
    {
        Intent cropIntent;

        cropIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
        // indicate image type and Uri
        cropIntent.setType("image/*");
        // set crop properties
        cropIntent.putExtra("crop", "true");
        // indicate aspect of desired crop
        cropIntent.putExtra("aspectX", 30);
        cropIntent.putExtra("aspectY", 30);
        cropIntent.putExtra("scale", false);
        cropIntent.putExtra("scaleUpIfNeeded", false);
        // indicate output X and Y
        cropIntent.putExtra("outputX", 30);
        cropIntent.putExtra("outputY", 30);
        // retrieve data on return
        cropIntent.putExtra("return-data", true);
        cropIntent.putExtra("circleCrop", true);
        // start the activity - we handle returning in onActivityResult
        //startActivityForResult(cropIntent, 1);

        return cropIntent;
    }

}
