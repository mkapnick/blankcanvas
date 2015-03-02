package tutor.cesh.session;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.HashMap;

import tutor.cesh.arrival.BeginningActivity;

/**
 * Created by michaelkapnick on 2/28/15.
 */
public class SessionManager
{
    private Context             context;
    private long                sessionValue;

    private final String        SESSION_KEY     = "loggedIn";
    private static final String IS_LOGGED_IN    = "isLoggedIn";
    private static final String KEY_EMAIL       = "email";
    private static final String KEY_PASSWORD    = "password";
    SharedPreferences           pref;
    SharedPreferences.Editor    editor;

    public SessionManager(Context context)
    {
        this.context        = context;
        this.sessionValue   = 189654871;
        this.pref           = this.context.getSharedPreferences(SESSION_KEY, 0); // 0 - for private mode
        this.editor         = pref.edit();
    }

    public void createLoginSession(String email, String password)
    {
        this.pref    = this.context.getSharedPreferences(SESSION_KEY, 0); // 0 - for private mode
        this.editor  = this.pref.edit();

        //on the login store the login
        this.editor.putLong(SESSION_KEY, sessionValue);
        this.editor.putBoolean(IS_LOGGED_IN, true);
        this.editor.putString(KEY_EMAIL, email);
        this.editor.putString(KEY_PASSWORD, password);

        this.editor.commit();
    }

    public void logOut()
    {
        this.pref    = this.context.getSharedPreferences(SESSION_KEY, 0); // 0 - for private mode
        this.editor  = pref.edit();

        this.editor.clear();
        this.editor.commit();

        startBeginningActivity();
    }

    public boolean isLoggedIn()
    {
        return this.pref.getBoolean(IS_LOGGED_IN, false);
    }

    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     * */
    public void branchToActivity()
    {
        // Check login status
        if(!isLoggedIn())
            startBeginningActivity();
    }

    private void startBeginningActivity()
    {
        Intent intent;

        // user is not logged in redirect him to Login Activity
        intent = new Intent(this.context, BeginningActivity.class);

        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        this.context.startActivity(intent);
    }

    public HashMap<String, String> getSessionDetails()
    {
        HashMap<String, String> map;

        map = new HashMap<String, String>();

        map.put(KEY_EMAIL, this.pref.getString(KEY_EMAIL, ""));
        map.put(KEY_PASSWORD, this.pref.getString(KEY_PASSWORD, ""));

        return map;
    }

}
