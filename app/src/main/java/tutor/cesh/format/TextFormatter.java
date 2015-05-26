package tutor.cesh.format;

/**
 * Formats text
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class TextFormatter
{
    /**
     * Capitalizes all first letters in a string
     *
     * @param s The string to capitalize all first letters in each word
     * @return
     */
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
