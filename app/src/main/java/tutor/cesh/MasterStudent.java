package tutor.cesh;

/**
 * Created by michaelk18 on 7/2/14.
 */
public class MasterStudent extends AbstractStudent
{
    private static MasterStudent student = null;

    private MasterStudent()
    {
        super();
    }

    public static MasterStudent getInstance()
    {
        if(student == null)
        {
            student = new MasterStudent();
        }

        return student;
    }

}
