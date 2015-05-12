package tutor.cesh.apisecurity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michaelkapnick on 5/11/15.
 */
public class JWTPayload  extends JSONObject
{
    private String key, path, method, body, secret, subject, issuer;
    private String [] keys = {"key", "path", "method", "expiration", "body", "secret", "subject", "issuer", "type", "alg"};

    public JWTPayload(String key, String path, String method, String body,
                      String secret, String subject, String issuer)
    {
        this.key        = key;
        this.path       = path;
        this.method     = method;
        this.body       = body;
        this.secret     = secret;
        this.subject    = subject;
        this.issuer     = issuer;

        init();
    }

    /**
     *
     */
    private void init()
    {
        try
        {
            super.put("type", "JWT");
            super.put("alg", "HS256");
            super.put("key", this.key);
            super.put("path", this.path);
            super.put("method", this.method);
            super.put("body", this.body);
            super.put("secret", this.secret);
            super.put("subject", this.subject);
            super.put("issuer", this.issuer);
        }
        catch(JSONException json)
        {

        }
    }

    /**
     * Returns the value mapped by {@code name}, or throws if no such mapping exists.
     *
     * @throws JSONException if no such mapping exists.
     */
    public Object get(String name) throws JSONException
    {
        boolean isValidKeyLookup;

        isValidKeyLookup = false;

        for(String key: this.keys)
        {
            if(key.equalsIgnoreCase(name))
            {
                isValidKeyLookup = true;
                break;
            }
        }

        if(isValidKeyLookup)
        {
            return super.get(name);
        }
        else
        {
            throw new JSONException("key is not valid [according to JWTPayload specs]: " + name);
        }
    }
}
