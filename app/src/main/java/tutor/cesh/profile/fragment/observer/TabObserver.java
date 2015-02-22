package tutor.cesh.profile.fragment.observer;

import android.view.View;
import android.widget.ImageView;

import tutor.cesh.profile.fragment.FragmentTabBehavior;
import tutor.cesh.profile.fragment.subject.TabSubject;

/**
 * Created by michaelkapnick on 2/16/15.
 */
public interface TabObserver extends FragmentTabBehavior
{
    public void updateFrontEnd();
    public void setTabSubject(TabSubject subject);
}
