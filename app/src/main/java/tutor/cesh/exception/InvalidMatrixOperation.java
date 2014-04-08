package tutor.cesh.exception;

/**
 * Created by michaelk18 on 4/2/14.
 */
public class InvalidMatrixOperation extends Exception
{
    public InvalidMatrixOperation()
    {
        super();
    }
    public InvalidMatrixOperation(String message)
    {
        super(message);
    }
    public InvalidMatrixOperation(String message, Throwable cause)
    {
        super(message, cause);
    }
    public InvalidMatrixOperation(Throwable cause)
    {
        super(cause);
    }
}
