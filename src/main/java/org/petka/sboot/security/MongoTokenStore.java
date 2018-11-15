package org.petka.sboot.security;

import org.petka.sboot.persistence.documents.AccessToken;
import org.petka.sboot.persistence.documents.RefreshToken;
import org.petka.sboot.persistence.repositories.AccessTokenRepository;
import org.petka.sboot.persistence.repositories.RefreshTokenRepository;
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
    private AccessTokenRepository accessTokenRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken accessToken) {
        return readAuthentication(accessToken.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        AccessToken accessToken = accessTokenRepository.findByTokenId(extractTokenKey(token));
        return accessToken != null ? accessToken.getAuthentication() : null;
    }

    @Override
    public void storeAccessToken(OAuth2AccessToken oauthAccessToken, OAuth2Authentication authentication) {
        String refreshToken = null;
        if (oauthAccessToken.getRefreshToken() != null) {
            refreshToken = oauthAccessToken.getRefreshToken().getValue();
        }

        if (readAccessToken(oauthAccessToken.getValue()) != null) {
            this.removeAccessToken(oauthAccessToken);
        }

        AccessToken accessToken = new AccessToken();
        accessToken.setTokenId(extractTokenKey(oauthAccessToken.getValue()));
        accessToken.setToken(oauthAccessToken);
        accessToken.setAuthenticationId(authenticationKeyGenerator.extractKey(authentication));
        accessToken.setUsername(authentication.isClientOnly() ? null : authentication.getName());
        accessToken.setClientId(authentication.getOAuth2Request().getClientId());
        accessToken.setAuthentication(authentication);
        accessToken.setRefreshToken(extractTokenKey(refreshToken));

        accessTokenRepository.save(accessToken);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        // Chekc if it works
        AccessToken accessToken = accessTokenRepository.findByTokenId(extractTokenKey(tokenValue));

        return accessToken != null ? accessToken.getToken() : null;
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken oAuth2AccessToken) {
        AccessToken accessToken = accessTokenRepository.findByTokenId(extractTokenKey(oAuth2AccessToken.getValue()));

        accessTokenRepository.delete(accessToken);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        RefreshToken token = new RefreshToken();
        token.setTokenId(extractTokenKey(refreshToken.getValue()));
        token.setToken(refreshToken);
        token.setAuthentication(authentication);
        refreshTokenRepository.save(token);
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        RefreshToken refreshToken = refreshTokenRepository.findByTokenId(extractTokenKey(tokenValue));
        return refreshToken != null ? refreshToken.getToken() : null;
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        RefreshToken рefreshToken = refreshTokenRepository.findByTokenId(extractTokenKey(oAuth2RefreshToken.getValue()));
        return рefreshToken != null ? рefreshToken.getAuthentication() : null;
    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken oAuth2RefreshToken) {
        RefreshToken рefreshToken = refreshTokenRepository.findByTokenId(extractTokenKey(oAuth2RefreshToken.getValue()));
        refreshTokenRepository.delete(рefreshToken);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        AccessToken accessToken = accessTokenRepository.findByRefreshToken(extractTokenKey(refreshToken.getValue()));
        accessTokenRepository.delete(accessToken);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        OAuth2AccessToken oAuth2AccessToken = null;
        String authenticationId = authenticationKeyGenerator.extractKey(authentication);

        AccessToken аccessToken = accessTokenRepository.findByAuthenticationId(authenticationId);

        if (аccessToken != null) {
            oAuth2AccessToken = аccessToken.getToken();
            if (oAuth2AccessToken != null && !authenticationId.equals(this.authenticationKeyGenerator.extractKey(this.readAuthentication(oAuth2AccessToken)))) {
                this.removeAccessToken(oAuth2AccessToken);
                this.storeAccessToken(oAuth2AccessToken, authentication);
            }
        }
        return oAuth2AccessToken;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String username) {
        Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
        List<AccessToken> accessTokens = accessTokenRepository.findByClientIdAndUsername(clientId, username);
        for (AccessToken accessToken : accessTokens) {
            tokens.add(accessToken.getToken());
        }
        return tokens;
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        Collection<OAuth2AccessToken> tokens = new ArrayList<OAuth2AccessToken>();
        List<AccessToken> accessTokens = accessTokenRepository.findAllByClientId(clientId);
        for (AccessToken accessToken : accessTokens) {
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
