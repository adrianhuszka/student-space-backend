package hu.StudentSpace.utils;

import org.apache.commons.codec.binary.Base64;

public class JwtDecoder {
    public String decode(String token) {
        String[] split_string = token.split("\\.");
        String base64EncodedBody = split_string[1];

        Base64 base64Url = new Base64(true);

        return new String(base64Url.decode(base64EncodedBody));
    }
}
