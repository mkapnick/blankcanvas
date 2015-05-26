package tutor.cesh.apisecurity;

import org.json.JSONObject;

/**
 *  Responsible for adding authorization to every HTTP API call
 *
 * @version v1.0
 * @author  Michael Kapnick
 */
public class APIAuthorization
{
    private static final String sharedSecret = "6ed9c32effbe2d74a96b390aedfa2646e74488709114b8c39f19507d3163dfc7";
    private static final String key          = "de87b04b7ff5ef26986d874caec2160c8a4e6da0d08417685e73c95eb8d158de";
    private static final String subject      = "Blankcanvas API";
    private static final String issuer       = "2015 Blankcanvas. All Rights Reserved";


    /**
     * Responsbile for creating a valid JWT that will be used as the Authorization header in the
     * API call
     *
     * @param params    The body of the API call
     * @param path      The path of the API call
     * @param method    The method of the API call
     *
     * @return          A valid JWT
     */
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
