package pl.domdom.gapi.web.service;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public interface JwtConfig {
    String getJwtToken(String githubAppId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException;
}
