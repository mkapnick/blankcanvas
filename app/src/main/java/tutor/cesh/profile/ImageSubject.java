package tutor.cesh.profile;

import java.util.ArrayList;

/**
 * Responsible for notifying observers when
 * images have changed on a user's profile
 *
 * @author Michael Kapnick
 * @version v1
 */
public class ImageSubject implements Subject
{

    private ArrayList<Observer> list;

    public ImageSubject()
    {
        list = new ArrayList<Observer>();
    }

    @Override
    public void add(Observer o)
    {
        list.add(o);
    }

    @Override
    public void remove(Observer o)
    {
        if(list.contains(o))
        {
            list.remove(o);
        }
    }

    @Override
    public void notifyObservers()
    {
        for(Observer o: list)
        {
            o.update();
        }
    }

    @Override
    public void notifyObserver(Observer o)
    {
        if(list.contains(o))
        {
            o.update();
        }
    }

    @Override
    public void notifyObserver(int index)
    {
        if(index >= 0 && index < this.list.size())
        {
            list.get(index).update();
        }
    }
}
