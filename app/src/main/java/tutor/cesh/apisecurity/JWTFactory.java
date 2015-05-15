package tutor.cesh.apisecurity;

import java.security.SecureRandom;
import java.util.Date;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jwt.*;

import org.apache.http.client.methods.HttpPost;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by michaelkapnick on 5/11/15.
 */
public class JWTFactory
{
    /**
     *
     * @throws JOSEException
     */
    public static String generateJWT(JWTPayload payload)
    {
        JWSSigner           signer;
        JWTClaimsSet        claimsSet;
        SignedJWT           signedJWT;
        String              compactForm;
        Map<String, Object> claims;

        //use shared secret (instead of generating one)
        claims          = new HashMap<String, Object>();

        // Create HMAC signer
        signer          = null; //will be created in try/catch

        // Prepare JWT with claims set
        claimsSet       = new JWTClaimsSet();

        try
        {
            // Create HMAC signer
            signer      = new MACSigner(payload.getString("secret"));

            claimsSet.setSubject(payload.getString("subject"));
            claimsSet.setIssuer(payload.getString("issuer"));
            claims.put("payload", payload);
        }
        catch(JSONException exception)
        {
            //do nothing
        }

        claimsSet.setAllClaims(claims);
        claimsSet.setExpirationTime(new Date(new Date().getTime() + (60 * 1000)));

        //cerate signed JWT, with HS256 Algorithm and the claimset
        signedJWT       = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

        // Apply the HMAC protection
        try
        {
            signedJWT.sign(signer);
        }
        catch(JOSEException jose)
        {
            //System.out.println("COULDN'T SIGN JWT");
        }

        // Serialize to compact form, produces something like
        // eyJhbGciOiJIUzI1NiJ9.SGVsbG8sIHdvcmxkIQ.onO9Ihudz3WkiauDO2Uhyuz0Y18UASXlSc1eS0NkWyA
        compactForm     = signedJWT.serialize();

        //System.out.println("OK!!! set compact form. Compact form is: " + compactForm);

        return compactForm;
    }

}
