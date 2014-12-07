package tutor.cesh.profile.fragment;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by michaelk18 on 12/4/14.
 */
public interface FragmentTabBehavior
{
    public void initializeUI(View inflatedView);
    public void setUpUserInfo();
    public int getLayout();
    public CharSequence getTitle();
    public String getTabName();
    public ImageView getCoverImageView();
    public String getName();
    public String getMajor();
    public String getYear();
    public String getAbout();
    public String getClasses();
    public void downloadCoverImageFromServer();
    public void downloadDataFromServer();
}
