package org.petka.sboot.persistence.convertes;


import org.bson.Document;
import org.petka.sboot.persistence.documents.AccessToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.stereotype.Component;

@Component
public class AccessTokenWriteConverter implements Converter<AccessToken, Document> {

    @Override
    public Document convert(AccessToken source) {
        Document document = new Document();
        document.put("tokenId", source.getTokenId());

        document.put("value", source.getToken().getValue());
        document.put("expiration", source.getToken().getExpiration());
        document.put("tokenType", source.getToken().getTokenType());

        DefaultExpiringOAuth2RefreshToken refreshToken = (DefaultExpiringOAuth2RefreshToken) source.getToken().getRefreshToken();
        document.put("refreshTokenValue", refreshToken.getValue());
        document.put("refreshTokenExpiration", refreshToken.getExpiration());

        document.put("scope", source.getToken().getScope());
        document.put("additionalInformation", source.getToken().getAdditionalInformation());

        document.put("username", source.getUsername());
        document.put("clientId", source.getClientId());
        document.put("authenticationId", source.getAuthenticationId());
        document.put("authentication", source.getAuthentication());
        document.put("refreshToken", source.getRefreshToken());

        return document;
    }
}
