package tutor.cesh.exception;

/**
 * Created by michaelk18 on 4/2/14.
 */
public class LengthErrorException extends Exception
{
    public LengthErrorException()
    {
        super();
    }
    public LengthErrorException(String message)
    {
        super(message);
    }
    public LengthErrorException(String message, Throwable cause)
    {
        super(message, cause);
    }
    public LengthErrorException(Throwable cause)
    {
        super(cause);
    }
}
