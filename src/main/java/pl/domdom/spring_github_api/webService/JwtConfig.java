package pl.domdom.spring_github_api.webService;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Date;

@Component
public class JwtConfig {

    @Value("${github.api.private.key.path}")
    private String privateKeyPath;

    @Value("${github.api.token.expiration.date}")
    private long expirationDate;

    private PrivateKey get(String filename) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

        File file = new File(filename);
        byte[] keyBytes = Files.readAllBytes(file.toPath());

        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    private String createJWT(String githubAppId, long ttlMillis) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        //The JWT signature algorithm we will be using to sign the token
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        //We will sign our JWT with our private key
        Key signingKey = get(privateKeyPath);

        //Let's set the JWT Claims
        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(now)
                .setIssuer(githubAppId)
                .signWith(signingKey, signatureAlgorithm);

        //if it has been specified, let's add the expiration
        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //Builds the JWT and serializes it to a compact, URL-safe string
        return builder.compact();
    }

    public String getJwtToken(String githubAppId) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
        return createJWT(githubAppId, expirationDate);
    }
}
