package tutor.cesh.profile.fragment.subject;

import tutor.cesh.profile.fragment.observer.TabObserver;

/**
 * Created by michaelkapnick on 2/16/15.
 */
public interface TabSubject
{
    public void addObserver(TabObserver observer);
    public void notifyObservers();
    public void removeObserver(TabObserver observer);
}
