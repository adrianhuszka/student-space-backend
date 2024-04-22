package hu.StudentSpace.utils;

import com.nimbusds.jose.shaded.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken {
    String email;
    private long exp;
    private long iat;
    private String jti;
    private String iss;
    private List<String> aud;
    private String sub;
    private String typ;
    private String azp;
    private String session_state;
    private String acr;
    private List<String> allowed_origins;
    private RealmAccess realm_access;
    private ResourceAccess resource_access;
    private String scope;
    private String sid;
    private boolean email_verified;
    private String name;
    private String phone_number;
    private String profile_picture;
    private String preferred_username;
    private String given_name;
    private String family_name;

    public static JwtToken fromJson(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, JwtToken.class);
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class ResourceAccess {
        private Map<String, RealmAccess> resourceAccessMap;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public class RealmAccess {
        private List<String> roles;
    }
}
