package tutor.cesh.profile.fragment;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by michaelk18 on 12/4/14.
 */
public interface FragmentTabBehavior
{
    public void         initializeUI(View inflatedView);
    public void         setUpUserInfo();
    public int          getLayout();
    public CharSequence getTitle();
    public String       getTabName();
    public ImageView    getCoverImageView();
    public void         downloadCoverImageFromServer();
}
