package hu.StudentSpace.utils;

import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class JwtDecoder {

    public JwtToken decode(@NotNull String token) {
        String[] split_string = token.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);

        return JwtToken.fromJson(new String(base64Url.decode(base64EncodedBody)));
    }
}
