package tutor.cesh.format;

/**
 * Created by michaelkapnick on 3/18/15.
 */
public class TextFormatter
{

    public static String capitalizeAllFirstLetters(String s)
    {
        char    firstLetter;
        String  result, word, capitalizedWord;
        String [] array;

        result      = "";

        if(s.length() > 0)
        {
            array       = s.split(" ");

            for(int i =0; i < array.length; i++)
            {
                word            = array[i];
                firstLetter     = Character.toUpperCase(word.charAt(0));
                capitalizedWord = firstLetter + word.substring(1);

                result          += capitalizedWord + " ";
            }
        }

        return result;
    }
}
