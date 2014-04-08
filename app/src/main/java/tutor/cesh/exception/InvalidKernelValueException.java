package tutor.cesh.exception;

/**
 * Created by michaelk18 on 4/2/14.
 */
public class InvalidKernelValueException extends Exception
{
    public InvalidKernelValueException()
    {
        super();
    }
    public InvalidKernelValueException(String message)
    {
        super(message);
    }
    public InvalidKernelValueException(String message, Throwable cause)
    {
        super(message, cause);
    }
    public InvalidKernelValueException(Throwable cause)
    {
        super(cause);
    }
}
