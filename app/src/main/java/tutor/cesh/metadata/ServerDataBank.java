package tutor.cesh.metadata;

import android.content.Context;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class that stores data from the server
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class ServerDataBank
{
    private static HashMap<String, ArrayList<String>>   serverDataMap;
    private static ServerDataBank serverDataBank;

    private ServerDataBank(Context c, HashMap<String, ArrayList<String>> data)
    {
        serverDataMap = data;
    }

    public static ServerDataBank getInstance(Context c, HashMap<String, ArrayList<String>> data)
    {
        if (serverDataBank == null)
            serverDataBank = new ServerDataBank(c, data);

        return serverDataBank;
    }

    public static ArrayList<String> getMajors() {
        return serverDataMap.get("majors");
    }

    public static ArrayList<String> getRates() {
        return serverDataMap.get("rates");
    }

    public static ArrayList<String> getYears() {
        return serverDataMap.get("years");
    }

    public static ArrayList<String> getMinors(){
        return serverDataMap.get("minors");
    }
}
