package tutor.cesh.exception;

/**
 * Created by michaelk18 on 4/2/14.
 */
public class NotASquareMatrixException extends Exception
{
    public NotASquareMatrixException()
    {
        super();
    }
    public NotASquareMatrixException(String message)
    {
        super(message);
    }
    public NotASquareMatrixException(String message, Throwable cause)
    {
        super(message, cause);
    }
    public NotASquareMatrixException(Throwable cause)
    {
        super(cause);
    }
}
