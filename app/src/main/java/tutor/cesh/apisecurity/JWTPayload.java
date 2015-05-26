package tutor.cesh.apisecurity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Encapsulates data that's used a payload when creating JWT's
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class JWTPayload  extends JSONObject
{
    private String key, path, method, body, secret, subject, issuer;
    private String [] keys = {"key", "path", "method", "expiration", "body", "secret",
                              "subject", "issuer", "type", "alg"};

    /**
     * Explicit value constructor
     *
     * @param key       The key
     * @param path      The path of the endpoint
     * @param method    The http method of the endpoint
     * @param body      The http body
     * @param secret    The shared secret between client and server
     * @param subject   The shared subject between client and server
     * @param issuer    The shared issuer between client and server
     */
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
     * Initialize this payload object
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
            //do nothing
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
