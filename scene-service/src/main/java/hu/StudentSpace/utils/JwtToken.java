package hu.StudentSpace.utils;

import com.nimbusds.jose.shaded.gson.Gson;

import java.util.List;
import java.util.Map;

public record JwtToken(
        long exp,
        long iat,
        String jti,
        String iss,
        List<String> aud,
        String sub,
        String typ,
        String azp,
        String session_state,
        String acr,
        List<String> allowed_origins,
        RealmAccess realm_access,
        ResourceAccess resource_access,
        String scope,
        String sid,
        boolean email_verified,
        String name,
        String phone_number,
        String profile_picture,
        String preferred_username,
        String given_name,
        String family_name,
        String email
) {
    public static JwtToken fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JwtToken.class);
    }

    public record RealmAccess(List<String> roles) {
    }

    public record ResourceAccess(Map<String, RealmAccess> resourceAccessMap) {
    }
}
