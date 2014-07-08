package tutor.cesh.profile;

import android.widget.EditText;

import org.json.JSONArray;

/**
 * Created by michaelk18 on 3/30/14.
 */
public interface TextFieldHelper
{
    public void     createBubbleOverText(EditText textField, GenericTextWatcher textWatcher, String [] strings, boolean inEditable);
    public String   createBubbleOverText(EditText textField, GenericTextWatcher textWatcher, JSONArray jsonArray);


}
