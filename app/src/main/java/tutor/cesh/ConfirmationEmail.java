package tutor.cesh;

import android.content.Intent;
import android.text.Html;

import java.io.BufferedReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by michaelk18 on 1/30/14.
 */
public class ConfirmationEmail implements Runnable
{
    private Intent intent;

    public ConfirmationEmail(Intent intent)
    {
        this.intent = intent;
    }
    @Override
    public void run()
    {
        URL             url;
        System.out.println("inside run!");

        try
        {
            url             = new URL("localhost:8080/ConfirmationLinkAndroid/verify-email?token=testingtesting123herewego");

            intent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml("Click the link below to activate account\n" +
                    "<a href=" + url + "\\>"+ url + "</a>"));

            //startActivity(Intent.createChooser(i, "Send mail..."));


        }

        catch(Exception e)
        {
            System.out.println("Something wrong connecting!");
        }

    }
}
