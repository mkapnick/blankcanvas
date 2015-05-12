package tutor.cesh.apisecurity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michaelkapnick on 5/11/15.
 */
public class APIAuthorization
{
    private static final String sharedSecret = "6ed9c32effbe2d74a96b390aedfa2646e74488709114b8c39f19507d3163dfc7";
    private static final String key          = "de87b04b7ff5ef26986d874caec2160c8a4e6da0d08417685e73c95eb8d158de";
    private static final String subject      = "Blankcanvas API";
    private static final String issuer       = "2015 Blankcanvas. All Rights Reserved";


    public static String getAuthorizationHeader(JSONObject params, String path, String method)
    {
        JWTPayload  payload;
        String      body, jwt;

        body        = (null == params) ? "" : params.toString();
        payload     = new JWTPayload(key, path, method, body, sharedSecret, subject, issuer);
        jwt         = JWTFactory.generateJWT(payload);

        return jwt;
    }
}
