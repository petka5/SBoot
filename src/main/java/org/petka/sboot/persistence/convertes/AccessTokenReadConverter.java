package org.petka.sboot.persistence.convertes;

import org.bson.Document;
import org.petka.sboot.persistence.documents.AccessToken;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;
import java.util.Set;

@Component
public class AccessTokenReadConverter implements Converter<Document, AccessToken> {

    @Override
    public AccessToken convert(Document source) {
       return null;
    }
}