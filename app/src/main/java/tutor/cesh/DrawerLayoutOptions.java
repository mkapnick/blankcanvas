package tutor.cesh;

/**
 * Created by michaelk18 on 8/10/14.
 */
public enum DrawerLayoutOptions
{
    TUTOR_LIST(1),
    STUDENT_LIST(2),
    PENDING_SESSIONS(3),
    GROUP_SESSIONS(4),
    INDIVIDUAL_SESSIONS(5);

    int position;

    DrawerLayoutOptions(int position)
    {
        this.position = position;
    }

    public int getPosition()
    {
        return this.position;
    }

}
