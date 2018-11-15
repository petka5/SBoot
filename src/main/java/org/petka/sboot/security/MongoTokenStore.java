package org.petka.sboot.security;

import org.petka.sboot.persistence.documents.MongoAccessToken;
import org.petka.sboot.persistence.documents.MongoRefreshToken;
import org.petka.sboot.persistence.repositories.MongoAccessTokenRepository;
import org.petka.sboot.persistence.repositories.MongoRefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.DefaultAuthenticationKeyGenerator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class MongoTokenStore implements TokenStore {

    private AuthenticationKeyGenerator authenticationKeyGenerator = new DefaultAuthenticationKeyGenerator();


    @Autowired
    private MongoAccessTokenRepository mongoAccessTokenRepository;

    @Autowired
    private MongoRefreshTokenRepository mongoRefreshTokenRepository;

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
        return readAuthentication(accessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        MongoAccessToken mongoAccessToken = mongoAccessTokenRepository.findByTokenId(extractTokenKey(token));
        return mongoAccessToken != null ? mongoAccessToken.getAuthentication() : null;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String refreshToken = null;
        if (accessToken.getRefreshToken() != null) {
            refreshToken = accessToken.getRefreshToken().getValue();
        }

        if (readAccessToken(accessToken.getValue()) != null) {
            this.removeAccessToken(accessToken);
        }

        MongoAccessToken mongoAccessToken = new MongoAccessToken();
        mongoAccessToken.setTokenId(extractTokenKey(accessToken.getValue()));
        mongoAccessToken.setToken(accessToken);
        mongoAccessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        mongoAccessToken.setUsername(authentication.isClientOnly() ? null : authentication.getName());
        mongoAccessToken.setClientId(authentication.getOAuth2Request().getClientId());
        mongoAccessToken.setAuthentication(authentication);
        mongoAccessToken.setRefreshToken(extractTokenKey(refreshToken));

        mongoAccessTokenRepository.save(mongoAccessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        // Chekc if it works
        MongoAccessToken mongoAccessToken = mongoAccessTokenRepository.findByTokenId(extractTokenKey(tokenValue));

        return mongoAccessToken != null ? mongoAccessToken.getToken() : null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        MongoAccessToken mongoAccessToken = mongoAccessTokenRepository.findByTokenId(extractTokenKey(oAuth2AccessToken.getValue()));

        mongoAccessTokenRepository.delete(mongoAccessToken);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        MongoRefreshToken token = new MongoRefreshToken();
        token.setTokenId(extractTokenKey(refreshToken.getValue()));
        token.setToken(refreshToken);
        token.setAuthentication(authentication);
        mongoRefreshTokenRepository.save(token);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        MongoRefreshToken mongoRefreshToken = mongoRefreshTokenRepository.findByTokenId(extractTokenKey(tokenValue));
        return mongoRefreshToken != null ? mongoRefreshToken.getToken() : null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken refreshToken) {
        MongoRefreshToken mongoRefreshToken = mongoRefreshTokenRepository.findByTokenId(extractTokenKey(refreshToken.getValue()));
        return mongoRefreshToken != null ? mongoRefreshToken.getAuthentication() : null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken refreshToken) {
        MongoRefreshToken mongoRefreshToken = mongoRefreshTokenRepository.findByTokenId(extractTokenKey(refreshToken.getValue()));
        mongoRefreshTokenRepository.delete(mongoRefreshToken);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        MongoAccessToken accessToken = mongoAccessTokenRepository.findByRefreshToken(extractTokenKey(refreshToken.getValue()));
        mongoAccessTokenRepository.delete(accessToken);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken accessToken = null;
        String authenticationId = authenticationKeyGenerator.extractKey(authentication);

        MongoAccessToken mongoAccessToken = mongoAccessTokenRepository.findByAuthenticationId(authenticationId);

        if (mongoAccessToken != null) {
            accessToken = mongoAccessToken.getToken();
            if (accessToken != null && !authenticationId.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(accessToken)))) {
                this.removeAccessToken(accessToken);
                this.storeAccessToken(accessToken, authentication);
            }
        }
        return accessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
        List<MongoAccessToken> accessTokens = mongoAccessTokenRepository.findByClientIdAndUsername(clientId, username);
        for (MongoAccessToken accessToken : accessTokens) {
            tokens.add(accessToken.getToken());
        }
        return tokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
        List<MongoAccessToken> accessTokens = mongoAccessTokenRepository.findAllByClientId(clientId);
        for (MongoAccessToken accessToken : accessTokens) {
            tokens.add(accessToken.getToken());
        }
        return tokens;
    }

    private String extractTokenKey(String value) {
        if (value == null) {
            return null;
        } else {
            MessageDigest digest;
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException var5) {
                throw new IllegalStateException("MD5 algorithm not available.  Fatal (should be in the JDK).");
            }

            try {
                byte[] e = digest.digest(value.getBytes("UTF-8"));
                return String.format("%032x", new Object[]{new BigInteger(1, e)});
            } catch (UnsupportedEncodingException var4) {
                throw new IllegalStateException("UTF-8 encoding not available.  Fatal (should be in the JDK).");
            }
        }
    }
}
