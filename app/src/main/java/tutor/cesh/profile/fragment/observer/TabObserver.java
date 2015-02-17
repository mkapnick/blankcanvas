package tutor.cesh.profile.fragment.observer;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by michaelkapnick on 2/16/15.
 */
public interface TabObserver
{
    public void         updateFrontEnd();
    public void         initializeUI(View inflatedView);
    public void         setUpUserInfo();
    public int          getLayout();
    public CharSequence getTitle();
    public String       getTabName();
    public ImageView    getCoverImageView();
    public void         downloadCoverImageFromServer();
}
