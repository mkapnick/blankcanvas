package tutor.cesh.profile;

/**
 * Created by michaelk18 on 4/20/14.
 */
public interface Subject
{
    public void add(Observer o);
    public void remove(Observer o);
    public void notifyObservers();
    public void notifyObserver(Observer o);
    public void notifyObserver(int index);

}
